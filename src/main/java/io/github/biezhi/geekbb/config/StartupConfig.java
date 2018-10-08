package io.github.biezhi.geekbb.config;

import io.github.biezhi.geekbb.constants.GeekDev;
import io.github.biezhi.geekbb.model.db.IpHit;
import io.github.biezhi.geekbb.model.db.Promotion;
import io.github.biezhi.geekbb.model.db.Setting;
import com.alibaba.druid.pool.DruidDataSource;
import com.blade.Blade;
import com.blade.Environment;
import com.blade.ioc.annotation.Bean;
import com.blade.kit.JsonKit;
import com.blade.loader.BladeLoader;
import com.blade.mvc.view.template.JetbrickTemplateEngine;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.enums.OrderBy;
import jetbrick.template.resolver.GlobalResolver;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static io.github.biezhi.geekbb.constants.GeekDevConst.SETTING_SITE_VERSION;
import static io.github.biezhi.anima.Anima.select;

/**
 * 启动配置
 *
 * @author biezhi
 * @date 2018/4/3
 */
@Bean
@Slf4j
public class StartupConfig implements BladeLoader {

    @Override
    public void preLoad(Blade blade) {
        Environment environment = blade.environment();

        // Github
        String secretState = "secret" + new Random().nextInt(999_999);

        OAuth20Service oAuth20Service = new ServiceBuilder(environment.getOrNull("github.clientId"))
                .apiSecret(environment.getOrNull("github.secret"))
                .state(secretState)
                .callback(environment.getOrNull("github.callbackUrl"))
                .debug()
                .build(GitHubApi.instance());

        if (null != oAuth20Service) {
            blade.register(oAuth20Service);
        }
    }

    @Override
    public void load(Blade blade) {

        log.info("Environment: {}", JsonKit.toString(blade.environment().toMap()));

        // Template
        JetbrickTemplateEngine templateEngine = new JetbrickTemplateEngine();
        GlobalResolver         globalResolver = templateEngine.getGlobalResolver();
        globalResolver.registerFunctions(TplFunction.class);

        templateEngine.getGlobalContext().set("useCdn", blade.environment().getBoolean("app.use-cdn", true));

        // JDBC
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(blade.environment().getOrNull("jdbc.url"));
        dataSource.setUsername(blade.environment().getOrNull("jdbc.username"));
        dataSource.setPassword(blade.environment().getOrNull("jdbc.password"));

        Anima.open(dataSource);

        String version = select().from(Setting.class).where(Setting::getSkey, SETTING_SITE_VERSION).one().getSvalue();
        String newVer  = new BigDecimal(version).add(new BigDecimal("0.01")).setScale(2).toString();

        new Setting().set(Setting::getSvalue, newVer).updateById(SETTING_SITE_VERSION);

        List<Setting> settings = select().from(Setting.class).where(Setting::getState, 1).all();
        GeekDev       geekDev  = GeekDev.me();
        for (Setting setting : settings) {
            geekDev.putSetting(setting.getSkey(), setting.getSvalue());
        }

        // 加载 IP 黑名单
        List<IpHit> ipHits = select().from(IpHit.class).where(IpHit::getExpired).gt(LocalDateTime.now()).all();
        if (null != ipHits) {
            geekDev.resetIpHits(ipHits.stream().map(IpHit::getIp).collect(Collectors.toList()));
        }

        List<Promotion> promotions = select().from(Promotion.class)
                .where(Promotion::getExpired)
                .gt(LocalDateTime.now()).order(Promotion::getSort, OrderBy.ASC)
                .order(Promotion::getId, OrderBy.DESC).all();
        geekDev.resetPromotion(promotions);

        blade.templateEngine(templateEngine);
    }

}
