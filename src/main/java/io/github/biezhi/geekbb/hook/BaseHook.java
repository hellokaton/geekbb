package io.github.biezhi.geekbb.hook;

import io.github.biezhi.geekbb.constants.GeekDev;
import io.github.biezhi.geekbb.constants.GeekDevConst;
import io.github.biezhi.geekbb.model.db.User;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Bean;
import com.blade.kit.StringKit;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
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
    public boolean before(RouteContext context) {
        String uri = context.uri();

        log.info("IP: {}, UA: {}", context.address(), context.userAgent());

        if (GeekDev.me().hasIp(context.address())) {
            context.text("YOUR IP HAS BEAN BLOCKED!");
            return false;
        }

        if (!GeekDevUtils.isLogin()) {
            String cookie = context.cookie(GeekDevConst.LOGIN_COOKIE_KEY);
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
                log.warn("IP: {} 访问: {}", context.address(), uri);
                context.text("你走错片场了.");
                return false;
            }
        }
        return true;
    }

}
