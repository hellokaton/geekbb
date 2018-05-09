package club.geek.dev.model.db;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;

/**
 * 个人详细信息
 *
 * @author biezhi
 * @date 2018/4/4
 */
@Data
@Table(pk = "uid")
public class Profile extends Model {

    /**
     * UID
     */
    private Long uid;

    /**
     * 粉丝数
     */
    private Integer followers;

    /**
     * 用户名
     */
    private String username;

    /**
     * 个性签名
     */
    private String bio;

    /**
     * Github 账号
     */
    private String github;

    /**
     * 个人主页
     */
    private String website;

    /**
     * 推特账号
     */
    private String twitter;

    /**
     * 知乎账号
     */
    private String zhihu;

    /**
     * 微博账号
     */
    private String weibo;

    /**
     * 坐标
     */
    private String location;

}
