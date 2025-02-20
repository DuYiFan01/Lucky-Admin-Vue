package cn.anlucky.luckyadmin.system.config;

import cn.anlucky.luckyadmin.system.service.SysRolesService;
import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserAuthConfig implements StpInterface {

    @Autowired
    private SysRolesService sysRolesService;

    /**
     * 获取用户的权限
     * @param loginId 用户ID
     * @param loginType 登录类型
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return sysRolesService.getPermissionByUserId(Long.parseLong(loginId.toString()));
    }

    /**
     * 获取用户的角色
     * @param loginId 用户ID
     * @param loginType 登录类型
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return sysRolesService.getRolesCodeByUserId(Long.parseLong(loginId.toString()));
    }
}
