package io.github.biezhi.geekbb.model.db;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 节点
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Data
@Table(pk = "nid")
public class Node extends Model {

    /**
     * 节点ID，唯一标识
     */
    private String nid;

    /**
     * 父级id
     */
    private String pid;

    /**
     * 节点名称
     */
    private String title;

    /**
     * 节点图片
     */
    private String image;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 节点下帖子数
     */
    private Integer topics;

    /**
     * 节点状态 1: 正常 0: 禁用
     */
    private Integer state;

    /**
     * 节点创建时间
     */
    private LocalDateTime created;

}
