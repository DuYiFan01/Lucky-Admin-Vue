package cn.anlucky.luckyadmin.app.service.impl;

import cn.anlucky.luckyadmin.app.pojo.SysOpenUsers;
import cn.anlucky.luckyadmin.app.mapper.SysOpenUsersMapper;
import cn.anlucky.luckyadmin.app.service.SysOpenUsersService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import cn.anlucky.luckyadmin.utils.StringUtils;
                                                                                                                    /**
 * 跨平台用户 服务实现类
 *
 * @author yifan.du
 * @since 2025-05-22 14:54:09
 */
@Service
public class SysOpenUsersServiceImp extends ServiceImpl<SysOpenUsersMapper, SysOpenUsers> implements SysOpenUsersService {

        /**
         * 条件分页查询跨平台用户信息
         * @param sysOpenUsers
         * @return List<SysOpenUsers>
         */
        public List<SysOpenUsers> pageByParams(SysOpenUsers sysOpenUsers){
            if(sysOpenUsers == null){
                return this.list();
            }
            LambdaQueryWrapper<SysOpenUsers> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(sysOpenUsers.getUserOpenId()), SysOpenUsers::getUserOpenId, sysOpenUsers.getUserOpenId());
            return this.list(wrapper);
        }
}
