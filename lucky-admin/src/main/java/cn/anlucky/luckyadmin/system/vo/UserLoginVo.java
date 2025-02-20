package cn.anlucky.luckyadmin.system.vo;

import lombok.Data;

@Data
public class UserLoginVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String code;
    /**
     * 验证码唯一标识
     */
    private String uuid;

}
