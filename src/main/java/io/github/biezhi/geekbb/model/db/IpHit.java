package io.github.biezhi.geekbb.model.db;

import io.github.biezhi.anima.Model;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * IP 黑名单
 *
 * @author biezhi
 * @date 2018/4/7
 */
@Data
public class IpHit extends Model {

    /**
     * 主键
     */
    private Long id;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * 过期时间
     */
    private LocalDateTime expired;

}
