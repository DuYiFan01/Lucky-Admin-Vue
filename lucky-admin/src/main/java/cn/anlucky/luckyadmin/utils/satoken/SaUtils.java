package cn.anlucky.luckyadmin.utils.satoken;

import cn.anlucky.luckyadmin.utils.StringUtils;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;

import java.util.List;

/**
 * SaToken 个人封装的工具类
 */
public class SaUtils {

    /**
     * 获取当前登录的token
     *
     * @return
     */
    public static String getToken() {
        return StpUtil.getTokenValue();
    }

    /**
     * 登录
     *
     * @param loginId
     */
    public static void login(Object loginId) {
        StpUtil.login(loginId);
    }

    /**
     * 判断是否登录
     * @return boolean true 登录 false 未登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * 登录并携带登录参数
     * @param loginId
     * @param saLoginModel
     */
    public static void login(Object loginId, SaLoginModel saLoginModel) {
        StpUtil.login(loginId, saLoginModel);
    }

    /**
     * 退出登录
     *
     * @param loginId
     */
    public static void logout(Object loginId) {
        StpUtil.logout(loginId);
    }

    /**
     * 获取当前登录的loginId
     *
     * @return
     */
    public static String getLoginId() {
        return StpUtil.getLoginId().toString();
    }
    /**
     * 获取当前登录的loginId
     *
     * @return
     */
    public static Long getLoginIdAsLong() {
        return StpUtil.getLoginIdAsLong();
    }

    /**
     * 根据Token获取登录的ID，未登录返回null
     * @param token
     * @return
     */
    public static Long getLoginIdByToken(String token){
        return Long.parseLong(StpUtil.getLoginIdByToken(token).toString());
    }


    /**
     * 注销当前登录
     */
    public static void logout() {
        StpUtil.logoutByTokenValue(getToken());
    }
    public static void logout(List<Long> userIds) {
        userIds.forEach(StpUtil::logout);
    }

    /**
     * 设置登录参数
     * @param key
     * @param value
     * @return
     */
    public static SaLoginModel setLoginParams(String key, Object value) {
    	return SaLoginConfig.setExtra(key, value);
    }

    /**
     * 获取登录参数
     * @param key
     * @return
     */
    public static Object getLoginParams(String key) {
    	return StpUtil.getExtra(key);
    }

    /**
     * 获取指定Token 的登录参数
     * @param token
     * @param key
     * @return
     */
    public static Object getLoginParamsByToken(String token,String key) {
    	return StpUtil.getExtra(token,key);
    }

    /**
     * 获取登录的用户名
     * @return
     */
    public static String getLoginUserName() {
    	return getLoginParams("username").toString();
    }

}
