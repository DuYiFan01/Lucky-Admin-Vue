package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.system.pojo.SysLoginLog;
import cn.anlucky.luckyadmin.system.mapper.SysLoginLogMapper;
import cn.anlucky.luckyadmin.system.service.SysLoginLogService;
import cn.anlucky.luckyadmin.utils.StringUtils;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 登录日志 服务实现类
 * </p>
 *
 * @author yifan.du
 * @since 2025-01-15 16:56:54
 */
@Service
public class SysLoginLogServiceImp extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

        /**
         * 条件分页查询登录日志信息
         * @param sysLoginLog
         * @return List<SysLoginLog>
         */
        public List<SysLoginLog> pageByParams(SysLoginLog sysLoginLog,boolean isAdmin){
            if(sysLoginLog == null){
                return this.list();
            }
            LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(sysLoginLog.getIp()), SysLoginLog::getIp, sysLoginLog.getIp());
            wrapper.eq(StringUtils.isNotBlank(sysLoginLog.getIpAddr()), SysLoginLog::getIpAddr, sysLoginLog.getIpAddr());
            wrapper.eq(StringUtils.isNotBlank(sysLoginLog.getBrowser()), SysLoginLog::getBrowser, sysLoginLog.getBrowser());
            wrapper.eq(StringUtils.isNotBlank(sysLoginLog.getOs()), SysLoginLog::getOs, sysLoginLog.getOs());
            wrapper.eq(StringUtils.isNotBlank(sysLoginLog.getStatus()), SysLoginLog::getStatus, sysLoginLog.getStatus());
            wrapper.eq(StringUtils.isNotBlank(sysLoginLog.getMsg()), SysLoginLog::getMsg, sysLoginLog.getMsg());
            if (isAdmin){
                // 管理员查所有
                wrapper.eq(StringUtils.isNotBlank(sysLoginLog.getUsername()),SysLoginLog::getUsername, sysLoginLog.getUsername());
            }else{
                // 只可以查询自己的登录日志
                wrapper.eq(SysLoginLog::getUsername,SaUtils.getLoginUserName());
            }
            wrapper.orderByDesc(SysLoginLog::getCreateTime);
            return this.list(wrapper);
        }
}
