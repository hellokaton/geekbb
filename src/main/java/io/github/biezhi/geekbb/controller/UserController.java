package io.github.biezhi.geekbb.controller;

import io.github.biezhi.geekbb.constants.GeekDevConst;
import io.github.biezhi.geekbb.enums.LogAction;
import io.github.biezhi.geekbb.model.db.Profile;
import io.github.biezhi.geekbb.model.vo.CommentVO;
import io.github.biezhi.geekbb.model.vo.ProfileVO;
import io.github.biezhi.geekbb.model.vo.TopicVO;
import io.github.biezhi.geekbb.service.CommentService;
import io.github.biezhi.geekbb.service.LogService;
import io.github.biezhi.geekbb.service.TopicService;
import io.github.biezhi.geekbb.service.UserService;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 用户控制器
 *
 * @author biezhi
 * @date 2018/4/4
 */
@Path
@Slf4j
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private LogService logService;

    @Inject
    private CommentService commentService;

    @Inject
    private TopicService topicService;

    /**
     * 查看个人主页
     *
     * @param username
     * @param request
     * @return
     */
    @GetRoute("/u/:username")
    public String profile(@PathParam String username, Request request) {
        ProfileVO profile = userService.findProfile(username);
        if (null == profile) {
            return GeekDevConst.PAGE_404;
        }
        request.attribute("profile", profile);

        List<CommentVO> commentVOS = commentService.recentComment(username);
        request.attribute("comments", commentVOS);

        List<TopicVO> topicVOS = topicService.recentTopic(username);
        request.attribute("topics", topicVOS);

        return GeekDevConst.PAGE_USER_PROFILE;
    }

    /**
     * 修改个人信息页面
     *
     * @param request
     * @return
     */
    @GetRoute("settings")
    public String settings(Request request) {
        if (!GeekDevUtils.isLogin()) {
            return GeekDevConst.PAGE_NO_PERMISSION;
        }
        ProfileVO profile = userService.findProfile(GeekDevUtils.getLoginUserName());
        request.attribute("profile", profile);
        return GeekDevConst.PAGE_SETTING;
    }

    /**
     * 修改个人信息
     *
     * @param profile
     * @return
     */
    @PostRoute("settings")
    @JSON
    public RestResponse<?> saveSetting(Profile profile) {
        if (!GeekDevUtils.isLogin()) {
            return RestResponse.fail().code(403);
        }
        try {
            profile.setBio(GeekDevUtils.unicodeToAliases(profile.getBio()));
            profile.updateById(GeekDevUtils.getLoginUID());
            logService.save(LogAction.SETTING_PROFILE, null);
            return RestResponse.ok();
        } catch (Exception e) {
            log.error("保存设置失败", e);
            return RestResponse.fail();
        }
    }

}
