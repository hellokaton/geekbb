package club.geek.dev.constants;

/**
 * Geek Dev Const
 *
 * @author biezhi
 * @date 2018/4/3
 */
public interface GeekDevConst {

    String LOGIN_SESSION_KEY   = "GEEK_DEV_USER";
    String LOGIN_COOKIE_KEY    = "_GEEK_DEV_U";
    String REDIRECT_COOKIE_KEY = "_GEEK_DEV_REDIRECT";

    String GITHUB_RESOURCE_URL = "https://api.github.com/user";

    String RELATE_DB_NAME        = "db/geek-dev.db";
    String RELATE_FOLLOWER         = "user:follower";
    String RELATE_FOLLOWING      = "user:following";
    String RELATE_TOPIC_LOVES    = "topic:loves";
    String RELATE_TOPIC_COLLECTS = "topic:collects";

    int HOME_TOPIC_LIMIT       = 8;
    int HOME_TOPIC_INTRO_WORDS = 65;

    int TOPIC_CONTENT_MIN_LEN = 5;
    int TOPIC_CONTENT_MAX_LEN = 50000;

    /**
     * 网站公告key
     */
    String SETTING_SITE_NOTICE_KEY = "site.notice";

    /**
     * 网站地址
     */
    String SETTING_SITE_URL = "site.url";

    /**
     * 网站标题
     */
    String SETTING_SITE_TITLE = "site.title";

    /**
     * 网站二级标题
     */
    String SETTING_SITE_SUBTITLE = "site.subtitle";

    /**
     * 网站关键词
     */
    String SETTING_SITE_KEYWORDS = "site.keywords";

    /**
     * 网站描述
     */
    String SETTING_SITE_DESCRIPTION = "site.description";

    /**
     * 站点版本号
     */
    String SETTING_SITE_VERSION = "site.version";

    /**
     * 是否允许注册
     */
    String SETTING_SITE_ALLOW_REGSITER = "site.allowRegister";

    /**
     * 是否允许发帖
     */
    String SETTING_SITE_ALLOW_TOPIC = "site.allowTopic";

    /**
     * 是否允许回复
     */
    String SETTING_SITE_ALLOW_COMMENT = "site.allowComment";

    /**
     * 网站运行多长时间
     */
    String SETTING_SITE_RUN_TIME = "site.runtime";

    /**
     * 首页展示多少个主题，分页条件
     */
    String SETTING_HOME_TOPIC_LIMIT = "home.topics";

    /**
     * 首页展示多少个节点
     */
    String SETTING_HOME_NODE_LIMIT = "home.nodes";

    /**
     * 站点总主题数
     */
    String SETTING_COUNT_TOPICS = "count.topics";

    /**
     * 站点总博客数
     */
    String SETTING_COUNT_BLOGS = "count.blogs";

    /**
     * 站点总用户数
     */
    String SETTING_COUNT_USERS = "count.users";

    /**
     * 站点总回复数
     */
    String SETTING_COUNT_COMMENTS = "count.comments";

    String PAGE_INDEX         = "index.html";
    String PAGE_RECENT        = "recent.html";
    String PAGE_SEARCH        = "search.html";
    String PAGE_ABOUT         = "about.html";
    String PAGE_FAQ           = "faq.html";
    String PAGE_TIPS          = "page_tips.html";
    String PAGE_NEW_TOPIC     = "topic_new.html";
    String PAGE_EDIT_TOPIC    = "topic_edit.html";
    String PAGE_TOPIC_DETAIL  = "topic_detail.html";
    String PAGE_SETTING       = "settings.html";
    String PAGE_NO_PERMISSION = "no_permission.html";
    String PAGE_404           = "page404.html";
    String PAGE_USER_PROFILE  = "profile.html";

}
