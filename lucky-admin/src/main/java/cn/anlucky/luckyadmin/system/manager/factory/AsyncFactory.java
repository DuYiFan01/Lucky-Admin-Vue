package cn.anlucky.luckyadmin.system.manager.factory;

import cn.anlucky.luckyadmin.system.constant.Constants;
import cn.anlucky.luckyadmin.system.enums.BusinessStatus;
import cn.anlucky.luckyadmin.system.enums.BusinessType;
import cn.anlucky.luckyadmin.system.enums.Code;
import cn.anlucky.luckyadmin.system.pojo.SysLoginLog;
import cn.anlucky.luckyadmin.system.pojo.SysOperLog;
import cn.anlucky.luckyadmin.system.service.SysLoginLogService;
import cn.anlucky.luckyadmin.system.service.SysOperLogService;
import cn.anlucky.luckyadmin.utils.LogUtils;
import cn.anlucky.luckyadmin.utils.ServletUtils;
import cn.anlucky.luckyadmin.utils.StringUtils;
import cn.anlucky.luckyadmin.utils.ip.AddressUtils;
import cn.anlucky.luckyadmin.utils.ip.IpUtils;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import cn.anlucky.luckyadmin.utils.spring.SpringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
            final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginLog loginLog = new SysLoginLog();
                loginLog.setUsername(username);
                loginLog.setIp(ip);
                loginLog.setIpAddr(address);
                loginLog.setBrowser(browser);
                loginLog.setOs(os);
                loginLog.setMsg(message);

                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    // 登录成功 注销成功 注册成功
                    loginLog.setStatus(BusinessStatus.SUCCESS.ordinal()+"");
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    // 登录失败
                    loginLog.setStatus(BusinessStatus.FAIL.ordinal()+"");
                }
                // 插入数据
                SpringUtils.getBean(SysLoginLogService.class).save(loginLog);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(SysOperLogService.class).save(operLog);
            }
        };
    }
}
