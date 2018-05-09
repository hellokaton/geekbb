package club.geek.dev.controller;

import club.geek.dev.constants.GeekDevConst;
import club.geek.dev.enums.TopicOrder;
import club.geek.dev.enums.TopicType;
import club.geek.dev.model.db.Node;
import club.geek.dev.model.db.Topic;
import club.geek.dev.model.param.QueryTopic;
import club.geek.dev.model.vo.TopicVO;
import club.geek.dev.service.TopicService;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Request;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.anima.page.Page;

import java.util.List;

import static io.github.biezhi.anima.Anima.select;

/**
 * 首页控制器
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Path
public class IndexController {

    @Inject
    private TopicService topicService;

    /**
     * 首页
     *
     * @param request
     * @return
     */
    @GetRoute("/")
    public String index(Request request) {

        List<Node> nodes = select().from(Node.class).where(Node::getPid).notEmpty().order(Node::getTopics, OrderBy.DESC).limit(10);
        request.attribute("nodes", nodes);

        String nid = request.query("node", "");

        Page<TopicVO> topics = topicService.findTopics(QueryTopic.builder().nid(nid).build());
        request.attribute("topics", topics);

        List<Topic> hotTopics = topicService.findHotTopics(TopicType.TOPIC);
        request.attribute("hotTopics", hotTopics);

        List<Topic> hotBlogs = topicService.findHotTopics(TopicType.BLOG);
        request.attribute("hotBlogs", hotBlogs);

        request.attribute("nid", nid);

        return GeekDevConst.PAGE_INDEX;
    }

    /**
     * 搜索页
     *
     * @param request
     * @param q
     * @return
     */
    @GetRoute("/search")
    public String search(Request request, @Param String q) {
        if (null == q || q.length() < 2) {
            return index(request);
        }
        List<TopicVO> topics = topicService.queryTopics(q);
        request.attribute("topics", topics);
        request.attribute("q", q);
        return GeekDevConst.PAGE_SEARCH;
    }

    /**
     * 最新主题页
     *
     * @param request
     * @param p
     * @param node
     * @return
     */
    @GetRoute("/recent")
    public String recent(Request request, @Param(defaultValue = "1") Integer p, @Param String node) {
        if (p < 1) {
            p = 1;
        }

        
        List<Node> nodes = select().from(Node.class).where(Node::getPid).notEmpty().order(Node::getTopics, OrderBy.DESC).all();
        request.attribute("nodes", nodes);

        Page<TopicVO> topics = topicService.findTopics(QueryTopic.builder().nid(node).page(p).limit(10).topicOrder(TopicOrder.RECENT).build());
        request.attribute("topics", topics);
        request.attribute("nid", null == node ? "" : node);
        request.attribute("nodeTitle", nodes.stream().filter(n -> n.getNid().equals(node)).map(Node::getTitle).findFirst().orElse(""));

        return GeekDevConst.PAGE_RECENT;
    }

}
