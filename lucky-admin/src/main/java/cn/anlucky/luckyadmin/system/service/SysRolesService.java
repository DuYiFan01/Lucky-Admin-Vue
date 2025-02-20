package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysRoles;
import cn.anlucky.luckyadmin.system.vo.RolesVo;
import cn.anlucky.luckyadmin.system.vo.SysRolesVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色信息 服务类
 *
 * @author yifan.du
 * @since 2025-01-16 15:50:19
 */
public interface SysRolesService extends IService<SysRoles> {

    /**
     * 条件分页查询角色信息信息
     *
     * @param sysRoles
     * @return List<SysRoles>
     */
    public List<SysRoles> pageByParams(SysRoles sysRoles);


    /**
     * 根据用户id查询角色信息
     *
     * @param userId
     * @return
     */
    public RolesVo getRolesByUserId(Long userId);

    /**
     * 根据用户id查询拥有的角色代码
     *
     * @param userId
     * @return
     */
    public List<String> getRolesCodeByUserId(Long userId);

    /**
     * 根据用户id查询拥有的角色Id
     *
     * @param userId
     * @return
     */
    public List<Long> getRolesIdByUserId(Long userId);

    /**
     * 根据用户ID查询当前用户拥有的权限信息
     *
     * @param userId 用户ID
     * @return 权限集合列表
     */
    public List<String> getPermissionByUserId(Long userId);

    /**
     * 保存角色信息
     *
     * @param sysRolesVo
     */
    public void saveRoles(SysRolesVo sysRolesVo);

    /**
     * 获取角色信息
     *
     * @param id
     * @return
     */
    public SysRolesVo getRolesVoById(Long id);

    /**
     * 修改角色信息
     *
     * @param sysRolesVo
     * @return
     */
    public void updateByIdRolesVo(SysRolesVo sysRolesVo);

    /**
     * 删除角色信息
     * @param ids
     */
    public void removeRolesByIds(List<Long> ids);

}
