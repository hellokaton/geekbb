package club.geek.dev;

import java.util.function.Function;

/**
 * @author biezhi
 * @date 2018/5/12
 */
public class Demo {

    public Function<String, Class> loader() {
        return Unchecked.function(Class::forName);
    }

    public void testWrap(){
        Unchecked.wrap(() -> {
            throw new Exception("我是一个硬生生的 bug.");
        });
    }
}
