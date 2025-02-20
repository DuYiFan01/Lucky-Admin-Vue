package cn.anlucky.luckyadmin.gen.service;


import java.util.Map;


public interface GenerationService {


    /**
     * 预览代码生成
     * @param parentPackage 父包名 如：cn.anlucky.luckyadmin
     * @param moduleName 模块名称 如：system
     * @param tableName 表名 如：sys_user
     * @return Map<String,String> key:entity mapper mapperXML service serviceImpl controller value:代码文本
     */
    Map<String,String> previewCode(String parentPackage,String moduleName, String tableName);


    /**
     * 生成代码到本机的指定位置
     * @param parentPackage 父包名
     * @param moduleName 模块名称
     * @param tableName 表名
     */
    void generateCode(String parentPackage,String moduleName, String tableName);

    /**
     * 将代码文件放入到一个压缩包当中进行下载
     * @param parentPackage 父包名
     * @param moduleName 模块名称
     * @param tableName 表名
     * @param zipName 压缩包名称
     * @return 压缩包
     */
    byte[] downloadCode(String parentPackage,String moduleName, String tableName,String zipName);

}
