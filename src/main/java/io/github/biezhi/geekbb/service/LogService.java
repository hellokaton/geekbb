package io.github.biezhi.geekbb.service;

import io.github.biezhi.geekbb.enums.LogAction;
import io.github.biezhi.geekbb.model.db.Log;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Bean;
import com.blade.kit.StringKit;
import com.blade.mvc.WebContext;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.anima.page.Page;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static io.github.biezhi.anima.Anima.select;

/**
 * 日志服务
 *
 * @author biezhi
 * @date 2018/4/6
 */
@Slf4j
@Bean
public class LogService {

    public Page<Log> findLogs(Log log, int page) {

        AnimaQuery<Log> query = select().from(Log.class);
        if (StringKit.isNotBlank(log.getUsername())) {
            query.and(Log::getUsername, log.getUsername());
        }
        if (StringKit.isNotBlank(log.getIp())) {
            query.and(Log::getIp, log.getIp());
        }
        if (StringKit.isNotBlank(log.getAction())) {
            query.and(Log::getAction, log.getAction());
        }
        return query.order(Log::getCreated, OrderBy.DESC).page(page, 10);
    }

    public void save(LogAction logAction, String param) {
        try {
            Log log = new Log();
            if (StringKit.isNotBlank(param)) {
                log.setAction(String.format(logAction.getDesc(), param));
            } else {
                log.setAction(logAction.getDesc());
            }
            log.setUsername(GeekDevUtils.getLoginUserName());
            log.setIp(WebContext.request().address());
            log.setUserAgent(WebContext.request().userAgent());
            log.setCreated(LocalDateTime.now());
            log.save();
        } catch (Exception e) {
            log.error("记录日志失败", e);
        }
    }

}
