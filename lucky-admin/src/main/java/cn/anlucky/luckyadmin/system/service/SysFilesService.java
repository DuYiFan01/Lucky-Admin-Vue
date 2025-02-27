package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.enums.FileBusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysFiles;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     * @param file 文件
     * @param fileBusinessType 业务类型
     * @return
     */
    public SysFiles uploadFile(MultipartFile file, FileBusinessType fileBusinessType);

    /**
     * 根据业务类型和文件业务id获取文件绝对路径
     * @param businessId
     * @param fileBusinessType
     * @return
     */
    public List<String> getFileAbsPath(Long businessId, FileBusinessType fileBusinessType);

    /**
     * 根据文件业务id获取文件绝对路径
     * @param fileBusinessId 文件业务id
     * @return 文件绝对路径
     */
    public String getFileAbsPath(Long fileBusinessId);




}
