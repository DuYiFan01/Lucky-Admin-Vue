package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.constant.Constants;
import cn.anlucky.luckyadmin.system.pojo.SysMenus;
import cn.anlucky.luckyadmin.system.mapper.SysMenusMapper;
import cn.anlucky.luckyadmin.system.service.SysMenusService;
import cn.anlucky.luckyadmin.system.vo.MetaVo;
import cn.anlucky.luckyadmin.system.vo.RouterVo;
import cn.anlucky.luckyadmin.utils.satoken.SaTokenDaoUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import cn.anlucky.luckyadmin.utils.StringUtils;

/**
 * 菜单信息 服务实现类
 *
 * @author yifan.du
 * @since 2025-01-16 16:02:54
 */
@Service
public class SysMenusServiceImp extends ServiceImpl<SysMenusMapper, SysMenus> implements SysMenusService {

    /**
     * 条件分页查询菜单信息信息
     *
     * @param sysMenus
     * @return List<SysMenus>
     */
    public List<SysMenus> pageByParams(SysMenus sysMenus) {
        if (sysMenus == null) {
            return this.list();
        }
        LambdaQueryWrapper<SysMenus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getTitle()), SysMenus::getTitle, sysMenus.getTitle());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getName()), SysMenus::getName, sysMenus.getName());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getPath()), SysMenus::getPath, sysMenus.getPath());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getComponent()), SysMenus::getComponent, sysMenus.getComponent());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getQuery()), SysMenus::getQuery, sysMenus.getQuery());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getMenuType()), SysMenus::getMenuType, sysMenus.getMenuType());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getRoles()), SysMenus::getRoles, sysMenus.getRoles());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getIcon()), SysMenus::getIcon, sysMenus.getIcon());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getCreateBy()), SysMenus::getCreateBy, sysMenus.getCreateBy());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getUpdateBy()), SysMenus::getUpdateBy, sysMenus.getUpdateBy());
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getRemark()), SysMenus::getRemark, sysMenus.getRemark());
        return this.list(wrapper);
    }

    /**
     * 根据角色id获取路由信息
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<RouterVo> getRouters(List<Long> roleIds) {
        List<SysMenus> menusTree = this.getMenusTree(roleIds,0);
        return buildRouters(menusTree);
    }

    /**
     * 获取所有App路由
     * @param roleIds 角色id集合
     * @return
     */
    @Override
    public List<RouterVo> getAppRouters(List<Long> roleIds) {
        List<SysMenus> menusTree = this.getMenusTree(roleIds,-1);
        return buildRouters(menusTree);
    }

    /**
     * 获取所有菜单
     *
     * @param sysMenus
     * @return
     */
    @Override
    public List<SysMenus> getMenuList(SysMenus sysMenus) {
        if (sysMenus == null) {
            return this.list();
        }
        LambdaQueryWrapper<SysMenus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(sysMenus.getTitle()), SysMenus::getTitle, sysMenus.getTitle());
        wrapper.eq(sysMenus.getVisible()!=null, SysMenus::getVisible, sysMenus.getVisible());
        wrapper.orderByAsc(SysMenus::getSort);
        return this.list(wrapper);
    }

    /**
     * 修改菜单信息
     *
     * @param sysMenus
     * @return
     */
    @Override
    public boolean updateMenuById(SysMenus sysMenus) {
        if (sysMenus.getId() == null) {
            throw new CustomException("菜单ID不能为空");
        }
        this.updateById(sysMenus);
        // ToDO:这里会导致缓存大部分失效有缓存雪崩的风险
        // 如果修改了菜单
        List<String> list = null;
        if (Constants.TYPE_BUTTON.equals(sysMenus.getMenuType())){
            // 修改按钮则失效全部的权限
            list = SaTokenDaoUtils.searchData(SaTokenDaoUtils.PERMISSIONS_CACHE, 0L, this.count());
        }else {
            // 修改的是菜单/目录/外链/内链则失效路由
            list = SaTokenDaoUtils.searchData(SaTokenDaoUtils.ROUTER_CACHE, 0L, this.count());
        }
        list.forEach(key->{
            // 自带的删除功能添加的前缀这里使用原生的删除功能
            SaTokenDaoUtils.getSaTokenDao().deleteObject(key);
        });
        return true;
    }

        /**
     * 构建路由
     *
     * @param menus
     * @return
     */
    private List<RouterVo> buildRouters(List<SysMenus> menus) {
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenus menu : menus) {
            RouterVo routerVo = new RouterVo();
            routerVo.setPath(menu.getPath());
            routerVo.setName(menu.getName());
            routerVo.setHidden(menu.getVisible() == 0);
            routerVo.setAlwaysShow(getAlwaysShow(menu));
            routerVo.setMeta(new MetaVo(menu.getTitle(), menu.getIcon(), menu.getIsCache() == 0, getLink(menu)));
            routerVo.setRedirect(getRedirect(menu));
            routerVo.setComponent(getComponent(menu));
            routerVo.setChildren(buildRouters(menu.getChildren() == null ? new ArrayList<>() : menu.getChildren()));
            routers.add(routerVo);
        }
        return routers;
    }

    /**
     * 获取redirect
     *
     * @param menu
     * @return
     */
    private String getRedirect(SysMenus menu) {
        // 如果是目录
        if (Constants.TYPE_DIR.equals(menu.getMenuType())) {
            return "noRedirect";
        }
        return null;
    }

    /**
     * 获取外链地址
     *
     * @param menu
     * @return
     */
    private String getLink(SysMenus menu) {
        if (Constants.TYPE_W_URL.equals(menu.getMenuType())) {
            // 如果是外链
            return menu.getPath();
        }
        return null;
    }

    /**
     * 获取 AlwaysShow
     *
     * @param menu
     * @return
     */
    public boolean getAlwaysShow(SysMenus menu) {
        if (Constants.TYPE_DIR.equalsIgnoreCase(menu.getMenuType())) {
            return true;
        }
        return false;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenus menu) {
        // 多级目录使用ParentView
        if (Constants.TYPE_DIR.equalsIgnoreCase(menu.getMenuType()) && menu.getParentId() != 0) {
            // 如果是目录，且不是根目录使用ParentView
            return Constants.PARENT_VIEW;
        }
        // 如果是菜单
        if (Constants.TYPE_MENU.equalsIgnoreCase(menu.getMenuType())){
            return menu.getComponent();
        }
        if (Constants.TYPE_N_URL.equalsIgnoreCase(menu.getMenuType())){
            // 如果是内链
            return Constants.IFRAME;
        }
        return Constants.LAYOUT;
    }

    /**
     * 获取菜单树
     * @param roleIds 角色的id集合
     * @param parentId 根父级菜单id
     * @return
     */
    public List<SysMenus> getMenusTree(List<Long> roleIds, int parentId) {
        // 根据角色查询角色拥有的菜单，并查询routers中是否存在，若不存在则添加
        // 这个方案没有合适的方式添加缓存
        // List<SysMenus> menus = this.baseMapper.getRouters(roleIds);
        // return getChildPerms(menus, 0);
        // 根据角色Id循环查询当前角色的菜单
        // 1. 保证菜单顺序
        // 2. 保证唯一
        List<SysMenus> list = new ArrayList<>();
        roleIds.forEach(roleId -> {
            // 获取当前角色的菜单缓存
            List<SysMenus> menus  = (List<SysMenus>) SaTokenDaoUtils.getObjectKey(SaTokenDaoUtils.ROUTER_CACHE + roleId);
            if (menus == null){
                // 缓存中没有，从数据库中查询
                menus = this.baseMapper.getRoutersByRoleId(roleId);
                // 放入缓存 过期时间一天
                SaTokenDaoUtils.setObjectKey(SaTokenDaoUtils.ROUTER_CACHE + roleId, menus,SaTokenDaoUtils.DAY_TIMEOUT);
            }
            list.addAll(menus);
        });
        Set<Long> seenIds = new HashSet<>();
        List<SysMenus> menus = list.stream().filter(menu -> seenIds.add(menu.getId()))
                .sorted(Comparator.comparingInt(SysMenus::getSort))
                .collect(Collectors.toList());
        return getChildPerms(menus, parentId);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenus> getChildPerms(List<SysMenus> list, int parentId) {
        List<SysMenus> returnList = new ArrayList<>();
        for (Iterator<SysMenus> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenus t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysMenus> list, SysMenus t) {
        // 得到子节点列表
        List<SysMenus> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenus tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 获取子节点列表
     *
     * @param list
     * @param t
     * @return
     */
    private List<SysMenus> getChildList(List<SysMenus> list, SysMenus t) {
        List<SysMenus> tlist = new ArrayList<>();
        Iterator<SysMenus> it = list.iterator();
        while (it.hasNext()) {
            SysMenus n = it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     *
     * @param list
     * @param t
     * @return
     */
    private boolean hasChild(List<SysMenus> list, SysMenus t) {
        return getChildList(list, t).size() > 0;
    }

}
