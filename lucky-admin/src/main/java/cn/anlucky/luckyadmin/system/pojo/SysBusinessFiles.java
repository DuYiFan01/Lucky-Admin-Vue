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
 * 业务文件
 *
 * @author yifan.du
 * @since 2025-05-22 17:25:41
 */
@Getter
@Setter
@ToString
@TableName("sys_business_files")
@Schema(name = "SysBusinessFiles", description = "业务文件")
public class SysBusinessFiles implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 业务分类
     */
    @TableField("business_type")
    @Schema(description = "业务分类")
    private String businessType;
    /**
     * 业务表ID
     */
    @TableField("business_id")
    @Schema(description = "业务表ID")
    private Long businessId;
    /**
     * 文件表ID
     */
    @TableField("file_id")
    @Schema(description = "文件表ID")
    private Long fileId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
