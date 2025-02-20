package cn.anlucky.luckyadmin.system.service;

import cn.anlucky.luckyadmin.system.pojo.SysUsers;
import cn.anlucky.luckyadmin.system.vo.AuthUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户信息 服务类
 *
 * @author yifan.du
 * @since 2025-01-16 10:08:19
 */
public interface SysUsersService extends IService<SysUsers> {

    /**
     * 条件分页查询用户信息信息
     *
     * @param sysUsers
     * @return List<SysUsers>
     */
    public List<SysUsers> pageByParams(SysUsers sysUsers);


    /**
     * 批量删除用户信息信息
     * @param ids
     */
    public void removeUsers(List<Long>  ids);


}
