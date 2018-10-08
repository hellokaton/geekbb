package io.github.biezhi.geekbb.model.db;

import io.github.biezhi.anima.Model;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统日志
 *
 * @author biezhi
 * @date 2018/4/6
 */
@Data
public class Log extends Model {

    /**
     * 主键
     */
    private Long id;

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 操作事件
     */
    private String action;

    /**
     * 操作人IP
     */
    private String ip;

    /**
     * 操作人 UA
     */
    private String userAgent;

    /**
     * 操作时间
     */
    private LocalDateTime created;

}
