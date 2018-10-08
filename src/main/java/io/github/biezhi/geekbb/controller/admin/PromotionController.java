package io.github.biezhi.geekbb.controller.admin;

import io.github.biezhi.geekbb.constants.GeekDev;
import io.github.biezhi.geekbb.enums.ErrorCode;
import io.github.biezhi.geekbb.enums.Position;
import io.github.biezhi.geekbb.model.db.Promotion;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.enums.OrderBy;

import java.time.LocalDateTime;
import java.util.List;

import static io.github.biezhi.anima.Anima.select;

/**
 * 推广管理
 * 过期
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Path("admin/promotion")
public class PromotionController {

    @GetRoute("/")
    public String promotion(Request request) {
        List<Promotion> promotions = select().from(Promotion.class).order(Promotion::getSort, OrderBy.ASC).all();
        request.attribute("promotions", promotions);
        request.attribute("active", "promotion");
        return "admin/promotion.html";
    }

    @GetRoute("/:id")
    @JSON
    public RestResponse promotionJSON(@PathParam Long id) {
        Promotion promotion = select().from(Promotion.class).byId(id);
        promotion.setTitle(GeekDevUtils.emojiToUnicode(promotion.getTitle()));

        String content = GeekDevUtils.emojiToUnicode(promotion.getContent());
        content = espace(content);
        promotion.setContent(content);

        String footer = GeekDevUtils.emojiToUnicode(promotion.getFooter());
        footer = espace(footer);
        promotion.setFooter(footer);

        return RestResponse.ok(promotion);
    }

    @PostRoute("save")
    @JSON
    public RestResponse save(Promotion promotion) {

        if (!GeekDevUtils.isMaster()) {
            return RestResponse.fail(ErrorCode.NO_PERMITION.getMsg());
        }

        promotion.setTitle(GeekDevUtils.unicodeToAliases(promotion.getTitle()));
        promotion.setContent(GeekDevUtils.unicodeToAliases(promotion.getContent()));

        promotion.setSort(0);
        promotion.setCreated(LocalDateTime.now());
        if (null != promotion.getId()) {
            promotion.updateById(promotion.getId());
        } else {
            if (Position.RIGHT_TOP.name().equals(promotion.getPosition())) {
                promotion.setExpired(LocalDateTime.now().plusYears(10));
            } else {
                promotion.setExpired(LocalDateTime.now().plusDays(-1));
            }
            promotion.save();
        }

        this.reShow();
        return RestResponse.ok();
    }

    @PostRoute("sort")
    @JSON
    public RestResponse sort(@Param String ids, @Param String sort) {
        if (!GeekDevUtils.isMaster()) {
            return RestResponse.fail(ErrorCode.NO_PERMITION.getMsg());
        }
        String[] idArr   = ids.split(",");
        String[] sortArr = sort.split(",");
        for (int i = 0; i < idArr.length; i++) {
            new Promotion().set(Promotion::getSort, Integer.parseInt(sortArr[i]))
                    .updateById(Long.parseLong(idArr[i]));
        }
        this.reShow();
        return RestResponse.ok();
    }

    private void reShow() {
        List<Promotion> promotions = select().from(Promotion.class)
                .where(Promotion::getExpired)
                .gt(LocalDateTime.now()).order(Promotion::getSort, OrderBy.ASC)
                .order(Promotion::getId, OrderBy.DESC).all();
        GeekDev.me().resetPromotion(promotions);
    }

    private String espace(String html) {
        if (null == html) {
            return html;
        }
        html = html.replace("\"", "\\\"");
        return html;
    }

}
