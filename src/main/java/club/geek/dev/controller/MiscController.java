package club.geek.dev.controller;

import club.geek.dev.constants.GeekDevConst;
import club.geek.dev.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Response;
import club.geek.dev.enums.LogAction;
import club.geek.dev.service.LogService;

/**
 * 其他杂项
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Path
public class MiscController {

    @Inject
    private LogService logService;

    /**
     * 关于页
     *
     * @return
     */
    @GetRoute("/about")
    public String about() {
        return GeekDevConst.PAGE_ABOUT;
    }

    /**
     * 常见问题页
     *
     * @return
     */
    @GetRoute("/faq")
    public String faq() {
        return GeekDevConst.PAGE_FAQ;
    }

    /**
     * 退出
     *
     * @param response
     */
    @GetRoute("logout")
    public void logout(Response response) {
        GeekDevUtils.logout();
        logService.save(LogAction.LOGOUT, null);
        response.redirect("/");
    }

}
