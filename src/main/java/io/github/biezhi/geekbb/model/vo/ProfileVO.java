package io.github.biezhi.geekbb.model.vo;

import io.github.biezhi.geekbb.model.db.Profile;
import lombok.Data;

import java.io.Serializable;

/**
 * 个人信息VO
 *
 * @author biezhi
 * @date 2018/4/4
 */
@Data
public class ProfileVO extends Profile implements Serializable {

    /**
     * 昵称
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

}
