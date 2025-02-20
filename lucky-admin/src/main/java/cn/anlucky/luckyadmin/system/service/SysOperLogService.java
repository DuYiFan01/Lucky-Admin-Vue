package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 操作日志 服务类
 *
 * @author yifan.du
 * @since 2025-01-15 17:02:49
 */
public interface SysOperLogService extends IService<SysOperLog> {

    /**
     * 条件分页查询
     * @param sysOperLog 条件
     * @param isAdmin true 查询所有的操作日志 false 查询当前用户操作日志
     * @return
     */
    public List<SysOperLog> pageByParams(SysOperLog sysOperLog, boolean isAdmin);

}
