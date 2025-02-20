package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.pojo.SysUserRoles;
import cn.anlucky.luckyadmin.system.mapper.SysUserRolesMapper;
import cn.anlucky.luckyadmin.system.service.SysUserRolesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import cn.anlucky.luckyadmin.utils.StringUtils;
     /**
 * 用户角色关联表 服务实现类
 *
 * @author yifan.du
 * @since 2025-01-16 15:52:20
 */
@Service
public class SysUserRolesServiceImp extends ServiceImpl<SysUserRolesMapper, SysUserRoles> implements SysUserRolesService {

        /**
         * 条件分页查询用户角色关联表信息
         * @param sysUserRoles
         * @return List<SysUserRoles>
         */
        public List<SysUserRoles> pageByParams(SysUserRoles sysUserRoles){
            if(sysUserRoles == null){
                return this.list();
            }
            LambdaQueryWrapper<SysUserRoles> wrapper = new LambdaQueryWrapper<>();
            return this.list(wrapper);
        }

         /**
          * 获取当前角色绑定的用户
          *
          * @param roleId
          * @return
          */
         @Override
         public List<SysUserRoles> getRoleHasUser(Long roleId) {
             if (roleId == null){
                 throw new CustomException("角色ID不能为空");
             }
             LambdaQueryWrapper<SysUserRoles> wrapper = new LambdaQueryWrapper<>();
             wrapper.eq(SysUserRoles::getRoleId, roleId);
             return this.list(wrapper);
         }

         /**
          * 获取角色绑定的用户ID列表
          *
          * @return
          */
         @Override
         public List<Long> getRoleHasUserToIds(Long roleId) {
             List<SysUserRoles> list = this.getRoleHasUser(roleId);
             return list.stream().map(SysUserRoles::getUserId).toList();
         }
     }
