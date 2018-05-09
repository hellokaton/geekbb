package club.geek.dev.controller;

import club.geek.dev.constants.GeekDev;
import club.geek.dev.constants.GeekDevConst;
import club.geek.dev.enums.LogAction;
import club.geek.dev.enums.UserState;
import club.geek.dev.exception.TipException;
import club.geek.dev.model.db.User;
import club.geek.dev.model.param.GithubCallback;
import club.geek.dev.service.LogService;
import club.geek.dev.service.UserService;
import club.geek.dev.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.kit.JsonKit;
import com.blade.kit.StringKit;
import com.blade.mvc.WebContext;
import com.blade.mvc.annotation.CookieParam;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;

import static club.geek.dev.constants.GeekDevConst.PAGE_TIPS;
import static io.github.biezhi.anima.Anima.select;

/**
 * 登录认证
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Path("auth")
@Slf4j
public class AuthController {

    @Inject
    private OAuth20Service oAuth20Service;

    @Inject
    private UserService userService;

    @Inject
    private LogService logService;

    @GetRoute("signin")
    public void signin(Request request, Response response, @Param String redirect) {

        String allow = GeekDev.me().getSetting(GeekDevConst.SETTING_SITE_ALLOW_REGSITER);
        if (!"1".equals(allow)) {
            request.attribute("tipMsg", "站点目前不开放注册。");
            response.render(PAGE_TIPS);
            return;
        }

        String authorizationUrl = oAuth20Service.getAuthorizationUrl();
        log.info("AuthorizationUrl: {}", authorizationUrl);
        if (StringKit.isNotBlank(redirect)) {
            response.cookie(GeekDevConst.REDIRECT_COOKIE_KEY, redirect, 3600);
        }
        response.redirect(authorizationUrl);
    }

    @GetRoute("github")
    public void callback(@Param String code, @CookieParam String _GEEK_DEV_REDIRECT, Response response) throws IOException {

        log.info("Code: {}", code);

        OAuthRequest request = new OAuthRequest(Verb.GET, GeekDevConst.GITHUB_RESOURCE_URL);

        com.github.scribejava.core.model.Response res = execute(code, request);

        String         body           = res.getBody();
        GithubCallback githubCallback = JsonKit.formJson(body, GithubCallback.class);

        long count = select().from(User.class).where(User::getUsername, githubCallback.getLogin()).in("state", 1, 2).count();
        if (count == 0) {
            try {
                userService.register(githubCallback);
            } catch (TipException e) {
                WebContext.request().attribute("tipMsg", "注册失败");
                response.render(PAGE_TIPS);
                return;
            }
        } else {
            new User().set(User::getLogined, LocalDateTime.now()).where(User::getUsername, githubCallback.getLogin()).update();
        }
        User user = select().from(User.class).where(User::getUsername, githubCallback.getLogin()).one();
        if (null == user) {
            log.warn("注册失败");
            response.redirect("/");
            return;
        }
        if (user.getState().equals(UserState.DISABLE.getState())) {
            WebContext.request().attribute("tipMsg", "该账号已被禁用，无法使用.");
            response.render(PAGE_TIPS);
            return;
        }
        // 保存到 Session
        GeekDevUtils.addLogin(user);

        logService.save(LogAction.LOGIN, null);

        if (StringKit.isNotBlank(_GEEK_DEV_REDIRECT)) {
            response.removeCookie(GeekDevConst.REDIRECT_COOKIE_KEY);
            response.redirect(_GEEK_DEV_REDIRECT);
        } else {
            response.redirect("/");
        }
    }

    private com.github.scribejava.core.model.Response execute(String code, OAuthRequest oAuthRequest) {
        try {
            final OAuth2AccessToken accessToken = oAuth20Service.getAccessToken(code);
            oAuth20Service.signRequest(accessToken, oAuthRequest);
            final com.github.scribejava.core.model.Response response = oAuth20Service.execute(oAuthRequest);
            log.info("code: {}", response.getCode());
            log.info("response: {}", response.getBody());
            return response;
        } catch (Exception e) {
            log.error("execute error", e);
        }
        return null;
    }

}
