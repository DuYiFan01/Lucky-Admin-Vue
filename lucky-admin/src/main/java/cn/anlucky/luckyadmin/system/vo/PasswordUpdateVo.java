package cn.anlucky.luckyadmin.system.vo;

import lombok.Data;

@Data
public class PasswordUpdateVo {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 原密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 确认密码
     */
    private String confirmPassword;
}
