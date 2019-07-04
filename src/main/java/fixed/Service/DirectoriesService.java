package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.Service.BaseService;
import fixed.util.MD5Util;
import fixed.util.SessionKit;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/11/5.
 */
public class DirectoriesService extends BaseService {
    public Map<String,Object> saveAll(List<Map<String,Object>> list, int index, int num ) {

        try{
            for(int i=0;i<list.size();i++){
                Map<String,Object> newMap = list.get(i);

                Record record = new Record();
                record.set("d_name", newMap.get("d_name"));
                record.set("d_code", newMap.get("d_code"));
                record.set("d_phone",  newMap.get("d_phone"));
                record.set("d_phone_simple",  newMap.get("d_phone_simple"));
                record.set("d_post",  newMap.get("d_post"));
                record.set("create_time", Util.getCurrentTime());
                record.set("create_user",  SessionKit.get().get("user_id"));
                if(newMap.get("dp_name")!=null&&newMap.get("dp_name").toString().length()>0){
                    Record top = Db.findFirst(Db.getSql("department.queryByName"),newMap.get("dp_name"));
                    if(top!=null){
                        record.set("dp_id", top.getInt("dp_id"));
                    }
                }
                Db.save("fixed_sys_directories", record);
                //添加到登录用户
                Record record2 = new Record();
                String dp_name = newMap.get("dp_name")+"";
                int r_id=0;
                int dp_id=0;
                if("嘉善县公安局".equals(dp_name)){
                    Record top = Db.findFirst(Db.getSql("role.queryByName"),"法制");
                    if(top!=null){
                        r_id = top.getInt("r_id");
                    }
                }else{
                    Record top = Db.findFirst(Db.getSql("role.queryByName"),"派出所");
                    if(top!=null){
                        r_id = top.getInt("r_id");
                    }
                }
                Record top = Db.findFirst(Db.getSql("department.queryByName"),newMap.get("dp_name"));
                if(top!=null){
                    dp_id = top.getInt("dp_id");
                }
                record2.set("r_id",r_id);
                record2.set("dp_id",  dp_id);
                record2.set("ui_name", newMap.get("d_name"));
                record2.set("ui_code",  newMap.get("d_code"));
                record2.set("ui_telno",  newMap.get("d_phone"));
                record2.set("ui_password",  MD5Util.MD5("123456"));
                record2.set("create_time", Util.getCurrentTime());
                record2.set("create_user",  SessionKit.get().get("user_id"));
                Db.save("fixed_sys_user", record2);
            }
        }catch (Exception e){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("index",index);
            map.put("num",num);
            return Util.getResultMap(200,"添加失败",map,Util.level_info,Util.model_prod);
        }

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("index",index);
        map.put("num",num);
        return Util.getResultMap(100,"添加成功",map,Util.level_info,Util.model_prod);
    }
}
