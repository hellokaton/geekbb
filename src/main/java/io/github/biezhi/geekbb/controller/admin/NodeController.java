package io.github.biezhi.geekbb.controller.admin;

import io.github.biezhi.geekbb.model.db.Node;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.enums.OrderBy;

import java.time.LocalDateTime;
import java.util.List;

import static io.github.biezhi.anima.Anima.select;

/**
 * @author biezhi
 * @date 2018/4/3
 */
@Path("admin/nodes")
public class NodeController {

    @GetRoute("/")
    public String nodes(Request request) {
        List<Node> nodes = select().from(Node.class).where(Node::getState, 1).order(Node::getTopics, OrderBy.DESC).all();
        request.attribute("nodes", nodes);
        request.attribute("active", "nodes");

        List<Node> pnodes = select().from(Node.class).where(Node::getPid, "").and(Node::getState, 1).order(Node::getTopics, OrderBy.DESC).all();
        request.attribute("pnodes", pnodes);
        return "admin/nodes.html";
    }

    @PostRoute("save")
    @JSON
    public RestResponse save(Node node) {
        if (select().from(Node.class).where(Node::getNid, node.getNid()).count() > 0) {
            node.updateById(node.getNid());
        } else {
            if (null == node.getPid()) {
                node.setPid("");
            }
            node.setTopics(0);
            node.setCreated(LocalDateTime.now());
            node.setState(1);
            node.save();
        }
        return RestResponse.ok();
    }

    @PostRoute("delete/:nid")
    @JSON
    public RestResponse delete(@PathParam String nid) {
        new Node().set(Node::getState, 0).updateById(nid);
        return RestResponse.ok();
    }

}
