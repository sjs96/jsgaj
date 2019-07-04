package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/11/5.
 */
public class OptionService extends BaseService {
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
            if (topid.equals(""+depart.get("o_id_top"))) {
                Map<String,Object> tree = new HashMap<String,Object>();
                tree.put("value",""+depart.get("o_id"));
                tree.put("label",""+depart.get("o_label"));
                List<Map<String,Object>> children = listtotee(list, ""+depart.get("o_id"));

                if(children!=null&&children.size()>0){
                    tree.put("children",children);
                }
                treelist.add(tree);
            }
        }
        return treelist;
    }
}
