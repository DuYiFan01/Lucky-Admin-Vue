package cn.anlucky.luckyadmin.system.controller;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysUsers;
import cn.anlucky.luckyadmin.system.service.SysUsersService;
import cn.anlucky.luckyadmin.system.service.UserLoginService;
import cn.anlucky.luckyadmin.utils.page.vo.PageDataVo;
import cn.anlucky.luckyadmin.vo.R;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import cn.anlucky.luckyadmin.system.base.controller.BaseController;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import cn.anlucky.luckyadmin.system.annotation.Log;

/**
 * 用户信息控制器
 *
 * @author yifan.du
 * @since 2025-02-17 13:33:37
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/sysUsers")
public class SysUsersController extends BaseController {

    private final SysUsersService sysUsersService;

    private final UserLoginService userLoginService;

    /**
     * 指定ID查询 用户信息信息
     *
     * @param id 主键ID
     * @return SysUsers
     */
    @SaCheckPermission("system::users::query")
    @GetMapping("/get/{id}")
    public R getById(@PathVariable(name = "id") Serializable id) {
        SysUsers sysUsers = sysUsersService.getById(id);
        return R.ok(sysUsers);
    }

    /**
     * 查询所有用户信息信息
     *
     * @return List<SysUsers>
     */
    @SaCheckPermission("system::users::query")
    @PostMapping("/list")
    public R list() {
        List<SysUsers> list = sysUsersService.list();
        return R.ok(list);
    }

    /**
     * 条件分页查询用户信息信息
     *
     * @param sysUsers 用户信息
     * @return List<SysUsers>
     */
    @SaCheckPermission("system::users::query")
    @PostMapping("/pageByParams")
    public R pageByParams(@RequestBody SysUsers sysUsers) {
        startPage();
        List<SysUsers> list = sysUsersService.pageByParams(sysUsers);
        PageDataVo tableData = getTableData(list);
        return R.ok(tableData);
    }

    /**
     * 新增用户信息信息
     *
     * @param sysUsers 用户信息
     * @return 添加成功
     */
    @SaCheckPermission("system::users::insert")
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public R save(@RequestBody SysUsers sysUsers) {
        if (sysUsersService.getById(sysUsers.getDelFlag()) != null) {
            throw new CustomException("ID已存在");
        }
        userLoginService.register(sysUsers);
        return R.ok("添加成功");
    }

    /**
     * 根据ID修改用户信息信息
     *
     * @param sysUsers 用户信息
     * @return 修改成功
     */
    @SaCheckPermission("system::users::update")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateById")
    public R updateById(@RequestBody SysUsers sysUsers) {
        sysUsersService.updateById(sysUsers);
        return R.ok("修改成功");
    }

    /**
     * 批量删除和删除用户信息信息
     *
     * @param ids 主键ID数组
     * @return 删除成功
     */
    @SaCheckPermission("system::users::delete")
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{ids}")
    public R deleteByIds(@PathVariable(name = "ids") Long[] ids) {
        sysUsersService.removeUsers(Arrays.asList(ids));
        return R.ok("删除成功");
    }

}
