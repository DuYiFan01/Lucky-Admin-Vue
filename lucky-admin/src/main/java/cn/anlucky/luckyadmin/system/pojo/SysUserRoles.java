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
 * 用户角色
 *
 * @author yifan.du
 * @since 2025-05-22 17:25:42
 */
@Getter
@Setter
@ToString
@TableName("sys_user_roles")
@Schema(name = "SysUserRoles", description = "用户角色")
public class SysUserRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @TableId("user_id")
    @Schema(description = "用户ID")
    private Long userId;
    /**
     * 角色ID
     */
    @TableField("role_id")
    @Schema(description = "角色ID")
    private Long roleId;
}
