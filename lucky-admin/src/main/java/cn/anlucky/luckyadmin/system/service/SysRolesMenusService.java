package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysRolesMenus;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
     /**
 * 角色和菜单关联表 服务类
 *
 * @author yifan.du
 * @since 2025-01-16 16:05:06
 */
public interface SysRolesMenusService extends IService<SysRolesMenus> {

    /**
     * 条件分页查询角色和菜单关联表信息
     * @param sysRolesMenus
     * @return List<SysRolesMenus>
     */
    public List<SysRolesMenus> pageByParams(SysRolesMenus sysRolesMenus);

}
