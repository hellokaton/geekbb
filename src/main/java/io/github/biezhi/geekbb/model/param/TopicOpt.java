package io.github.biezhi.geekbb.model.param;

import lombok.Data;

/**
 * 设置帖子状态
 *
 * @author biezhi
 * @date 2018/4/5
 */
@Data
public class TopicOpt {

    /**
     * 帖子id
     */
    private String tid;

    /**
     * 是否锁定
     */
    private Boolean lock;

    /**
     * 是否解锁
     */
    private Boolean unlock;

    /**
     * 是否删除
     */
    private Boolean delete;

}
