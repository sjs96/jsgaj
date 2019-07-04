package specific.service.base;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.Service.BaseService;
import fixed.util.SessionKit;
import fixed.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/11/5.
 */
public class FilesService extends BaseService {
    //　保存
    public Map<String, Object>  save(Map<String,Object> map){
        Map<String,Object> data = (Map<String,Object>) map.get("data");
        String primaryKey = (String) map.get("primaryKey");
        String id = (String) map.get("id");
        String tableName = (String) map.get("tableName");
        if(id==null||id.length()<1){
            Map<String, Object> returnMap = create(data, tableName, primaryKey);
            Map<String,Object> idmap =  (Map)returnMap.get("json");
            String f_id = idmap.get("id")+"";
            Record option1 = Db.findFirst(Db.getSql("files.queryByCreate1"));
            Record option2 = Db.findFirst(Db.getSql("files.queryByCreate2"),f_id);
            Record record = new Record();
            record.set("fs_type_option",option2.get("o_id",""));
            record.set("fs_type",option2.get("o_value","") );
            record.set("fs_type_text",option2.get("o_label","") );
            record.set("fs_inout_type_option",option1.get("o_id","") );
            record.set("fs_inout_type",option1.get("o_value","") );
            record.set("fs_inout_type_text",option1.get("o_label","") );
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            record.set("fs_inout_time",dateFormat.format(new Date()));
            record.set("f_id",f_id );
            record.set("create_time", Util.getCurrentTime());
            record.set("create_user", SessionKit.get().get("user_id"));
            Db.save("sys_files_state", record);
            return returnMap;
        }
        return update(data,tableName,primaryKey,id);
    }
    //　查询单个
    public Map<String, Object> queryById(String QueryByIdSql, Object... obj){
        Record file = Db.findFirst(QueryByIdSql,obj);
        List<Record> labelList= Db.find(Db.getSql("option.queryByLabel"),"出入柜操作");
        int fs_inout_type_option = 0;
        if(file!=null){
            if(labelList!=null){
                for(Record r :labelList){
                    if(r.getInt("value")!=file.getInt("fs_inout_type_option")&&file.getInt("fs_inout_type_option")!=null){
                        fs_inout_type_option =r.getInt("value");
                    }
                }
            }
            file.set("fs_inout_type_option",fs_inout_type_option);
        }
        return Util.getResultMapByID(file);
    }
}
