package io.github.biezhi.geekbb.service;

import io.github.biezhi.geekbb.constants.GeekDevConst;
import io.github.biezhi.geekbb.constants.TipMaps;
import io.github.biezhi.geekbb.enums.ErrorCode;
import io.github.biezhi.geekbb.enums.LogAction;
import io.github.biezhi.geekbb.exception.TipException;
import io.github.biezhi.geekbb.model.db.Comment;
import io.github.biezhi.geekbb.model.db.Topic;
import io.github.biezhi.geekbb.model.vo.CommentVO;
import io.github.biezhi.geekbb.model.vo.ProfileVO;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.kit.BeanKit;
import com.blade.kit.StringKit;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.enums.OrderBy;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.biezhi.geekbb.constants.GeekDevConst.TOPIC_CONTENT_MAX_LEN;
import static io.github.biezhi.geekbb.constants.GeekDevConst.TOPIC_CONTENT_MIN_LEN;
import static io.github.biezhi.anima.Anima.select;

/**
 * 评论服务
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Bean
@Slf4j
public class CommentService {

    @Inject
    private LogService logService;

    @Inject
    private UserService userService;

    @Inject
    private SettingsService settingsService;

    /**
     * 根据帖子ID查询评论列表
     *
     * @param tid
     * @return
     */
    public List<CommentVO> findComments(String tid) {
        return select().from(Comment.class).where(Comment::getTid, tid)
                .order(Comment::getCreated, OrderBy.ASC)
                .map(this::parseVO).collect(Collectors.toList());
    }

    /**
     * 查询某个用户最近发布的评论
     *
     * @param username
     * @return
     */
    public List<CommentVO> recentComment(String username) {
        List<CommentVO> commentVOS = Anima.select().bySQL(CommentVO.class, "select a.cid, a.tid, b.title as topicTitle, a.content, a.created from comments a left join topics b on a.tid = b.tid where a.author = ? order by a.created desc limit 10", username).all();
        if (null != commentVOS) {
            return commentVOS.stream().map(commentVO -> {
                String content = commentVO.getContent();
                content = GeekDevUtils.cleanContent(content);
                if (content.trim().length() == 0) {
                    content = "<kbd style='color:#212529;'>这是一段不可描述的回复内容</kbd>";
                }
                commentVO.setContent(content);
                return commentVO;
            }).collect(Collectors.toList());
        }
        return commentVOS;
    }

    private CommentVO parseVO(Comment comment) {
        CommentVO commentVO = BeanKit.copy(comment, CommentVO.class);
        ProfileVO profileVO = userService.findProfile(comment.getAuthor());
        commentVO.setUser(profileVO);

        String content = comment.getContent();
        content = GeekDevUtils.cleanContent(content);

        if (content.trim().length() == 0) {
            content = "<kbd style='color:#212529;'>这是一段不可描述的回复内容</kbd>";
        }
        commentVO.setContent(content);
        return commentVO;
    }

    /**
     * 发表评论
     *
     * @param comment
     * @throws TipException
     */
    public void sendComment(Comment comment) throws TipException {
        if (null == comment) {
            throw TipMaps.build(ErrorCode.PARAMETER_IS_MISS);
        }
        if (StringKit.isBlank(comment.getTid())) {
            throw TipMaps.build(ErrorCode.PARAMETER_IS_MISS);
        }
        if (StringKit.isBlank(comment.getContent())) {
            throw TipMaps.build(ErrorCode.PARAMETER_IS_MISS);
        }
        if (comment.getContent().length() < TOPIC_CONTENT_MIN_LEN) {
            throw TipMaps.build(ErrorCode.CONTENT_MIN_LENGTH);
        }
        if (comment.getContent().length() > TOPIC_CONTENT_MAX_LEN) {
            throw TipMaps.build(ErrorCode.CONTENT_MAX_LENGTH);
        }

        Comment lastComment = select(Comment::getCreated).from(Comment.class).order(Comment::getCreated, OrderBy.DESC).one();
        if (null != lastComment && Duration.between(lastComment.getCreated(), LocalDateTime.now()).toMillis() < 30_000) {
            throw TipMaps.build(ErrorCode.TOO_FAST);
        }

        Topic topic = select().from(Topic.class).where(Topic::getTid, comment.getTid())
                .and(Topic::getUsername, comment.getOwner()).one();

        if (null == topic) {
            throw TipMaps.build(ErrorCode.ILLEGAL_REQUEST);
        }


        String content = GeekDevUtils.unicodeToAliases(comment.getContent());
        comment.setContent(content);

        TipException tipException = Anima.atomic(() -> {
            comment.setCreated(LocalDateTime.now());
            comment.save();

            int    comments = topic.getComments() + 1;
            double weight;

            // 自己评论不加权
            if (comment.getAuthor().equals(topic.getUsername())) {
                weight = topic.getWeight();
            } else {
                weight = GeekDevUtils.calcWeight(topic.getLoves(), topic.getComments() + 1, 0, System.currentTimeMillis() / 1000);
            }

            new Topic().set(Topic::getComments, comments).set(Topic::getWeight, weight)
                    .updateById(comment.getTid());
            // 通知被@的用户

            // 站点settings + 1
            settingsService.settingPlus(GeekDevConst.SETTING_COUNT_COMMENTS);

            logService.save(LogAction.COMMENT_TOPIC, comment.getTid());

        }).catchAndReturn(e -> {
            log.error("评论失败", e);
            return new TipException(e.getMessage());
        });
        if (null != tipException) {
            throw tipException;
        }
    }

}
