package cn.anlucky.luckyadmin.system.vo;

import cn.anlucky.luckyadmin.system.pojo.SysRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取分配用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserVo {

    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 所有的角色
     */
    private List<SysRoles> rolesAll;
    /**
     * 拥有的角色
     */
    private List<SysRoles> hasRoles;
}
