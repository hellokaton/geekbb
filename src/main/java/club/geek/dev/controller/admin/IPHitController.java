package club.geek.dev.controller.admin;

import club.geek.dev.constants.GeekDev;
import club.geek.dev.model.db.IpHit;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.enums.OrderBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.biezhi.anima.Anima.select;

/**
 * @author biezhi
 * @date 2018/4/3
 */
@Path("admin/iphits")
public class IPHitController {

    @GetRoute("/")
    public String nodes(Request request) {
        List<IpHit> ipHits = select().from(IpHit.class).order(IpHit::getCreated, OrderBy.DESC).all();
        request.attribute("iphits", ipHits);
        request.attribute("active", "iphits");
        return "admin/iphits.html";
    }

    @PostRoute("save")
    @JSON
    public RestResponse save(@Param String ip) {
        IpHit ipHit = new IpHit();
        ipHit.setIp(ip);
        ipHit.setCreated(LocalDateTime.now());
        ipHit.setExpired(LocalDateTime.now().plusMonths(1));
        ipHit.save();

        List<IpHit> ipHits = select().from(IpHit.class).where(IpHit::getExpired).gt(LocalDateTime.now()).all();
        if (null != ipHits) {
            GeekDev.me().resetIpHits(ipHits.stream().map(IpHit::getIp).collect(Collectors.toList()));
        }

        return RestResponse.ok();
    }

}
