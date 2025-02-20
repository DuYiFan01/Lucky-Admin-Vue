package cn.anlucky.luckyadmin.gen.config;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
public class GenerationBean {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean(name = "generationPo")
    public GenerationPo generationPo() {
        GenerationPo generationPo = new GenerationPo();
        generationPo.setDbUrl(dbUrl);
        generationPo.setDbUserName(dbUserName);
        generationPo.setDbPassword(dbPassword);
        generationPo.setAuthor("yifan.du");
        generationPo.setOutputDir("D:/Desktop/luckyGenerator/111");
        generationPo.setPackageName("cn.anlucky");
        generationPo.setMouldName("system");
        generationPo.setCreateTimeField("create_time");
        generationPo.setUpdateTimeField("update_time");
        generationPo.setCreateByField("create_by");
        generationPo.setUpdateByField("update_by");
        generationPo.setDeleteFlagField("del_flag");
        generationPo.setEntityPackage("pojo");
        generationPo.setMapperPackage("mapper");
        generationPo.setMapperXmlPackage("mapper.xml");
        generationPo.setServicePackage("service");
        generationPo.setServiceImplPackage("service.impl");
        generationPo.setControllerPackage("controller");
        return generationPo;
    }

}
