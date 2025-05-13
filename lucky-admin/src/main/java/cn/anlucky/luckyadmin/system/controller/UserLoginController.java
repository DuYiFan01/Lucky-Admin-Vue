package cn.anlucky.luckyadmin.system.controller;


import cn.anlucky.luckyadmin.system.annotation.Log;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysFiles;
import cn.anlucky.luckyadmin.system.pojo.SysUsers;
import cn.anlucky.luckyadmin.system.service.UserLoginService;
import cn.anlucky.luckyadmin.system.vo.PasswordUpdateVo;
import cn.anlucky.luckyadmin.system.vo.RouterVo;
import cn.anlucky.luckyadmin.system.vo.UserLoginVo;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import cn.anlucky.luckyadmin.vo.R;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 登录注册Controller
 */
@Tag(name = "登录注册Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserLoginController {

    private final UserLoginService userLoginService;

    /**
     * 登录
     *
     * @param userLoginVo 用户登录信息
     * @return LoginVo
     */
    @PostMapping("/login")
    public R login(@RequestBody UserLoginVo userLoginVo) {
        return R.ok(userLoginService.login(userLoginVo));
    }

    /**
     * 注册
     *
     * @param sysUsers 用户信息
     * @return LoginVo
     */
    @PostMapping("/register")
    public R register(@RequestBody SysUsers sysUsers) {
        return R.ok(userLoginService.register(sysUsers));
    }

    /**
     * 注销登录
     * @return
     */
    @GetMapping("/logout")
    public R logout() {
        SaUtils.logout();
        return R.ok("登出成功");
    }


    /**
     * 获取用户信息
     *
     * @param token token
     * @return UserInfo
     */
    @GetMapping("/info")
    public R info(@Param("token") String token) {
        return R.ok(userLoginService.getUserInfo(token));
    }

    /**
     * 修改用户信息
     *
     * @param sysUsers 用户信息
     * @return sysUsers
     */
    @Log(title = "修改资料", businessType = BusinessType.UPDATE)
    @PostMapping("/updateInfo")
    public R updateInfo(@RequestBody SysUsers sysUsers) {
        userLoginService.updateUserInfo(sysUsers);
        return R.ok("修改成功");
    }

    /**
     * 修改用户密码
     *
     * @param passwordUpdateVo 用户密码信息
     * @return sysUsers
     */
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    @PostMapping("/updatePassword")
    public R updatePasswrod(@RequestBody PasswordUpdateVo passwordUpdateVo) {
        userLoginService.updateUserPasswrod(passwordUpdateVo);
        return R.ok("修改成功");
    }

    /**
     * 获取路由
     *
     * @return R
     */
    @GetMapping("/getRouters")
    public R getRouters() {
        Long userId = SaUtils.getLoginIdAsLong();
        List<RouterVo> routers = userLoginService.getRouters(userId);
        return R.ok(routers);
    }

    /**
     * 获取App路由
     *
     * @return R
     */
    @GetMapping("/getAppRouters")
    public R getAppRouters() {
        Long userId = SaUtils.getLoginIdAsLong();
        List<RouterVo> routers = userLoginService.getAppRouters(userId);
        return R.ok(routers);
    }

    /**
     * 获取用户可分配角色信息
     *
     * @param userId
     * @return
     */
    @SaCheckPermission("system::users::auth")
    @GetMapping("/getAuthUser/{userId}")
    public R getAuthUser(@PathVariable(name = "userId") Long userId) {
        return R.ok(userLoginService.getAuthUser(userId));
    }

    /**
     * 用户分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @SaCheckPermission("system::users::auth")
    @Log(title = "分配角色", businessType = BusinessType.UPDATE)
    @PostMapping("/saveAuthUser/{userId}")
    public R saveAuthUser(@PathVariable(name = "userId") String userId, @RequestBody List<Long> roleIds) {
        userLoginService.saveAuthUser(Long.parseLong(userId), roleIds);
        return R.ok("分配成功");
    }

    /**
     * 获取角色可分配的用户
     *
     * @param roleId
     * @return
     */
    @SaCheckPermission("system::roles::auth")
    @GetMapping("/getAuthRole/{roleId}")
    public R getAuthRole(@PathVariable(name = "roleId") Long roleId) {
        return R.ok(userLoginService.getAuthRole(roleId));
    }

    /**
     * 获取角色可分配的用户
     *
     * @param roleId
     * @return
     */
    @SaCheckPermission("system::roles::auth")
    @PostMapping("/saveAuthRole/{roleId}")
    public R saveAuthRole(@PathVariable(name = "roleId") String roleId, @RequestBody List<Long> userIds) {
        userLoginService.saveAuthRole(Long.valueOf(roleId), userIds);
        return R.ok("分配成功");
    }


    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public R uploadUserAvatar(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            throw new RuntimeException("上传文件不能为空");
        }
        SysFiles sysFiles = userLoginService.uploadUserAvatar(file);
        return R.ok("上传成功",sysFiles.getStoragePath());
    }





}
