package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysBusinessFiles;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 服务类
 *
 * @author yifan.du
 * @since 2025-02-26 10:57:07
 */
public interface SysBusinessFilesService extends IService<SysBusinessFiles> {

    /**
     * 条件分页查询信息
     *
     * @param sysBusinessFiles
     * @return List<SysBusinessFiles>
     */
    public List<SysBusinessFiles> pageByParams(SysBusinessFiles sysBusinessFiles);

    /**
     *  保存业务类型
     * @param sysBusinessFiles sysBusinessFiles
     * @param isRepet 是否允许重复 true 允许重复() false 不允许不重复
     * @return
     */
    public boolean saveFileBusinessType(SysBusinessFiles sysBusinessFiles,boolean isRepet);


}
