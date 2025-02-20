package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysLoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 登录日志 服务类
 *
 * @author yifan.du
 * @since 2025-01-15 16:51:49
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 条件分页查询
     * @param sysLoginLog 条件
     * @param isAdmin true 查询所有人的日志 false 查询当前用户的日志
     * @return
     */
    public List<SysLoginLog> pageByParams(SysLoginLog sysLoginLog, boolean isAdmin);

}
