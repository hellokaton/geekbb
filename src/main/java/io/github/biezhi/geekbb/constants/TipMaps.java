package io.github.biezhi.geekbb.constants;

import io.github.biezhi.geekbb.enums.ErrorCode;
import io.github.biezhi.geekbb.exception.TipException;

import java.util.*;

/**
 * @author biezhi
 * @date 2018/4/6
 */
public class TipMaps {

    private static final Map<ErrorCode, List<String>> tips   = new HashMap<>();
    public static        Random                       random = new Random();

    static {
        // init
        tips.put(ErrorCode.PARAMETER_IS_MISS, Arrays.asList("这是一个值得研究的请求", "貌似缺少了什么参数", "冷静分析，不要慌"));
        tips.put(ErrorCode.NO_PERMITION, Arrays.asList("您没有权限操作", "这不是你该触碰的领域", "你走进了不法之地"));
        tips.put(ErrorCode.CONTENT_MAX_LENGTH, Arrays.asList("你的话也忒多了吧", "内容最多为 50000 个字符"));
        tips.put(ErrorCode.TOO_FAST, Arrays.asList("操作频率过快，请稍后再试", "猥琐发育，不要浪 🙅", "宁死不做三秒男", "你的速度惊人的赶超麒麟臂"));
    }

    public static TipException build(ErrorCode errorCode) {
        return new TipException(getMsg(errorCode));
    }

    public static String getMsg(ErrorCode errorCode) {
        List<String> list = tips.get(errorCode);
        if (null == list || list.isEmpty()) {
            return errorCode.getMsg();
        }
        return list.get(random.nextInt(list.size()));
    }

}
