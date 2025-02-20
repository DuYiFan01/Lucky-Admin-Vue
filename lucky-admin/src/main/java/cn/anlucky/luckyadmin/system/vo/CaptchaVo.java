package cn.anlucky.luckyadmin.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVo {
    /**
     * 是否开启验证码
     */
    private Boolean captchaEnabled;
    /**
     * uuid
     */
    private String uuid;

    /**
     * Base64 图片数据
     */
    private String image;

}
