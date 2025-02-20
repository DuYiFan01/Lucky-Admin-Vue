package cn.anlucky.luckyadmin.utils.satoken;

import cn.dev33.satoken.secure.BCrypt;

/**
 * 密码校验工具类
 */
public class PasswordEncode {

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String encode(String password) {
        return BCrypt.hashpw(password,BCrypt.gensalt(10));
    }

    /**
     * 比较加密后的密码和明文密码是否一致
     * @param password
     * @param encodedPassword
     * @return
     */
    public static boolean matches(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(encode("123456"));// $2a$10$woi6WPgat914KB71bUys4ekeWYZyrNrOIOEHb3.2i9FxRnaRd5ioi
        //1111111 $2a$10$KKlNmPCsbIYxOqRvq8ShEedNDMKMqtUcjJ68E32jU55QgFahpW9.C
    }

}
