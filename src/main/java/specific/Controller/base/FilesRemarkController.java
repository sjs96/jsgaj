package specific.Controller.base;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;
import specific.service.base.FilesRemarkService;
import specific.service.base.FilesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/11/2.
 */
public class FilesRemarkController extends Controller {
    public static FilesRemarkService service = new FilesRemarkService();


    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("filesRemark.queryCount",kv);
        SqlPara query = Db.getSqlPara("filesRemark.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("fr_remark",getPara("fr_remark","") );
        data.put("f_id",getParaToInt("f_id",0) );
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","fr_id");
        map.put("id",getPara("fr_id"));
        map.put("tableName","sys_files_remark");
        map.put("data",data);

        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("filesRemark.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("filesRemark.setDeleteInfo"), Db.getSql("filesRemark.delete"),JSONSerializer.deserialize(getPara("default_id_"),List.class)));
    }
    @Parameter.Must({"f_id"})
    public void queryByLog(){
        renderJson(service.queryByAll(Db.getSql("filesRemark.queryByLog"),getPara("f_id")));
    }
    public void queryByLog2(){
        renderJson(service.queryByAll(Db.getSql("filesRemark.queryByLog"),getPara("f_id")));
    }
}
