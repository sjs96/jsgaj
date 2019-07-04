package fixed.util;
import java.util.Map;

/**
 * Created by 15205834418 on 2019/4/15.
 */
public class SessionKit {
    static ThreadLocal<Map<String,Object>> tl = new ThreadLocal<>();
    public static void put(Map<String,Object> s) {
        tl.set(s);
    }
    public static Map<String,Object> get() {
        return tl.get();
    }
    public static void remove() {
        tl.remove();
    }
}