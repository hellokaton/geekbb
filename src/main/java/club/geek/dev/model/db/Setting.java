package club.geek.dev.model.db;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;

/**
 * 系统配置
 *
 * @author biezhi
 * @date 2018/4/4
 */
@Data
@Table(pk = "skey")
public class Setting extends Model {

    /**
     * 键
     */
    private String skey;

    /**
     * 值
     */
    private String svalue;

    /**
     * 1.启用 0.禁用
     */
    private Integer state;

}
