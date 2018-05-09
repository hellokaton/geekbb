package club.geek.dev.hook;

import club.geek.dev.constants.GeekDev;
import club.geek.dev.constants.GeekDevConst;
import club.geek.dev.model.db.User;
import club.geek.dev.utils.GeekDevUtils;
import com.blade.ioc.annotation.Bean;
import com.blade.kit.StringKit;
import com.blade.mvc.hook.Signature;
import com.blade.mvc.hook.WebHook;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import lombok.extern.slf4j.Slf4j;

import static io.github.biezhi.anima.Anima.select;

/**
 * BaseWebhook
 *
 * @author biezhi
 * @date 2018/4/5
 */
@Bean
@Slf4j
public class BaseHook implements WebHook {

    @Override
    public boolean before(Signature signature) {
        Request  request  = signature.request();
        Response response = signature.response();
        String   uri      = request.uri();

        log.info("IP: {}, UA: {}", request.address(), request.userAgent());

        if (GeekDev.me().hasIp(request.address())) {
            response.text("YOUR IP HAS BEAN BLOCKED!");
            return false;
        }

        if (!GeekDevUtils.isLogin()) {
            String cookie = request.cookie(GeekDevConst.LOGIN_COOKIE_KEY);
            if (StringKit.isNotBlank(cookie)) {
                try {
                    Long uid  = GeekDevUtils.decodeUID(cookie);
                    User user = select().from(User.class).byId(uid);
                    GeekDevUtils.addLogin(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (uri.startsWith("/admin")) {
            if (GeekDevUtils.isAdmin() || GeekDevUtils.isMaster()) {
            } else {
                log.warn("IP: {} 访问: {}", request.address(), uri);
                response.text("你走错片场了.");
                return false;
            }
        }
        return true;
    }

}
