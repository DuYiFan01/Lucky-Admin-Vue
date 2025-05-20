package cn.anlucky.luckyadmin.system.controller;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.enums.FileBusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysFiles;
import cn.anlucky.luckyadmin.system.service.SysFilesService;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * 控制器
 *
 * @author yifan.du
 * @since 2025-02-25 16:19:40
 */
@RestController
@Tag(name = "Controller")
@RequiredArgsConstructor
@RequestMapping("/system/sysFiles")
public class SysFilesController extends BaseController {

    private final SysFilesService sysFilesService;

    /**
     * 指定ID查询 信息
     *
     * @param id 主键ID
     * @return SysFiles
     */
    @Operation(summary = "id查询一个SysFiles")
    @SaCheckPermission("system::files::query")
    @GetMapping("/get/{id}")
    public R getById(@PathVariable(name = "id") Serializable id) {
        SysFiles sysFiles = sysFilesService.getById(id);
        return R.ok(sysFiles);
    }

    /**
     * 查询所有信息
     *
     * @return List<SysFiles>
     */
    @Operation(summary = "查询所有SysFiles信息")
    @SaCheckPermission("system::files::query")
    @PostMapping("/list")
    public R list() {
        List<SysFiles> list = sysFilesService.list();
        return R.ok(list);
    }

    /**
     * 条件分页查询信息
     *
     * @param sysFiles
     * @return List<SysFiles>
     */
    @Operation(summary = "条件分页查询SysFiles信息")
    @SaCheckPermission("system::files::query")
    @PostMapping("/pageByParams")
    public R pageByParams(@RequestBody SysFiles sysFiles) {
        startPage();
        List<SysFiles> list = sysFilesService.pageByParams(sysFiles);
        PageDataVo tableData = getTableData(list);
        return R.ok(tableData);
    }

    /**
     * 新增信息
     *
     * @param sysFiles
     * @return 添加成功
     */
    @Operation(summary = "上传文件")
    @SaCheckPermission("system::files::insert")
    @Log(title = "新增文件", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public R save(@RequestParam("files") MultipartFile[] files) {
        List<SysFiles> sysFiles = sysFilesService.uploadFiles(files, FileBusinessType.File_UPLOAD);
        return R.ok("添加成功",sysFiles);
    }
    /**
     * 新增信息
     *
     * @param file
     * @return 添加成功
     */
    @Operation(summary = "上传文件")
    @SaCheckPermission("system::files::insert")
    @Log(title = "新增文件", businessType = BusinessType.INSERT)
    @PostMapping("/uploadFile")
    public R save(@RequestParam("file") MultipartFile file,@RequestParam("fileBusinessType") FileBusinessType fileBusinessType) {
        SysFiles sysFile = sysFilesService.uploadFile(file, fileBusinessType);
        return R.ok("添加成功",sysFile);
    }

    /**
     * 批量删除和删除信息
     *
     * @param ids 主键ID数组
     * @return 删除成功
     */
    @Operation(summary = "批量删除和删除SysFiles信息")
    @SaCheckPermission("system::files::delete")
    @Log(title = "删除文件", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{ids}")
    public R deleteByIds(@PathVariable(name = "ids") Long[] ids) {
        if (ids.length <= 0) {
            throw new CustomException("请选择要删除的数据");
        }
        sysFilesService.removeBatch(Arrays.asList(ids));
        return R.ok("删除成功");
    }

    /**
     * 获取所有文件业务类型
     * @return
     */
    @SaCheckPermission("system::files::query")
    @Operation(summary = "获取所有文件业务类型")
    @GetMapping("/getFileBusinessType")
    public R getFileBusinessType() {
        return R.ok(sysFilesService.getAllFileBusinessType());
    }


}
