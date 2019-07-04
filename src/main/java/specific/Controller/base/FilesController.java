package specific.Controller.base;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.MyJurisdictionService;
import fixed.parameter.Parameter;
import fixed.util.ExcelUtil2;
import fixed.util.JSONSerializer;
import fixed.util.JxlsUtil;
import fixed.util.Util;
import specific.service.base.FilesService;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 15205834418 on 2018/11/2.
 */
public class FilesController extends Controller {
    public static FilesService service = new FilesService();
    static MyJurisdictionService jurisdiction_service = new MyJurisdictionService();

    public void query()  {
        System.out.println("--query-");
        Kv kv = Kv.create();
        Util.setParaVal(kv,"f_type_option",getPara("f_type_option"));
        Util.setParaVal(kv,"fs_type_option",getPara("fs_type_option"));
        Util.setParaVal(kv,"f_name",getPara("f_name"));
        Util.setParaVal(kv,"dp_id",getPara("dp_id"));
        Util.setParaVal(kv,"f_state",getPara("f_state"));
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv,"qzcs_time1",getPara("qzcs_time1"));
        Util.setParaVal(kv,"qzcs_time2",getPara("qzcs_time2"));
        Util.setParaVal(kv,"f_suspect",getPara("f_suspect"));
        jurisdiction_service.jurisdiction(kv);
        String tx_time =  getPara("tx_time");
        if(tx_time!=null&&tx_time.length()>0){
            String []tx_times = tx_time.split(",");
            Util.setParaVal(kv,"tx_time1",tx_times[0]);
            Util.setParaVal(kv,"tx_time2",tx_times[1]);
        }

        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("files.queryCount",kv);
        SqlPara query = Db.getSqlPara("files.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("f_name",getPara("f_name","") );
        data.put("f_suspect",getPara("f_suspect","") );
        data.put("f_case_time",getPara("f_case_time","") );
        data.put("f_type_option",getPara("f_type_option","") );
        data.put("u_id_create",getParaToInt("u_id_create",0) );
        data.put("u_id_notify",getPara("u_id_notify","") );
        Record option = Db.findFirst(Db.getSql("option.queryById"),getPara("f_type_option",""));
        if(option!=null){
            data.put("f_type",option.getInt("o_value") );
            data.put("f_type_text",option.get("o_label") );
        }else{
            data.put("f_type",0 );
            data.put("f_type_text","" );
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","f_id");
        map.put("id",getPara("f_id"));
        map.put("tableName","sys_files");
        map.put("data",data);

        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("files.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete( Db.getSql("files.setDeleteInfo"), Db.getSql("files.delete"),JSONSerializer.deserialize(getPara("default_id_"),List.class)));
    }
    @Parameter.Must({"o_label"})
    public void queryByLabel(){
        int o_id_top = getParaToInt("o_id_top", 0);
        if(o_id_top>0){
            renderJson(service.queryByAll(Db.getSql("option.queryByLabel2"),"事由",o_id_top));
        }else{
            renderJson(service.queryByAll(Db.getSql("option.queryByLabel"), "事由"));
        }
    }
    public  void  toExecl(){
      /*  String json="[{\"title\":{\"title\":\"嘉善县公安局案管中心案件进出登记表\",\"rowStart\":0,\"colStart\":0,\"rowEnd\":0,\"colEnd\":6},\"map\":{\"list\":[{\"rowStart\":1,\"colStart\":0,\"rowEnd\":1,\"colEnd\":1,\"name\":\"_案件编号_\"},{\"rowStart\":2,\"colStart\":0,\"rowEnd\":2,\"colEnd\":1,\"name\":\"_入案管时间_\"},{\"rowStart\":3,\"colStart\":0,\"rowEnd\":3,\"colEnd\":1,\"name\":\"案件名称\"},{\"rowStart\":4,\"colStart\":0,\"rowEnd\":6,\"colEnd\":1,\"name\":\"_案件名称_\"},{\"rowStart\":7,\"colStart\":0,\"rowEnd\":7,\"colEnd\":1,\"name\":\"嫌疑人姓名\"},{\"rowStart\":8,\"colStart\":0,\"rowEnd\":101010,\"colEnd\":1,\"name\":\"_嫌疑人姓名_\"},{\"rowStart\":1,\"colStart\":2,\"rowEnd\":2,\"colEnd\":4,\"name\":\"_承办单位_\"},{\"rowStart\":3,\"colStart\":2,\"rowEnd\":3,\"colEnd\":2,\"name\":\"出案管中心时间\"},{\"rowStart\":3,\"colStart\":3,\"rowEnd\":3,\"colEnd\":3,\"name\":\"事由\"},{\"rowStart\":3,\"colStart\":4,\"rowEnd\":3,\"colEnd\":4,\"name\":\"经办人\"},{\"rowStart\":1,\"colStart\":5,\"rowEnd\":2,\"colEnd\":6,\"name\":\"_主办人_\"},{\"rowStart\":3,\"colStart\":5,\"rowEnd\":3,\"colEnd\":5,\"name\":\"进案管中心时间\"},{\"rowStart\":3,\"colStart\":6,\"rowEnd\":3,\"colEnd\":6,\"name\":\"经办人\"}]},\"list\":{\"min\":2,\"max\":9,\"content\":4,\"list\":[{\"name\":\"出案管中心时间\",\"field\":\"fs_inout_time\"},{\"name\":\"事由\",\"field\":\"fs_type_text\"},{\"name\":\"经办人\",\"colNum\":4,\"field\":\"d_name\"},{\"name\":\"进案管中心时间\",\"field\":\"fs_inout_time2\"},{\"name\":\"经办人\",\"colNum\":6,\"field\":\"d_name2\"}]}}]";
        Record file = Db.findFirst(Db.getSql("files.queryByExecl1"),getPara("f_id",""));
        json = json.replaceAll("_案件编号_","案件编号：");
        json = json.replaceAll("_入案管时间_","入案管时间："+(file.get("create_time")+"").substring(0,16));
        json = json.replaceAll("_案件名称_",file.get("f_name")+"");
        json = json.replaceAll("_嫌疑人姓名_",file.get("f_suspect")+"");
        json = json.replaceAll("_承办单位_","承办单位："+file.get("dp_nickname")+"");
        json = json.replaceAll("_主办人_","主办人："+file.get("d_name")+"");
        List<Record> list1 = Db.find(Db.getSql("files.queryByExecl2"), getPara("f_id", ""));
        List<Record> list2 =  Db.find(Db.getSql("files.queryByExecl3"),getPara("f_id",""));
        int size= 0;
        if(list1!=null){
            size= list1.size();
        }
        if(list2!=null&&size<list2.size()){
            size= list2.size();
        }

        if(size>5){
            json = json.replaceAll("101010",size+3+"");
        }else{
            json = json.replaceAll("101010","8");
        }
        List<Map<String,Object>>  data = new ArrayList();
        for(int i =0;i<size;i++){
            Map<String,Object> map = new HashMap<>();
            if(list1.size()>i){
                map.put("fs_type_text",list1.get(i).get("fs_type_text"));
                map.put("d_name",file.get("d_name"));
                map.put("fs_inout_time",(list1.get(i).get("fs_inout_time")+"").substring(0,16));
            }else{
                map.put("fs_type_text","");
                map.put("d_name","");
                map.put("fs_inout_time","");
            }
            if(list2.size()>i){
                map.put("d_name2",file.get("d_name"));
                map.put("fs_inout_time2",(list2.get(i).get("fs_inout_time")+"").substring(0,16));
            }else{
                map.put("d_name2","");
                map.put("fs_inout_time2","");
            }
            data.add(map);
        }
        List<Map<String,Object>>  set = JSONSerializer.deserialize(json,List.class);
        Map<String,Object> list= ExcelUtil2.createExecl1("案卷",set,data);
        renderJson( Util.getResultMap(Util.state_ok,"成功",list,Util.level_info,Util.model_dev));

*/


        String execlLengthStr = PropKit.get("execlLength");
        int execlLength = 20;
        if(execlLengthStr!=null&&execlLengthStr.length()>0){
            execlLength = Integer.parseInt(execlLengthStr);
        }
        try {
            Record file = Db.findFirst(Db.getSql("files.queryByExecl1"),getPara("f_id",""));
            List<Record> list1 = Db.find(Db.getSql("files.queryByExecl2"), getPara("f_id", ""));
            List<Record> list2 =  Db.find(Db.getSql("files.queryByExecl3"),getPara("f_id",""));

            int size= 0;
            if(list1!=null){
                size= list1.size();
            }
            if(list2!=null&&size<list2.size()){
                size= list2.size();
            }
            List<Map<String,Object>>  nameList = new ArrayList();
            List<Map<String,Object>>  data = new ArrayList();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");

            for(int i =0;i<size;i++){
                Map<String,Object> map = new HashMap<>();
                if(list1.size()>i){
                    map.put("fs_type_text",list1.get(i).get("fs_type_text"));
                    map.put("d_name",file.get("d_name"));
                    map.put("fs_inout_time",sdf2.format(sdf.parse((list1.get(i).get("fs_inout_time")+"").substring(0,16))));
                }else{
                    map.put("fs_type_text","");
                    map.put("d_name","");
                    map.put("fs_inout_time","");
                }
                if(list2.size()>i){
                    map.put("d_name2",file.get("d_name"));
                    map.put("fs_inout_time2",sdf2.format(sdf.parse((list2.get(i).get("fs_inout_time")+"").substring(0,16))));
                }else{
                    map.put("d_name2","");
                    map.put("fs_inout_time2","");
                }
                data.add(map);
            }
            for(;data.size()<execlLength;){
                Map<String,Object> map = new HashMap<>();
                map.put("fs_type_text","");
                map.put("d_name","");
                map.put("fs_inout_time","");
                map.put("d_name2","");
                map.put("fs_inout_time2","");
                data.add(map);
            }
            for(;(data.size()-4)!=nameList.size();){
                Map<String,Object> map = new HashMap<>();
                if(nameList.size()<1){
                    map.put("f_suspect",file.get("f_suspect"));
                }else{
                    map.put("f_suspect","");
                }
                nameList.add(map);

            }
            String dir = getRequest().getServletContext().getRealPath("/");
            // 模板路径和输出流
            String templatePath = dir+ "\\execl\\jsgaj.xls";
            File dirfile = new File(dir+"\\execlOut\\");
            if (!dirfile.exists() && !dirfile.isDirectory()) {
                dirfile.mkdirs();
            }
            OutputStream os = null;
            os = new FileOutputStream(dir+"\\execlOut\\嘉善县公安局案管中心案件进出登记表-"+file.get("f_name")+".xls");
            // 定义一个Map，往里面放入要在模板中显示数据
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("create_time", sdf2.format(sdf.parse((file.get("create_time")+"").substring(0,16))) );
            model.put("f_name", file.get("f_name"));
            model.put("f_suspect",file.get("f_suspect"));
            model.put("dp_nickname", file.get("dp_nickname"));
            model.put("d_name", file.get("d_name"));
            model.put("mylist", data);
            model.put("nameList", nameList);
            //调用之前写的工具类，传入模板路径，输出流，和装有数据Map
            JxlsUtil.exportExcel(templatePath, os, model);
            os.close();
            System.out.println("完成");

            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("fileName","嘉善县公安局案管中心案件进出登记表-"+file.get("f_name")+".xls");
            returnMap.put("url","\\execlOut\\嘉善县公安局案管中心案件进出登记表-"+file.get("f_name")+".xls");
            renderJson( Util.getResultMap(Util.state_ok,"成功",returnMap,Util.level_info,Util.model_dev));
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        renderJson( Util.getResultMap(Util.state_ok,"成功",null,Util.level_info,Util.model_dev));
    }
    public  void  toExecl3(){
        String json="[{\"title\":{\"title\":\"嘉善县公安局案管中心案件进出登记表\",\"rowStart\":0,\"colStart\":0,\"rowEnd\":0,\"colEnd\":6},\"map\":{\"list\":[{\"rowStart\":1,\"colStart\":0,\"rowEnd\":1,\"colEnd\":1,\"name\":\"_案件编号_\"},{\"rowStart\":2,\"colStart\":0,\"rowEnd\":2,\"colEnd\":1,\"name\":\"_入案管时间_\"},{\"rowStart\":3,\"colStart\":0,\"rowEnd\":3,\"colEnd\":1,\"name\":\"案件名称\"},{\"rowStart\":4,\"colStart\":0,\"rowEnd\":6,\"colEnd\":1,\"name\":\"_案件名称_\"},{\"rowStart\":7,\"colStart\":0,\"rowEnd\":7,\"colEnd\":1,\"name\":\"嫌疑人姓名\"},{\"rowStart\":8,\"colStart\":0,\"rowEnd\":101010,\"colEnd\":1,\"name\":\"_嫌疑人姓名_\"},{\"rowStart\":1,\"colStart\":2,\"rowEnd\":2,\"colEnd\":4,\"name\":\"_承办单位_\"},{\"rowStart\":3,\"colStart\":2,\"rowEnd\":3,\"colEnd\":2,\"name\":\"出案管中心时间\"},{\"rowStart\":3,\"colStart\":3,\"rowEnd\":3,\"colEnd\":3,\"name\":\"事由\"},{\"rowStart\":3,\"colStart\":4,\"rowEnd\":3,\"colEnd\":4,\"name\":\"经办人\"},{\"rowStart\":1,\"colStart\":5,\"rowEnd\":2,\"colEnd\":6,\"name\":\"_主办人_\"},{\"rowStart\":3,\"colStart\":5,\"rowEnd\":3,\"colEnd\":5,\"name\":\"进案管中心时间\"},{\"rowStart\":3,\"colStart\":6,\"rowEnd\":3,\"colEnd\":6,\"name\":\"经办人\"}]},\"list\":{\"min\":2,\"max\":9,\"content\":4,\"list\":[{\"name\":\"出案管中心时间\",\"field\":\"fs_inout_time\"},{\"name\":\"事由\",\"field\":\"fs_type_text\"},{\"name\":\"经办人\",\"colNum\":4,\"field\":\"d_name\"},{\"name\":\"进案管中心时间\",\"field\":\"fs_inout_time2\"},{\"name\":\"经办人\",\"colNum\":6,\"field\":\"d_name2\"}]}}]";
        Record file = Db.findFirst(Db.getSql("files.queryByExecl1"),getPara("f_id",""));
        json = json.replaceAll("_案件编号_","案件编号：");
        json = json.replaceAll("_入案管时间_","入案管时间："+(file.get("create_time")+"").substring(0,16));
        json = json.replaceAll("_案件名称_",file.get("f_name")+"");
        json = json.replaceAll("_嫌疑人姓名_",file.get("f_suspect")+"");
        json = json.replaceAll("_承办单位_","承办单位："+file.get("dp_nickname")+"");
        json = json.replaceAll("_主办人_","主办人："+file.get("d_name")+"");
        List<Record> list1 = Db.find(Db.getSql("files.queryByExecl2"), getPara("f_id", ""));
        List<Record> list2 =  Db.find(Db.getSql("files.queryByExecl3"),getPara("f_id",""));
        int size= 0;
        if(list1!=null){
            size= list1.size();
        }
        if(list2!=null&&size<list2.size()){
            size= list2.size();
        }

        if(size>5){
            json = json.replaceAll("101010",size+3+"");
        }else{
            json = json.replaceAll("101010","8");
        }
        List<Map<String,Object>>  data = new ArrayList();
        for(int i =0;i<size;i++){
            Map<String,Object> map = new HashMap<>();
            if(list1.size()>i){
                map.put("fs_type_text",list1.get(i).get("fs_type_text"));
                map.put("d_name",file.get("d_name"));
                map.put("fs_inout_time",(list1.get(i).get("fs_inout_time")+"").substring(0,16));
            }else{
                map.put("fs_type_text","");
                map.put("d_name","");
                map.put("fs_inout_time","");
            }
            if(list2.size()>i){
                map.put("d_name2",file.get("d_name"));
                map.put("fs_inout_time2",(list2.get(i).get("fs_inout_time")+"").substring(0,16));
            }else{
                map.put("d_name2","");
                map.put("fs_inout_time2","");
            }
            data.add(map);
        }
        List<Map<String,Object>>  set = JSONSerializer.deserialize(json,List.class);
        Map<String,Object> list= ExcelUtil2.createExecl1("案卷",set,data);
        renderJson( Util.getResultMap(Util.state_ok,"成功",list,Util.level_info,Util.model_dev));
    }
    public  void  toExecl2(){
        String json="[{\"title\":{\"title\":\"案卷\",\"rowStart\":0,\"colStart\":0,\"rowEnd\":0,\"colEnd\":6},\"map\":{\"list\":[{\"rowStart\":1,\"colStart\":0,\"rowEnd\":1,\"colEnd\":0,\"name\":\"案件名称\"},{\"rowStart\":1,\"colStart\":1,\"rowEnd\":1,\"colEnd\":1,\"name\":\"部门\"},{\"rowStart\":1,\"colStart\":2,\"rowEnd\":1,\"colEnd\":2,\"name\":\"办案人\"},{\"rowStart\":1,\"colStart\":3,\"rowEnd\":1,\"colEnd\":3,\"name\":\"措施名称\"},{\"rowStart\":1,\"colStart\":4,\"rowEnd\":1,\"colEnd\":4,\"name\":\"嫌疑人\"},{\"rowStart\":1,\"colStart\":5,\"rowEnd\":1,\"colEnd\":5,\"name\":\"措施时间\"},{\"rowStart\":1,\"colStart\":0,\"rowEnd\":1,\"colEnd\":0,\"name\":\"到期时间\"},{\"rowStart\":1,\"colStart\":6,\"rowEnd\":1,\"colEnd\":6,\"name\":\"案卷状态\"},{\"rowStart\":1,\"colStart\":7,\"rowEnd\":1,\"colEnd\":7,\"name\":\"案卷类型\"}]},\"list\":{\"min\":0,\"max\":2,\"content\":2,\"list\":[{\"name\":\"案件名称\",\"field\":\"f_name\"},{\"name\":\"部门\",\"field\":\"dp_nickname\"},{\"name\":\"办案人\",\"field\":\"d_name\"},{\"name\":\"措施名称\",\"field\":\"fo_operate_type_str\"},{\"name\":\"嫌疑人\",\"field\":\"f_suspect\"},{\"name\":\"措施时间\",\"field\":\"fo_operate_time1\"},{\"name\":\"到期时间\",\"field\":\"fo_operate_time2\"},{\"name\":\"案卷状态\",\"field\":\"fs_type_str\"},{\"name\":\"案卷类型\",\"field\":\"f_type_str\"}]}}]";

        Kv kv = Kv.create();

        Util.setParaVal(kv,"f_type_option",getPara("f_type_option"));
        Util.setParaVal(kv,"f_name",getPara("f_name"));
        Util.setParaVal(kv,"dp_id",getPara("dp_id"));
        Util.setParaVal(kv,"f_state",getPara("f_state"));
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        Util.setParaVal(kv,"qzcs_time1",getPara("qzcs_time1"));
        Util.setParaVal(kv,"qzcs_time2",getPara("qzcs_time2"));
        Util.setParaVal(kv,"f_suspect",getPara("f_suspect"));
        jurisdiction_service.jurisdiction(kv);
        String tx_time =  getPara("tx_time");
        if(tx_time!=null&&tx_time.length()>0){
            String []tx_times = tx_time.split(",");
            Util.setParaVal(kv,"tx_time1",tx_times[0]);
            Util.setParaVal(kv,"tx_time2",tx_times[1]);
        }
        List<String> ids = JSONSerializer.deserialize(getPara("ids"),List.class);
        if(ids!=null&&ids.size()>0){
            Util.setParaVal(kv, "ids", ids);
        }
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("files.query",kv);

        List<Record> list1 =  Db.find(count);
        List<Map<String,Object>>  data = new ArrayList();
        for(Record r:list1){
            data.add(r.getColumns());
        }
        List<Map<String,Object>>  set = JSONSerializer.deserialize(json,List.class);
        Map<String,Object> list= ExcelUtil2.createExecl1("案卷",set,data);
        renderJson( Util.getResultMap(Util.state_ok,"成功",list,Util.level_info,Util.model_dev));
    }
}
