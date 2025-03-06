package cn.anlucky.luckyadmin.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信配置
 */
@Component
@ConfigurationProperties(prefix="wx")
public class WxConfig {

    @Getter
    private static String appid;
    @Getter
    private static String secret;

    public void setAppId(String appid) {
        WxConfig.appid = appid;
    }

    public void setSecret(String secret) {
        WxConfig.secret = secret;
    }
}
