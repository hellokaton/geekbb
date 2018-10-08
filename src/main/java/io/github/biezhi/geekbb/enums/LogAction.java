package io.github.biezhi.geekbb.enums;

import lombok.Getter;

/**
 * 日志操作类型
 *
 * @author biezhi
 * @date 2018/4/6
 */
public enum LogAction {

    DISABLE_USER("禁用用户: %s"),
    ENABLE_USER("启用用户: %s"),
    SET_ADMIN("设置用户为管理员: %s"),
    REMOVE_ADMIN("取消用户为管理员: %s"),
    SAVE_TOPIC("发布帖子: %s"),
    LOCK_TOPIC("锁定帖子: %s"),
    UNLOCK_TOPIC("解锁帖子: %s"),
    DELETE_TOPIC("删除帖子: %s"),
    COMMENT_TOPIC("评论帖子: %s"),
    LOVE_TOPIC("点赞帖子: %s"),
    UNLOVE_TOPIC("取消点赞帖子: %s"),
    COLLECT_TOPIC("收藏帖子: %s"),
    UNCOLLECT_TOPIC("取消收藏帖子: %s"),
    SETTING_PROFILE("设置个人信息"),
    UPLOAD_AVATAR("上传头像"),
    STOP_USER("停用用户: %s"),
    EDIT_NODE("编辑节点: %s"),
    ADD_NODE("新增节点: %s"),
    REGISTER("用户注册"),
    LOGIN("登录站点"),
    LOGOUT("退出站点"),;

    @Getter
    private String desc;

    LogAction(String desc) {
        this.desc = desc;
    }

}
