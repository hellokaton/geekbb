package io.github.biezhi.geekbb.model.db;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子、博客评论
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Data
@Table(pk = "cid")
public class Comment extends Model {

    /**
     * 评论id
     */
    private Long cid;

    /**
     * 帖子id
     */
    private String tid;

    /**
     * 评论人 username
     */
    private String author;

    /**
     * 帖子作者 username
     */
    private String owner;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 发布时间
     */
    private LocalDateTime created;

}
