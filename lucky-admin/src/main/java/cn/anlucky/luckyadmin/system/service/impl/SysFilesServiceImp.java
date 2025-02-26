package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.system.enums.FileBusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysFiles;
import cn.anlucky.luckyadmin.system.mapper.SysFilesMapper;
import cn.anlucky.luckyadmin.system.service.SysFilesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import cn.anlucky.luckyadmin.utils.StringUtils;

/**
 * 服务实现类
 *
 * @author yifan.du
 * @since 2025-02-25 16:19:40
 */
@Service
public class SysFilesServiceImp extends ServiceImpl<SysFilesMapper, SysFiles> implements SysFilesService {

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
     */
    @Override
    public void uploadFile() {

    }
}
