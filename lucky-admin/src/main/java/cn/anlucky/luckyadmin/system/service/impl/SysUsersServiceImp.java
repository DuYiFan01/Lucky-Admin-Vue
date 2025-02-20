package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.pojo.SysUserRoles;
import cn.anlucky.luckyadmin.system.pojo.SysUsers;
import cn.anlucky.luckyadmin.system.mapper.SysUsersMapper;
import cn.anlucky.luckyadmin.system.service.SysUserRolesService;
import cn.anlucky.luckyadmin.system.service.SysUsersService;
import cn.anlucky.luckyadmin.system.vo.AuthUserVo;
import cn.anlucky.luckyadmin.utils.satoken.SaTokenDaoUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cn.anlucky.luckyadmin.utils.StringUtils;

/**
 * 用户信息 服务实现类
 *
 * @author yifan.du
 * @since 2025-02-17 13:33:37
 */
@Service
public class SysUsersServiceImp extends ServiceImpl<SysUsersMapper, SysUsers> implements SysUsersService {

    @Autowired
    private SysUserRolesService sysUserRolesService;

    /**
     * 条件分页查询用户信息信息
     *
     * @param sysUsers
     * @return List<SysUsers>
     */
    public List<SysUsers> pageByParams(SysUsers sysUsers) {
        if (sysUsers == null) {
            return this.list();
        }
        LambdaQueryWrapper<SysUsers> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(sysUsers.getUsername()), SysUsers::getUsername, sysUsers.getUsername());
        wrapper.eq(StringUtils.isNotBlank(sysUsers.getName()), SysUsers::getName, sysUsers.getName());
        wrapper.eq(StringUtils.isNotBlank(sysUsers.getPhone()), SysUsers::getPhone, sysUsers.getPhone());
        wrapper.eq(StringUtils.isNotBlank(sysUsers.getSex()), SysUsers::getSex, sysUsers.getSex());
        wrapper.eq(StringUtils.isNotBlank(sysUsers.getEmail()), SysUsers::getEmail, sysUsers.getEmail());
        wrapper.eq(StringUtils.isNotBlank(sysUsers.getEnabled()), SysUsers::getEnabled, sysUsers.getEnabled());
        return this.list(wrapper);
    }

    /**
     * 批量删除用户信息信息
     *
     * @param ids
     */
    @Override
    public void removeUsers(List<Long> ids) {
        if (ids==null || ids.size()<=0){
            throw new CustomException("删除失败，请选择要删除的数据");
        }
        // 判断当前用户下是否有角色绑定
        LambdaQueryWrapper<SysUserRoles> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUserRoles::getUserId,ids);
        List<SysUserRoles> list = sysUserRolesService.list(wrapper);
        if ( list.size()>0){
            // 批量查询，若查到数据证明当前用户存在角色绑定，则不能删除
            throw new CustomException("删除失败，当前用户下存在角色绑定，请先解除绑定");
        }
        //没有角色绑定，进行批量删除
        this.removeBatchByIds(ids);
        for (Long id : ids) {
            // 删除缓存中当前用户的角色信息和权限信息
            SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.ROLES_CACHE + id);
            SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.PERMISSIONS_CACHE + id);
        }

    }
}
