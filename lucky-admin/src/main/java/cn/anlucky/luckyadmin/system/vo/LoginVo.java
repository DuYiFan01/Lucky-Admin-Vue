package cn.anlucky.luckyadmin.system.vo;

import lombok.Data;

@Data
public class LoginVo {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录成功的Token
     */
    private String token;
}
