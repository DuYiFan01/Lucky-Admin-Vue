package cn.anlucky.luckyadmin.system.controller;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysOperLog;
import cn.anlucky.luckyadmin.system.service.SysOperLogService;
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
 * 操作日志控制器
 *
 * @author yifan.du
 * @since 2025-02-20 09:28:37
 */
@RestController
@Tag(name = "操作日志Controller")
@RequiredArgsConstructor
@RequestMapping("/system/sysOperLog")
public class SysOperLogController extends BaseController {

    private final SysOperLogService sysOperLogService;

    /**
    * 指定ID查询 操作日志信息
    * @param id 主键ID
    * @return SysOperLog
    */
    @Operation(summary = "id查询一个SysOperLog")
    @SaCheckPermission("system::logs::operlog::query")
    @GetMapping("/get/{id}")
    public R getById(@PathVariable(name = "id") Serializable id) {
        SysOperLog sysOperLog = sysOperLogService.getById(id);
        return R.ok(sysOperLog);
    }

    /**
     * 查询所有操作日志信息
     * @return List<SysOperLog>
     */
    @Operation(summary = "查询所有SysOperLog信息")
    @SaCheckPermission("system::logs::operlog::query")
    @PostMapping("/list")
    public R list() {
        List<SysOperLog> list = sysOperLogService.list();
        return R.ok(list);
    }

    /**
     * 条件分页查询操作日志信息
     * @param sysOperLog 操作日志
     * @return List<SysOperLog>
     */
    @Operation(summary = "条件分页查询SysOperLog信息")
    @SaCheckPermission("system::logs::operlog::query")
    @PostMapping("/pageByParams")
    public R pageByParams(@RequestBody SysOperLog sysOperLog) {
        startPage();
        List<SysOperLog> list = sysOperLogService.pageByParams(sysOperLog);
        PageDataVo tableData = getTableData(list);
        return R.ok(tableData);
    }

    /**
     * 新增操作日志信息
     * @param sysOperLog 操作日志
     * @return 添加成功
     */
    @Operation(summary = "新增SysOperLog信息")
    @SaCheckPermission("system::logs::operlog::insert")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public R save(@RequestBody SysOperLog sysOperLog) {
        if(sysOperLogService.getById(sysOperLog.getId())!=null){
            throw new CustomException("ID已存在");
        }
        sysOperLogService.save(sysOperLog);
        return R.ok("添加成功");
    }
    /**
     * 根据ID修改操作日志信息
     * @param sysOperLog 操作日志
     * @return 修改成功
     */
    @Operation(summary = "修改SysOperLog信息")
    @SaCheckPermission("system::logs::operlog::update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @PostMapping("/updateById")
    public R updateById(@RequestBody SysOperLog sysOperLog) {
        sysOperLogService.updateById(sysOperLog);
        return R.ok("修改成功");
    }

    /**
    * 批量删除和删除操作日志信息
    * @param ids 主键ID数组
    * @return 删除成功
    */
    @Operation(summary = "批量删除和删除SysOperLog信息")
    @SaCheckPermission("system::logs::operlog::delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{ids}")
    public R deleteByIds(@PathVariable(name = "ids") Serializable[] ids) {
        if (ids.length <= 0){
            throw new CustomException("请选择要删除的数据");
        }
        sysOperLogService.removeBatchByIds(Arrays.asList(ids));
        return R.ok("删除成功");
    }

}
