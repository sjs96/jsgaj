package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.JSONSerializer;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableSettingService  extends BaseService {
    public Map<String, Object> save(Map<String,Object> map , String table_setting_model  ,int ui_id){
        Db.update(Db.getSql("tableSetting.deleteModel"),ui_id,  table_setting_model);
        Map<String, Object> resultMap = super.save(map);
        return resultMap;
    }
    // 全部
    public Map<String, Object>  queryByUIID(String QuerySql,Object... obj){
        Record returnRecor = new Record();
        Map<String, Object> tableSetting = new HashMap<>();
        List<Record> tableSettingList = Db.find(Db.getSql("tableSetting.queryByUIID"),obj);
        for(int i = 0;i<tableSettingList.size();i++){
            Record recor = tableSettingList.get(i);
            String table_setting_content = recor.get("table_setting_content");
            List<String> setUpMap = JSONSerializer.deserialize(table_setting_content,List.class);
            returnRecor.set(""+recor.get("table_setting_model"),setUpMap);
        }
        return Util.getResultMapByID(returnRecor);
    }
}
