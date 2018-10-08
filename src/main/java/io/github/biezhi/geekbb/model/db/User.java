package io.github.biezhi.geekbb.model.db;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Data
@Table(pk = "uid")
public class User extends Model {

    /**
     * UID
     */
    private Long uid;

    /**
     * 昵称
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色 @see RoleType
     */
    private String role;

    /**
     * 注册时间
     */
    private LocalDateTime created;

    /**
     * 最后登录时间
     */
    private LocalDateTime logined;

    /**
     * 最后更新时间
     */
    private LocalDateTime updated;

    /**
     * 用户状态 @see UserState
     */
    private Integer       state;

}
