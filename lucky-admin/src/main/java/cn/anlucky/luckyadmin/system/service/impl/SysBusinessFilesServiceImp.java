package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.system.pojo.SysBusinessFiles;
import cn.anlucky.luckyadmin.system.mapper.SysBusinessFilesMapper;
import cn.anlucky.luckyadmin.system.service.SysBusinessFilesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import cn.anlucky.luckyadmin.utils.StringUtils;

/**
 * 服务实现类
 *
 * @author yifan.du
 * @since 2025-02-26 10:57:07
 */
@Service
public class SysBusinessFilesServiceImp extends ServiceImpl<SysBusinessFilesMapper, SysBusinessFiles> implements SysBusinessFilesService {

    /**
     * 条件分页查询信息
     *
     * @param sysBusinessFiles
     * @return List<SysBusinessFiles>
     */
    public List<SysBusinessFiles> pageByParams(SysBusinessFiles sysBusinessFiles) {
        if (sysBusinessFiles == null) {
            return this.list();
        }
        LambdaQueryWrapper<SysBusinessFiles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(sysBusinessFiles.getBusinessType()), SysBusinessFiles::getBusinessType, sysBusinessFiles.getBusinessType());
        return this.list(wrapper);
    }

    /**
     * 保存业务类型
     *
     * @param sysBusinessFiles sysBusinessFiles
     * @param isRepet          是否允许多个 true 允许多个() false 不允许多个
     * @return
     */
    @Override
    public boolean saveFileBusinessType(SysBusinessFiles sysBusinessFiles, boolean isRepet) {
        if (!isRepet) {
            // 不允许多个
            // 查询业务ID 业务类型 是否有数据，有数据更新，没有数据插入
            LambdaQueryWrapper<SysBusinessFiles> wrapper = new LambdaQueryWrapper<SysBusinessFiles>()
                    .eq(SysBusinessFiles::getBusinessId, sysBusinessFiles.getBusinessId())
                    .eq(SysBusinessFiles::getBusinessType, sysBusinessFiles.getBusinessType());
            List<SysBusinessFiles> list = this.list(wrapper);
            if (list!=null && list.size() > 0) {
                // 已经有数据存在，插入ID 更新数据
                sysBusinessFiles.setId(list.get(0).getId());
            }
        }
        // 允许多个直接插入数据
        return this.saveOrUpdate(sysBusinessFiles);
    }
}
