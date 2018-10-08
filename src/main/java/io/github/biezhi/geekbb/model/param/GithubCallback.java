package io.github.biezhi.geekbb.model.param;

import lombok.Data;

/**
 * Github 回调参数
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Data
public class GithubCallback {

    /**
     * 唯一id
     */
    private Long id;

    /**
     * 登录名
     */
    private String login;

    /**
     * 头像URL
     */
    private String avatar_url;

    /**
     * 博客地址
     */
    private String blog;

    /**
     * 昵称
     */
    private String name;

    /**
     * 坐标
     */
    private String location;

    /**
     * 个性签名
     */
    private String bio;

    /**
     * 邮箱
     */
    private String email;

}
