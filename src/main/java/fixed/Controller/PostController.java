package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.PostService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;
import fixed.util.WordUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostController extends Controller{
    static PostService service = new PostService();


    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"po_name",getPara("po_name"));
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("post.queryCount",kv);
        SqlPara query = Db.getSqlPara("post.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }

    public void queryByAll(){
        renderJson(service.queryByAll(Db.getSql("post.queryByAll")));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("po_name",getPara("po_name","") );
        data.put("po_duty", getPara("po_duty",""));
        data.put("po_note",getPara("po_note",""));
        data.put("po_sort",getParaToInt("po_sort",1));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","po_id");
        map.put("id",getPara("po_id"));
        map.put("tableName","fixed_sys_post");
        map.put("data",data);

        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("post.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("post.setDeleteInfo"), Db.getSql("post.delete"),JSONSerializer.deserialize(getPara("default_id_"),List.class)));

    }

    @Parameter.Must({"dp_id"})
    public void queryByDepart(){
        String dp_id = getPara("dp_id");
        String [] id = dp_id.split(",");
        dp_id = id[id.length-1];
        renderJson(service.queryByAll(Db.getSql("post.queryByDepart"),dp_id));
    }

}
