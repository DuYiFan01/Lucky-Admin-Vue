package cn.anlucky.luckyadmin.system.dto;

import cn.anlucky.luckyadmin.system.enums.FileBusinessType;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysFilesDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 文件ID
     */
    @Schema(description = "文件ID")
    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;
    /**
     * 原始文件名
     */
    @TableField("original_name")
    @Schema(description = "原始文件名")
    private String originalName;
    /**
     * 当前文件名
     */
    @TableField("file_name")
    @Schema(description = "当前文件名")
    private String fileName;
    /**
     * 存储路径
     */
    @TableField("storage_path")
    @Schema(description = "存储路径")
    private String storagePath;
    /**
     * 哈希值
     */
    @TableField("file_hash")
    @Schema(description = "哈希值")
    private String fileHash;
    /**
     * 文件类型
     */
    @TableField("file_type")
    @Schema(description = "文件类型")
    private String fileType;
    /**
     * 业务分类
     */
    @Schema(description = "业务分类")
    @TableField("file_business_type")
    private FileBusinessType fileBusinessType;
    /**
     * 文件大小(字节)
     */
    @TableField("file_size")
    @Schema(description = "文件大小(字节)")
    private Long fileSize;
    /**
     * 逻辑删除标志，0-未删除，1-已删除
     */
    @TableLogic
    @TableField("del_flag")
    @Schema(description = "逻辑删除标志，0-未删除，1-已删除")
    private Integer delFlag;
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
     * 备注
     */
    @TableField("remark")
    @Schema(description = "备注")
    private String remark;
}
