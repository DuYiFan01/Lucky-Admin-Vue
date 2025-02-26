package cn.anlucky.luckyadmin.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 项目全局配置
 */
@Component
@ConfigurationProperties(prefix="lucky")
public class LuckyConfig {

    /**
     * 验证码开关
     */
    private static Boolean captchaEnabled;
    /**
     * 验证码类型 number-数字 text-字符 mixed-数字+字母 math-计算
     */
    private static String captchaType;

    /**
     * ip获取地址开关
     */
    private static Boolean ipLocation;

    /**
     * 文件上传地址
     */
    private static String profile;


    public void setCaptchaEnabled(Boolean captchaEnabled) {
        LuckyConfig.captchaEnabled = captchaEnabled;
    }

    public static Boolean getCaptchaEnabled() {
        return captchaEnabled;
    }

    public void setCaptchaType(String captchaType) {
        LuckyConfig.captchaType = captchaType;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setIpLocation(Boolean ipLocation) {
        LuckyConfig.ipLocation = ipLocation;
    }

    public static Boolean getIpLocation() {
        return ipLocation;
    }

    public void setProfile(String profile) {
        LuckyConfig.profile = profile;
    }

    public static String getProfile() {
        return profile;
    }

}
