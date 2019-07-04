package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.MessageService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/8/22.
 */
public class    MessageController extends Controller {
    static MessageService service = new MessageService();

    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "m_name", getPara("m_name"));
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("message.queryCount", kv);
        SqlPara query = Db.getSqlPara("message.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), query.getPara()));
    }


    public void save() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("m_name", getPara("m_name", ""));
        data.put("m_content", getPara("m_content", ""));
        data.put("m_time", getPara("m_time", ""));
        data.put("m_area", getPara("m_area", ""));
        data.put("m_area_new", getPara("m_area_new", ""));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "m_id");
        map.put("id", getPara("m_id"));
        map.put("tableName", "fixed_message");
        map.put("data", data);
        List<String> area = JSONSerializer.deserialize(getPara("m_area_list"),List.class);
        renderJson(service.save(map,area));
    }
    public void getNum() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("m_name", getPara("m_name", ""));
        data.put("m_content", getPara("m_content", ""));
        data.put("m_time", getPara("m_time", ""));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "m_id");
        map.put("id", getPara("m_id"));
        map.put("tableName", "fixed_message");
        map.put("data", data);
        List<String> area = JSONSerializer.deserialize(getPara("m_area_list"),List.class);
        renderJson(service.getNum(getPara("m_name", ""),getPara("m_content", ""),getPara("m_time", ""),area));
    }
    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("message.queryById"), getPara("default_id_")));
    }


    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete( Db.getSql("message.setDeleteInfo"), Db.getSql("message.delete"), JSONSerializer.deserialize(getPara("default_id_"),List.class)));

    }
    public void queryByAll(){
        renderJson(service.queryByAll(Db.getSql("message.queryByAll")));
    }
}
