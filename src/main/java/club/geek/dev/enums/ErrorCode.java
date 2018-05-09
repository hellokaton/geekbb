package club.geek.dev.enums;

import lombok.Getter;

/**
 * 错误代码
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Getter
public enum ErrorCode {

    NOT_LOGIN(2001, "请登录后进行操作"),
    PARAMETER_IS_MISS(2002, "参数缺失"),
    CONTENT_MIN_LENGTH(2003, "内容最少为 5 个字符"),
    CONTENT_MAX_LENGTH(2004, "内容最多为 50000 个字符"),
    ILLEGAL_REQUEST(2005, "非法请求，禁止提交"),
    TOO_FAST(2006, "操作频率过快，请稍后再试"),
    TITLE_MIN_LENGTH(2007, "标题最短为 5 个字符"),
    NO_PERMITION(2009, "您没有权限操作"),
    TITLE_MAX_LENGTH(2008, "标题最长为 50 个字符"),
    XSS_TIP(2009, "XSS 有害健康，请勿服用 :p"),;
    private int    code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
