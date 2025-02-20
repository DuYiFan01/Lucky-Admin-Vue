package cn.anlucky.luckyadmin.gen.config;

import cn.anlucky.luckyadmin.system.base.controller.BaseController;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis-plus 代码生成器配置
 */
public class GenerationConfig {

    private GenerationPo generationPo;

    public GenerationConfig(GenerationPo generationPo) {
        this.generationPo = generationPo;
    }


    /**
     * 获取配置构建器
     * @param tableName
     * @return
     */
    public ConfigBuilder getConfigBuilder(String tableName, String parentPackageName, String mouldName) {
        ConfigBuilder configBuilder = new ConfigBuilder(
                this.getPackageConfigBuilder(parentPackageName, mouldName).build(),
                this.getDataSourceConfigBuilder(generationPo.getDbUrl(), generationPo.getDbUserName(), generationPo.getDbPassword()).build(),
                this.getStrategyConfigBuilder(tableName),
                null,
                this.getGlobalConfigBuilder(generationPo.getOutputDir(), generationPo.getAuthor()).build(),
                this.getInjectionConfigBuilder(tableName).build());
        return configBuilder;
    }

    /**
     * 获取包配置构建器
     * @param parentPackageName 父包名
     * @param mouldName 父包模块名
     * @return PackageConfig.Builder
     */
    private PackageConfig.Builder getPackageConfigBuilder(String parentPackageName, String mouldName) {
        return new PackageConfig.Builder()
                .parent(parentPackageName) // 设置父包名
                .moduleName(mouldName) // 设置父包模块名
                .entity(generationPo.getEntityPackage()) // 设置 Entity 包名
                .service(generationPo.getServicePackage()) // 设置 Service 包名
                .serviceImpl(generationPo.getServiceImplPackage()) // 设置 Service Impl 包名
                .mapper(generationPo.getMapperPackage()) // 设置 Mapper 包名
                .xml(generationPo.getMapperXmlPackage()) // 设置 Mapper XML 包名
                .controller(generationPo.getControllerPackage()); // 设置 Controller 包名
    }

    /**
     * 获取数据源配置构建器
     * @param dbUrl 数据库连接地址
     * @param dbUserName 数据库名称
     * @param dbPassword 数据库密码
     * @return DataSourceConfig.Builder
     */
    private DataSourceConfig.Builder getDataSourceConfigBuilder(String dbUrl,String dbUserName,String dbPassword) {
        return new DataSourceConfig.Builder(dbUrl,dbUserName,dbPassword)
                .schema("mybatis-plus") // 数据库 schema(部分数据库适用)
                .keyWordsHandler(new MySqlKeyWordsHandler()); // 数据库关键字处理器
    }

    /**
     * 获取策略配置
     * @param tableName 数据库表名称
     * @return StrategyConfig
     */
    private StrategyConfig getStrategyConfigBuilder(String tableName) {
        return new StrategyConfig.Builder()
                .addInclude(tableName) // 设置要生成的表名
                .enableCapitalMode() // 开启大写命名
                .enableSkipView() // 开启跳过视图
                .disableSqlFilter() // 禁用 SQL 过滤
                .entityBuilder() // 实体策略
                .logicDeleteColumnName(generationPo.getDeleteFlagField()) // 逻辑删除字段名
                .enableFileOverride()
                .enableLombok()
                .enableTableFieldAnnotation()
                .naming(NamingStrategy.underline_to_camel) // 下划线转驼峰
                .columnNaming(NamingStrategy.underline_to_camel) // 下划线转驼峰
                .addTableFills(
                        new Column(generationPo.getCreateTimeField(), FieldFill.INSERT), // 自动填充创建时间
                        new Column(generationPo.getUpdateTimeField(), FieldFill.INSERT_UPDATE), // 自动填充更新时间
                        new Column(generationPo.getCreateByField(), FieldFill.INSERT), // 自动填充创建人
                        new Column(generationPo.getUpdateByField(), FieldFill.INSERT_UPDATE) // 自动填充更新人
                )
                .formatFileName("%s")
                .javaTemplate(generationPo.getTemplatesMap().get(GenerationPo.ENTITY)) // 实体类模板路径
                // .disable() // 禁用实体类生成
                .serviceBuilder() // service 策略
                .enableFileOverride()
                .superServiceClass(ConstVal.SUPER_SERVICE_CLASS)
                .superServiceImplClass(ConstVal.SUPER_SERVICE_IMPL_CLASS)
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImp")
                .serviceTemplate(generationPo.getTemplatesMap().get(GenerationPo.SERVICE)) // service 模板路径
                .serviceImplTemplate(generationPo.getTemplatesMap().get(GenerationPo.SERVICE_IMPL)) // service impl 模板路径
                // .disableService() // 禁用 Service 层生成
                .mapperBuilder() // mapper 策略
                .enableFileOverride()
                .superClass(ConstVal.SUPER_MAPPER_CLASS)
                .enableBaseResultMap()
                .enableBaseColumnList()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .mapperTemplate(generationPo.getTemplatesMap().get(GenerationPo.MAPPER)) // mapper 模板路径
                .mapperXmlTemplate(generationPo.getTemplatesMap().get(GenerationPo.MAPPER_XML)) // mapper xml 模板路径
                .controllerBuilder() // controller 策略
                .superClass(BaseController.class) // 设置父类
                .enableFileOverride()
                .enableHyphenStyle()
                .enableRestStyle()
                .formatFileName("%sController")
                .template(generationPo.getTemplatesMap().get(GenerationPo.CONTROLLER)) // controller 模板路径
                .build();
    }

    /**
     * 获取全局配置构建器
     * @param outputDirectory 输出目录
     * @param author 作者
     * @return GlobalConfig.Builder
     */
    private GlobalConfig.Builder getGlobalConfigBuilder(String outputDirectory, String author) {
        return new GlobalConfig.Builder()
                .disableOpenDir() // 允许自动打开输出目录
                .outputDir(outputDirectory) // 设置输出目录
                .author(author) // 设置作者名
                .enableSpringdoc() // 开启 Swagger 模式
                .dateType(DateType.TIME_PACK) // 设置时间类型策略
                .commentDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取注入配置构建器
     * @param tableName 数据库表名称
     * @return InjectionConfig.Builder
     */
    private InjectionConfig.Builder getInjectionConfigBuilder(String tableName) {
        /**
         * 这里为自定义模板配置的位置，添加模板请到 generationPo.getCustomFiles() 方法中去 new 一个 CustomFile 对象，并且添加到list当中去
         * 默认情况下，在使用默认模板时不生成自定义模板，如果要使用自定义模板，请将 isDefalutTemplate 改为 false
         */
        List<CustomFile> customFiles = generationPo.getCustomFiles(tableName);

        return new InjectionConfig.Builder()
                .customFile(customFiles)
                .beforeOutputFile((tableInfo, objectMap)->{
                    // 可以在这里添加自定义逻辑，如修改 objectMap 中的配置
                });
    }

}
