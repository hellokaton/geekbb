package club.geek.dev.controller.admin;

import club.geek.dev.constants.GeekDev;
import club.geek.dev.enums.ErrorCode;
import club.geek.dev.model.db.Setting;
import club.geek.dev.service.RelationService;
import club.geek.dev.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.Anima;

import java.util.List;

import static io.github.biezhi.anima.Anima.select;

/**
 * @author biezhi
 * @date 2018/4/3
 */
@Path("admin/sys")
public class SysController {

    @Inject
    private RelationService relationService;

    @GetRoute("/")
    public String sys(Request request) {

        request.attribute("active", "sys");
        return "admin/sys.html";
    }

    @PostRoute("reset")
    @JSON
    public RestResponse reset() {
        if(!GeekDevUtils.isMaster()){
            return RestResponse.fail(ErrorCode.NO_PERMITION.getMsg());
        }
        Anima.execute("update nodes a set a.topics = (select count(*) from topics b where b.state = 1 and b.node_id = a.nid)");
        Anima.execute("update settings set svalue = (select count(*) from topics where state = 1 and topic_type = 'TOPIC') where skey = 'count.topics'");
        Anima.execute("update settings set svalue = (select count(*) from topics where state = 1 and topic_type = 'BLOG') where skey = 'count.blogs'");
        Anima.execute("update settings set svalue = (select count(*) from comments) where skey = 'count.comments'");
        Anima.execute("update settings set svalue = (select count(*) from users where state = 1) where skey = 'count.users'");
        Anima.execute("update topics a set a.loves = (select count(*) from relations b where b.event_id = a.tid and b.relate_type = 'LOVE')");
        Anima.execute("update topics a set a.collects = (select count(*) from relations b where b.event_id = a.tid and b.relate_type = 'COLLECT')");
        // 更新我的粉丝数
//        Anima.execute("update profiles a set a.followers = (select count(*) from relations b where b.event_id = STRAIGHT_JOIN(a.uid) and b.relate_type = 'FOLLOW')");

        List<Setting> settings = select().from(Setting.class).all();
        GeekDev       geekDev  = GeekDev.me();
        geekDev.clearSettings();
        for (Setting setting : settings) {
            geekDev.putSetting(setting.getSkey(), setting.getSvalue());
        }
        return RestResponse.ok();
    }

    @PostRoute("relateSync")
    @JSON
    public RestResponse relateSync(@Param String to){
        relationService.sync(to);
        return RestResponse.ok();
    }
}
