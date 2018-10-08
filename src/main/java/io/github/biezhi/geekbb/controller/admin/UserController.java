package io.github.biezhi.geekbb.controller.admin;

import io.github.biezhi.geekbb.constants.TipMaps;
import io.github.biezhi.geekbb.enums.ErrorCode;
import io.github.biezhi.geekbb.enums.LogAction;
import io.github.biezhi.geekbb.enums.RoleType;
import io.github.biezhi.geekbb.enums.UserState;
import io.github.biezhi.geekbb.model.db.User;
import io.github.biezhi.geekbb.service.LogService;
import io.github.biezhi.geekbb.service.UserService;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import io.github.biezhi.anima.page.Page;

/**
 * @author biezhi
 * @date 2018/4/3
 */
@Path("admin/users")
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private LogService logService;

    @GetRoute("/")
    public String users(@Param(defaultValue = "1") Integer page, @Param String q, Request request) {
        Page<User> userPage = userService.findUsers(page, q);
        request.attribute("users", userPage);
        request.attribute("q", q);
        request.attribute("active", "users");
        return "admin/users.html";
    }

    @PostRoute("role")
    @JSON
    public RestResponse updateRole(User user) {
        if (user.getRole().equals(RoleType.ADMIN.name()) && !GeekDevUtils.isMaster()) {
            return RestResponse.fail(TipMaps.getMsg(ErrorCode.NO_PERMITION));
        }
        new User().set(User::getRole, user.getRole()).where(User::getUsername, user.getUsername()).update();
        if (user.getRole().equals(RoleType.ADMIN.name())) {
            logService.save(LogAction.SET_ADMIN, user.getUsername());
        }
        if (user.getRole().equals(RoleType.MEMBER.name())) {
            logService.save(LogAction.REMOVE_ADMIN, user.getUsername());
        }
        return RestResponse.ok();
    }

    @PostRoute("state")
    @JSON
    public RestResponse updateState(User user) {

        new User().set(User::getState, user.getState()).where(User::getUsername, user.getUsername()).update();

        if (user.getState().equals(UserState.DISABLE.getState())) {
            logService.save(LogAction.DISABLE_USER, user.getUsername());
        }
        if (user.getState().equals(UserState.NORMAL.getState())) {
            logService.save(LogAction.ENABLE_USER, user.getUsername());
        }
        return RestResponse.ok();
    }

}
