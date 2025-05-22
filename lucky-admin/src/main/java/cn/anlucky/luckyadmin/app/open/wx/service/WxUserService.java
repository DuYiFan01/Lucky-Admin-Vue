package cn.anlucky.luckyadmin.app.open.wx.service;

import cn.anlucky.luckyadmin.system.vo.LoginVo;

/**
 * 微信小程序用户服务
 */
public interface WxUserService {


    /**
     * 微信一键登录
     * @param code 微信一键登录返回的code
     * @return 用户信息
     */
    public LoginVo wxLogin(String code);

}
