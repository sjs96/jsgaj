package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.MessageService;
import fixed.Service.OptionService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fixed.util.Util.state_ok;

/**
 * Created by 15205834418 on 2018/8/22.
 */
public class OptionController extends Controller {
    static OptionService service = new OptionService();

    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("option.queryCount", kv);
        SqlPara query = Db.getSqlPara("option.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), query.getPara()));
    }


    public void save() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("o_label", getPara("o_label", ""));
        data.put("o_value", getPara("o_value", ""));
        data.put("o_order", getPara("o_order", ""));
        data.put("o_state", getPara("o_state", ""));
        data.put("o_id_top", getParaToInt("o_id_top", 0));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "o_id");
        map.put("id", getPara("o_id"));
        map.put("tableName", "fixed_sys_option");
        map.put("data", data);
        Record r = Db.findFirst(Db.getSql("option.isExistence"),getPara("o_value", ""),getParaToInt("o_id_top", 0),getParaToInt("o_id",0));
        if(null!=r){
            renderJson(Util.getResultMap(Util.state_err,"当前值已存在",null,Util.level_info,Util.model_prod));
           return;
        }
        renderJson(service.save(map));
    }

    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("option.queryById"), getPara("default_id_")));
    }


    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete( Db.getSql("option.setDeleteInfo"), Db.getSql("option.delete"), JSONSerializer.deserialize(getPara("default_id_"),List.class)));

    }
    public void queryByAll(){
        renderJson(service.queryByAll(Db.getSql("option.queryByAll")));
    }
    @Parameter.Must({"o_label"})
    public void queryByLabel(){
        int o_id_top = getParaToInt("o_id_top", 0);
        if(o_id_top>0){
            renderJson(service.queryByAll(Db.getSql("option.queryByLabel2"), getPara("o_label"),o_id_top));
        }else{
            renderJson(service.queryByAll(Db.getSql("option.queryByLabel"), getPara("o_label")));
        }
    }
    public void getTree(){
        renderJson(service.getTree(Db.getSql("option.queryByAll")));
    }
}
