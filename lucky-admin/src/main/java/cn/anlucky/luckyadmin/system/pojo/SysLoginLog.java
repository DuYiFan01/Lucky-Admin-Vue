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
 * 登录日志
 *
 * @author yifan.du
 * @since 2025-05-22 17:25:41
 */
@Getter
@Setter
@ToString
@TableName("sys_login_log")
@Schema(name = "SysLoginLog", description = "登录日志")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 登录账号
     */
    @TableField("username")
    @Schema(description = "登录账号")
    private String username;
    /**
     * 登录IP地址
     */
    @TableField("ip")
    @Schema(description = "登录IP地址")
    private String ip;
    /**
     * 登录地点
     */
    @TableField("ip_addr")
    @Schema(description = "登录地点")
    private String ipAddr;
    /**
     * 浏览器类型
     */
    @TableField("browser")
    @Schema(description = "浏览器类型")
    private String browser;
    /**
     * 操作系统
     */
    @TableField("os")
    @Schema(description = "操作系统")
    private String os;
    /**
     * 登录状态（0-失败 1-成功）
     */
    @TableField("`status`")
    @Schema(description = "登录状态（0-失败 1-成功）")
    private String status;
    /**
     * 提示消息
     */
    @TableField("msg")
    @Schema(description = "提示消息")
    private String msg;
    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "访问时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
