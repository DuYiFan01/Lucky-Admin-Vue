package cn.anlucky.luckyadmin.system.constant;


/**
 * 通用常量信息
 *
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";
    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";
    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * Layout组件标识
     */
    public static final String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    public final static String PARENT_VIEW = "ParentView";

    /**
     * iframe
     */
    public static final String IFRAME = "Iframe";

    /**
     * 菜单类型（目录）
     */
    public static final String TYPE_DIR = "M";

    /**
     * 菜单类型（菜单）
     */
    public static final String TYPE_MENU = "C";

    /**
     * 菜单类型（按钮）
     */
    public static final String TYPE_BUTTON = "F";

    /**
     * 菜单类型（内链）
     */
    public static final String TYPE_N_URL = "N";

    /**
     * 菜单类型（外链）
     */
    public static final String TYPE_W_URL = "W";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 文件Hash 算法
     */
    public static final String SHA256 = "SHA-256";
    /**
     * 文件Hash 算法
     */
    public static final String MD5 = "MD5";

    /**
     * 文件存储位置 本地
     */
    public static final String FILE_LOCATION_LOCAL = "LOCAL";

    /**
     * 文件存储位置 七牛
     */
    public static final String FILE_LOCATION_QINIU = "QINIU";

    /**
     * APP菜单父级ID
     */
    public static final Long MENU_APP = -1L;
}
