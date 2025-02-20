package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.config.LuckyConfig;
import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.constant.Constants;
import cn.anlucky.luckyadmin.system.manager.AsyncManager;
import cn.anlucky.luckyadmin.system.manager.factory.AsyncFactory;
import cn.anlucky.luckyadmin.system.pojo.SysMenus;
import cn.anlucky.luckyadmin.system.pojo.SysUserRoles;
import cn.anlucky.luckyadmin.system.pojo.SysUsers;
import cn.anlucky.luckyadmin.system.service.*;
import cn.anlucky.luckyadmin.system.vo.*;
import cn.anlucky.luckyadmin.utils.DateUtils;
import cn.anlucky.luckyadmin.utils.StringUtils;
import cn.anlucky.luckyadmin.utils.ip.IpUtils;
import cn.anlucky.luckyadmin.utils.satoken.PasswordEncode;
import cn.anlucky.luckyadmin.utils.satoken.SaTokenDaoUtils;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import cn.dev33.satoken.stp.SaLoginModel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户登录服务实现类
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private SysUsersService sysUsersService;
    @Autowired
    private SysRolesService sysRolesService;
    @Autowired
    private SysMenusService sysMenusService;
    @Autowired
    private SysUserRolesService sysUserRolesService;
    @Autowired
    private LuckyConfig luckyConfig;


    /**
     * 用户登录
     *
     * @param userLoginVo 用户登录信息
     * @return 用户信息
     */
    @Override
    public LoginVo login(UserLoginVo userLoginVo) {
        LoginVo loginvo = new LoginVo();
        try {
            // 校验用户信息
            validateUserLoginVo(userLoginVo);

            // 校验验证码
            validateCaptcha(userLoginVo.getUsername(), userLoginVo.getCode(), userLoginVo.getUuid());

            // 查询用户信息
            SysUsers users = sysUsersService.getOne(new LambdaQueryWrapper<SysUsers>().eq(SysUsers::getUsername, userLoginVo.getUsername()));

            if (users == null) {
                throw new CustomException("用户名或密码错误");
            }
            // 校验密码
            if (!PasswordEncode.matches(userLoginVo.getPassword(), users.getPassword())) {
                throw new CustomException("用户名或密码错误");
            }
            // 登录成功
            // 设置登录Token携带信息
            SaLoginModel saLoginModel = new SaLoginModel();
            saLoginModel.setExtra("id", users.getId());
            saLoginModel.setExtra("username", users.getUsername());
            // 携带参数登录
            SaUtils.login(users.getId(), saLoginModel);
            // 设置返回响应
            loginvo.setId(users.getId());
            loginvo.setUsername(users.getUsername());
            loginvo.setToken(SaUtils.getToken());
            // 记录成功日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userLoginVo.getUsername(), Constants.LOGIN_SUCCESS, "登录成功"));
            // 记录登录信息 登录IP和最后登录时间
            recordLoginInfo(users.getId());
        } catch (CustomException e) {
            // 记录失败日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userLoginVo.getUsername(), Constants.LOGIN_FAIL, e.getMessage()));
            throw new CustomException(e.getMessage());
        }
        return loginvo;
    }

    /**
     * 用户注册
     *
     * @param sysUser 用户登录信息
     * @return 用户信息
     */
    @Override
    public SysUsers register(SysUsers sysUser) {
        if (sysUser == null) {
            throw new CustomException("用户信息不能为空");
        }
        if (sysUser.getUsername() == null || sysUser.getUsername().trim().isEmpty()) {
            throw new CustomException("用户名不能为空");
        }
        if (sysUser.getPassword() == null || sysUser.getPassword().trim().isEmpty()) {
            // 如果密码为空，设置默认密码为 1234567
            sysUser.setPassword(PasswordEncode.encode("1234567"));
        }
        if (sysUser.getName() == null || sysUser.getName().trim().isEmpty()) {
            throw new CustomException("昵称不能为空");
        }
        if (sysUser.getSex() == null || sysUser.getSex().trim().isEmpty()) {
            throw new CustomException("性别不能为空");
        }
        sysUsersService.save(sysUser);
        return sysUser;
    }

    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public UserInfoVo getUserInfo(String token) {
        Long userId = null;
        // 如果token为空，则获取当前登录用户
        if (StringUtils.isBlank(token)) {
            userId = SaUtils.getLoginIdAsLong();
        } else {
            userId = SaUtils.getLoginIdByToken(token);
        }
        // 创建视图对象
        UserInfoVo userInfoVo = new UserInfoVo();
        // 查询用户名称
        SysUsers users = sysUsersService.getById(userId);
        // 查询用户角色代码
        List<String> rolesCode = sysRolesService.getRolesCodeByUserId(userId);
        // 查询用户权限
        List<String> permission = sysRolesService.getPermissionByUserId(userId);
        // 设置响应值
        userInfoVo.setId(users.getId());
        userInfoVo.setUsername(users.getUsername());
        userInfoVo.setName(users.getName());
        userInfoVo.setSex(users.getSex());
        userInfoVo.setEmail(users.getEmail());
        userInfoVo.setPhone(users.getPhone());
        userInfoVo.setAvatar(users.getAvatar());
        userInfoVo.setCreateTime(users.getCreateTime());
        userInfoVo.setRoles(rolesCode);
        userInfoVo.setPermissions(permission);
        return userInfoVo;
    }

    /**
     * 获取当前登录用户的信息
     *
     * @return UserInfoVo 用户信息
     */
    @Override
    public UserInfoVo getUserInfo() {
        return this.getUserInfo(null);
    }

    /**
     * 更新用户信息
     *
     * @param sysUsers 用户信息
     * @return SysUsers
     */
    @Override
    public SysUsers updateUserInfo(SysUsers sysUsers) {
        if (sysUsers == null) {
            throw new CustomException("用户信息不能为空");
        }
        if (sysUsers.getId() == null || sysUsers.getId() == 0) {
            throw new CustomException("用户ID不能为空");
        }
        if (sysUsers.getName() == null || sysUsers.getName().trim().isEmpty()) {
            throw new CustomException("昵称不能为空");
        }
        if (sysUsers.getEmail() == null || sysUsers.getEmail().trim().isEmpty()) {
            throw new CustomException("邮箱不能为空");
        }
        if (sysUsers.getSex() == null || sysUsers.getSex().trim().isEmpty()) {
            throw new CustomException("性别不能为空");
        }
        sysUsersService.updateById(sysUsers);
        return sysUsers;
    }

    /**
     * 更新用户密码
     *
     * @param passwordUpdateVo 用户信息
     * @return SysUsers
     */
    @Override
    public SysUsers updateUserPasswrod(PasswordUpdateVo passwordUpdateVo) {
        if (passwordUpdateVo == null) {
            throw new CustomException("用户信息不能为空");
        }
        if (passwordUpdateVo.getId() == null || passwordUpdateVo.getId() == 0) {
            throw new CustomException("用户ID不能为空");
        }
        if (passwordUpdateVo.getOldPassword() == null || passwordUpdateVo.getOldPassword().trim().isEmpty()) {
            throw new CustomException("旧密码不能为空");
        }
        if (passwordUpdateVo.getNewPassword() == null || passwordUpdateVo.getNewPassword().trim().isEmpty()) {
            throw new CustomException("新密码不能为空");
        }
        if (passwordUpdateVo.getConfirmPassword() == null || passwordUpdateVo.getConfirmPassword().trim().isEmpty()) {
            throw new CustomException("确认密码不能为空");
        }
        if (!passwordUpdateVo.getNewPassword().equals(passwordUpdateVo.getConfirmPassword())) {
            throw new CustomException("新密码与确认密码不一致");
        }
        SysUsers users = sysUsersService.getById(passwordUpdateVo.getId());
        if (!PasswordEncode.matches(passwordUpdateVo.getOldPassword(), users.getPassword())) {
            throw new CustomException("旧密码错误");
        }
        if (PasswordEncode.matches(passwordUpdateVo.getNewPassword(), users.getPassword())) {
            throw new CustomException("新密码与旧密码不能相同");
        }
        SysUsers sysUsers = new SysUsers();
        sysUsers.setId(passwordUpdateVo.getId());
        String password = PasswordEncode.encode(passwordUpdateVo.getNewPassword());
        sysUsers.setPassword(password);
        sysUsersService.updateById(sysUsers);
        users.setPassword(sysUsers.getPassword());
        SaUtils.logout(users.getId());
        return users;
    }

    /**
     * 获取当前登录用户的路由信息
     *
     * @param userId 用户的ID
     * @return 路由信息
     */
    @Override
    public List<RouterVo> getRouters(Long userId) {
        // 获取当前用户的角色代码
        List<Long> rolesids = sysRolesService.getRolesIdByUserId(userId);
        // 获取树形菜单信息 此时并没有构建为前端可以识别的路由信息
        List<RouterVo> routers = sysMenusService.getRouters(rolesids);
        // 构建路由信息
        return routers;
    }

    /**
     * 获取用户可分配角色
     *
     * @param userId
     * @return
     */
    @Override
    public AuthUserVo getAuthUser(Long userId) {
        AuthUserVo authUserVo = new AuthUserVo();
        if (userId == null || userId == 0) {
            throw new CustomException("用户ID不能为空");
        }
        authUserVo.setUserId(userId);
        authUserVo.setRolesAll(sysRolesService.list());
        authUserVo.setHasRoles(sysRolesService.getRolesByUserId(userId).getRoles());
        return authUserVo;
    }

    /**
     * 保存分配角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色ID
     */
    @Transactional
    @Override
    public void saveAuthUser(Long userId, List<Long> roleIds) {
        if (userId == null) {
            throw new CustomException("用户ID不能为空");
        }
        // 删除当前用户的所有角色
        sysUserRolesService.removeById(userId);
        // 添加当前用户的角色
        List<SysUserRoles> list = new ArrayList<>();
        roleIds.forEach(roleId -> {
            SysUserRoles sysUserRoles = new SysUserRoles();
            sysUserRoles.setUserId(userId);
            sysUserRoles.setRoleId(roleId);
            list.add(sysUserRoles);
        });
        // 批量插入
        sysUserRolesService.saveBatch(list);
        // 删除缓存中当前用户的角色信息
        SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.ROLES_CACHE + userId);
    }

    /**
     * 获取角色可分配的用户
     *
     * @param roleId
     * @return
     */
    @Override
    public AuthRoleVo getAuthRole(Long roleId) {
        AuthRoleVo authRoleVo = new AuthRoleVo();
        if (roleId == null) {
            throw new CustomException("角色ID不能为空");
        }
        authRoleVo.setRoleId(roleId);
        authRoleVo.setUsersAll(sysUsersService.list());
        authRoleVo.setHasUsersIds(sysUserRolesService.getRoleHasUserToIds(roleId));
        return authRoleVo;
    }

    /**
     * 保存分配用户信息
     *
     * @param roleId
     * @param userIds
     */
    @Transactional
    @Override
    public void saveAuthRole(Long roleId, List<Long> userIds) {
        if (roleId == null){
            throw new CustomException("角色ID不能为空");
        }
        LambdaQueryWrapper<SysUserRoles> wrapper = new LambdaQueryWrapper<>();
        // 删除当前角色的所有用户
        wrapper.eq(SysUserRoles::getRoleId, roleId);
        List<Long> deleteUserId = sysUserRolesService.list(wrapper).stream().map(SysUserRoles::getUserId).toList();
        sysUserRolesService.remove(wrapper);
        // 添加当前用户的角色
        List<SysUserRoles> list = new ArrayList<>();
        userIds.forEach(userId -> {
            SysUserRoles sysUserRoles = new SysUserRoles();
            sysUserRoles.setUserId(userId);
            sysUserRoles.setRoleId(roleId);
            list.add(sysUserRoles);
        });
        // 批量插入
        sysUserRolesService.saveBatch(list);
        // 删除缓存中当前用户的角色信息.
        /**
         * 这里会使得缓存一下失效很大一部分
         * 理解例子： 如有用户ID：1、2、3、4、5、6
         * 当前角色ID为1，当前角色绑定了1,2,3用户
         * 当修改当前角色的用户为2 4 6用户
         * 那么被删除缓存的用户为：1,3,4,6
         * 1,3 被删除 因为1，3减少了角色  这里删除前使用查询可获得全部
         * 4,6 被删除 因为4，6增加了角色  这里取得传入的用户ID
         * 则 两个列表中重复的即变的用户不需要删除缓存
         *
         * 解决方式：
         * 两个集合取交集，取得重复的数据
         * 两个集合合并，并且删除交集部分为当前需要删除缓存的部分
         */
        List<Long> deleteIds = Stream.concat(deleteUserId.stream(), userIds.stream())
                .filter(x -> !(deleteUserId.contains(x) && userIds.contains(x)))
                .distinct() // 确保结果集中没有重复的元素
                .collect(Collectors.toList());
        // 删除缓存
        deleteIds.forEach(id -> {
            SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.ROLES_CACHE + id);
        });
    }


    /**
     * 校验用户信息
     *
     * @param userLoginVo 用户登录信息
     */
    private void validateUserLoginVo(UserLoginVo userLoginVo) {
        if (userLoginVo == null) {
            throw new CustomException("用户登录信息不能为空");
        }
        if (userLoginVo.getUsername() == null || userLoginVo.getPassword() == null) {
            throw new CustomException("用户名或密码不能为空");
        }
        if (userLoginVo.getUsername().trim().isEmpty() || userLoginVo.getPassword().trim().isEmpty()) {
            throw new CustomException("用户名或密码不能为空");
        }
    }


    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    private void validateCaptcha(String username, String code, String uuid) {
        Boolean captchaEnabled = luckyConfig.getCaptchaEnabled();
        if (!captchaEnabled) {
            // 如果验证码是关闭状态
            return;
        }
        // 获取缓存中的验证码
        String captcha = (String) SaTokenDaoUtils.getObjectKey(SaTokenDaoUtils.CAPTCHA_CACHE + uuid);
        // 验证验证码
        if (captcha == null) {
            throw new CustomException("验证码已过期");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            // 验证码不正确
            throw new CustomException("验证码不正确");
        }
        // 验证码验证正确后删除验证码
        SaTokenDaoUtils.deleteObjectKey(SaTokenDaoUtils.CAPTCHA_CACHE + uuid);
    }


    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     */
    private void recordLoginInfo(Long userId) {
        SysUsers sysUsers = new SysUsers();
        sysUsers.setId(userId);
        sysUsers.setLoginIp(IpUtils.getIpAddr());
        sysUsers.setLoginTime(LocalDateTime.now());
        sysUsers.setUpdateBy("system");
        sysUsersService.updateById(sysUsers);
    }

}
