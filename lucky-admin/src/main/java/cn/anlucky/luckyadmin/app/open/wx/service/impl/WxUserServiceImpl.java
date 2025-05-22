package cn.anlucky.luckyadmin.app.open.wx.service.impl;

import cn.anlucky.luckyadmin.app.enums.UserTypeEnums;
import cn.anlucky.luckyadmin.app.open.wx.api.WxApiService;
import cn.anlucky.luckyadmin.app.open.wx.api.vo.Code2SessionVo;
import cn.anlucky.luckyadmin.app.open.wx.service.WxUserService;
import cn.anlucky.luckyadmin.app.pojo.SysOpenUsers;
import cn.anlucky.luckyadmin.app.service.SysOpenUsersService;
import cn.anlucky.luckyadmin.app.utils.OpenUserGenUtils;
import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.constant.Constants;
import cn.anlucky.luckyadmin.system.manager.AsyncManager;
import cn.anlucky.luckyadmin.system.manager.factory.AsyncFactory;
import cn.anlucky.luckyadmin.system.pojo.SysUsers;
import cn.anlucky.luckyadmin.system.pojo.UserLoginDetail;
import cn.anlucky.luckyadmin.system.service.SysUsersService;
import cn.anlucky.luckyadmin.system.service.UserLoginService;
import cn.anlucky.luckyadmin.system.vo.LoginVo;
import cn.anlucky.luckyadmin.utils.ServletUtils;
import cn.anlucky.luckyadmin.utils.ip.AddressUtils;
import cn.anlucky.luckyadmin.utils.ip.IpUtils;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import cn.dev33.satoken.stp.SaLoginModel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 微信小程序用户服务实现类
 */
@Service
public class WxUserServiceImpl implements WxUserService {

    @Autowired
    private WxApiService wxApiService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private SysOpenUsersService sysOpenUsersService;
    @Autowired
    private SysUsersService sysUsersService;

    /**
     * 微信一键登录
     *
     * @param code 微信一键登录返回的code
     * @return 用户信息
     */
    @Transactional
    @Override
    public LoginVo wxLogin(String code) {
        if (code == null) {
            throw new CustomException("令牌code为空");
        }
        LoginVo loginvo = new LoginVo();
        try {
            // 小程序登录
            Code2SessionVo code2SessionVo = wxApiService.code2Session(code);
            // 获取openId
            String openId = code2SessionVo.getOpenid();
            // 查询当前OpenId是否绑定了系统账号
            LambdaQueryWrapper<SysOpenUsers> wrapper = new LambdaQueryWrapper<>();
            // 微信小程序
            wrapper.eq(SysOpenUsers::getSysUserType, UserTypeEnums.WX_MINI_PROGRAM.getCode());
            // open Id
            wrapper.eq(SysOpenUsers::getUserOpenId, openId);
            // 只获取PC系统的ID
            wrapper.select(SysOpenUsers::getSysUserId);
            // 获取UserId
            SysOpenUsers sysOpenUsers = sysOpenUsersService.getOne(wrapper);
            // 如果没有绑定系统用户，那么注册系统用户实行绑定
            // 自动生成微信小程序用户名称
            String openUserName = null;
            if (sysOpenUsers == null) {
                while (true) {
                    openUserName = OpenUserGenUtils.genOpenUsername(UserTypeEnums.WX_MINI_PROGRAM);
                    // 查询当前用户名是否注册为系统用户
                    if (sysUsersService.count(new LambdaQueryWrapper<SysUsers>().eq(SysUsers::getUsername, openUserName)) == 0) {
                        break;
                    }
                }
                SysUsers sysUsers = new SysUsers();
                sysUsers.setUsername(openUserName);
                // 注册系统用户
                userLoginService.register(sysUsers);
                sysOpenUsers = new SysOpenUsers();
                sysOpenUsers.setSysUserId(sysUsers.getId());
                sysOpenUsers.setUserOpenId(openId);
                sysOpenUsers.setSysUserType(UserTypeEnums.WX_MINI_PROGRAM.getCode());
                // 保存用户和openId的绑定关系
                sysOpenUsersService.save(sysOpenUsers);
            }

            // 查询用户信息
            SysUsers users = sysUsersService.getById(sysOpenUsers.getSysUserId());
            String username = users.getUsername();
            Long userId = users.getId();

            // 用户登录
            // 设置登录Token携带信息
            SaLoginModel saLoginModel = new SaLoginModel();
            saLoginModel.setExtra("id", userId);
            saLoginModel.setExtra("username", username);
            // 携带参数登录
            SaUtils.login(userId, saLoginModel);
            String token = SaUtils.getToken();
            // 添加登录信息到缓存中
            UserLoginDetail userLoginDetail = new UserLoginDetail();
            userLoginDetail.setUserId(userId);
            userLoginDetail.setUsername(username);
            userLoginDetail.setToken(token);
            setUserLoginDetailCache(userLoginDetail);
            // 设置返回响应
            loginvo.setId(userId);
            loginvo.setUsername(username);
            loginvo.setToken(token);
            // 记录成功日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功"));
            // 记录登录信息 登录IP和最后登录时间
            recordLoginInfo(userId);
        } catch (Exception e) {
            // 记录失败日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginvo.getUsername(), Constants.LOGIN_FAIL, e.getMessage()));
            e.printStackTrace();
            throw new CustomException(e.getMessage());

        }
        return loginvo;
    }

    /**
     * 设置用户登录信息缓存
     * @param userLoginDetail 用户登录信息
     */
    private void setUserLoginDetailCache(UserLoginDetail userLoginDetail){
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        String address = AddressUtils.getRealAddressByIP(ip);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        userLoginDetail.setOs(os);
        userLoginDetail.setUserAgent(browser);
        userLoginDetail.setAddress(address);
        userLoginDetail.setLoginTime(LocalDateTime.now());
        userLoginDetail.setIp(ip);
        // 插入数据
        SaUtils.getTokenSessionByToken(userLoginDetail.getToken()).set(SaUtils.USER_LOGIN_DETAIL,userLoginDetail);
        //
        // // 遍历登录用户
        // List<String> list = StpUtil.searchTokenValue("", 0, 100, true);
        // System.out.println("list = " + list);
        // for (String s : list) {
        //     SaSession session = StpUtil.getTokenSessionByToken(s);
        //     Object loginDetail = session.get("loginDetail");
        //     System.out.println("loginDetail = " + loginDetail);
        // }
    }
    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     */
    private void recordLoginInfo(Long userId) {
        SysUsers sysUsers = new SysUsers();
        sysUsers.setId(userId);
        sysUsers.setLoginIp(IpUtils.getIpAddr());
        sysUsers.setLoginTime(LocalDateTime.now());
        sysUsers.setUpdateBy("system");
        sysUsersService.updateById(sysUsers);
    }
}
