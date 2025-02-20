package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.system.pojo.SysOperLog;
import cn.anlucky.luckyadmin.system.mapper.SysOperLogMapper;
import cn.anlucky.luckyadmin.system.service.SysOperLogService;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import cn.anlucky.luckyadmin.utils.StringUtils;

/**
 * 操作日志 服务实现类
 *
 * @author yifan.du
 * @since 2025-01-15 17:02:49
 */
@Service
public class SysOperLogServiceImp extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    /**
     * 条件分页查询
     * @param sysOperLog 条件
     * @param isAdmin true 查询所有的操作日志 false 查询当前用户操作日志
     * @return
     */
    public List<SysOperLog> pageByParams(SysOperLog sysOperLog, boolean isAdmin) {
        if (sysOperLog == null) {
            return this.list();
        }
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getTitle()), SysOperLog::getTitle, sysOperLog.getTitle());
        wrapper.eq(sysOperLog.getBusinessType() != null, SysOperLog::getBusinessType, sysOperLog.getBusinessType());
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getMethod()), SysOperLog::getMethod, sysOperLog.getMethod());
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getRequestMethod()), SysOperLog::getRequestMethod, sysOperLog.getRequestMethod());
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getOperUrl()), SysOperLog::getOperUrl, sysOperLog.getOperUrl());
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getOperIp()), SysOperLog::getOperIp, sysOperLog.getOperIp());
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getOperLocation()), SysOperLog::getOperLocation, sysOperLog.getOperLocation());
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getOperParam()), SysOperLog::getOperParam, sysOperLog.getOperParam());
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getJsonResult()), SysOperLog::getJsonResult, sysOperLog.getJsonResult());
        wrapper.eq(sysOperLog.getStatus() != null, SysOperLog::getStatus, sysOperLog.getStatus());
        wrapper.eq(StringUtils.isNotBlank(sysOperLog.getErrorMsg()), SysOperLog::getErrorMsg, sysOperLog.getErrorMsg());
        if (isAdmin){
            // 管理员查询所有
            wrapper.eq(StringUtils.isNotBlank(sysOperLog.getUsername()) ,SysOperLog::getUsername, sysOperLog.getUsername());
        }else{
            wrapper.eq(SysOperLog::getUsername, SaUtils.getLoginUserName());
        }

        wrapper.orderByDesc(SysOperLog::getOperTime);
        return this.list(wrapper);
    }
}
