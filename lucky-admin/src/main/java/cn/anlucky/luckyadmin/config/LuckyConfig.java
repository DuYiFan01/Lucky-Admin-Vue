package cn.anlucky.luckyadmin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 项目全局配置
 */
@Getter
@Configuration
public class LuckyConfig {

    /**
     * 验证码开关
     */
    @Value("${lucky.admin.captcha}")
    private Boolean captchaEnabled;
    /**
     * 验证码类型 number-数字 text-字符 mixed-数字+字母 math-计算
     */
    @Value("${lucky.admin.captcha-type}")
    private String captchaType;

    /**
     * ip获取地址开关
     */
    @Value("${lucky.admin.ip-location}")
    private Boolean ipLocation;




}
