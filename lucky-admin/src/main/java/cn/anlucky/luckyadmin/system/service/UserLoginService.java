package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysMenus;
import cn.anlucky.luckyadmin.system.pojo.SysUsers;
import cn.anlucky.luckyadmin.system.vo.*;

import java.util.List;

/**
 * 用户登录服务
 */
public interface UserLoginService {

    /**
     * 用户登录
     * @param userLoginVo 用户登录信息
     * @return 用户信息
     */
    public LoginVo login(UserLoginVo userLoginVo);

    /**
     * 用户注册
     * @param sysUser 用户登录信息
     * @return 用户信息
     */
    public SysUsers register(SysUsers sysUser);

    /**
     * 获取指定Token的信息
     * @param token 登录的Token
     * @return UserInfoVo 用户信息
     */
    public UserInfoVo getUserInfo(String token);

    /**
     * 获取当前登录用户的信息
     * @return UserInfoVo 用户信息
     */
    public UserInfoVo getUserInfo();

    /**
     * 更新用户信息
     * @param sysUsers 用户信息
     * @return SysUsers
     */
    public SysUsers updateUserInfo(SysUsers sysUsers);

    /**
     * 更新用户密码
     * @param passwordUpdateVo 用户信息
     * @return SysUsers
     */
    public SysUsers updateUserPasswrod(PasswordUpdateVo passwordUpdateVo);

    /**
     * 获取当前登录用户的路由信息
     * @param userId 用户的ID
     * @return 路由信息
     */
    public List<RouterVo> getRouters(Long userId);


    /**
     * 获取用户可分配角色
     * @param userId
     * @return
     */
    public AuthUserVo getAuthUser(Long userId);

    /**
     * 保存分配角色信息
     * @param userId 用户ID
     * @param roleIds 角色ID
     */
    public void saveAuthUser(Long userId, List<Long> roleIds);


    /**
     * 获取角色可分配的用户
     * @param roleId
     * @return
     */
    public AuthRoleVo getAuthRole(Long roleId);


    /**
     * 保存分配用户信息
     * @param roleId
     * @param userIds
     */
    public void saveAuthRole(Long roleId ,List<Long> userIds);


}
