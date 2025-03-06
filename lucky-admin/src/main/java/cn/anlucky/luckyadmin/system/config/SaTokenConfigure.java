package cn.anlucky.luckyadmin.system.config;

import cn.anlucky.luckyadmin.config.LuckyConfig;
import cn.anlucky.luckyadmin.system.constant.Constants;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * Sa-Token 配置
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Value("${server.servlet.context-path}")
    private String path;

    /**
     * SaToken 拦截器权限校验
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handler -> {
                    StpUtil.checkLogin();
                    SaRouter.match("/swagger-ui/**",r->StpUtil.checkPermission("tools::swagger::query"));
                    SaRouter.match("/api-docs",r->StpUtil.checkPermission("tools::swagger::query"));
                }))
                .addPathPatterns("/**")
                .excludePathPatterns( "/user/login","/user/register","/user/getCode","/user/logout");
        // /user/register 注册地址
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + LuckyConfig.getProfile() + "/");
    }
    /**
     * Sa-Token 权限认证规则
     *
     * @return
     */
    public Map<String, String> getAuthRules() {
        return null;
    }
    /**
     * Sa-Token 整合 jwt (Simple 简单模式)
     *
     * @return
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}
