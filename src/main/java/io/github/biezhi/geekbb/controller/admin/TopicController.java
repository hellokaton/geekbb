package io.github.biezhi.geekbb.controller.admin;

import io.github.biezhi.geekbb.constants.TipMaps;
import io.github.biezhi.geekbb.model.db.Topic;
import io.github.biezhi.geekbb.model.param.TopicOpt;
import io.github.biezhi.geekbb.service.TopicService;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.page.Page;

import static io.github.biezhi.geekbb.enums.ErrorCode.NO_PERMITION;

/**
 * @author biezhi
 * @date 2018/4/3
 */
@Path("admin/topics")
public class TopicController {

    @Inject
    private TopicService topicService;

    @GetRoute("/")
    public String topics(Request request, Topic topic, @Param(defaultValue = "1") Integer page) {
        Page<Topic> topicPage = topicService.findAdminTopics(topic, page);
        request.attribute("topics", topicPage);
        request.attribute("active", "topics");
        request.attribute("topic", topic);
        return "admin/topics.html";
    }

    @PostRoute("lockOrDelete")
    @JSON
    public RestResponse lockOrDelete(TopicOpt topicOpt) {
        if (null != topicOpt.getLock() && topicOpt.getLock()) {
            new Topic().set(Topic::getState, 2).updateById(topicOpt.getTid());
        }
        if (null != topicOpt.getDelete() && topicOpt.getDelete()) {
            if (!GeekDevUtils.isMaster()) {
                return RestResponse.fail(TipMaps.getMsg(NO_PERMITION));
            }
            new Topic().set(Topic::getState, 0).updateById(topicOpt.getTid());
        }
        if (null != topicOpt.getUnlock() && topicOpt.getUnlock()) {
            new Topic().set(Topic::getState, 1).updateById(topicOpt.getTid());
        }
        return RestResponse.ok();
    }
}
