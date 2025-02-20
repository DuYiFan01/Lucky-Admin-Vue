package cn.anlucky.luckyadmin.system.vo;

import cn.anlucky.luckyadmin.system.pojo.SysRoles;
import lombok.Data;

import java.util.List;

@Data
public class RolesVo {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 角色列表
     */
    private List<SysRoles> roles;

}
