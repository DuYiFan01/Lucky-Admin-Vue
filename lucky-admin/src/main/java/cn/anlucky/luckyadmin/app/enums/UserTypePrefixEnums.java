package cn.anlucky.luckyadmin.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypePrefixEnums {
    /**
     * 系统用户
     */
    SYSTEM_USER_PREFIX(UserTypeEnums.SYSTEM_USER, "sys"),
    /**
     * 微信小程序
     */
    WX_MINI_PROGRAM_PREFIX(UserTypeEnums.WX_MINI_PROGRAM, "wx"),
    /**
     * 抖音小程序
     */
    DOUYIN_MINI_PROGRAM_PREFIX(UserTypeEnums.DOUYIN_MINI_PROGRAM, "dy");

    /**
     * 用户类型
     */
    private UserTypeEnums userTypeEnums;
    /**
     * 前缀
     */
    private String prefix;

    /**
     * 根据用户类型code获取前缀
     * @param code
     * @return
     */
    public static String getPrefix(Integer code) {
        for (UserTypePrefixEnums userTypePrefixEnums : UserTypePrefixEnums.values()) {
            if (userTypePrefixEnums.getUserTypeEnums().getCode().equals(code)) {
                return userTypePrefixEnums.getPrefix();
            }
        }
        return null;
    }

}
