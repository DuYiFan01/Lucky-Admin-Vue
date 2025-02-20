package cn.anlucky.luckyadmin.system.vo;

import cn.anlucky.luckyadmin.system.pojo.SysRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRolesVo extends SysRoles {

    /**
     * 角色对应的菜单id集合
     */
    private List<Long> menusIds;

}
