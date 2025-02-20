package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysMenus;
import cn.anlucky.luckyadmin.system.vo.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单信息 服务类
 *
 * @author yifan.du
 * @since 2025-01-16 16:02:54
 */
public interface SysMenusService extends IService<SysMenus> {


    /**
     * 条件分页查询菜单信息信息
     *
     * @param sysMenus
     * @return List<SysMenus>
     */
    public List<SysMenus> pageByParams(SysMenus sysMenus);

    /**
     * 根据角色id获取路由信息
     *
     * @param roleIds
     * @return
     */
    public List<RouterVo> getRouters(List<Long> roleIds);

    /**
     * 获取所有菜单
     *
     * @param sysMenus
     * @return
     */
    public List<SysMenus> getMenuList(SysMenus sysMenus);

    /**
     * 修改菜单信息
     * @param sysMenus
     * @return
     */
    public boolean updateMenuById(SysMenus sysMenus);


}
