package cn.anlucky.luckyadmin.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String title = "Lucky-Admin-Vue-API";
    private static final String description = "Lucky-Admin-Vue-API 快速开发API文档，帮助您进行快速开发系统，快速了解整个项目API的调用以及调用某个API需要的参数";

    private static final String version = "1.0";

    private static final String tokenName = "token";

    /**
     * API分组，可以自定义去分组
     */
    // @Bean
    // public GroupedOpenApi publicApi() {
    //     return GroupedOpenApi.builder()
    //             .group("user") // 组名称
    //             .pathsToMatch("/user/**") // 对应组的路径
    //             .build();
    // }

    @Bean
    public OpenAPI customOpenApi()
    {
        return new OpenAPI()
                .info(getApiInfo());
    }

    /**
     * 添加摘要信息
     */
    public Info getApiInfo()
    {
        return new Info()
                // 设置标题
                .title(title)
                // 描述
                .description(description)
                // 作者信息
                .contact(new Contact().name("yifan.du"))
                // 版本
                .version(version);
    }
}
