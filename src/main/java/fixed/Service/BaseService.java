package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.util.SessionKit;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseService {
    // 分页
    public Map<String, Object>   queryByPage(int page,int rows,String countSql,String QuerySql,Object... obj){
        Page<Record> list = Db.paginateByFullSql(page,rows,countSql,QuerySql,obj);
        return Util.getResultMapByPage(list);
    }
    // 全部
    public Map<String, Object>  queryByAll(String QuerySql,Object... obj){
        List<Record> list = Db.find(QuerySql,obj);
        return Util.getResultMapByAll(list);
    }
    // 全部
    public Map<String, Object>  queryByAll(SqlPara sql){
        List<Record> list = Db.find(sql);
        return Util.getResultMapByAll(list);
    }

    //查询模块
    public Map<String, Object>  queryByProId(String QuerySql,int pro_id){
        List<Record> list = Db.find(QuerySql,pro_id);
        return Util.getResultMapByAll(list);
    }



    //　查询单个
    public Map<String, Object>  queryById(String QueryByIdSql,Object... obj){
        Record user = Db.findFirst(QueryByIdSql,obj);
        return Util.getResultMapByID(user);
    }
    //　保存
    public Map<String, Object>  save(Map<String,Object> map ){
        Map<String,Object> data = (Map<String,Object>) map.get("data");
        String primaryKey = (String) map.get("primaryKey");
        String id = (String) map.get("id");
        String tableName = (String) map.get("tableName");
        if(id==null||id.length()<1){
            return create(data,tableName,primaryKey);
        }
        return update(data,tableName,primaryKey,id);
    }
    //　更新
    public Map<String, Object>  update(Map<String,Object> data,String tableName,String primaryKey,String val){
        Record user = Db.findById(tableName,primaryKey, val);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            user.set(entry.getKey()+"", entry.getValue());
        }
        user.set("update_user", SessionKit.get().get("user_id"));
        user.set("update_time",Util.getCurrentTime());
        boolean bool = Db.update(tableName,primaryKey, user);
        return Util.getResultMapByEdit(bool);
    }
    //添加
    public Map<String, Object>  create(Map<String,Object> data,String tableName,String primaryKey){
        Record record = new Record();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            record.set(entry.getKey()+"", entry.getValue());
        }
        record.set("create_time", Util.getCurrentTime());
        record.set("create_user", SessionKit.get().get("user_id"));
        boolean bool = Db.save(tableName, record);
        Map<String,Object> map = new HashMap<>();
        map.put("id",record.get("id"));
        return Util.getResultMapByAdd(bool,map);
    }

    //删除
    public Map<String, Object> delete(String Sql,String Sql2,List deleteId){
        int count = 0;
        for(int i = 0;i<deleteId.size();i++){
            Db.update(Sql,SessionKit.get().get("user_id"), deleteId.get(i));
            count += Db.update(Sql2,  deleteId.get(i));
        }
        return Util.getResultMapByDel(count);
    }
}
