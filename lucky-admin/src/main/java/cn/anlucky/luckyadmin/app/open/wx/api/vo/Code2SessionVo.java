package cn.anlucky.luckyadmin.app.open.wx.api.vo;


import lombok.Data;

/**
 * 微信登录返回参数
 */
@Data
public class Code2SessionVo {
    /**
     * 会话秘钥
     */
    private String session_key;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID
     */
    private String unionid;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 错误码
     */
    private Integer errcode;

}
