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
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单信息
 *
 * @author yifan.du
 * @since 2025-05-22 17:25:42
 */
@Getter
@Setter
@ToString
@TableName("sys_menus")
@Schema(name = "SysMenus", description = "菜单信息")
public class SysMenus implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 菜单名称
     */
    @TableField("title")
    @Schema(description = "菜单名称")
    private String title;
    /**
     * 路由名称
     */
    @TableField("`name`")
    @Schema(description = "路由名称")
    private String name;
    /**
     * 父菜单ID
     */
    @TableField("parent_id")
    @Schema(description = "父菜单ID")
    private Long parentId;
    /**
     * 显示顺序
     */
    @TableField("sort")
    @Schema(description = "显示顺序")
    private Integer sort;
    /**
     * 路由地址
     */
    @TableField("`path`")
    @Schema(description = "路由地址")
    private String path;
    /**
     * 组件路径
     */
    @TableField("`component`")
    @Schema(description = "组件路径")
    private String component;
    /**
     * 路由参数
     */
    @TableField("`query`")
    @Schema(description = "路由参数")
    private String query;
    /**
     * 是否缓存（0-不缓存 1-缓存）
     */
    @TableField("is_cache")
    @Schema(description = "是否缓存（0-不缓存 1-缓存）")
    private Integer isCache;
    /**
     * 菜单类型（M-目录 C-菜单 F-按钮 N-内联 W-外链）
     */
    @TableField("menu_type")
    @Schema(description = "菜单类型（M-目录 C-菜单 F-按钮 N-内联 W-外链）")
    private String menuType;
    /**
     * 菜单状态（0-隐藏 1-显示）
     */
    @TableField("`visible`")
    @Schema(description = "菜单状态（0-隐藏 1-显示）")
    private Integer visible;
    /**
     * 权限标识
     */
    @TableField("roles")
    @Schema(description = "权限标识")
    private String roles;
    /**
     * 菜单图标
     */
    @TableField("icon")
    @Schema(description = "菜单图标")
    private String icon;
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
    /**
     * 子菜单
     */
    @Schema(description = "子菜单")
    @TableField(exist = false)
    private List<SysMenus> children;
}
