package cn.anlucky.luckyadmin.vo;


import cn.anlucky.luckyadmin.system.enums.Code;
import lombok.Data;

/**
 * R类用于封装API请求的响应结果
 * 它提供了一系列静态方法来创建响应对象，包括成功和错误情况
 */
@Data
public class R<T> {
    // HTTP状态码
    private String code;
    // 响应消息
    private String message;
    // 响应数据
    private T data;

    // 私有构造函数
    private R(String code, String message, T data) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建一个成功的响应对象
     *
     * @param data 响应携带的数据
     * @return 成功的响应对象，状态码为"200"，消息为"success"
     */
    public static <T> R<T> ok(T data) {
        return new R<>(Code.SUCCESS_CODE, "success", data);
    }

    /**
     * 创建一个带有自定义消息的成功的响应对象
     *
     * @param message 响应消息
     * @param data    响应携带的数据
     * @return 成功的响应对象，状态码为"200"
     */
    public static <T> R<T> ok(String message, T data) {
        return new R<>(Code.SUCCESS_CODE, message, data);
    }

    /**
     * 创建一个带有自定义消息的成功的响应对象，不携带数据
     *
     * @param message 响应消息
     * @return 成功的响应对象，状态码为"200"，不携带数据
     */
    public static R<Void> ok(String message) {
        return new R<>(Code.SUCCESS_CODE, message, null);
    }

    /**
     * 创建一个错误的响应对象，使用默认状态码"-1"
     *
     * @param message 错误消息
     * @return 错误的响应对象，状态码为"-1"
     */
    public static R<Void> error(String message) {
        return new R<>(Code.ERROR_CODE, message, null);
    }

    /**
     * 创建一个带有自定义状态码和消息的错误响应对象
     *
     * @param code    状态码
     * @param message 错误消息
     * @return 错误的响应对象
     */
    public static <T> R<T> error(String code, String message) {
        return new R<>(code, message, null);
    }

    /**
     * 创建一个带有自定义状态码、消息和数据的错误响应对象
     *
     * @param code    状态码
     * @param message 错误消息
     * @param data    错误响应携带的数据
     * @return 错误的响应对象
     */
    public static <T> R<T> error(String code, String message, T data) {
        return new R<>(code, message, data);
    }

    /**
     * 创建一个带有自定义消息和数据的错误响应对象，使用默认状态码"-1"
     *
     * @param message 错误消息
     * @param data    错误响应携带的数据
     * @return 错误的响应对象，状态码为"-1"
     */
    public static <T> R<T> error(String message, T data) {
        return new R<>(Code.ERROR_CODE, message, data);
    }

}


