package club.geek.dev.utils;

import club.geek.dev.config.TplFunction;
import club.geek.dev.constants.GeekDevConst;
import club.geek.dev.enums.RoleType;
import club.geek.dev.model.db.User;
import com.blade.kit.Hashids;
import com.blade.kit.StringKit;
import com.blade.mvc.WebContext;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.vdurmont.emoji.EmojiParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author biezhi
 * @date 2018/4/3
 */
public final class GeekDevUtils {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890";

    private static final Hashids hashids = new Hashids("geek-dev", 12, ALPHABET);

    private static final Whitelist WHITELIST = Whitelist.basic();

    /**
     * 配置过滤化参数, 不对代码进行格式化
     */
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.base).prettyPrint(false);

    static {
        //增加可信标签到白名单
        WHITELIST.addTags("embed", "span", "div", "kbd", "p", "br", "h1", "h2", "h3", "h4", "h5", "blockquote", "img");
        //增加可信属性
        WHITELIST.addAttributes(":all", "style", "class", "id", "name", "target", "src", "alt", "title");
    }

    public static synchronized String genTid() {
        return hashids.encode(2018, 04, 05, System.currentTimeMillis() + StringKit.rand(0, 100));
    }

    public static Long decodeUID(String hash) {
        return hashids.decode(hash)[1];
    }

    public static Long decodeTid(String hash) {
        return hashids.decode(hash)[3];
    }

    public static String genTid(long tid) {
        return hashids.encode(2018, 04, 05, tid);
    }

    public static String encodeId(Long hash) {
        return hashids.encode(2018, hash);
    }

    public static void addLogin(User user) {
        Request request = WebContext.request();
        request.session().attribute(GeekDevConst.LOGIN_SESSION_KEY, user);

        Response response = WebContext.response();
        // 7 天
        int    maxAge = 7 * 24 * 60 * 60;
        String cookie = encodeId(user.getUid());
        response.cookie(GeekDevConst.LOGIN_COOKIE_KEY, cookie, maxAge);
    }

    public static void logout() {
        Request request = WebContext.request();
        request.session().removeAttribute(GeekDevConst.LOGIN_SESSION_KEY);
        Response response = WebContext.response();
        response.removeCookie(GeekDevConst.LOGIN_COOKIE_KEY);
        response.removeCookie(GeekDevConst.REDIRECT_COOKIE_KEY);
    }

    public static boolean isLogin() {
        Request request = WebContext.request();
        return null != request.session().attribute(GeekDevConst.LOGIN_SESSION_KEY);
    }

    public static User getLoginUser() {
        Request request = WebContext.request();
        User    user    = request.session().attribute(GeekDevConst.LOGIN_SESSION_KEY);
        return user;
    }

    public static String getLoginUserName() {
        return getLoginUser().getUsername();
    }

    public static Long getLoginUID() {
        return getLoginUser().getUid();
    }

    public static boolean isAdmin() {
        return null != getLoginUser() && getLoginUser().getRole().equals(RoleType.ADMIN.name());
    }

    public static boolean isMaster() {
        return null != getLoginUser() && getLoginUser().getRole().equals(RoleType.MASTER.name());
    }

    public static String cleanHtml(String html) {
        return Jsoup.clean(html, "", WHITELIST, outputSettings);
    }

    public static String cleanTitle(String title) {
        title = emojiToUnicode(title);
        return cleanHtml(title);
    }

    public static String cleanContent(String content) {
        content = emojiToUnicode(content);
        content = TplFunction.mdToHtml(content);
        return cleanHtml(content);
    }

    /**
     * 计算帖子权重
     * <p>
     * 根据点赞数、收藏数、评论数、下沉数、创建时间计算
     *
     * @param likes    点赞数：权重占比1
     * @param comments 评论数：权重占比2
     * @param gains    增益数：权重占比-1
     * @param created  创建时间，越早权重越低
     * @return
     */
    public static double calcWeight(int likes, int comments, int gains, long created) {
        long score = Math.max(likes - 1, 1) + comments * 2 - gains;
        // 投票方向
        int sign = (score == 0) ? 0 : (score > 0 ? 1 : -1);
        // 帖子争议度
        double order = Math.log10(Math.max(Math.abs(score), 1));
        // 1501748867是项目创建时间
        double seconds = created - 1501748867;
        return Double.parseDouble(String.format("%.2f", order + sign * seconds / 45000));
    }

    public static String unicodeToAliases(String text) {
        if (StringKit.isBlank(text)) {
            return text;
        }
        return EmojiParser.parseToAliases(text);
    }

    public static String emojiToUnicode(String text) {
        if (StringKit.isBlank(text)) {
            return text;
        }
        return EmojiParser.parseToUnicode(text);
    }

    /**
     * 获取@的用户列表
     *
     * @param str
     * @return
     */
    public static Set<String> getAtUsers(String str) {
        Set<String> users = new HashSet<>();
        if (StringKit.isNotBlank(str)) {
            Pattern pattern = Pattern.compile("\\@([a-zA-Z_0-9-]+)\\s");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                users.add(matcher.group(1));
            }
        }
        return users;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(genTid());
        }
    }

}
