package club.geek.dev.config;

import com.blade.exception.ValidatorException;
import com.blade.ioc.annotation.Bean;
import com.blade.mvc.WebContext;
import com.blade.mvc.handler.DefaultExceptionHandler;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理器
 *
 * @author biezhi
 * @date 2018/4/6
 */
@Bean
@Slf4j
public class ExceptionHandler extends DefaultExceptionHandler {

    @Override
    public void handle(Exception e) {
        if (e instanceof NumberFormatException) {
            log.warn(e.getMessage());
            Request request = WebContext.request();
            WebContext.response().redirect(request.uri());
        } else if (e instanceof ValidatorException) {
            log.warn(e.getMessage());
            WebContext.response().json(RestResponse.fail(e.getMessage()));
        } else {
            super.handle(e);
        }
    }
}
