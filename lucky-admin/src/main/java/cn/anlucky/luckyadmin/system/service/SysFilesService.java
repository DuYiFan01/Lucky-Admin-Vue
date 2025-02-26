package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysFiles;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 服务类
 *
 * @author yifan.du
 * @since 2025-02-25 16:19:40
 */
public interface SysFilesService extends IService<SysFiles> {

    /**
     * 条件分页查询信息
     *
     * @param sysFiles
     * @return List<SysFiles>
     */
    public List<SysFiles> pageByParams(SysFiles sysFiles);


    /**
     * 获取所有文件业务类型
     * @return
     */
    public Map<String,String> getAllFileBusinessType();



    /**
     * 上传文件
     */
    public void uploadFile();


}
