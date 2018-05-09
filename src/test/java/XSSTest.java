import club.geek.dev.config.TplFunction;
import club.geek.dev.utils.GeekDevUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * @author biezhi
 * @date 2018/4/7
 */
public class XSSTest {

    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

    public static void main(String[] args) {
        String markdown = "```js\n" +
                "alert(\"Hello World\");\n" +
                "```\n\n" +
                "<script>alert(222);</script>";

        System.out.println("原始 MARKDOWN: ");
        System.out.println(markdown);

        String html = TplFunction.mdToHtml(markdown);

        System.out.println("转换为 HTML: ");
        System.out.println(html);

        String safe = GeekDevUtils.cleanHtml(html);

        System.out.println("JSOUP 输出: ");
        System.out.println(safe);

        

        String h = "<SCRIPT SRC=//ha.ckers.org/.j><SCRIPT>alert(/XSS/.source)</SCRIPT>";
        String cleanHtml = Jsoup.clean(h, Whitelist.relaxed());
        System.out.println(cleanHtml);

        System.out.println(GeekDevUtils.decodeTid("zoneimc5fd8ngmey2v"));
    }

}
