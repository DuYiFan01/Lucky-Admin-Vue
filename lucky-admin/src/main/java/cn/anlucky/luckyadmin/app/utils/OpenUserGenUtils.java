package cn.anlucky.luckyadmin.app.utils;

import cn.anlucky.luckyadmin.app.enums.UserTypeEnums;
import cn.anlucky.luckyadmin.app.enums.UserTypePrefixEnums;
import cn.anlucky.luckyadmin.system.controller.CaptchaController;
import cn.anlucky.luckyadmin.utils.text.Convert;

/**
 * 跨平台用户生成工具类
 *
 * @author anlucky
 */
public class OpenUserGenUtils {


    /**
     * 生成跨平台用户名
     * @param userType
     * @return
     */
    public static String genOpenUsername(UserTypeEnums  userType) {
        return UserTypePrefixEnums.getPrefix(userType.getCode()) + CaptchaController.getCaptchaByMixedType(12);
    }



}
