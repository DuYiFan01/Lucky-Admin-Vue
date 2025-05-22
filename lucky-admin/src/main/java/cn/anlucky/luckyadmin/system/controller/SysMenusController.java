package cn.anlucky.luckyadmin.system.controller;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysMenus;
import cn.anlucky.luckyadmin.system.service.SysMenusService;
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
 * 菜单信息控制器
 *
 * @author yifan.du
 * @since 2025-02-06 10:00:06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/sysMenus")
public class SysMenusController extends BaseController {

    private final SysMenusService sysMenusService;

    /**
     * 指定ID查询 菜单信息信息
     *
     * @param id 主键ID
     * @return SysMenus
     */
    @SaCheckPermission("system::menus::query")
    @GetMapping("/get/{id}")
    public R getById(@PathVariable(name = "id") Serializable id) {
        SysMenus sysMenus = sysMenusService.getById(id);
        return R.ok(sysMenus);
    }

    /**
     * 查询所有菜单信息信息
     *
     * @return List<SysMenus>
     */
    @SaCheckPermission("system::menus::query")
    @PostMapping("/list")
    public R list(@RequestBody SysMenus sysMenus) {
        List<SysMenus> list = sysMenusService.getMenuList(sysMenus);
        return R.ok(list);
    }

    /**
     * 条件分页查询菜单信息信息
     *
     * @param sysMenus 菜单信息
     * @return List<SysMenus>
     */
    @SaCheckPermission("system::menus::query")
    @PostMapping("/pageByParams")
    public R pageByParams(@RequestBody SysMenus sysMenus) {
        startPage();
        List<SysMenus> list = sysMenusService.pageByParams(sysMenus);
        PageDataVo tableData = getTableData(list);
        return R.ok(tableData);
    }

    /**
     * 新增菜单信息信息
     *
     * @param sysMenus 菜单信息
     * @return 添加成功
     */
    @SaCheckPermission("system::menus::insert")
    @Log(title = "菜单信息", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public R save(@RequestBody SysMenus sysMenus) {
        if (sysMenusService.getById(sysMenus.getId()) != null) {
            throw new CustomException("ID已存在");
        }
        sysMenusService.save(sysMenus);
        return R.ok("添加成功");
    }

    /**
     * 根据ID修改菜单信息信息
     *
     * @param sysMenus 菜单信息
     * @return 修改成功
     */
    @SaCheckPermission("system::menus::update")
    @Log(title = "菜单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateById")
    public R updateById(@RequestBody SysMenus sysMenus) {
        sysMenusService.updateMenuById(sysMenus);
        return R.ok("修改成功");
    }

    /**
     * 批量删除和删除菜单信息信息
     *
     * @param ids 主键ID数组
     * @return 删除成功
     */
    @SaCheckPermission("system::menus::delete")
    @Log(title = "菜单信息", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{ids}")
    public R deleteByIds(@PathVariable(name = "ids") Serializable[] ids) {
        if (ids.length <= 0) {
            throw new CustomException("请选择要删除的数据");
        }
        sysMenusService.removeBatchByIds(Arrays.asList(ids));
        return R.ok("删除成功");
    }

}
