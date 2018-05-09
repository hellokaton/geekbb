package club.geek.dev.model.param;

import club.geek.dev.constants.GeekDevConst;
import club.geek.dev.enums.TopicOrder;
import club.geek.dev.enums.TopicType;
import lombok.Builder;
import lombok.Data;

/**
 * 帖子查询参数
 *
 * @author biezhi
 * @date 2018/4/5
 */
@Data
@Builder
public class QueryTopic {

    /**
     * 帖子排序规则
     */
    @Builder.Default
    private TopicOrder topicOrder = TopicOrder.DEFAULT;

    /**
     * 页码
     */
    @Builder.Default
    private int        page       = 1;

    /**
     * 每页条数
     */
    @Builder.Default
    private int        limit      = GeekDevConst.HOME_TOPIC_LIMIT;

    /**
     * 帖子类型
     */
    @Builder.Default
    private TopicType  topicType  = TopicType.TOPIC;

    /**
     * 节点id
     */
    private String nid;

}
