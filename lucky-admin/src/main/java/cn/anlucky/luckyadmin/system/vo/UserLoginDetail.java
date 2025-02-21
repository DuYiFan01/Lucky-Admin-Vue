package cn.anlucky.luckyadmin.system.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDetail {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录IP地址
     */
    private String ip;
    /**
     * 登录IP地点
     */
    private String address;
    /**
     * 登录浏览器
     */
    private String userAgent;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime loginTime;
    /**
     * token
     */
    private String token;


}
