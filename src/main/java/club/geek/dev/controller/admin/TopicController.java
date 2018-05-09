package club.geek.dev.controller.admin;

import club.geek.dev.constants.TipMaps;
import club.geek.dev.model.db.Topic;
import club.geek.dev.model.param.TopicOpt;
import club.geek.dev.service.TopicService;
import club.geek.dev.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.page.Page;

import static club.geek.dev.enums.ErrorCode.NO_PERMITION;

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
