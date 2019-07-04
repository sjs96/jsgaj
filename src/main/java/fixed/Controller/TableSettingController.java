package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.TableSettingService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableSettingController extends Controller {
    static TableSettingService service = new TableSettingService();

    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("tableSetting.queryCount",kv);
        SqlPara query = Db.getSqlPara("tableSetting.query",kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }

    public void queryByAll(){
        renderJson(service.queryByAll(Db.getSql("tableSetting.queryByAll")));
    }

    public void queryByUIID(){
        renderJson(service.queryByUIID(Db.getSql("tableSetting.queryByAll"),getParaToInt("user_id",0)));
    }



    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("table_setting_model",getPara("table_setting_model","") );
        data.put("user_id",getPara("user_id","") );
        data.put("table_setting_content",getPara("table_setting_content","") );

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","table_setting_id");
        map.put("id",getPara("table_setting_id"));
        map.put("tableName","fixed_table_setting");
        map.put("data",data);

        renderJson(service.save(map,getPara("table_setting_model",""),getParaToInt("id",0)));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("tableSetting.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("tableSetting.setDeleteInfo"), Db.getSql("tableSetting.delete"), JSONSerializer.deserialize(getPara("default_id_"),List.class)));

    }

}

