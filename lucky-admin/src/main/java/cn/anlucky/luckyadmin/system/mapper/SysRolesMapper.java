package cn.anlucky.luckyadmin.system.mapper;

import cn.anlucky.luckyadmin.system.pojo.SysRoles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * <p>
 * 角色信息 Mapper 接口
 * </p>
 *
 * @author yifan.du
 * @since 2025-01-16 15:50:19
 */
public interface SysRolesMapper extends BaseMapper<SysRoles> {

    /**
     * 根据用户ID查询角色信息
     * @param userId
     * @return
     */
    public List<SysRoles> getRolesByUserId(Long userId);

    /**
     * 根据用户ID查询权限信息
     * @param roleId
     * @return
     */
    public List<String> getPermissionByRoleIds(Long roleId);

    /**
     * 获取所有Web菜单ID
     * @return
     */
    public List<Long> getWebMenusId();

    /**
     * 获取所有App菜单ID
     * @return
     */
    public List<Long> getAppMenusId();

}
