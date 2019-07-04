package specific.Controller.base;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.OptionService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/11/12.
 */
public class SetController  extends Controller {
    static OptionService service = new OptionService();

    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("set.queryCount", kv);
        SqlPara query = Db.getSqlPara("set.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), query.getPara()));
    }


    public void save() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("o_value_special", getPara("o_value_special", ""));
        data.put("o_value_special2", getPara("o_value_special2", ""));
        data.put("o_value_special3", getPara("o_value_special3", ""));
        data.put("o_value_special4", getPara("o_value_special4", ""));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "o_id");
        map.put("id", getPara("o_id"));
        map.put("tableName", "fixed_sys_option");
        map.put("data", data);
        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("set.queryById"), getPara("default_id_")));
    }


    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete( Db.getSql("set.setDeleteInfo"), Db.getSql("set.delete"), JSONSerializer.deserialize(getPara("default_id_"),List.class)));

    }
    public void queryByAll(){
        renderJson(service.queryByAll(Db.getSql("set.queryByAll")));
    }
   
}
