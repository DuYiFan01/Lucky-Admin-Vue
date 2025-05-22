package cn.anlucky.luckyadmin.app.open.wx.api.impl;

import cn.anlucky.luckyadmin.app.open.wx.api.WxApiService;
import cn.anlucky.luckyadmin.app.open.wx.api.vo.Code2SessionVo;
import cn.anlucky.luckyadmin.config.WxConfig;
import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.utils.http.HttpUtils;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

/**
 * 微信小程序接口实现类
 * @author anlucky
 */
@Service
public class WxApiServiceImpl implements WxApiService {

    /**
     * 小程序登录
     *
     * @param code
     */
    @Override
    public Code2SessionVo code2Session(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WxConfig.getAppid() + "&secret=" + WxConfig.getSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        String s = HttpUtils.sendGet(url);
        Code2SessionVo code2SessionVo = JSONObject.parseObject(s, Code2SessionVo.class);
        if (code2SessionVo.getErrcode() != null) {
            // 报错了
            throw new CustomException(code2SessionVo.getErrmsg());
        }
        // 没有报错
        return code2SessionVo;
    }
}
