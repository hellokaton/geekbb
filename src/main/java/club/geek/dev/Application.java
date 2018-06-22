package club.geek.dev;

import com.blade.Blade;

/**
 * Geek Dev Club
 *
 * @author biezhi
 * @date 2018/4/3
 */
public class Application {
    public static void main(String[] args) {
        Blade.of().start(Application.class, args);
    }
}
