package club.geek.dev.task;

import com.blade.ioc.annotation.Bean;
import com.blade.task.TaskManager;
import com.blade.task.annotation.Schedule;
import lombok.extern.slf4j.Slf4j;

/**
 * GeekDev 定时任务
 *
 * @author biezhi
 * @date 2018/4/10
 */
@Bean
@Slf4j
public class GeekDevTask {

    @Schedule(name = "syncRelate", cron = "0 0 1 * * ?")
    public void syncRelate() {
        log.info("开始同步...");
    }

}
