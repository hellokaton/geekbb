package club.geek.dev.model.vo;

import club.geek.dev.model.db.Topic;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 帖子VO
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Data
@ToString(callSuper = true)
public class TopicVO extends Topic {

    /**
     * 帖子摘要
     */
    private String intro;

    /**
     * 发布人
     */
    private ProfileVO user;

    /**
     * 是否已经点赞，已登录时判断
     */
    private Boolean isLove;

    /**
     * 是否已经收藏，已登录时判断
     */
    private Boolean isCollect;

    /**
     * 帖子评论列表
     */
    private List<CommentVO> commentList;

}
