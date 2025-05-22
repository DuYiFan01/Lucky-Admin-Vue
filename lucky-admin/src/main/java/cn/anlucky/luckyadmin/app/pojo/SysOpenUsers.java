package cn.anlucky.luckyadmin.app.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 跨平台用户
 *
 * @author yifan.du
 * @since 2025-05-22 14:54:09
 */
@Getter
@Setter
@ToString
@TableName("sys_open_users")
@Schema(name = "SysOpenUsers", description = "跨平台用户")
public class SysOpenUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 绑定id
     */
    @Schema(description = "绑定id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 系统账户Id
     */
    @TableField("sys_user_id")
    @Schema(description = "系统账户Id")
    private Long sysUserId;
    /**
     * 跨平台用户标识
     */
    @TableField("user_open_id")
    @Schema(description = "跨平台用户标识")
    private String userOpenId;
    /**
     * 平台标识 0-系统账户 1-微信小程序 2-抖音小程序
     */
    @TableField("sys_user_type")
    @Schema(description = "平台标识 0-系统账户 1-微信小程序 2-抖音小程序")
    private Integer sysUserType;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /**
     * 逻辑删除标志，0-未删除，1-已删除
     */
    @TableLogic
    @TableField("del_flag")
    @Schema(description = "逻辑删除标志，0-未删除，1-已删除")
    private Integer delFlag;
}
