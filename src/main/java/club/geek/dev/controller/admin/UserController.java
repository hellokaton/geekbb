package club.geek.dev.controller.admin;

import club.geek.dev.constants.TipMaps;
import club.geek.dev.enums.ErrorCode;
import club.geek.dev.enums.LogAction;
import club.geek.dev.enums.RoleType;
import club.geek.dev.enums.UserState;
import club.geek.dev.model.db.User;
import club.geek.dev.service.LogService;
import club.geek.dev.service.UserService;
import club.geek.dev.utils.GeekDevUtils;
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
