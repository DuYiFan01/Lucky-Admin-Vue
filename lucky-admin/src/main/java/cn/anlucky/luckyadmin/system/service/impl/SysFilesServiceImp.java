package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.enums.FileBusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysBusinessFiles;
import cn.anlucky.luckyadmin.system.pojo.SysFiles;
import cn.anlucky.luckyadmin.system.mapper.SysFilesMapper;
import cn.anlucky.luckyadmin.system.service.SysBusinessFilesService;
import cn.anlucky.luckyadmin.system.service.SysFilesService;
import cn.anlucky.luckyadmin.utils.file.FileCheckUtils;
import cn.anlucky.luckyadmin.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.anlucky.luckyadmin.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 服务实现类
 *
 * @author yifan.du
 * @since 2025-02-25 16:19:40
 */
@Service
public class SysFilesServiceImp extends ServiceImpl<SysFilesMapper, SysFiles> implements SysFilesService {

    @Autowired
    private SysBusinessFilesService sysbusinessFilesService;


    /**
     * 条件分页查询信息
     *
     * @param sysFiles
     * @return List<SysFiles>
     */
    public List<SysFiles> pageByParams(SysFiles sysFiles) {
        if (sysFiles == null) {
            return this.list();
        }
        LambdaQueryWrapper<SysFiles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(sysFiles.getOriginalName()), SysFiles::getOriginalName, sysFiles.getOriginalName());
        wrapper.eq(StringUtils.isNotBlank(sysFiles.getFileName()), SysFiles::getFileName, sysFiles.getFileName());
        wrapper.eq(StringUtils.isNotBlank(sysFiles.getStoragePath()), SysFiles::getStoragePath, sysFiles.getStoragePath());
        wrapper.eq(StringUtils.isNotBlank(sysFiles.getFileHash()), SysFiles::getFileHash, sysFiles.getFileHash());
        wrapper.eq(StringUtils.isNotBlank(sysFiles.getFileType()), SysFiles::getFileType, sysFiles.getFileType());
        wrapper.eq(StringUtils.isNotBlank(sysFiles.getFileBusinessType()), SysFiles::getFileBusinessType, sysFiles.getFileBusinessType());
        wrapper.eq(StringUtils.isNotBlank(sysFiles.getRemark()), SysFiles::getRemark, sysFiles.getRemark());
        return this.list(wrapper);
    }

    /**
     * 获取所有文件业务类型
     *
     * @return
     */
    @Override
    public Map<String, String> getAllFileBusinessType() {
        return FileBusinessType.getAllFileBusinessType();
    }

    /**
     * 上传文件
     *
     * @param file             文件
     * @param fileBusinessType 业务类型
     * @return SysFiles 文件信息
     */
    @Transactional
    @Override
    public SysFiles uploadFile(MultipartFile file, FileBusinessType fileBusinessType) {
        SysFiles sysFiles = null;
        // 查询哈希当前文件是否存在
        LambdaQueryWrapper<SysFiles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysFiles::getFileHash, FileCheckUtils.getFileHash(file));
        sysFiles = this.getOne(wrapper);
        if (sysFiles == null) {
            // 上传文件
            sysFiles = FileUploadUtils.uploadFile(file, fileBusinessType);
            // 保存文件表信息
            this.save(sysFiles);
        }
        return sysFiles;
    }

    /**
     * 根据业务类型和文件业务id获取文件绝对路径
     *
     * @param businessId
     * @param fileBusinessType
     * @return
     */
    @Override
    public List<String> getFileAbsPath(Long businessId, FileBusinessType fileBusinessType) {
        if (businessId == null || fileBusinessType == null) {
            throw new CustomException("参数不能为空");
        }
        return this.baseMapper.getFileAbsPath(businessId, fileBusinessType.getBusinessType());
    }

    /**
     * 根据文件业务id获取文件绝对路径
     *
     * @param fileBusinessId 文件业务id
     * @return 文件绝对路径
     */
    @Override
    public String getFileAbsPath(Long fileBusinessId) {
        return this.baseMapper.getFileAbsPathById(fileBusinessId);
    }

}
