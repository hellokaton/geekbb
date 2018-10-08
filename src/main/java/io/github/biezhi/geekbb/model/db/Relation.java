package io.github.biezhi.geekbb.model.db;

import io.github.biezhi.anima.Model;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 关联关系
 *
 * @author biezhi
 * @date 2018/4/9
 */
@Data
public class Relation extends Model {

    /**
     * 主键
     */
    private Long id;

    /**
     * UID
     */
    private Long uid;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 关系类型 @see RelateType
     */
    private String relateType;

    /**
     * 关系产生时间
     */
    private LocalDateTime created;

}
