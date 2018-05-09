package club.geek.dev.model.vo;

import club.geek.dev.model.db.Comment;
import lombok.Data;
import lombok.ToString;

/**
 * 评论VO
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Data
@ToString(callSuper = true)
public class CommentVO extends Comment {

    /**
     * 评论人
     */
    private ProfileVO user;

    /**
     * 评论的帖子标题
     */
    private String topicTitle;

}
