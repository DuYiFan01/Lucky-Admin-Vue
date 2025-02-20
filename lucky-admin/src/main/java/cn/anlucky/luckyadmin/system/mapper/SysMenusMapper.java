package cn.anlucky.luckyadmin.system.mapper;

import cn.anlucky.luckyadmin.system.pojo.SysMenus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * <p>
 * 菜单信息 Mapper 接口
 * </p>
 *
 * @author yifan.du
 * @since 2025-01-16 16:02:54
 */
public interface SysMenusMapper extends BaseMapper<SysMenus> {

    /**
     * 根据角色id查询路由
     * @param roleIds
     * @return
     */
    List<SysMenus> getRouters(List<Long> roleIds);

    /**
     * 根据角色id查询路由
     * @param roleIds
     * @return
     */
    List<SysMenus> getRoutersByRoleId(Long roleId);

}
