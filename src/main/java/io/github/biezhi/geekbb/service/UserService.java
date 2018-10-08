package io.github.biezhi.geekbb.service;

import io.github.biezhi.geekbb.constants.GeekDevConst;
import io.github.biezhi.geekbb.enums.ErrorCode;
import io.github.biezhi.geekbb.enums.LogAction;
import io.github.biezhi.geekbb.enums.RoleType;
import io.github.biezhi.geekbb.enums.UserState;
import io.github.biezhi.geekbb.exception.TipException;
import io.github.biezhi.geekbb.model.db.Profile;
import io.github.biezhi.geekbb.model.db.User;
import io.github.biezhi.geekbb.model.param.GithubCallback;
import io.github.biezhi.geekbb.model.vo.ProfileVO;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.anima.page.Page;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static io.github.biezhi.anima.Anima.select;

/**
 * 用户服务
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Bean
@Slf4j
public class UserService {

    @Inject
    private SettingsService settingsService;

    @Inject
    private LogService logService;

    /**
     * 根据用户名查找个人信息
     *
     * @param username
     * @return
     */
    public ProfileVO findProfile(String username) {
        if (StringKit.isBlank(username) || username.length() < 2) {
            return null;
        }
        User user = select().from(User.class).where(User::getUsername, username).one();
        if (null == user) {
            user = select().from(User.class).where(User::getName, username).one();
        }
        if (null == user) {
            return null;
        }
        Profile   profile   = select().from(Profile.class).byId(user.getUid());
        ProfileVO profileVO = new ProfileVO();
        profileVO.setAvatar(user.getAvatar());
        profileVO.setEmail(user.getEmail());
        profileVO.setName(user.getName());
        profileVO.setUsername(user.getUsername());
        profileVO.setBio(profile.getBio());
        profileVO.setGithub(profile.getGithub());
        profileVO.setTwitter(profile.getTwitter());
        profileVO.setLocation(profile.getLocation());
        profileVO.setZhihu(profile.getZhihu());
        profileVO.setWebsite(profile.getWebsite());
        profileVO.setWeibo(profile.getWeibo());
        return profileVO;
    }

    /**
     * 注册
     *
     * @param githubCallback
     * @throws TipException
     */
    public void register(GithubCallback githubCallback) throws TipException {
        TipException tipException = Anima.atomic(() -> {
            User user = new User();
            user.setUid(githubCallback.getId());
            user.setUsername(githubCallback.getLogin());

            String name = GeekDevUtils.unicodeToAliases(githubCallback.getName());
            String bio  = GeekDevUtils.unicodeToAliases(githubCallback.getBio());

            if (StringKit.isEmpty(name)) {
                user.setName(githubCallback.getLogin());
            } else {
                user.setName(name);
            }

            user.setEmail(githubCallback.getEmail());
            user.setUid(githubCallback.getId());
            user.setAvatar(githubCallback.getAvatar_url());
            user.setRole(RoleType.MEMBER.name());
            user.setCreated(LocalDateTime.now());
            user.setState(UserState.NORMAL.getState());
            user.setLogined(LocalDateTime.now());
            user.save();

            Profile profile = new Profile();
            profile.setUid(githubCallback.getId());
            profile.setGithub(githubCallback.getLogin());
            profile.setBio(bio);
            profile.setUsername(githubCallback.getLogin());
            profile.save();

            // 站点settings + 1
            settingsService.settingPlus(GeekDevConst.SETTING_COUNT_USERS);

            logService.save(LogAction.REGISTER, null);

        }).catchAndReturn(e -> {
            log.error("注册失败", e);
            return new TipException(ErrorCode.NO_PERMITION);
        });
        if (null != tipException) {
            throw tipException;
        }
    }

    /**
     * 后台分页查询用户列表
     *
     * @param page
     * @param q
     * @return
     */
    public Page<User> findUsers(Integer page, String q) {
        AnimaQuery<User> query = select().from(User.class);
        if (StringKit.isNotBlank(q)) {
            query.and(User::getUsername).like("%" + q + "%")
                    .or("`name` LIKE ?", "%" + q + "%");
        }
        return query.order(User::getCreated, OrderBy.DESC).page(page, 10);
    }


}
