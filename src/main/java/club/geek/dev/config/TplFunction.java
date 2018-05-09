package club.geek.dev.config;

import club.geek.dev.constants.GeekDev;
import club.geek.dev.enums.Position;
import club.geek.dev.model.db.Promotion;
import club.geek.dev.model.vo.ProfileVO;
import club.geek.dev.utils.GeekDevUtils;
import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.mvc.WebContext;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模板函数
 *
 * @author biezhi
 * @date 2018/4/5
 */
public class TplFunction {

    private static final List<Extension> extensions = Arrays.asList(TablesExtension.create());
    private static final Parser          parser     = Parser.builder().extensions(extensions).build();

    public static String socialInfo(ProfileVO profileVO) {
        if (null == profileVO) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if (StringKit.isNotBlank(profileVO.getGithub())) {
            builder.append("<a title='Github' target='_blank' class='social-info' href='https://github.com/")
                    .append(profileVO.getGithub())
                    .append("' class='ml-2'>")
                    .append("<i class='czs-github-logo'></i></a>");
        }
        if (StringKit.isNotBlank(profileVO.getTwitter())) {
            builder.append("<a title='Twitter' target='_blank' class='social-info' href='https://twitter.com/")
                    .append(profileVO.getTwitter())
                    .append("' class='ml-2'>")
                    .append("<i class='czs-twitter'></i></a>");
        }
        if (StringKit.isNotBlank(profileVO.getWeibo())) {
            builder.append("<a title='微博' target='_blank' class='social-info' href='https://weibo.com/u/")
                    .append(profileVO.getWeibo())
                    .append("' class='ml-2'>")
                    .append("<i class='czs-weibo'></i></a>");
        }
        if (StringKit.isNotBlank(profileVO.getZhihu())) {
            builder.append("<a title='知乎' target='_blank' class='social-info' href='https://www.zhihu.com/people/")
                    .append(profileVO.getZhihu())
                    .append("' class='ml-2'>")
                    .append("<i class='czs-zhihu'></i></a>");
        }
        return builder.toString();
    }

    public static String siteConfig(String key) {
        return GeekDev.me().getSetting(key);
    }

    public static String siteConfig(String key, String defaultValue) {
        String value = siteConfig(key);
        if (StringKit.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    public static String siteKeywords(String keywords) {
        if (StringKit.isBlank(keywords)) {
            return siteConfig("site.keywords");
        }
        return keywords;
    }

    public static String siteDescription(String description) {
        if (StringKit.isBlank(description)) {
            return siteConfig("site.description");
        }
        return description;
    }

    public static String siteTitle(String title) {
        if (StringKit.isBlank(title)) {
            if (StringKit.isNotBlank(siteConfig("site.subtitle"))) {
                return siteConfig("site.title") + " - " + siteConfig("site.subtitle");
            }
            return siteConfig("site.title");
        } else {
            return title + " - " + siteConfig("site.title");
        }
    }

    public static String prettyTime(LocalDateTime time) {
        return DateKit.prettyTime(time, Locale.CHINA);
    }

    public static String extract(String text, int size) {
        return extract(text, size, "...");
    }

    public static String extract(String text, int size, String append) {
        if (StringKit.isBlank(text)) {
            return "";
        }
        if (text.length() < size) {
            return text;
        }
        return text.substring(0, size) + append;
    }

    public static String intro(String content, int count) {
        if (null == content || content.length() < count) {
            return content;
        }
        return content.substring(0, count);
    }

    public static String siteUrl(String subUrl) {
        if (StringKit.isBlank(subUrl)) {
            return siteConfig("site.url");
        }
        if (!subUrl.startsWith("/")) {
            subUrl = "/" + subUrl;
        }
        if ("/".equals(subUrl)) {
            return siteConfig("site.url");
        }
        String url = siteConfig("site.url") + subUrl;
        if (url.contains("//")) {
            url = url.replaceAll("//", "/");
        }
        return url;
    }

    public static String currentUrl() {
        return WebContext.request().uri();
    }

    public static String emoji(String text) {
        return GeekDevUtils.emojiToUnicode(text);
    }

    public static String datetime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    }

    public static String mdToHtml(String markdown) {

        markdown = GeekDevUtils.emojiToUnicode(markdown);

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .attributeProviderFactory(context -> new LinkAttributeProvider())
                .extensions(extensions).build();

        String content = renderer.render(document);
        if (content.contains("@")) {
            content = content.replaceAll("@([\\w\\x{4e00}-\\x{9fa5}]{2,10})\\s{1}", "<a href='/u/$1'>@$1</a>&nbsp;");
        }

        // 支持gist代码输出
        if (content.contains("https://gist.github.com/")) {
            content = content.replaceAll("https://gist.github.com/([0-9A-Za-z_]+)/([0-9A-Za-z_]+)", "<script src=\"https://gist.github.com/$1/$2.js\"></script>");
        }
        return content;
    }

    /**
     * 提取html中的文字
     *
     * @param html
     * @return
     */
    public static String htmlToText(String html) {
        if (StringKit.isNotBlank(html)) {
            return Jsoup.clean(html, Whitelist.none());
        }
        return "";
    }

    public static String version() {
        return siteConfig("site.version");
    }

    public static boolean gtNow(LocalDateTime time) {
        return LocalDateTime.now().compareTo(time) < 0;
    }

    public static boolean ltNow(LocalDateTime time) {
        return LocalDateTime.now().compareTo(time) > 0;
    }

    /**
     * 右上角公告
     *
     * @return
     */
    public static Promotion affiche() {
        return GeekDev.me().getPromotions().stream()
                .filter(promotion -> promotion.getPosition().equals(Position.RIGHT_TOP.name()))
                .findFirst().get();
    }

    public static List<Promotion> promotions() {
        return GeekDev.me().getPromotions().stream()
                .filter(promotion -> promotion.getPosition().equals(Position.RIGHT_BOTTOM.name()))
                .collect(Collectors.toList());
    }

    static class LinkAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof FencedCodeBlock) {
                String info = ((FencedCodeBlock) node).getInfo();
                if (info != null && !info.isEmpty()) {
                    int    space = info.indexOf(" ");
                    String language;
                    if (space == -1) {
                        language = info;
                    } else {
                        language = info.substring(0, space);
                    }
                    attributes.put("class", "hljs " + language);
                }
            }
        }
    }

}
