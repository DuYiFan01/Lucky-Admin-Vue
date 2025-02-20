package cn.anlucky.luckyadmin.gen.config;

import cn.anlucky.luckyadmin.utils.StringUtils;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成配置实体类
 */
@Data
public class GenerationPo {

    public static final String ENTITY = "entity";
    public static final String MAPPER = "mapper";
    public static final String MAPPER_XML = "mapperXml";
    public static final String SERVICE = "service";
    public static final String SERVICE_IMPL = "serviceImpl";
    public static final String CONTROLLER = "controller";
    public static final String VUE = "vue";
    public static final String JS = "js";
    public static final String SQL = "sql";

    /**
     * 是否使用默认模板
     * 默认模板：template/default/*
     * 自定义模板：template/lucky/*
     * 若想使用自己的模板，则设置为false，并配置GenerationConfig中的getTemplatesMap
     */
    public static final boolean isDefalutTemplate = false;

    private static Map<String, String> templatesMap;

    private List<CustomFile> customFiles;

    /**
     * 数据库连接地址
     */
    private String dbUrl;
    /**
     * 数据库用户名
     */
    private String dbUserName;
    /**
     * 数据库密码
     */
    private String dbPassword;
    /**
     * 作者
     */
    private String author;
    /**
     * 输出目录
     */
    private String outputDir;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 模板名称
     */
    private String mouldName;
    /**
     * 自动填充创建时间字段名称
     */
    private String createTimeField;
    /**
     * 自动填充修改时间字段名称
     */
    private String updateTimeField;
    /**
     * 自动填充创建人字段名称
     */
    private String createByField;
    /**
     * 自动填充修改人字段名称
     */
    private String updateByField;
    /**
     * 逻辑删除字段名称
     */
    private String deleteFlagField;
    /**
     * 实体类所在包名
     */
    private String entityPackage;
    /**
     * mapper接口所在包名
     */
    private String mapperPackage;
    /**
     * mapper.xml文件所在包名
     */
    private String mapperXmlPackage;
    /**
     * service所在包名
     */
    private String servicePackage;
    /**
     * service实现类所在包名
     */
    private String serviceImplPackage;
    /**
     * controller所在包名
     */
    private String controllerPackage;

    public Map<String, String> getTemplatesMap() {
        if (templatesMap != null){
            return templatesMap;
        }
        templatesMap = new HashMap<>();
        if (isDefalutTemplate){
            // 设置默认模板位置
            templatesMap.put(ENTITY, "/templates/default/java/entity.java");
            templatesMap.put(MAPPER, "/templates/default/java/mapper.java");
            templatesMap.put(MAPPER_XML, "/templates/default/xml/mapper.xml");
            templatesMap.put(SERVICE, "/templates/default/java/service.java");
            templatesMap.put(SERVICE_IMPL, "/templates/default/java/serviceImpl.java");
            templatesMap.put(CONTROLLER, "/templates/default/java/controller.java");
        }else {
            // 设置自定义模板位置
            templatesMap.put(ENTITY, "/templates/lucky/java/entity.java");
            templatesMap.put(MAPPER, "/templates/lucky/java/mapper.java");
            templatesMap.put(MAPPER_XML, "/templates/lucky/xml/mapper.xml");
            templatesMap.put(SERVICE, "/templates/lucky/java/service.java");
            templatesMap.put(SERVICE_IMPL, "/templates/lucky/java/serviceImpl.java");
            templatesMap.put(CONTROLLER, "/templates/lucky/java/controller.java");
        }
        return templatesMap;
    }

    /**
     * 自定义文件配置
     * @param tableName
     * @return
     */
    public List<CustomFile> getCustomFiles(String tableName) {
        // 请保证return的值不为null 这里必须 new
        List<CustomFile> list = new ArrayList<>();
        if (isDefalutTemplate){
            // 如果使用默认模板将不生成自定义
            // 如果您想生成，请直接在这里配置即可
        }else {
            CustomFile vue = new CustomFile.Builder()
                    .fileName("/index.vue")
                    .templatePath("/templates/lucky/vue/index.vue.vm")
                    .packageName("vue")
                    .enableFileOverride().build();

            CustomFile js = new CustomFile.Builder()
                    .fileName("/"+StringUtils.toCamelCase(tableName)+".js") // sys_users -> sysUsers  + .js
                    .templatePath("/templates/lucky/js/index.js.vm")
                    .packageName("js")
                    .enableFileOverride().build();

            CustomFile sql = new CustomFile.Builder()
                    .fileName("/"+StringUtils.toCamelCase(tableName) + ".sql")
                    .templatePath("/templates/lucky/sql/menus.sql.vm")
                    .packageName("sql")
                    .enableFileOverride().build();
            list.add(vue);
            list.add(js);
            list.add(sql);
        }
        this.customFiles = list;
        return list;
    }


}
