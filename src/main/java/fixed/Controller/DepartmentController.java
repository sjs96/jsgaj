package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.DepartmentService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DepartmentController extends Controller {
    static DepartmentService service = new DepartmentService();
    public void query(){
        Kv kv = Kv.create();
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("department.queryCount",kv);
        SqlPara query = Db.getSqlPara("department.query",kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("dp_name",getPara("dp_name","") );
        data.put("dp_topid",getParaToInt("dp_topid",0));
        data.put("dp_note",getPara("dp_note",""));
        data.put("dp_nickname",getPara("dp_nickname",""));
        data.put("dp_code",getPara("dp_code",""));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","dp_id");
        map.put("id",getPara("dp_id"));
        map.put("tableName","fixed_sys_department");
        map.put("data",data);

        renderJson(service.save(map));
    }

    public void saveAll() {
        List<Map<String,Object>> list  = JSONSerializer.deserialize(getPara("data"),List.class);
        int index = getParaToInt("index");
        int num = getParaToInt("num");
        renderJson(service.saveAll(list,index,num));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("department.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("department.setDeleteInfo"), Db.getSql("department.delete"),JSONSerializer.deserialize(getPara("default_id_"),List.class)));
    }

    public void getTree(){
        renderJson(service.getTree(Db.getSql("department.queryByAll")));
    }
}
