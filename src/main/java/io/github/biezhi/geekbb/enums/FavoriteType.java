package io.github.biezhi.geekbb.enums;

import lombok.Getter;

/**
 * 喜爱类型
 *
 * @author biezhi
 * @date 2018/4/6
 */
public enum FavoriteType {

    LOVE(1), COLLECT(2);

    @Getter
    private int type;

    FavoriteType(int type) {
        this.type = type;
    }
}
