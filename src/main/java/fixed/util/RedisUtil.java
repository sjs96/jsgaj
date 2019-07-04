package fixed.util;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import java.util.*;

public class RedisUtil {
    public static final String dianya="dianya--";
    public static final String dianliu="dianliu--";
    public static final String yougonggonglv="yougonggonglv--";
    public static final String shizaigonglv="shizaigonglv--";
    public static final String wugonggonglv="wugonggonglv--";
    public static final String gonglvyinshu="gonglvyinshu--";
    public static final String pinlv="pinlv--";
    public static final String yougongzongdianliang="yougongzongdianliang--";
    public static final String shengyudianliang="shengyudianliang--";
    public static final String zhuce="zhuce--";
    public static final String AccountError="AccountError--";
    public static final String Login="login--";
    public static final String Query="query--";
    public static final String Current="current--";
    public static final String TC="TC--";
    public static final String TM="TM--";
    public static final String lixian="lixian--";

    public static void setValue(String name,String key,Map<String,Object> map,int seconds){
        //保存到redis 缓存
        Cache bbsCache = Redis.use();
        bbsCache.setex(name+key,seconds,map);
    }
    public static Map<String,Object> getValue(String name,String key){
        //保存到redis 缓存
        Cache bbsCache = Redis.use();
        Map<String,Object> map = bbsCache.get(name+key);
        return map;
    }
    public static Long del(String name,String key){
        //保存到redis 缓存
        Cache bbsCache = Redis.use();
        return bbsCache.del(name+key);
    }
    public static boolean isKey(String name,String key){
        //保存到redis 缓存
        Cache bbsCache = Redis.use();
        return bbsCache.exists(name+key);
    }

    public static Map<String,Object> getMeter(String key){
        Cache bbsCache = Redis.use();
        Map<String,Object> user = bbsCache.get(key);
        //判断是否有表缓存
        if(user==null){
            user = new HashMap<>();
            Record meter = Db.findFirst(Db.getSql("meter.isExistence"),key.substring(1));
            user.put("m_id",meter.get("m_id"));
            bbsCache.setex(key,60*60*24,user);
        }
        return user;
    }

    public static Map<String,Object> getCurrentALL(){
        //保存到redis 缓存
        Cache bbsCache = Redis.use();
        Set<String> keys = bbsCache.keys("current--*");
        Map<String,Object> m_area = new HashMap<>();
        for (String key : keys) {
            String myKey = key.substring(9);
            Map<String,Object> map = bbsCache.get(key);
            m_area.put(myKey,map);
        }
        return m_area;
    }

    public static Map<String,Object> getQueryAll(){
        Cache bbsCache = Redis.use();
        Set<String> keys = bbsCache.keys("query--*");
        Map<String,Object> m_area = new HashMap<>();
        for (String key : keys) {
            String myKey = key.substring(7);
            Map<String,Object>  materNo = bbsCache.get(key);
            m_area.put(myKey,materNo.get("m_no"));
        }
        return m_area;
    }
    public static  List<Map<String,Object>> getLoginAll(){
        Cache bbsCache = Redis.use();
        Set<String> keys = bbsCache.keys(Login+"*");
        List<Map<String,Object>> list = new ArrayList<>();
        for (String key : keys) {
            Map<String,Object>  user = bbsCache.get(key);
            list.add(user);
        }
        return list;
    }
}
