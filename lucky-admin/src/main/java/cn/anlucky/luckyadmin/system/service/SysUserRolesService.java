package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysUserRoles;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户角色关联表 服务类
 *
 * @author yifan.du
 * @since 2025-01-16 15:52:20
 */
public interface SysUserRolesService extends IService<SysUserRoles> {

    /**
     * 条件分页查询用户角色关联表信息
     *
     * @param sysUserRoles
     * @return List<SysUserRoles>
     */
    public List<SysUserRoles> pageByParams(SysUserRoles sysUserRoles);


    /**
     * 获取当前角色绑定的用户
     * @param roleId
     * @return
     */
    public List<SysUserRoles> getRoleHasUser(Long roleId);

    /**
     * 获取角色绑定的用户ID列表
     * @return
     */
    public List<Long> getRoleHasUserToIds(Long roleId);

}
