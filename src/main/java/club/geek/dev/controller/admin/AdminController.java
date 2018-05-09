package club.geek.dev.controller.admin;

import club.geek.dev.constants.GeekDev;
import club.geek.dev.enums.ErrorCode;
import club.geek.dev.model.db.Log;
import club.geek.dev.model.db.Setting;
import club.geek.dev.service.LogService;
import club.geek.dev.service.UserService;
import club.geek.dev.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.Route;
import com.blade.mvc.http.HttpMethod;
import com.blade.mvc.http.Request;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.page.Page;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.biezhi.anima.Anima.select;

/**
 * @author biezhi
 * @date 2018/4/3
 */
@Path("admin")
public class AdminController {

    @Inject
    private UserService userService;

    @Inject
    private LogService logService;

    @GetRoute("index")
    public String index() {
        return "admin/index.html";
    }

    @GetRoute("logs")
    public String users(@Param(defaultValue = "1") Integer page, Log log, Request request) {
        Page<Log> logPage = logService.findLogs(log, page);
        request.attribute("logs", logPage);
        request.attribute("log", log);
        request.attribute("active", "logs");
        return "admin/logs.html";
    }

    @Route("site")
    public String site(Request request) {
        if (!GeekDevUtils.isMaster()) {
            request.attribute("infoMsg", ErrorCode.NO_PERMITION.getMsg());
            return "admin/site.html";
        }

        boolean          isPost   = request.httpMethod().equals(HttpMethod.POST);
        Iterator<String> iterator = request.parameters().keySet().iterator();
        while (isPost && iterator.hasNext()) {
            String param = iterator.next();
            String value = request.query(param, "");

            new Setting().set(Setting::getSvalue, value).updateById("site." + param);
        }

        List<Setting> settings = select().from(Setting.class).all();

        if (isPost) {
            GeekDev geekDev = GeekDev.me();
            geekDev.clearSettings();
            for (Setting setting : settings) {
                geekDev.putSetting(setting.getSkey(), setting.getSvalue());
            }
            request.attribute("infoMsg", "保存成功");
        }

        Map<String, String> siteMap = settings.stream()
                .filter(setting -> setting.getSkey().startsWith("site."))
                .peek(setting -> setting.setSkey(setting.getSkey().replaceFirst("site.", "")))
                .collect(Collectors.toMap(Setting::getSkey, Setting::getSvalue));

        request.attribute("site", siteMap);
        request.attribute("active", "site");
        return "admin/site.html";
    }

}
