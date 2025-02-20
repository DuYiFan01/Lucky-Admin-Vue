package cn.anlucky.luckyadmin.system.pojo;

    import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
    import com.fasterxml.jackson.annotation.JsonFormat;
    import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色信息
 *
 * @author yifan.du
 * @since 2025-02-13 16:08:01
 */
@Getter
@Setter
@ToString
@TableName("sys_roles")
@Schema(name = "SysRoles", description = "角色信息")
public class SysRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 角色名称
     */
    @TableField("`name`")
    @Schema(description = "角色名称")
    private String name;
    /**
     * 角色描述
     */
    @TableField("`description`")
    @Schema(description = "角色描述")
    private String description;
    /**
     * 显示顺序
     */
    @TableField("sort")
    @Schema(description = "显示顺序")
    private Integer sort;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
}
