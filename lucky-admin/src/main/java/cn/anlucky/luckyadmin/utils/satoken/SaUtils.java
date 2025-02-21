package cn.anlucky.luckyadmin.utils.satoken;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * SaToken 个人封装的工具类
 */
public class SaUtils {

    public static final String TOKEN_PREFIX = "token:login:token:";
    public static final String USER_LOGIN_DETAIL = "USER_LOGIN_DETAIL";


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
    public static void logoutByTokenValue(String tokenValue) {
        StpUtil.logoutByTokenValue(tokenValue);
    }

    /**
     * 批量下线指定ID
     * @param userIds
     */
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
    /**
     * 获取登录的用户名
     * @return
     */
    public static String getLoginUserNameByToken(String token) {
        return getLoginParamsByToken(token,"username").toString();
    }

    /**
     * 校验是否具有指定的权限码
     * @param permissionCode
     * @return true 有 false 没有
     */
    public static boolean hasPermission(String permissionCode){
        return StpUtil.hasPermission(permissionCode);
    }

    /**
     * 登录后可用
     * 获取Token的Session
     * @return SaSession
     */
    public static SaSession getTokenSession(){
        return StpUtil.getTokenSession();
    }

    /**
     * 登录后可用
     * 获取指定Token的Session
     * @return
     */
    public static SaSession getTokenSessionByToken(String tokenValue){
        return StpUtil.getTokenSessionByToken(tokenValue);
    }

    /**
     * 登录后可用
     * 获取所有Token
     * @param sort （true=正序：先登录的在前，false=反序：后登录的在前）。
     */
    public static List<String> searchAllTokenValue(boolean sort){
        return searchTokenValue(0,-1,sort);
    }

    /**
     * 获取所有Token
     * @param start 开始位置
     * @param size 数量
     * @param sort （true=正序：先登录的在前，false=反序：后登录的在前）。
     * @return
     */
    public static List<String> searchTokenValue(int start, int size, boolean sort){
        List<String> list = StpUtil.searchTokenValue("", start, size, sort);
        List<String> tokenList = new ArrayList<>();
        // 将list 中token 值提取出来 删除前缀 token:login:token:
        list.forEach(token -> {
            tokenList.add(token.replace(TOKEN_PREFIX, ""));
        });
        return tokenList;
    }



}
