package cn.anlucky.luckyadmin.system.vo;

import cn.anlucky.luckyadmin.system.pojo.SysUsers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRoleVo {

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 所有用户
     */
    private List<SysUsers> usersAll;
    /**
     * 拥有的用户
     */
    private List<Long> hasUsersIds;
}
