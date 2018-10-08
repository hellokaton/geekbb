package io.github.biezhi.geekbb.controller;

import io.github.biezhi.geekbb.constants.GeekDevConst;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Response;
import io.github.biezhi.geekbb.enums.LogAction;
import io.github.biezhi.geekbb.service.LogService;

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
