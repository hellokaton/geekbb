package io.github.biezhi.geekbb.service;

import io.github.biezhi.geekbb.config.TplFunction;
import io.github.biezhi.geekbb.constants.GeekDevConst;
import io.github.biezhi.geekbb.constants.TipMaps;
import io.github.biezhi.geekbb.enums.ErrorCode;
import io.github.biezhi.geekbb.enums.FavoriteType;
import io.github.biezhi.geekbb.enums.LogAction;
import io.github.biezhi.geekbb.enums.TopicType;
import io.github.biezhi.geekbb.exception.TipException;
import io.github.biezhi.geekbb.model.db.Topic;
import io.github.biezhi.geekbb.model.db.TopicTag;
import io.github.biezhi.geekbb.model.param.QueryTopic;
import io.github.biezhi.geekbb.model.vo.ProfileVO;
import io.github.biezhi.geekbb.model.vo.TopicVO;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import io.github.biezhi.geekbb.validator.GeekDevValidator;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.kit.BeanKit;
import com.blade.kit.StringKit;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.anima.page.Page;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.biezhi.geekbb.constants.GeekDevConst.HOME_TOPIC_INTRO_WORDS;
import static io.github.biezhi.anima.Anima.select;

/**
 * 帖子服务
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Bean
@Slf4j
public class TopicService {

    @Inject
    private LogService      logService;
    @Inject
    private UserService     userService;
    @Inject
    private CommentService  commentService;
    @Inject
    private SettingsService settingsService;
    @Inject
    private RelationService relationService;

    /**
     * 查看帖子列表
     *
     * @param queryTopic
     * @return
     */
    public Page<TopicVO> findTopics(QueryTopic queryTopic) {
        Page<Topic> topicPage = null;
        if (queryTopic.getPage() < 1) {
            queryTopic.setPage(1);
        }

        AnimaQuery<Topic> query = select().from(Topic.class).where(Topic::getState, 1).and(Topic::getTopicType, queryTopic.getTopicType().name());

        if (StringKit.isNotBlank(queryTopic.getNid())) {
            query.and(Topic::getNodeId, queryTopic.getNid());
        }

        switch (queryTopic.getTopicOrder()) {
            case DEFAULT:
                topicPage = query.order(Topic::getWeight, OrderBy.DESC).page(queryTopic.getPage(), queryTopic.getLimit());
                break;
            case RECENT:
                topicPage = query.order(Topic::getCreated, OrderBy.DESC).page(queryTopic.getPage(), queryTopic.getLimit());
                break;
            case POPULAR:
                topicPage = query.and(Topic::getPopular, true).order(Topic::getCreated, OrderBy.DESC).page(queryTopic.getPage(), queryTopic.getLimit());
                break;
            default:
                break;
        }

        return topicPage.map(this::parseToTopicVO);
    }

    /**
     * 查询某个用户最近发布的帖子
     *
     * @param username
     * @return
     */
    public List<TopicVO> recentTopic(String username) {
        return select().from(Topic.class).where(Topic::getState, 1)
                .and(Topic::getUsername, username)
                .order(Topic::getCreated, OrderBy.DESC).limit(10).stream()
                .map(topic -> {
                    TopicVO topicVO = new TopicVO();
                    topicVO.setTid(topic.getTid());
                    String title = topic.getTitle();
                    title = GeekDevUtils.cleanTitle(title);

                    topicVO.setTitle(title);
                    topicVO.setNodeId(topic.getNodeId());
                    topicVO.setNodeTitle(topic.getNodeTitle());
                    topicVO.setCreated(topic.getCreated());
                    String content = topic.getContent();

                    content = TplFunction.htmlToText(TplFunction.mdToHtml(content));
                    if (content.length() > HOME_TOPIC_INTRO_WORDS) {
                        topicVO.setIntro(content.substring(0, HOME_TOPIC_INTRO_WORDS));
                    } else {
                        topicVO.setIntro(content);
                    }
                    return topicVO;
                }).collect(Collectors.toList());
    }

    /**
     * 查找热门帖子
     *
     * @param topicType
     * @return
     */
    public List<Topic> findHotTopics(TopicType topicType) {
        List<Topic> topicVOS = select(Topic::getTid, Topic::getTitle).from(Topic.class)
                .where(Topic::getTopicType, topicType.name())
                .and(Topic::getState, 1)
                .order(Topic::getCreated, OrderBy.DESC)
                .order(Topic::getComments, OrderBy.DESC)
                .limit(5);

        return topicVOS;
    }

    private TopicVO parseToTopicVO(Topic topic) {

        TopicVO topicVO = BeanKit.copy(topic, TopicVO.class);

        String title = topic.getTitle();
        title = GeekDevUtils.cleanTitle(title);

        topicVO.setTitle(title);
        topicVO.setNodeId(topic.getNodeId());
        topicVO.setNodeTitle(topic.getNodeTitle());
        String content = topic.getContent();

        content = TplFunction.htmlToText(TplFunction.mdToHtml(content));
        if (content.length() > HOME_TOPIC_INTRO_WORDS) {
            topicVO.setIntro(content.substring(0, HOME_TOPIC_INTRO_WORDS));
        } else {
            topicVO.setIntro(content);
        }

        ProfileVO profileVO = userService.findProfile(topic.getUsername());
        topicVO.setUser(profileVO);

        return topicVO;
    }

    /**
     * 单个帖子详情
     *
     * @param tid
     * @return
     */
    public TopicVO findTopic(Long uid, String tid) {
        Topic topic = select().from(Topic.class).byId(tid);
        if (null != topic && topic.getState().equals(1)) {
            TopicVO topicVO = parseToTopicVO(topic);

            String content = topic.getContent();
            content = GeekDevUtils.cleanContent(content);

            topicVO.setContent(content);
            topicVO.setCommentList(commentService.findComments(tid));

            if (null != uid) {
                topicVO.setIsLove(this.isFavorite(tid, uid, FavoriteType.LOVE));
                topicVO.setIsCollect(this.isFavorite(tid, uid, FavoriteType.COLLECT));
            }

            return topicVO;
        }
        return null;
    }

    private boolean isFavorite(String tid, Long uid, FavoriteType favoriteType) {
        return relationService.isFavorite(tid, uid, favoriteType);
    }

    /**
     * 创建帖子
     *
     * @param topic
     * @throws TipException
     */
    public String createTopic(Topic topic) throws TipException {
//        if (null == topic) {
//            throw TipMaps.build(ErrorCode.PARAMETER_IS_MISS);
//        }
//        if (StringKit.isBlank(topic.getTitle())) {
//            throw TipMaps.build(ErrorCode.PARAMETER_IS_MISS);
//        }
//        if (topic.getTitle().length() < 5) {
//            throw TipMaps.build(ErrorCode.TITLE_MIN_LENGTH);
//        }
//        if (topic.getTitle().length() > 50) {
//            throw TipMaps.build(ErrorCode.TITLE_MAX_LENGTH);
//        }
//        if (StringKit.isBlank(topic.getContent())) {
//            throw TipMaps.build(ErrorCode.PARAMETER_IS_MISS);
//        }
//        if (topic.getContent().length() < 5) {
//            throw TipMaps.build(ErrorCode.CONTENT_MIN_LENGTH);
//        }
//        if (topic.getContent().length() > 50_000) {
//            throw TipMaps.build(ErrorCode.CONTENT_MAX_LENGTH);
//        }
//
//        if (GeekDevUtils.cleanHtml(topic.getTitle()).trim().length() == 0) {
//            throw TipMaps.build(ErrorCode.XSS_TIP);
//        }
//
//        if (GeekDevUtils.cleanHtml(topic.getContent()).trim().length() == 0) {
//            throw TipMaps.build(ErrorCode.XSS_TIP);
//        }

        GeekDevValidator.saveTopic(topic);

        Topic lastPublish = select(Topic::getCreated).from(Topic.class).where(Topic::getUsername, topic.getUsername()).order(Topic::getCreated, OrderBy.DESC).one();
        // 5分钟以内重复发帖
        if (null != lastPublish && Duration.between(lastPublish.getCreated(), LocalDateTime.now()).toMinutes() < 5) {
            throw TipMaps.build(ErrorCode.TOO_FAST);
        }

        String tid = GeekDevUtils.genTid();

        String title = GeekDevUtils.unicodeToAliases(topic.getTitle());
        topic.setTitle(title);

        String content = GeekDevUtils.unicodeToAliases(topic.getContent());
        topic.setContent(content);

        Anima.atomic(() -> {
            topic.setTid(tid);
            topic.setPopular(false);
            topic.setLoves(0);
            topic.setCollects(0);
            topic.setComments(0);
            double weight = GeekDevUtils.calcWeight(0, 0, 0, System.currentTimeMillis() / 1000);
            topic.setWeight(weight);
            topic.setState(1);
            topic.setCreated(LocalDateTime.now());
            topic.setUpdated(LocalDateTime.now());
            topic.save();

            // node + 1
            Anima.execute("update nodes set topics = topics + 1 where nid = ?", topic.getNodeId());
            if (TopicType.BLOG.name().equals(topic.getTopicType())) {
                // 保存文章标签
                if (StringKit.isNotBlank(topic.getTags())) {
                    this.saveTags(tid, topic.getTags().split(","));
                }
            } else {
                // 通知被@的用户
            }

            // 站点settings + 1
            if (TopicType.TOPIC.name().equals(topic.getTopicType())) {
                settingsService.settingPlus(GeekDevConst.SETTING_COUNT_TOPICS);
            } else {
                settingsService.settingPlus(GeekDevConst.SETTING_COUNT_BLOGS);
            }

        });

        logService.save(LogAction.SAVE_TOPIC, tid);
        return tid;
    }

    public void editTopic(Topic topic) throws TipException {
        GeekDevValidator.editTopic(topic);

        Topic lastUpdate = select(Topic::getUpdated).from(Topic.class).where(Topic::getUsername, topic.getUsername()).one();
        // 十分钟以内重复发帖
        if (null != lastUpdate && Duration.between(lastUpdate.getUpdated(), LocalDateTime.now()).toMinutes() < 10) {
            throw TipMaps.build(ErrorCode.NOT_LOGIN);
        }

        Anima.atomic(() -> {
            String tid = GeekDevUtils.genTid();
            topic.setTid(tid);
            topic.setPopular(false);
            topic.setLoves(0);
            topic.setComments(0);
            double weight = GeekDevUtils.calcWeight(0, 0, 0, System.currentTimeMillis() / 1000);
            topic.setWeight(weight);
            topic.setCreated(LocalDateTime.now());
            topic.setUpdated(LocalDateTime.now());
            topic.save();

            // 保存文章标签
            if (TopicType.BLOG.name().equals(topic.getTopicType()) && StringKit.isNotBlank(topic.getTags())) {
                this.saveTags(tid, topic.getTags().split(","));
            }
            // 通知被@的用户

        });
    }

    private void saveTags(String tid, String[] tags) {
        Anima.delete().from(TopicTag.class).where(TopicTag::getTid).eq(tid).execute();
        for (String tag : tags) {
            TopicTag topicTag = new TopicTag();
            topicTag.setTid(tid);
            topicTag.setTagName(tag);
            topicTag.setCreated(LocalDateTime.now());
            topicTag.save();
        }
    }

    /**
     * 点赞、收藏 和取消点赞、收藏
     *
     * @param uid
     * @param tid
     * @param favoriteType
     */
    public Integer favoriteTopic(Long uid, String tid, FavoriteType favoriteType) {
        if (isFavorite(tid, uid, favoriteType)) {
            // 取消
            if (favoriteType.equals(FavoriteType.LOVE)) {
                relationService.unLoveTopic(uid, tid);
                Anima.execute("update topics set loves = loves - 1 where tid = ?", tid);
                logService.save(LogAction.UNLOVE_TOPIC, tid);
            } else {
                relationService.unCollectTopic(uid, tid);
                Anima.execute("update topics set collects = collects - 1 where tid = ?", tid);
                logService.save(LogAction.UNCOLLECT_TOPIC, tid);
            }
            return 0;
        } else {
            if (favoriteType.equals(FavoriteType.LOVE)) {
                relationService.loveTopic(uid, tid);
                Anima.execute("update topics set loves = loves + 1 where tid = ?", tid);
                logService.save(LogAction.LOVE_TOPIC, tid);
            } else {
                relationService.collectTopic(uid, tid);
                Anima.execute("update topics set collects = collects + 1 where tid = ?", tid);
                logService.save(LogAction.COLLECT_TOPIC, tid);
            }
            return 1;
        }
    }

    /**
     * 根据搜索词查询帖子列表
     *
     * @param q
     * @return
     */
    public List<TopicVO> queryTopics(final String q) {
        return select().from(Topic.class).where(Topic::getTitle)
                .like("%" + q + "%")
                .and(Topic::getState, 1)
                .order(Topic::getCreated, OrderBy.DESC)
                .map(this::parseToTopicVO)
                .peek(topicVO -> {
                    String title = topicVO.getTitle();
                    topicVO.setTitle(title.replaceFirst(q, "<font color=red>" + q + "</font>"));
                })
                .collect(Collectors.toList());
    }

    /**
     * 后台查询帖子列表
     *
     * @param topic
     * @param page
     * @return
     */
    public Page<Topic> findAdminTopics(Topic topic, Integer page) {
        AnimaQuery<Topic> query = select().from(Topic.class);
        if (StringKit.isNotBlank(topic.getTitle())) {
            query.where(Topic::getTitle).like("%" + topic.getTitle() + "%");
        }
        if (StringKit.isNotBlank(topic.getTopicType())) {
            query.and(Topic::getTopicType, topic.getTopicType());
        }
        if (StringKit.isNotBlank(topic.getUsername())) {
            query.and(Topic::getUsername, topic.getUsername());
        }
        return query.order(Topic::getCreated, OrderBy.DESC).page(page, 10);
    }

}