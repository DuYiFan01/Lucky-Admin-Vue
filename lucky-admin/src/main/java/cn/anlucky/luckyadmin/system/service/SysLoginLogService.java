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
     * 条件分页查询登录日志信息
     * @param sysLoginLog
     * @return List<SysLoginLog>
     */
    public List<SysLoginLog> pageByParams(SysLoginLog sysLoginLog);

}
