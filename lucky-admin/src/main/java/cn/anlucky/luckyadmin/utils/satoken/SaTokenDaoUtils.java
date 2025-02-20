package cn.anlucky.luckyadmin.utils.satoken;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDao;

import java.util.List;

/**
 * SaTokenDao 是数据持久层接口，负责所有会话数据的底层写入和读取。
 */
public class SaTokenDaoUtils {

    /**
     * 缓存前缀
     */
    private static final String PREFIX = "Lucky";
    /**
     * 角色缓存
     */
    public static final String ROLES_CACHE ="roles:";
    /**
     * 权限缓存
     */
    public static final String PERMISSIONS_CACHE = "permissions:";
    /**
     * 路由缓存
     */
    public static final String ROUTER_CACHE = "router:";
    /**
     * 验证码缓存
     */
    public static final String CAPTCHA_CACHE = "captcha:";
    /**
     * 代码生成包信息缓存
     */
    public static final String GENERATION_PACKAGE_CACHE = "generation:";


    /**
     * 一天缓存时间
     */
    public static final Long DAY_TIMEOUT = 60 * 60 * 24L;

    /**
     * 获取 SaTokenDao 对象
     *
     * @return
     */
    public static SaTokenDao getSaTokenDao() {
        return SaManager.getSaTokenDao();
    }

    /**
     * 获取字符串缓存
     *
     * @param key
     * @return
     */
    public static String getKey(String key) {
        return getSaTokenDao().get(PREFIX + ":" + key);
    }

    /**
     * 设置字符串缓存 自定义时间
     * @param key
     * @param value
     * @param timeout SaTokenDaoUtils.DAY_TIMEOUT * n 一天
     */
    public static void setKey(String key, String value, Long timeout) {
        getSaTokenDao().set(PREFIX + ":" + key, value, timeout);
    }
    /**
     * 设置字符串缓存 永不过期
     * @param key
     * @param value
     */
    public static void setKey(String key, String value) {
        getSaTokenDao().set(PREFIX + ":" + key, value, SaTokenDao.NEVER_EXPIRE);
    }
    /**
     * 删除字符串缓存
     * @param key
     */
    public static void deleteKey(String key) {
        getSaTokenDao().delete(PREFIX + ":" + key);
    }

    /**
     * 更新字符串缓存 过期时间不变
     * @param key
     * @param value
     */
    public static void updateKey(String key, String value){
        getSaTokenDao().update(PREFIX + ":" + key, value);
    }

    /**
     * 获取字符串缓存剩余存活时间 （单位：秒）
     * @param key
     * @return
     */
    public static Long getTimeout(String key){
        return getSaTokenDao().getTimeout(PREFIX + ":" + key);
    }

    /**
     * 更新字符串缓存剩余存活时间
     * @param key
     * @param timeout
     */
    public static void updateTimeout(String key, Long timeout){
        getSaTokenDao().updateTimeout(PREFIX + ":" + key, timeout);
    }

    /**
     * 添加字符串缓存剩余存活时间 原有时间 + 新增时间
     * @param key
     * @param timeout
     */
    public static void addTimeout(String key, Long timeout){
        getSaTokenDao().updateTimeout(PREFIX + ":" + key, getTimeout(key) + timeout);
    }

    /**
     * 获取对象缓存
     *
     * @param key
     * @return
     */
    public static Object getObjectKey(String key) {
        return getSaTokenDao().getObject(PREFIX + ":" + key);
    }

    /**
     * 设置字符串缓存 自定义时间
     * @param key
     * @param value
     * @param timeout SaTokenDaoUtils.DAY_TIMEOUT * n 一天
     */
    public static void setObjectKey(String key, Object value, Long timeout) {
        getSaTokenDao().setObject(PREFIX + ":" + key, value, timeout);
    }
    /**
     * 设置字符串缓存 永不过期
     * @param key
     * @param value
     */
    public static void setObjectKey(String key, Object value) {
        getSaTokenDao().setObject(PREFIX + ":" + key, value, SaTokenDao.NEVER_EXPIRE);
    }
    /**
     * 删除对象缓存
     * @param key
     */
    public static void deleteObjectKey(String key) {
        getSaTokenDao().deleteObject(PREFIX + ":" + key);
    }

    /**
     * 更新字符串缓存 过期时间不变
     * @param key
     * @param value
     */
    public static void updateObjectKey(String key, Object value){
        getSaTokenDao().updateObject(PREFIX + ":" + key, value);
    }

    /**
     * 获取字符串缓存剩余存活时间 （单位：秒）
     * @param key
     * @return
     */
    public static Long getObjectTimeout(String key){
        return getSaTokenDao().getObjectTimeout(PREFIX + ":" + key);
    }

    /**
     * 更新字符串缓存剩余存活时间
     * @param key
     * @param timeout
     */
    public static void updateObjectTimeout(String key, Long timeout){
        getSaTokenDao().updateObjectTimeout(PREFIX + ":" + key, timeout);
    }

    /**
     * 添加字符串缓存剩余存活时间 原有时间 + 新增时间
     * @param key
     * @param timeout
     */
    public static void addObjectTimeout(String key, Long timeout){
        getSaTokenDao().updateObjectTimeout(PREFIX + ":" + key, getTimeout(key) + timeout);
    }

    /**
     * 搜索数据
     * @param key
     * @param start
     * @param size
     * @return
     */
    public static List<String> searchData(String key,Long start,Long size){
        return getSaTokenDao().searchData(PREFIX, key, start.intValue(), size.intValue(), false);
    }










}
