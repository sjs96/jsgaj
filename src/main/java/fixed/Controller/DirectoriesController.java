package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.MyJurisdictionService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;
import fixed.Service.DirectoriesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/11/2.
 */
public class DirectoriesController extends Controller {
    public static DirectoriesService service = new DirectoriesService();

    static MyJurisdictionService jurisdiction_service = new MyJurisdictionService();
    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("directories.queryCount",kv);
        SqlPara query = Db.getSqlPara("directories.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }

    public void saveAll() {
        List<Map<String,Object>> list  = JSONSerializer.deserialize(getPara("data"),List.class);
        int index = getParaToInt("index");
        int num = getParaToInt("num");
        renderJson(service.saveAll(list,index,num));
    }
    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("d_name",getPara("d_name","") );
        data.put("dp_id",getParaToInt("dp_id",0) );
        data.put("d_phone",getPara("d_phone","") );
        data.put("d_phone_simple",getPara("d_phone_simple","") );
        data.put("d_code",getPara("d_code","") );
        data.put("d_post",getPara("d_post","") );
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","d_id");
        map.put("id",getPara("d_id"));
        map.put("tableName","fixed_sys_directories");
        map.put("data",data);

        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("directories.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("directories.setDeleteInfo"), Db.getSql("directories.delete"),JSONSerializer.deserialize(getPara("default_id_"),List.class)));
    }
    public void queryByAll(){
        renderJson(service.queryByAll(Db.getSql("directories.queryByAll")));
    }
    public void queryByDepartment(){
        Kv kv = Kv.create();
        jurisdiction_service.jurisdiction(kv);
        SqlPara count = Db.getSqlPara("directories.queryByDepartment",kv);
        renderJson(service.queryByAll(count));
    }
}
