package cn.anlucky.luckyadmin.gen.service.impl;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.gen.config.GenerationConfig;
import cn.anlucky.luckyadmin.gen.config.GenerationPo;
import cn.anlucky.luckyadmin.system.constant.Constants;
import cn.anlucky.luckyadmin.gen.service.GenerationService;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class GenerationServiceImpl implements GenerationService {

    @Autowired
    private GenerationPo generationPo;

    /**
     * 预览代码生成
     *
     * @param parentPackage 父包名 如：cn.anlucky.luckyadmin
     * @param moduleName    模块名称 如：system
     * @param tableName     表名 如：sys_user
     * @return Map<String, String> key:文件名称如SysUsers.java value:代码文本
     */
    @Override
    public Map<String, String> previewCode(String parentPackage, String moduleName, String tableName) {
        // 获得基础模板生成的代码
        Map<String, String> map = generateBasicsTemplate(parentPackage, moduleName, tableName);
        // 获得自定义模板生成的代码
        Map<String, String> map1 = generateCustomTemplate(parentPackage, moduleName, tableName);
        // 合并两个Map
        map.putAll(map1);
        map.forEach((key, value) -> {
            System.out.println("key = " + key);
        });

        return map;
    }

    /**
     * 生成代码到本机的指定位置
     *
     * @param parentPackage 父包名
     * @param moduleName    模块名称
     * @param tableName     表名
     */
    @Override
    public void generateCode(String parentPackage, String moduleName, String tableName) {
        // 创建生成配置对象
        GenerationConfig generationConfig = new GenerationConfig(generationPo);
        // 获得configBuilder对象
        ConfigBuilder configBuilder = generationConfig.getConfigBuilder(tableName, parentPackage, moduleName);
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine().init(configBuilder);
        ((AbstractTemplateEngine)templateEngine).setConfigBuilder(configBuilder);
        ((AbstractTemplateEngine)templateEngine).init(configBuilder).batchOutput().open();
        log.info("代码生成成功,文件输出位置{}", configBuilder.getGlobalConfig().getOutputDir());
    }

    /**
     * 将代码文件放入到一个压缩包当中进行下载
     *
     * @param parentPackage 父包名
     * @param moduleName    模块名称
     * @param tableName     表名
     * @return 压缩包
     */
    @Override
    public byte[] downloadCode(String parentPackage, String moduleName, String tableName,String zipName) {
        // 获得代码
        Map<String, String> map = previewCode(parentPackage, moduleName, tableName);
        // 获得包路径
        String packagePath = packageToPath(parentPackage) + "/" + packageToPath(moduleName) + "/";
        // 创建ZIP输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 创建ZIP对象
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 循环添加zip代码文件
        try {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                zip.putNextEntry(new ZipEntry(packagePath + key));
                IOUtils.write(map.get(key), zip);
            }
            zip.flush();
            zip.closeEntry();
            IOUtils.closeQuietly(zip);
        } catch (Exception e) {
            throw new CustomException("生成压缩包失败");
        }
        return outputStream.toByteArray();
    }


    /**
     * 读取文件内容
     * @param fileName 文件名
     * @return 文件内容字符串
     * @throws CustomException 如果文件名为空或文件不存在
     */
    private String readFileContent(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new CustomException("文件名不能为空");
        }
        StringBuilder content = new StringBuilder();
        ClassPathResource resource = new ClassPathResource(fileName);
        if (!resource.exists()) {
            throw new CustomException("文件 " + fileName + " 不存在");
        }
        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Constants.UTF8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    content.append("\n");
                }
                content.append(line);
            }
        } catch (IOException e) {
            throw new CustomException("读取文件 " + fileName + " 时发生错误"+e.getMessage());
        }
        return content.toString();
    }

    /**
     * 将包名转换为路径
     * @param packageName 包名
     * @return 路径
     */
    private String packageToPath(String packageName) {
        return packageName.replace(".", "/");
    }


    /**
     * 生成基础模板
     * @param parentPackage 父包名
     * @param moduleName 模块名称
     * @param tableName 表名
     * @return Map key 为Java中文件名称 value 为生成Java代码内容
     */
    private Map<String,String> generateBasicsTemplate(String parentPackage, String moduleName, String tableName){
        // 创建生成配置对象
        GenerationConfig generationConfig = new GenerationConfig(generationPo);
        // 获得configBuilder对象
        ConfigBuilder configBuilder = generationConfig.getConfigBuilder(tableName, parentPackage, moduleName);
        // 取得表信息
        List<TableInfo> tableInfos = configBuilder.getTableInfoList();
        TableInfo tableInfo = tableInfos.get(0);
        // 创建模板引擎对象
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine().init(configBuilder);
        // 获取框架模板列表 key 为模板名称 value 为模板位置
        Map<String, String> templatesMap = generationPo.getTemplatesMap();
        // 创建Map添加生成的代码
        HashMap<String, String> map = new HashMap<>();
        Map<String, Object> objectMap = templateEngine.getObjectMap(configBuilder, tableInfo);
        // 循环生成代码
        templatesMap.forEach((key, value) -> {
            try {
                String writer = templateEngine.writer(objectMap, tableInfo.getEntityName(),readFileContent(templateEngine.templateFilePath(value)));
                switch (key){
                    case GenerationPo.ENTITY:
                        map.put(tableInfo.getEntityName() + ".java", writer);
                        break;
                    case GenerationPo.MAPPER:
                        map.put(tableInfo.getMapperName() + ".java", writer);
                        break;
                    case GenerationPo.MAPPER_XML:
                        map.put(tableInfo.getXmlName() + ".xml", writer);
                        break;
                    case GenerationPo.SERVICE:
                        map.put(tableInfo.getServiceName() + ".java", writer);
                        break;
                    case GenerationPo.SERVICE_IMPL:
                        map.put(tableInfo.getServiceImplName() + ".java", writer);
                        break;
                    case GenerationPo.CONTROLLER:
                       map.put(tableInfo.getControllerName() + ".java", writer);
                        break;
                    default:
                        throw new CustomException("未知的代码类型：" + key);
                }
            }catch (Exception e){
                log.error("生成代码失败代码类型 = {}，错误原因 = {}，错误模板 = {}", key,e.getMessage(),value);
                throw new CustomException("生成代码失败代码类型：" + key+"错误原因：" +e.getMessage());
            }
        });
        return map;
    }

    /**
     * 生成自定义模板
     * @param parentPackage 父包名
     * @param moduleName 模块名称
     * @param tableName 表名
     * @return Map key 为Java中文件名称 value 为生成Java代码内容
     */
    private Map<String,String> generateCustomTemplate(String parentPackage, String moduleName, String tableName){
        // 创建生成配置对象
        GenerationConfig generationConfig = new GenerationConfig(generationPo);
        // 获得configBuilder对象
        ConfigBuilder configBuilder = generationConfig.getConfigBuilder(tableName, parentPackage, moduleName);
        // 取得表信息
        List<TableInfo> tableInfos = configBuilder.getTableInfoList();
        TableInfo tableInfo = tableInfos.get(0);
        // 创建模板引擎对象
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine().init(configBuilder);
        // 获取框架模板列表 key 为模板名称 value 为模板位置
        List<CustomFile> customFiles = generationPo.getCustomFiles(tableName);
        // 创建Map添加生成的代码
        HashMap<String, String> map = new HashMap<>();
        Map<String, Object> objectMap = templateEngine.getObjectMap(configBuilder, tableInfo);
        for (CustomFile customFile : customFiles) {
            String key = customFile.getPackageName();
            String value = customFile.getTemplatePath();
            try {
                String writer = templateEngine.writer(objectMap, tableInfo.getEntityName(),readFileContent(templateEngine.templateFilePath(value)));
                map.put(customFile.getFileName(), writer);
            }catch (Exception e){
                log.error("生成代码失败代码类型 = {}，错误原因 = {}，错误模板 = {}", customFile.getPackageName(),e.getMessage(),value);
                throw new CustomException("生成代码失败代码类型：" + key+"错误原因：" +e.getMessage());
            }
        }
        return map;
    }




}
