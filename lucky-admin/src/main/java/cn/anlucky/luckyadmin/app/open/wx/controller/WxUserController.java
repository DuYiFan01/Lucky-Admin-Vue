package cn.anlucky.luckyadmin.app.open.wx.controller;

import cn.anlucky.luckyadmin.app.open.wx.service.WxUserService;
import cn.anlucky.luckyadmin.system.vo.LoginVo;
import cn.anlucky.luckyadmin.vo.R;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "微信小程序配置相关Controller")
@RequiredArgsConstructor
@RequestMapping("/app/wx")
public class WxUserController {

    private final WxUserService wxUserService;

    /**
     * 微信小程序登录
     * @param code
     * @return
     */
    @PostMapping("/login")
    public R login(@Parameter String code){
        LoginVo loginVo = wxUserService.wxLogin(code);
        return R.ok(loginVo);
    }
}
