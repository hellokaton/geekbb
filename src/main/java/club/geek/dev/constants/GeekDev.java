package club.geek.dev.constants;

import club.geek.dev.model.db.Promotion;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * GeekDev 全局缓存
 *
 * @author biezhi
 * @date 2018/4/5
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeekDev {

    private static final GeekDev INSTANCE = new GeekDev();

    private Map<String, String> config     = new HashMap<>();
    private List<Promotion>     promotions = new ArrayList<>();
    private Set<String>         ipHits     = new HashSet<>();

    public static GeekDev me() {
        return INSTANCE;
    }

    public void putSetting(String skey, String svalue) {
        config.put(skey, svalue);
    }

    public String getSetting(String key) {
        return config.get(key);
    }

    public void clearSettings() {
        config.clear();
    }

    public void resetPromotion(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public void resetIpHits(List<String> ips) {
        ipHits.clear();
        ipHits.addAll(ips);
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public boolean hasIp(String ip) {
        return ipHits.contains(ip);
    }

}