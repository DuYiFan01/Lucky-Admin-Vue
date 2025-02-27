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
    @Getter
    private static Boolean captchaEnabled;
    /**
     * 验证码类型 number-数字 text-字符 mixed-数字+字母 math-计算
     */
    @Getter
    private static String captchaType;

    /**
     * ip获取地址开关
     */
    @Getter
    private static Boolean ipLocation;

    /**
     * 文件上传位置 local-本地 qiniu-七牛
     */
    @Getter
    private static String fileLocation;

    /**
     * 文件上传地址,末尾没有 "/"
     */
    @Getter
    private static String profile;

    /**
     * 文件最大上传大小
     */
    @Getter
    private static Long fileMaxSize;

    /**
     * 文件名最大长度
     */
    @Getter
    private static Integer fileNameMaxLength;

    /**
     * 七牛云存储AK
     */
    @Getter
    private static String qiniuAk;
    /**
     * 七牛云存储SK
     */
    @Getter
    private static String qiniuSk;
    /**
     * 七牛云存储空间
     */
    @Getter
    private static String qiniuBucket;


    public void setCaptchaEnabled(Boolean captchaEnabled) {
        LuckyConfig.captchaEnabled = captchaEnabled;
    }

    public void setCaptchaType(String captchaType) {
        LuckyConfig.captchaType = captchaType;
    }

    public void setIpLocation(Boolean ipLocation) {
        LuckyConfig.ipLocation = ipLocation;
    }


    public void setProfile(String profile) {
        boolean b = profile.endsWith("/");
        if (b) {
            profile = profile.substring(0, profile.length() - 1);
        }
        LuckyConfig.profile = profile;
    }

    public void setQiniuAk(String qiniuAk) {
        LuckyConfig.qiniuAk = qiniuAk;
    }

    public void setQiniuSk(String qiniuSk) {
        LuckyConfig.qiniuSk = qiniuSk;
    }

    public void setFileMaxSize(Long fileMaxSize) {
        LuckyConfig.fileMaxSize = fileMaxSize * 1024 * 1024L;;
    }


    public void setFileNameMaxLength(Integer fileNameMaxLength) {
        LuckyConfig.fileNameMaxLength = fileNameMaxLength;
    }

    public void setFileLocation(String fileLocation) {
        LuckyConfig.fileLocation = fileLocation;
    }

    public void setQiniuBucket(String qiniuBucket) {
        LuckyConfig.qiniuBucket = qiniuBucket;
    }
}
