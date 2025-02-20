package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.pojo.SysRoles;
import cn.anlucky.luckyadmin.system.mapper.SysRolesMapper;
import cn.anlucky.luckyadmin.system.pojo.SysRolesMenus;
import cn.anlucky.luckyadmin.system.pojo.SysUserRoles;
import cn.anlucky.luckyadmin.system.service.SysRolesMenusService;
import cn.anlucky.luckyadmin.system.service.SysRolesService;
import cn.anlucky.luckyadmin.system.service.SysUserRolesService;
import cn.anlucky.luckyadmin.system.vo.RolesVo;
import cn.anlucky.luckyadmin.system.vo.SysRolesVo;
import cn.anlucky.luckyadmin.utils.satoken.SaTokenDaoUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import cn.anlucky.luckyadmin.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色信息 服务实现类
 *
 * @author yifan.du
 * @since 2025-01-16 15:50:19
 */
@Service
public class SysRolesServiceImp extends ServiceImpl<SysRolesMapper, SysRoles> implements SysRolesService {

    @Autowired
    private SysRolesMenusService sysRolesMenusService;
    @Autowired
    private SysUserRolesService sysUserRolesService;

    /**
     * 条件分页查询角色信息信息
     *
     * @param sysRoles
     * @return List<SysRoles>
     */
    public List<SysRoles> pageByParams(SysRoles sysRoles) {
        if (sysRoles == null) {
            return this.list();
        }
        LambdaQueryWrapper<SysRoles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(sysRoles.getName()), SysRoles::getName, sysRoles.getName());
        wrapper.eq(StringUtils.isNotBlank(sysRoles.getDescription()), SysRoles::getDescription, sysRoles.getDescription());
        wrapper.orderByAsc(SysRoles::getSort);
        return this.list(wrapper);
    }

    /**
     * 根据用户id查询角色信息
     *
     * @param userId
     * @return
     */
    @Override
    public RolesVo getRolesByUserId(Long userId) {
        // 从缓存中获取当前用户的角色
        List<SysRoles> list = (List<SysRoles>) SaTokenDaoUtils.getObjectKey(SaTokenDaoUtils.ROLES_CACHE + userId);
        if (list == null) {
            // 缓存中没有，从数据库中查询
            list = this.baseMapper.getRolesByUserId(userId);
            // 放入缓存 过期时间一天
            SaTokenDaoUtils.setObjectKey(SaTokenDaoUtils.ROLES_CACHE + userId, list, SaTokenDaoUtils.DAY_TIMEOUT);
            // todo:在用户对应的角色发生改变的时候 删除缓存
        }
        RolesVo rolesVo = new RolesVo();
        rolesVo.setId(userId);
        rolesVo.setRoles(list);
        return rolesVo;
    }

    /**
     * 根据用户id查询拥有的角色代码
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getRolesCodeByUserId(Long userId) {
        RolesVo rolesVo = this.getRolesByUserId(userId);
        List<String> list = new ArrayList<>();
        rolesVo.getRoles().forEach(item -> {
            list.add(item.getName());
        });
        return list;
    }

    /**
     * 根据用户id查询拥有的角色Id
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> getRolesIdByUserId(Long userId) {
        RolesVo rolesVo = this.getRolesByUserId(userId);
        List<Long> list = new ArrayList<>();
        rolesVo.getRoles().forEach(item -> {
            list.add(item.getId());
        });
        return list;
    }

    /**
     * 根据用户ID查询当前用户拥有的权限信息
     *
     * @param userId 用户ID
     * @return 权限集合列表
     */
    @Override
    public List<String> getPermissionByUserId(Long userId) {
        // 获取当前用户拥有的角色ID
        List<Long> roleIds = this.getRolesIdByUserId(userId);
        ArrayList<String> list = new ArrayList<>();
        // 根据角色ID查询当前用户拥有的权限信息
        roleIds.forEach(roleId -> {
            // 从缓存中获取当前角色的权限信息
            List<String> permissionByRoleIds = (List<String>) SaTokenDaoUtils.getObjectKey(SaTokenDaoUtils.PERMISSIONS_CACHE + roleId);
            if (permissionByRoleIds == null) {
                // 缓存中没有，从数据库中查询
                permissionByRoleIds = this.baseMapper.getPermissionByRoleIds(roleId);
                // 放入缓存 过期时间一天
                SaTokenDaoUtils.setObjectKey(SaTokenDaoUtils.PERMISSIONS_CACHE + roleId, permissionByRoleIds, SaTokenDaoUtils.DAY_TIMEOUT);
                // todo: 在角色对应的权限发生改变的时候 删除缓存
            }
            list.addAll(permissionByRoleIds);
        });
        // 去重复
        LinkedHashSet<String> permissions = new LinkedHashSet<>(list);
        return permissions.stream().toList();
    }

    /**
     * 保存角色信息
     *
     * @param sysRolesVo
     */
    @Transactional
    @Override
    public void saveRoles(SysRolesVo sysRolesVo) {

        SysRoles sysRoles = new SysRoles();
        BeanUtils.copyProperties(sysRolesVo, sysRoles);
        // 保存角色信息
        this.save(sysRoles);
        List<SysRolesMenus> list = new ArrayList<>();
        sysRolesVo.getMenusIds().forEach(menuId -> {
            // 保存角色菜单关系
            SysRolesMenus rolesMenus = new SysRolesMenus();
            rolesMenus.setRoleId(sysRoles.getId());
            rolesMenus.setMenuId(menuId);
            list.add(rolesMenus);
        });
        // 保存菜单和角色的关系
        sysRolesMenusService.saveBatch(list);
        // 删除当前角色的权限缓存
        SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.PERMISSIONS_CACHE + sysRoles.getId());
        // 删除当前角色的路由
        SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.ROUTER_CACHE + sysRoles.getId());
    }

    /**
     * 获取角色信息
     *
     * @param id
     * @return
     */
    @Override
    public SysRolesVo getRolesVoById(Long id) {
        SysRoles sysRoles = this.getById(id);
        if (sysRoles == null) {
            throw new CustomException("角色不存在");
        }
        SysRolesVo sysRolesVo = new SysRolesVo();
        BeanUtils.copyProperties(sysRoles, sysRolesVo);
        List<Long> menusIds = sysRolesMenusService.list(new LambdaQueryWrapper<SysRolesMenus>().eq(SysRolesMenus::getRoleId, id)).stream().map(SysRolesMenus::getMenuId).toList();
        sysRolesVo.setMenusIds(menusIds);
        return sysRolesVo;
    }

    /**
     * 修改角色信息
     *
     * @param sysRolesVo
     * @return
     */
    @Transactional
    @Override
    public void updateByIdRolesVo(SysRolesVo sysRolesVo) {
        if (sysRolesVo.getId() == null) {
            throw new CustomException("角色ID不能为空");
        }

        SysRoles sysRoles = new SysRoles();
        BeanUtils.copyProperties(sysRolesVo, sysRoles);
        // 删除角色菜单关系
        sysRolesMenusService.remove(new LambdaQueryWrapper<SysRolesMenus>().eq(SysRolesMenus::getRoleId, sysRolesVo.getId()));
        List<SysRolesMenus> list = new ArrayList<>();
        sysRolesVo.getMenusIds().forEach(menuId -> {
            SysRolesMenus rolesMenus = new SysRolesMenus();
            rolesMenus.setRoleId(sysRolesVo.getId());
            rolesMenus.setMenuId(menuId);
            list.add(rolesMenus);
        });
        // 修改角色信息
        this.updateById(sysRoles);
        // 保存角色菜单关系
        sysRolesMenusService.saveBatch(list);
        // 删除当前角色的权限缓存
        SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.PERMISSIONS_CACHE + sysRolesVo.getId());
        // 删除当前角色的路由
        SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.ROUTER_CACHE + sysRolesVo.getId());
    }

    /**
     * 删除角色信息
     *
     * @param ids
     */
    @Override
    public void removeRolesByIds(List<Long> ids) {
        // 用户绑定了角色
        // 角色绑定了菜单
        // 校验是否有用户绑定此角色
        if (ids == null || ids.size() <= 0) {
            throw new CustomException("删除失败，请选择要删除的数据");
        }
        LambdaQueryWrapper<SysUserRoles> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUserRoles::getRoleId,ids);
        // 查询当前角色上是否绑定了用户
        List<SysUserRoles> list = sysUserRolesService.list(wrapper);
        if ( list.size()>0){
            // 批量查询，若查到数据证明当前角色绑定了用户不可以删除
            throw new CustomException("删除失败，当前角色存在用户绑定，禁止删除");
        }
        LambdaQueryWrapper<SysRolesMenus> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(SysRolesMenus::getRoleId,ids);
        List<SysRolesMenus> list1 = sysRolesMenusService.list(wrapper1);
        if ( list1.size()>0){
            // 批量查询，若查到数据证明当前角色绑定了菜单不可以删除
            throw new CustomException("删除失败，当前角色存在菜单绑定，禁止删除");
        }
        this.removeBatchByIds(ids);
        for (Long id : ids) {
            // 删除缓存中当前用户的角色信息和权限信息
            SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.ROLES_CACHE + id);
            SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.PERMISSIONS_CACHE + id);
        }
    }


}
