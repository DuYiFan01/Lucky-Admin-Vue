package cn.anlucky.luckyadmin.exception;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException {

    /**
     * 异常信息
     *
     * @param message
     */
    public CustomException(String message) {
        super(message);
    }
}
