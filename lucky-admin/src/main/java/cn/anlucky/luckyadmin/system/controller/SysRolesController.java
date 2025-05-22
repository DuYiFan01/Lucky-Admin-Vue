package cn.anlucky.luckyadmin.system.controller;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysRoles;
import cn.anlucky.luckyadmin.system.service.SysRolesService;
import cn.anlucky.luckyadmin.system.vo.SysRolesVo;
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
 * 角色信息控制器
 *
 * @author yifan.du
 * @since 2025-02-17 17:56:51
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/sysRoles")
public class SysRolesController extends BaseController {

    private final SysRolesService sysRolesService;

    /**
     * 指定ID查询 角色信息信息
     *
     * @param id 主键ID
     * @return SysRoles
     */
    @SaCheckPermission("system::roles::query")
    @GetMapping("/get/{id}")
    public R getById(@PathVariable(name = "id") Long id) {
        SysRoles sysRoles = sysRolesService.getRolesVoById(id);
        return R.ok(sysRoles);
    }

    /**
     * 查询所有角色信息信息
     *
     * @return List<SysRoles>
     */
    @SaCheckPermission("system::roles::query")
    @PostMapping("/list")
    public R list() {
        List<SysRoles> list = sysRolesService.list();
        return R.ok(list);
    }

    /**
     * 条件分页查询角色信息信息
     *
     * @param sysRoles 角色信息
     * @return List<SysRoles>
     */
    @SaCheckPermission("system::roles::query")
    @PostMapping("/pageByParams")
    public R pageByParams(@RequestBody SysRoles sysRoles) {
        startPage();
        List<SysRoles> list = sysRolesService.pageByParams(sysRoles);
        PageDataVo tableData = getTableData(list);
        return R.ok(tableData);
    }

    /**
     * 新增角色信息信息
     *
     * @param sysRoles 角色信息
     * @return 添加成功
     */
    @SaCheckPermission("system::roles::insert")
    @Log(title = "角色信息", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public R save(@RequestBody SysRolesVo sysRolesVo) {
        if (sysRolesService.getById(sysRolesVo.getId()) != null) {
            throw new CustomException("ID已存在");
        }
        sysRolesService.saveRoles(sysRolesVo);
        return R.ok("添加成功");
    }

    /**
     * 根据ID修改角色信息信息
     *
     * @param sysRolesVo 角色信息
     * @return 修改成功
     */
    @SaCheckPermission("system::roles::update")
    @Log(title = "角色信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateById")
    public R updateById(@RequestBody SysRolesVo sysRolesVo) {
        sysRolesService.updateByIdRolesVo(sysRolesVo);
        return R.ok("修改成功");
    }

    /**
     * 根据ID修改角色信息信息
     *
     * @param sysRolesVo 角色信息
     * @return 修改成功
     */
    @SaCheckPermission("system::roles::update")
    @Log(title = "角色信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAppById")
    public R updateAppById(@RequestBody SysRolesVo sysRolesVo) {
        sysRolesService.updateAppByIdRolesVo(sysRolesVo);
        return R.ok("修改成功");
    }

    /**
     * 批量删除和删除角色信息信息
     *
     * @param ids 主键ID数组
     * @return 删除成功
     */
    @SaCheckPermission("system::roles::delete")
    @Log(title = "角色信息", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{ids}")
    public R deleteByIds(@PathVariable(name = "ids") Long[] ids) {
        if (ids.length <= 0) {
            throw new CustomException("请选择要删除的数据");
        }
        sysRolesService.removeRolesByIds(Arrays.asList(ids));
        return R.ok("删除成功");
    }

}
