package cn.anlucky.luckyadmin.system.pojo;

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
 * 操作日志
 *
 * @author yifan.du
 * @since 2025-05-22 17:25:42
 */
@Getter
@Setter
@ToString
@TableName("sys_oper_log")
@Schema(name = "SysOperLog", description = "操作日志")
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日志主键
     */
    @Schema(description = "日志主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 模块标题
     */
    @TableField("title")
    @Schema(description = "模块标题")
    private String title;
    /**
     * 业务类型（0-其它 1-新增 2-修改 3-删除）
     */
    @TableField("business_type")
    @Schema(description = "业务类型（0-其它 1-新增 2-修改 3-删除）")
    private Integer businessType;
    /**
     * 方法名称
     */
    @TableField("method")
    @Schema(description = "方法名称")
    private String method;
    /**
     * 请求方式
     */
    @Schema(description = "请求方式")
    @TableField("request_method")
    private String requestMethod;
    /**
     * 操作类别（0-其它 1-后台用户 2-手机端用户）
     */
    @TableField("operator_type")
    @Schema(description = "操作类别（0-其它 1-后台用户 2-手机端用户）")
    private Integer operatorType;
    /**
     * 用户名称
     */
    @TableField("username")
    @Schema(description = "用户名称")
    private String username;
    /**
     * 用户ID
     */
    @TableField("userid")
    @Schema(description = "用户ID")
    private Long userid;
    /**
     * 请求URL
     */
    @TableField("oper_url")
    @Schema(description = "请求URL")
    private String operUrl;
    /**
     * 主机地址
     */
    @TableField("oper_ip")
    @Schema(description = "主机地址")
    private String operIp;
    /**
     * 操作地点
     */
    @TableField("oper_location")
    @Schema(description = "操作地点")
    private String operLocation;
    /**
     * 请求参数
     */
    @TableField("oper_param")
    @Schema(description = "请求参数")
    private String operParam;
    /**
     * 返回参数
     */
    @TableField("json_result")
    @Schema(description = "返回参数")
    private String jsonResult;
    /**
     * 操作状态（0-异常 1-正常）
     */
    @TableField("`status`")
    @Schema(description = "操作状态（0-异常 1-正常）")
    private Integer status;
    /**
     * 错误消息
     */
    @TableField("error_msg")
    @Schema(description = "错误消息")
    private String errorMsg;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("oper_time")
    @Schema(description = "操作时间")
    private LocalDateTime operTime;
    /**
     * 消耗时间
     */
    @TableField("cost_time")
    @Schema(description = "消耗时间")
    private Long costTime;
}
