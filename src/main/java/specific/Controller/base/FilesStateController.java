package specific.Controller.base;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;
import specific.service.base.FilesService;
import specific.service.base.FilesStateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/11/2.
 */
public class FilesStateController extends Controller {
    public static FilesStateService service = new FilesStateService();


    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("filesState.queryCount",kv);
        SqlPara query = Db.getSqlPara("filesState.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("fs_type_option",getPara("fs_type_option","") );
        Record option = Db.findFirst(Db.getSql("option.queryById"),getPara("fs_type_option",""));
        if(option!=null){
            data.put("fs_type",option.getInt("o_value") );
            data.put("fs_type_text",option.get("o_label") );
        }else{
            data.put("fs_type",0 );
            data.put("fs_type_text","" );
        }
        data.put("fs_inout_type_option",getPara("fs_inout_type_option","") );
        option = Db.findFirst(Db.getSql("option.queryById"),getPara("fs_inout_type_option",""));
        if(option!=null){
            data.put("fs_inout_type",option.getInt("o_value") );
            data.put("fs_inout_type_text",option.get("o_label") );
        }else{
            data.put("fs_inout_type",0 );
            data.put("fs_inout_type_text","" );
        }
        data.put("fs_inout_time",getPara("fs_inout_time","") );
        data.put("f_id",getParaToInt("f_id",0) );
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","fo_id");
        map.put("id",getPara("fo_id"));
        map.put("tableName","sys_files_state");
        map.put("data",data);

        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("filesState.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("filesState.setDeleteInfo"), Db.getSql("filesState.delete"),JSONSerializer.deserialize(getPara("default_id_"),List.class)));
    }
    @Parameter.Must({"f_id"})
    public void queryByLog(){
        renderJson(service.queryByAll(Db.getSql("filesState.queryByLog"),getPara("f_id")));
    }
}
