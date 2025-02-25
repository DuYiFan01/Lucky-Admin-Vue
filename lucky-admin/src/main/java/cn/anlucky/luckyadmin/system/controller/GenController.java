package cn.anlucky.luckyadmin.system.controller;


import cn.anlucky.luckyadmin.system.annotation.Log;
import cn.anlucky.luckyadmin.system.base.controller.BaseController;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.pojo.GenPo;
import cn.anlucky.luckyadmin.system.pojo.TablePo;
import cn.anlucky.luckyadmin.system.service.GenService;
import cn.anlucky.luckyadmin.utils.page.vo.PageDataVo;
import cn.anlucky.luckyadmin.vo.R;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tools/gen")
@RequiredArgsConstructor
public class GenController extends BaseController {

    private final GenService genService;


    /**
     * 获取所有表信息
     *
     * @return
     */
    @SaCheckPermission("tools::gen::query")
    @PostMapping("/tableList")
    public R tableList(@RequestBody TablePo tablePo) {
        startPage();
        List<TablePo> list = genService.showTableSatus(tablePo);
        PageDataVo tableData = getTableData(list);
        return R.ok(tableData);
    }

    /**
     * 预览生成的代码
     *
     * @param genPo
     * @return
     */
    @SaCheckPermission("tools::gen::query")
    @PostMapping("/previewCode")
    public R previewCode(@RequestBody GenPo genPo) {
        return R.ok(genService.previewCode(genPo));
    }

    /**
     * 获取代码生成的配置信息
     * @return
     */
    @SaCheckPermission("tools::gen::query")
    @GetMapping("/getGenPo")
    public R getGenPo() {
        return R.ok(genService.getGenPo());
    }


    /**
     * 下载代码
     * @param response
     * @param tableName
     * @throws IOException
     */
    @SaCheckPermission("tools::gen::query")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/downloadCode")
    public void downloadCode(HttpServletResponse response,
                             @Param("tableName") String tableName,
                             @Param("packageName") String packageName,
                             @Param("mouldName") String mouldName
    ) throws IOException {
        GenPo genPo = new GenPo(tableName, packageName, mouldName);
        byte[] data = genService.downloadCode(genPo);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"Lucky.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

}
