package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.system.pojo.SysRolesMenus;
import cn.anlucky.luckyadmin.system.mapper.SysRolesMenusMapper;
import cn.anlucky.luckyadmin.system.service.SysRolesMenusService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import cn.anlucky.luckyadmin.utils.StringUtils;
     /**
 * 角色和菜单关联表 服务实现类
 *
 * @author yifan.du
 * @since 2025-01-16 16:05:06
 */
@Service
public class SysRolesMenusServiceImp extends ServiceImpl<SysRolesMenusMapper, SysRolesMenus> implements SysRolesMenusService {

        /**
         * 条件分页查询角色和菜单关联表信息
         * @param sysRolesMenus
         * @return List<SysRolesMenus>
         */
        public List<SysRolesMenus> pageByParams(SysRolesMenus sysRolesMenus){
            if(sysRolesMenus == null){
                return this.list();
            }
            LambdaQueryWrapper<SysRolesMenus> wrapper = new LambdaQueryWrapper<>();
            return this.list(wrapper);
        }
}
