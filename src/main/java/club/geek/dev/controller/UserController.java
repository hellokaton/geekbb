package club.geek.dev.controller;

import club.geek.dev.constants.GeekDevConst;
import club.geek.dev.enums.LogAction;
import club.geek.dev.model.db.Profile;
import club.geek.dev.model.vo.CommentVO;
import club.geek.dev.model.vo.ProfileVO;
import club.geek.dev.model.vo.TopicVO;
import club.geek.dev.service.CommentService;
import club.geek.dev.service.LogService;
import club.geek.dev.service.TopicService;
import club.geek.dev.service.UserService;
import club.geek.dev.utils.GeekDevUtils;
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
