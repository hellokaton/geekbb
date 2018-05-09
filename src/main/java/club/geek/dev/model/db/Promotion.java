package club.geek.dev.model.db;

import io.github.biezhi.anima.Model;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 推广信息
 *
 * @author biezhi
 * @date 2018/4/7
 */
@Data
public class Promotion extends Model {

    /**
     * 主键
     */
    private Long id;

    /**
     * 推广排序
     */
    private Integer sort;

    /**
     * 推广标题
     */
    private String title;

    /**
     * 推广ICON
     */
    private String icon;

    /**
     * 推广内容HTML
     */
    private String content;

    /**
     * 推广位置
     */
    private String position;

    /**
     * 推广底部信息
     */
    private String footer;

    /**
     * 推广创建时间
     */
    private LocalDateTime created;

    /**
     * 推广过期时间
     */
    private LocalDateTime expired;

}
