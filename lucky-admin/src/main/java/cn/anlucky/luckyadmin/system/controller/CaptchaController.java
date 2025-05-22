package cn.anlucky.luckyadmin.system.controller;


import cn.anlucky.luckyadmin.config.LuckyConfig;

import cn.anlucky.luckyadmin.system.config.CaptchaConfig;
import cn.anlucky.luckyadmin.system.vo.CaptchaVo;
import cn.anlucky.luckyadmin.utils.satoken.SaTokenDaoUtils;
import cn.anlucky.luckyadmin.utils.sign.Base64;
import cn.anlucky.luckyadmin.utils.text.Convert;
import cn.anlucky.luckyadmin.utils.uuid.IdUtils;
import cn.anlucky.luckyadmin.vo.R;
import com.google.code.kaptcha.Producer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码获取
 */
@RestController
@Tag(name = "图片验证码Controller")
@RequestMapping("/user")
public class CaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;
    /**
     * 数字验证码长度
     */
    private final Integer numberTypeLength = 4;
    /**
     * 文字验证码长度
     */
    private final Integer textTypeLength = 4;
    /**
     * 混合验证码长度
     */
    private final Integer mixedTypeLength = 4;
    /**
     * 获取验证码
     * @return
     */
    @Operation(summary = "获取验证码")
    @GetMapping("/getCode")
    public R getCode(){
        Boolean captchaEnabled = LuckyConfig.getCaptchaEnabled();
        CaptchaVo captchaVo = new CaptchaVo();
        // 设置验证码是否开启状态
        captchaVo.setCaptchaEnabled(captchaEnabled);
        if (!captchaEnabled) {
            // 验证码关闭
            return R.ok(captchaVo);
        }

        // 获取UUID
        String uuid = IdUtils.simpleUUID();
        String code = null;
        BufferedImage image = null;

        if (LuckyConfig.getCaptchaType().equals(CaptchaConfig.CAPTCHA_TYPE_NUM)){
            // 数字验证码
            code = getCaptchaByNumberType(numberTypeLength);
        }else if(LuckyConfig.getCaptchaType().equals(CaptchaConfig.CAPTCHA_TYPE_TEXT)){
            // 字母验证码
            code = getCaptchaByTextType(textTypeLength);
        }else if(LuckyConfig.getCaptchaType().equals(CaptchaConfig.CAPTCHA_TYPE_MIXED)){
            // 数字字母混合验证
            code = getCaptchaByMixedType(mixedTypeLength);
        } else if (LuckyConfig.getCaptchaType().equals(CaptchaConfig.CAPTCHA_TYPE_MATH)) {
            // 计算验证码
            code = getCaptchaByNumberType(numberTypeLength);
        } else {
            // 默认是数字验证码
            // 数字验证码
            code = getCaptchaByNumberType(numberTypeLength);
        }
        // 生成图片
        image = captchaProducer.createImage(code);

        // 设置验证码到redis
        SaTokenDaoUtils.setObjectKey(SaTokenDaoUtils.CAPTCHA_CACHE + uuid, code, 5*60L);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return R.error(e.getMessage());
        }
        captchaVo.setUuid(uuid);
        captchaVo.setImage(Base64.encode(os.toByteArray()));
        return R.ok(captchaVo);
    }
    /**
     * 获取数字验证码
     * @param length 位数长度
     * @return
     */
    public static String getCaptchaByNumberType(int length){
        if (length > 0){
            StringBuilder captcha = new StringBuilder();
            for (int i = 0; i < length; i++) {
                captcha.append(getRandomNumber());
            }
            return captcha.toString();
        }
        return null;
    }
    /**
     * 获取字母验证码
     * @param length
     * @return
     */
    public static String getCaptchaByTextType(int length){
        if (length > 0){
            StringBuilder captcha = new StringBuilder();
            for (int i = 0; i < length; i++) {
                captcha.append(getRandomText());
            }
            return captcha.toString();
        }
        return null;
    }
    /**
     * 获取混合验证码
     * @param length
     * @return
     */
    public static String getCaptchaByMixedType(int length){
        if (length > 0){
            StringBuilder captcha = new StringBuilder();
            for (int i = 0; i < length; i++) {
                if(Convert.toInt(getRandomNumber()) > 5){
                    captcha.append(getRandomText());
                }else {
                    captcha.append(getRandomNumber());
                }
            }
            return captcha.toString();
        }
        return null;
    }
    /**
     * 获取1位 0-9 随机数字
     * @return
     */
    public static String getRandomNumber(){
        String[] number = {"0","1","2","3","4","5","6","7","8","9"};
        return number[(int)(Math.random()*10)];
    }

    /**
     * 获取1位 a-z A-Z 随机字符
     * @return
     */
    public static String getRandomText(){
        String[] text = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        return text[(int)(Math.random()*52)];
    }

}
