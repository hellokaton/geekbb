package club.geek.dev.service;

import club.geek.dev.constants.GeekDev;
import club.geek.dev.model.db.Setting;
import com.blade.ioc.annotation.Bean;
import com.blade.kit.StringKit;

import java.util.List;

import static io.github.biezhi.anima.Anima.select;

/**
 * 系统配置服务
 *
 * @author biezhi
 * @date 2018/4/7
 */
@Bean
public class SettingsService {

    /**
     * 保存一个系统配置
     *
     * @param key
     */
    public void settingPlus(String key) {
        Setting setting = select().from(Setting.class).where(Setting::getSkey, key).one();
        if (null != setting) {
            String value = setting.getSvalue();
            if (StringKit.isNotBlank(value)) {
                setting.setSvalue(String.valueOf(Long.valueOf(value) + 1));
            } else {
                setting.setSvalue("0");
            }
            setting.updateById(key);
        }

        List<Setting> settings = select().from(Setting.class).all();
        GeekDev       geekDev  = GeekDev.me();
        geekDev.clearSettings();
        for (Setting item : settings) {
            geekDev.putSetting(item.getSkey(), item.getSvalue());
        }
    }

}
