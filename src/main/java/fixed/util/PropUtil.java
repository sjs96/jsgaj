package fixed.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/4/13.
 */
public class PropUtil {

    private static Properties prop;

    private PropUtil(){}

    static {
        if(prop == null){
            prop = new Properties();
        }
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("sms.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return prop == null ? "" : prop.getProperty(key);
    }

    public static int getInt(String key){
        try {
            return get(key) == null ? 0 : Integer.valueOf(get(key));
        }catch (Exception e){
        }
        return 0;
    }

    public static long getLong(String key){
        try {
            return get(key) == null ? 0 : Long.valueOf(get(key));
        }catch (Exception e){
        }
        return 0l;
    }

    public static boolean getBool(String key){
        try {
            return get(key) == null ? false : Boolean.valueOf(get(key));
        }catch (Exception e){
        }
        return false;
    }
}
