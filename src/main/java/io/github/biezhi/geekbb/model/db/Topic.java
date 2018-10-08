package io.github.biezhi.geekbb.model.db;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Ignore;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 主题，帖子、博客
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Data
@Table(pk = "tid")
public class Topic extends Model {

    /**
     * 帖子ID
     */
    private String tid;

    /**
     * 所属节点ID
     */
    private String nodeId;

    /**
     * 所属节点标题
     */
    private String nodeTitle;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 发布人用户名
     */
    private String username;

    /**
     * 主题类型，BLOG、TOPIC
     */
    private String topicType;

    /**
     * 帖子评论数
     */
    private Integer comments;

    /**
     * 帖子点赞数
     */
    private Integer loves;

    /**
     * 帖子被收藏数
     */
    private Integer collects;

    /**
     * 是否精华帖
     */
    private Boolean popular;

    /**
     * 帖子权重
     */
    private Double weight;

    /**
     * 帖子状态 @see TopicType
     */
    private Integer state;

    /**
     * 帖子创建时间
     */
    private LocalDateTime created;

    /**
     * 帖子最后更新时间
     */
    private LocalDateTime updated;

    @Ignore
    private String tags;

}
