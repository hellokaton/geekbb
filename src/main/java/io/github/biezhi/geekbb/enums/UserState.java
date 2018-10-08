package io.github.biezhi.geekbb.enums;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author biezhi
 * @date 2018/4/3
 */
public enum UserState {

    NORMAL(1), DISABLE(2), DELETE(3);

    @Getter
    private int state;

    UserState(int state) {
        this.state = state;
    }

}
