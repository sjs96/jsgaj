package fixed.util;

import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/9/10.
 */
public class MapUtil {
    public static List<Map<String,Object>> get(List<Map<String,Object>> list,String name, String type ){
        for(int i =0;i<list.size();i++){
            Map<String,Object> map = list.get(i);
            if(name.equals(map.get("type"))){
                List<Map<String,Object>> list2 = (List<Map<String,Object>>)map.get("data");
                for(int l =0;l<list2.size();l++){
                    Map<String,Object> map2 = list2.get(l);
                    if(type.equals(map2.get("type"))){
                        return (List<Map<String,Object>>)map2.get("data");
                    }
                }
            }
        }
        return null;
    }
}
