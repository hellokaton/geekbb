package club.geek.dev.controller;

import club.geek.dev.constants.GeekDevConst;
import club.geek.dev.constants.GeekDev;
import club.geek.dev.constants.TipMaps;
import club.geek.dev.enums.ErrorCode;
import club.geek.dev.enums.FavoriteType;
import club.geek.dev.enums.TopicType;
import club.geek.dev.exception.TipException;
import club.geek.dev.model.db.Comment;
import club.geek.dev.model.db.Node;
import club.geek.dev.model.db.Topic;
import club.geek.dev.model.vo.TopicVO;
import club.geek.dev.service.CommentService;
import club.geek.dev.service.TopicService;
import club.geek.dev.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.enums.OrderBy;

import java.util.List;

import static io.github.biezhi.anima.Anima.select;

/**
 * 主题、博客控制器
 *
 * @author biezhi
 * @date 2018/4/5
 */
@Path
public class TopicController {

    @Inject
    private TopicService topicService;

    @Inject
    private CommentService commentService;

    /**
     * 查看主题详情
     *
     * @param tid
     * @param request
     * @return
     */
    @GetRoute("/t/:tid")
    public String topicDetail(@PathParam String tid, Request request) {
        Long uid = null;
        if (GeekDevUtils.isLogin()) {
            uid = GeekDevUtils.getLoginUID();
        }
        TopicVO topicVO = topicService.findTopic(uid, tid);
        if (null == topicVO) {
            return GeekDevConst.PAGE_404;
        }
        request.attribute("topic", topicVO);
        return GeekDevConst.PAGE_TOPIC_DETAIL;
    }

    /**
     * 点赞和取消点赞
     *
     * @param tid
     * @return
     */
    @PostRoute("/topic/love/:tid")
    @JSON
    public RestResponse<?> love(@PathParam String tid) {
        if (!GeekDevUtils.isLogin()) {
            return RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), TipMaps.getMsg(ErrorCode.NOT_LOGIN));
        }
        Integer data = topicService.favoriteTopic(GeekDevUtils.getLoginUID(), tid, FavoriteType.LOVE);
        return RestResponse.ok(data);
    }

    /**
     * 收藏和取消收藏
     *
     * @param tid
     * @return
     */
    @PostRoute("/topic/collect/:tid")
    @JSON
    public RestResponse<?> collect(@PathParam String tid) {
        if (!GeekDevUtils.isLogin()) {
            return RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), TipMaps.getMsg(ErrorCode.NOT_LOGIN));
        }
        Integer data = topicService.favoriteTopic(GeekDevUtils.getLoginUID(), tid, FavoriteType.COLLECT);
        return RestResponse.ok(data);
    }

    /**
     * 评论主题
     *
     * @param comment
     * @return
     */
    @PostRoute("/topic/comment")
    @JSON
    public RestResponse<?> comment(Comment comment) {
        if (!GeekDevUtils.isLogin()) {
            return RestResponse.fail(ErrorCode.NOT_LOGIN.getMsg());
        }
        String allow = GeekDev.me().getSetting(GeekDevConst.SETTING_SITE_ALLOW_COMMENT);
        if (!"1".equals(allow)) {
            return RestResponse.fail("站点目前禁止回复");
        }
        comment.setAuthor(GeekDevUtils.getLoginUserName());
        try {
            commentService.sendComment(comment);
        } catch (TipException e) {
            return RestResponse.fail(e.getMessage());
        }
        return RestResponse.ok();
    }

    /**
     * 创建主题
     *
     * @param request
     * @param response
     * @return
     */
    @GetRoute("/topic/new")
    public String create(Request request, Response response) {
        if (!GeekDevUtils.isLogin()) {
            response.redirect("/auth/signin");
            return null;
        }
        List<Node> nodes = select().from(Node.class).where(Node::getPid).notEmpty().order(Node::getTopics, OrderBy.DESC).all();
        request.attribute("nodes", nodes);
        return GeekDevConst.PAGE_NEW_TOPIC;
    }

    /**
     * 发布主题
     *
     * @param topic
     * @return
     */
    @PostRoute("/topic/save")
    @JSON
    public RestResponse saveTopic(Topic topic) {
        if (!GeekDevUtils.isLogin()) {
            return RestResponse.fail(ErrorCode.NOT_LOGIN.getMsg());
        }
        String allow = GeekDev.me().getSetting(GeekDevConst.SETTING_SITE_ALLOW_TOPIC);
        if (!"1".equals(allow)) {
            return RestResponse.fail("站点目前禁止发布");
        }

        try {
            topic.setUsername(GeekDevUtils.getLoginUserName());
            String tid = topicService.createTopic(topic);
            return RestResponse.ok(tid);
        } catch (TipException e) {
            return RestResponse.fail(e.getMessage());
        }
    }

}
