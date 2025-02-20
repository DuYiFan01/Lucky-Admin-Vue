package cn.anlucky.luckyadmin.system.pojo;

     import com.baomidou.mybatisplus.annotation.TableField;
     import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色和菜单关联表
 *
 * @author yifan.du
 * @since 2025-02-13 16:08:01
 */
@Getter
@Setter
@ToString
@TableName("sys_roles_menus")
@Schema(name = "SysRolesMenus", description = "角色和菜单关联表")
public class SysRolesMenus implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    @TableId("role_id")
    @Schema(description = "角色ID")
    private Long roleId;
    /**
     * 菜单ID
     */
    @TableField("menu_id")
    @Schema(description = "菜单ID")
    private Long menuId;
}
