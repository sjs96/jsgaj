package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.RoleService;
import fixed.Service.UserService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleController extends Controller {
    static RoleService service = new RoleService();
    static UserService userService = new UserService();

    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"r_name",getPara("r_name"));
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("role.queryCount",kv);
        SqlPara query = Db.getSqlPara("role.query",kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }

    public void queryByAll(){
        renderJson(service.queryByAll(Db.getSql("role.queryByAll")));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("r_name",getPara("r_name","") );

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","r_id");
        map.put("id",getPara("r_id"));
        map.put("tableName","fixed_sys_role");
        map.put("data",data);

        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("role.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("role.setDeleteInfo"), Db.getSql("role.delete"), JSONSerializer.deserialize(getPara("default_id_"),List.class)));

    }

    @Parameter.Must({"dp_id"})
    public void queryByDepart(){
        String dp_id = getPara("dp_id");
        String [] id = dp_id.split(",");
        dp_id = id[id.length-1];
        renderJson(service.queryByAll(Db.getSql("role.queryByDepart"),dp_id));
    }

}

