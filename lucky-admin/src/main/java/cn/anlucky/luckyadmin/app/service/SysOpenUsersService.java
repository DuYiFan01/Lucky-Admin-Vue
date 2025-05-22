package cn.anlucky.luckyadmin.app.service;

import cn.anlucky.luckyadmin.app.pojo.SysOpenUsers;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
    /**
 * 跨平台用户 服务类
 *
 * @author yifan.du
 * @since 2025-05-22 14:54:09
 */
public interface SysOpenUsersService extends IService<SysOpenUsers> {

    /**
     * 条件分页查询跨平台用户信息
     * @param sysOpenUsers
     * @return List<SysOpenUsers>
     */
    public List<SysOpenUsers> pageByParams(SysOpenUsers sysOpenUsers);

}
