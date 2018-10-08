package io.github.biezhi.geekbb.controller;

import io.github.biezhi.geekbb.constants.GeekDevConst;
import io.github.biezhi.geekbb.model.db.Node;
import io.github.biezhi.geekbb.model.db.Topic;
import io.github.biezhi.geekbb.service.TopicService;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.geekbb.enums.ErrorCode;
import io.github.biezhi.geekbb.enums.TopicType;
import io.github.biezhi.geekbb.exception.TipException;

import java.util.List;

import static io.github.biezhi.anima.Anima.select;

/**
 * 博客控制器
 *
 * @author biezhi
 * @date 2018/4/5
 */
@Path("blog")
public class BlogController {

    @Inject
    private TopicService topicService;

    @GetRoute("new")
    public String create(Request request) {
        if (!GeekDevUtils.isLogin()) {
            return GeekDevConst.PAGE_NO_PERMISSION;
        }
        List<Node> nodes = select().from(Node.class).where(Node::getPid).notEmpty().order(Node::getTopics, OrderBy.DESC).all();
        request.attribute("nodes", nodes);
        return GeekDevConst.PAGE_NEW_TOPIC;
    }

    @PostRoute("save")
    @JSON
    public RestResponse saveBlog(Topic topic) {
        if (!GeekDevUtils.isLogin()) {
            return RestResponse.fail(ErrorCode.NOT_LOGIN.getMsg());
        }
        try {
            topic.setTopicType(TopicType.BLOG.name());
            topic.setUsername(GeekDevUtils.getLoginUserName());
            String tid = topicService.createTopic(topic);
            return RestResponse.ok(tid);
        } catch (TipException e) {
            return RestResponse.fail(e.getMessage());
        }
    }

}
