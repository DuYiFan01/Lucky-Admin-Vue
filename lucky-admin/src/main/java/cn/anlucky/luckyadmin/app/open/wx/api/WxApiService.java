package cn.anlucky.luckyadmin.app.open.wx.api;

import cn.anlucky.luckyadmin.app.open.wx.api.vo.Code2SessionVo;

/**
 * 微信小程序接口
 * @author anlucky
 */
public interface WxApiService {
    /**
     * 小程序登录
     * @param code
     */
    public Code2SessionVo code2Session(String code);
}
