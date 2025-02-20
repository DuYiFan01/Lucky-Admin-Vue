package cn.anlucky.luckyadmin.exception;

import cn.anlucky.luckyadmin.system.enums.Code;
import cn.anlucky.luckyadmin.vo.R;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<Void> handleCustomException(CustomException ex) {
        return R.error(ex.getMessage());
    }

    /**
     * SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public R<Void> handleDuplicateKeyException(SQLException ex) {
        ex.printStackTrace();
        return R.error(ex.getMessage());
    }
    /**
     * 登录异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLoginException(NotLoginException ex) {
        ex.printStackTrace();
        return R.error(Code.NO_LOGIN_CODE, Code.NO_LOGIN_MESSAGE);
    }

    /**
     * 权限异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NotPermissionException.class)
    public R<Void> handleAuthorizationException(NotPermissionException ex) {
        log.error("用户没有权限:{}", ex.getPermission());
        ex.printStackTrace();
        return R.error(Code.NO_PERM_CODE, Code.NO_PERM_MESSAGE);
    }

    /**
     * 角色异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NotRoleException.class)
    public R<Void> handlerException(NotRoleException ex) {
        log.error("用户没有角色：{}", ex.getRole());
        ex.printStackTrace();
        return R.error(Code.NO_ROLE_CODE, Code.NO_ROLE_MESSAGE);
    }

    /**
     * 其他异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception ex) {
        ex.printStackTrace();
        return R.error(ex.getMessage());
    }
}
