package specific.Controller.base;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;
import specific.service.base.FilesOperateService;
import specific.service.base.FilesService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 15205834418 on 2018/11/2.
 */
public class FilesOperateController extends Controller {
    public static FilesOperateService service = new FilesOperateService();


    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("filesOperate.queryCount",kv);
        SqlPara query = Db.getSqlPara("filesOperate.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }


    public void save(){
        String optionStr = "";

        Map<String,Object> data = new HashMap<String,Object>();
        String fo_operate_type_option = getPara("fo_operate_type_option1","");
        String f_type_str = getPara("f_type_str","");
        if(fo_operate_type_option==null||fo_operate_type_option.length()<1){
            fo_operate_type_option =  getPara("fo_operate_type_option2","");
        }
        data.put("fo_operate_type_option",fo_operate_type_option );
        Record option = Db.findFirst(Db.getSql("option.queryById"),fo_operate_type_option);
        if(option!=null){
            optionStr = option.get("o_label");
            data.put("fo_operate_type",option.getInt("o_value") );
            data.put("fo_operate_type_text",option.get("o_label") );
        }else{
            data.put("fo_operate_type",0 );
            data.put("fo_operate_type_text","");
        }
        int addDay = 3;
        if("在办".equals(optionStr)){
            addDay = 30;
        }

        try {
            addDay = Integer.parseInt(option.get("o_value_special4")+"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String fo_operate_time1 = getPara("fo_operate_time1","");
        String fo_operate_time2 =  getPara("fo_operate_time2","");

        if(fo_operate_time1==null||fo_operate_time1.length()<1){
            Date  day = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar ca = Calendar.getInstance();
            ca.setTime(day);
            ca.add(Calendar.DATE, 1);
            fo_operate_time1=format.format(ca.getTime());
        }
        if(fo_operate_time2==null||fo_operate_time2.length()<1){
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date  currdate = format.parse(fo_operate_time1);
                Calendar ca = Calendar.getInstance();
                ca.setTime(currdate);
                ca.add(Calendar.DATE, addDay);
                fo_operate_time2=format.format(ca.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        data.put("fo_operate_time1",fo_operate_time1+" 00:00:00");
        data.put("fo_operate_time2",fo_operate_time2+" 23:59:59");
        data.put("f_id",getParaToInt("f_id",0) );
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","fo_id");
        map.put("id",getPara("fo_id"));
        map.put("tableName","sys_files_operate");
        map.put("data",data);

        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("filesOperate.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("filesOperate.setDeleteInfo"), Db.getSql("filesOperate.delete"),JSONSerializer.deserialize(getPara("default_id_"),List.class)));
    }
    @Parameter.Must({"f_id"})
    public void queryByLog(){
        renderJson(service.queryByAll(Db.getSql("filesOperate.queryByLog"),getPara("f_id")));
    }
}
