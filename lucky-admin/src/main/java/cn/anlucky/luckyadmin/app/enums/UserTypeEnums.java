package cn.anlucky.luckyadmin.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 开放用户类型枚举
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnums {

    SYSTEM_USER(0, "系统用户"),
    WX_MINI_PROGRAM(1, "微信小程序"),
    DOUYIN_MINI_PROGRAM(2, "抖音小程序");

    /**
     * 代码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    /**
     * 根据code获取描述
     * @param code
     * @return
     */
    public static String getDesc(Integer code) {
        for (UserTypeEnums value : UserTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }

}
