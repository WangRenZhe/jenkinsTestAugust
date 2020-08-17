package com.example.mybatis.mybatisdemo.check;

/**
 * @author wangrenzhe
 * @date 2020/8/6 17:16
 */
public class ParamException extends RuntimeException {
    private static final long serialVersionUID = 3587817360301590447L;
    private String code;
    private String message;

    public ParamException(Throwable cause) {
        super(cause);
    }

    public ParamException(String message) {
        super(message);
        this.message = message;
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ParamException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ParamException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

}
