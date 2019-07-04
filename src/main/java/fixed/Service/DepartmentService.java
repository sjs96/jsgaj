package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.SessionKit;
import fixed.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentService extends BaseService{
    public Map<String,Object> saveAll(List<Map<String,Object>> list, int index, int num ) {

        try{
            for(int i=0;i<list.size();i++){
                Map<String,Object> newMap = list.get(i);

                Record record = new Record();
                record.set("dp_name", newMap.get("dp_name"));
                record.set("dp_nickname", newMap.get("dp_nickname"));
                record.set("dp_code",  newMap.get("dp_code"));
                record.set("create_time", Util.getCurrentTime());
                record.set("create_user",  SessionKit.get().get("user_id"));
                int dp_topid=0;
                if(newMap.get("dp_top")!=null&&newMap.get("dp_top").toString().length()>0){
                    Record top = Db.findFirst(Db.getSql("department.queryByName"),newMap.get("dp_top"));
                    if(top!=null){
                        dp_topid = top.getInt("dp_id");
                    }
                }
                record.set("dp_topid", dp_topid);
                Db.save("fixed_sys_department", record);
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
    public Map<String, Object> getTree(String QuerySql, Object... obj){
        List<Record> list = Db.find(QuerySql,obj);
        List<Map<String,Object>> newList= listtotee(list,"0");
        if(newList!=null&&newList.size()>0){
            return Util.getResultMap(Util.state_ok,"有数据",newList,Util.level_info,Util.model_dev);
        }
        return Util.getResultMap(Util.state_err_bypage,"暂无数据",newList,Util.level_info,Util.model_dev);
    }
    public List<Map<String,Object>> listtotee(List<Record> list, String topid) {
        List<Map<String,Object>> treelist = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < list.size(); i++) {
            Record depart = list.get(i);
            if (topid.equals(""+depart.get("dp_topid"))) {
                Map<String,Object> tree = new HashMap<String,Object>();
                tree.put("value",""+depart.get("dp_id"));
                tree.put("label",""+depart.get("dp_name"));
                List<Map<String,Object>> children = listtotee(list, ""+depart.get("dp_id"));

                if(children!=null&&children.size()>0){
                    tree.put("children",children);
                }
                treelist.add(tree);
            }
        }
        return treelist;
    }
}
