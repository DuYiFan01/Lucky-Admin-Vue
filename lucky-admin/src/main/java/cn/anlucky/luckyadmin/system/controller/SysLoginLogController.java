package cn.anlucky.luckyadmin.system.controller;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysLoginLog;
import cn.anlucky.luckyadmin.system.service.SysLoginLogService;
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
 * 登录日志控制器
 *
 * @author yifan.du
 * @since 2025-02-20 09:28:37
 */
@RestController
@Tag(name = "登录日志Controller")
@RequiredArgsConstructor
@RequestMapping("/system/sysLoginLog")
public class SysLoginLogController extends BaseController {

    private final SysLoginLogService sysLoginLogService;

    /**
    * 指定ID查询 登录日志信息
    * @param id 主键ID
    * @return SysLoginLog
    */
    @Operation(summary = "id查询一个SysLoginLog")
    @SaCheckPermission("system::logs::loginlog::query")
    @GetMapping("/get/{id}")
    public R getById(@PathVariable(name = "id") Serializable id) {
        SysLoginLog sysLoginLog = sysLoginLogService.getById(id);
        return R.ok(sysLoginLog);
    }

    /**
     * 查询所有登录日志信息
     * @return List<SysLoginLog>
     */
    @Operation(summary = "查询所有SysLoginLog信息")
    @SaCheckPermission("system::logs::loginlog::query")
    @PostMapping("/list")
    public R list() {
        List<SysLoginLog> list = sysLoginLogService.list();
        return R.ok(list);
    }

    /**
     * 条件分页查询登录日志信息
     * @param sysLoginLog 登录日志
     * @return List<SysLoginLog>
     */
    @Operation(summary = "条件分页查询SysLoginLog信息")
    @SaCheckPermission("system::logs::loginlog::query")
    @PostMapping("/pageByParams")
    public R pageByParams(@RequestBody SysLoginLog sysLoginLog) {
        startPage();
        List<SysLoginLog> list = sysLoginLogService.pageByParams(sysLoginLog);
        PageDataVo tableData = getTableData(list);
        return R.ok(tableData);
    }

    /**
     * 新增登录日志信息
     * @param sysLoginLog 登录日志
     * @return 添加成功
     */
    @Operation(summary = "新增SysLoginLog信息")
    @SaCheckPermission("system::logs::loginlog::insert")
    @Log(title = "登录日志", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public R save(@RequestBody SysLoginLog sysLoginLog) {
        if(sysLoginLogService.getById(sysLoginLog.getId())!=null){
            throw new CustomException("ID已存在");
        }
        sysLoginLogService.save(sysLoginLog);
        return R.ok("添加成功");
    }
    /**
     * 根据ID修改登录日志信息
     * @param sysLoginLog 登录日志
     * @return 修改成功
     */
    @Operation(summary = "修改SysLoginLog信息")
    @SaCheckPermission("system::logs::loginlog::update")
    @Log(title = "登录日志", businessType = BusinessType.UPDATE)
    @PostMapping("/updateById")
    public R updateById(@RequestBody SysLoginLog sysLoginLog) {
        sysLoginLogService.updateById(sysLoginLog);
        return R.ok("修改成功");
    }

    /**
    * 批量删除和删除登录日志信息
    * @param ids 主键ID数组
    * @return 删除成功
    */
    @Operation(summary = "批量删除和删除SysLoginLog信息")
    @SaCheckPermission("system::logs::loginlog::delete")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{ids}")
    public R deleteByIds(@PathVariable(name = "ids") Serializable[] ids) {
        if (ids.length <= 0){
            throw new CustomException("请选择要删除的数据");
        }
        sysLoginLogService.removeBatchByIds(Arrays.asList(ids));
        return R.ok("删除成功");
    }

}
