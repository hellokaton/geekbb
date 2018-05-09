package club.geek.dev.exception;

import club.geek.dev.enums.ErrorCode;

/**
 * 提示信息异常
 *
 * @author biezhi
 * @date 2018/4/4
 */
public class TipException extends Exception {

    private ErrorCode errorCode;
    private String    msg;

    public TipException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public TipException(String message) {
        super(message);
        this.msg = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return null != msg ? msg : errorCode.getMsg();
    }
}
