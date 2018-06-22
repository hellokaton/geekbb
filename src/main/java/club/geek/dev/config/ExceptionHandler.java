package club.geek.dev.config;

import com.blade.ioc.annotation.Bean;
import com.blade.mvc.handler.DefaultExceptionHandler;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
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
    public void handleException(Exception e, Request request, Response response) {
        if (e instanceof NumberFormatException) {
            log.warn(e.getMessage());
            response.redirect(request.uri());
        } else {
            super.handle(e);
        }
    }
}
