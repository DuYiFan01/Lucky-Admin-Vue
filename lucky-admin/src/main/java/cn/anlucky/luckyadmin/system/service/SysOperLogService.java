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
     * 条件分页查询操作日志信息
     * @param sysOperLog
     * @return List<SysOperLog>
     */
    public List<SysOperLog> pageByParams(SysOperLog sysOperLog);

}
