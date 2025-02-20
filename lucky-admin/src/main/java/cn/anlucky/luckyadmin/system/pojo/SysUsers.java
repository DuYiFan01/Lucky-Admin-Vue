package cn.anlucky.luckyadmin.system.pojo;

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
 * 用户信息
 *
 * @author yifan.du
 * @since 2025-02-13 16:08:01
 */
@Getter
@Setter
@ToString
@TableName("sys_users")
@Schema(name = "SysUsers", description = "用户信息")
public class SysUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 账号
     */
    @TableField("username")
    @Schema(description = "账号")
    private String username;
    /**
     * 密码
     */
    @TableField("`password`")
    @Schema(description = "密码")
    private String password;
    /**
     * 用户类型(00-系统用户)
     */
    @TableField("user_type")
    @Schema(description = "用户类型(00-系统用户)")
    private String userType;
    /**
     * 昵称
     */
    @TableField("`name`")
    @Schema(description = "昵称")
    private String name;
    /**
     * 手机号
     */
    @TableField("phone")
    @Schema(description = "手机号")
    private String phone;
    /**
     * 0-女 1-男 2-未知
     */
    @TableField("sex")
    @Schema(description = "0-女 1-男 2-未知")
    private String sex;
    /**
     * 邮箱
     */
    @TableField("email")
    @Schema(description = "邮箱")
    private String email;
    /**
     * 头像地址
     */
    @TableField("avatar")
    @Schema(description = "头像地址")
    private String avatar;
    /**
     * 0-禁用 1-启用
     */
    @TableField("enabled")
    @Schema(description = "0-禁用 1-启用")
    private String enabled;
    /**
     * 最后登录IP
     */
    @TableField("login_ip")
    @Schema(description = "最后登录IP")
    private String loginIp;
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @TableField("login_time")
    @Schema(description = "最后登录时间")
    private LocalDateTime loginTime;
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
    /**
     * 逻辑删除标志，0-未删除，1-已删除
     */
    @TableLogic
    @TableField("del_flag")
    @Schema(description = "逻辑删除标志，0-未删除，1-已删除")
    private Integer delFlag;
}
