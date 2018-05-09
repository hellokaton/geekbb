package club.geek.dev.enums;

import lombok.Getter;

/**
 * 广告坐标
 *
 * @author biezhi
 * @date 2018/4/7
 */
public enum Position {

    RIGHT_TOP("右上角"), RIGHT_BOTTOM("右下角"), LEFT_BOTTOM("左下角");

    @Getter
    private String desc;

    Position(String desc) {
        this.desc = desc;
    }

}
