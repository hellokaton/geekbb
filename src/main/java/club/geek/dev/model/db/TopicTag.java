package club.geek.dev.model.db;

import io.github.biezhi.anima.Model;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子标签关联
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Data
public class TopicTag extends Model {

    /**
     * 主键
     */
    private Long id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 标签id
     */
    private String tid;

    /**
     * 标签创建时间
     */
    private LocalDateTime created;

}
