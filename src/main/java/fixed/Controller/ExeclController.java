package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.ExeclService;
import fixed.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExeclController extends Controller {
    static ExeclService service = new ExeclService();
    public  void  execl(){

        Map<String, Object> setUpMap = JSONSerializer.deserialize(getPara("setUp_execl_"),Map.class);
        Map<String, Object> paramsMap = JSONSerializer.deserialize(getPara("setUp_execl_params"),Map.class);
        String name = (String)setUpMap.get("name");
        String sql = (String)setUpMap.get("sql");
        List<String> fieldList = (List<String>)setUpMap.get("fieldList");
        List<String> titleList = (List<String>)setUpMap.get("titleList");


        Map<String, Object> map = queryByAll(sql,paramsMap);
        List<Record> records = (List)map.get("json");
        List<Map<String, Object>> valist= new ArrayList<Map<String, Object>>();
        for(int i=0;i<records.size();i++){
            valist.add(records.get(i).getColumns());
        }

        Map<String, Object> fileName= ExcelUtil.toExecl(name,name,valist,fieldList,titleList);
        renderJson(Util.getResultMapByExecl(fileName));
    }
    public  void  readExecl(){
        String url = getRequest().getServletContext().getRealPath("/")+getPara("url");
        File file = new File(url);
        Map<String, Object> settingMap = JSONSerializer.deserialize(getPara("set"),Map.class);
        List<Map<String,Object>> list= ExcelUtil.readExecl(file,settingMap,settingMap);
        renderJson( Util.getResultMap(Util.state_ok,"成功",list,Util.level_info,Util.model_dev));
    }

    public Map<String, Object> queryByAll( String sql, Map<String, Object> setUpMap){
        Kv kv = Kv.create();
        for (Map.Entry<String, Object> entry : setUpMap.entrySet()) {
            Util.setParaVal(kv,entry.getKey(),entry.getValue()+"");
        }
        SqlPara mysql = Db.getSqlPara(sql,kv);
        return service.queryByAll(mysql);
    }
    public  void  test(){
        String url = "E:\\工作资料\\Template_payment_slip.xlsx";
        String json="[{\"page\":0,\"map\":{\"list\":[{\"row\":0,\"col\":8,\"field\":\"a_id\"},{\"row\":1,\"col\":8,\"field\":\"b_id\"}]},\"list\":{\"min\":2,\"max\":6,\"content\":7,\"list\":[{\"name\":\"班级\",\"field\":\"c_id\"},{\"name\":\"姓名\",\"field\":\"d_id\"},{\"name\":\"学费\",\"field\":\"e_id\"},{\"name\":\"校服\",\"field\":\"f_id\"}]}}]";
        File file = new File(url);
        List<Map<String,Object>>  set = JSONSerializer.deserialize(json,List.class);
        Map<String,Object> list= ExcelUtil2.readExecl1(file,set);
        renderJson( Util.getResultMap(Util.state_ok,"成功",list,Util.level_info,Util.model_dev));
    }
    public  void  test2(){
        String json="[{\"title\":{\"title\":\"嘉善县公安局案管中心案件进出登记表\",\"rowStart\":0,\"colStart\":0,\"rowEnd\":0,\"colEnd\":6},\"map\":{\"list\":[{\"rowStart\":1,\"colStart\":0,\"rowEnd\":1,\"colEnd\":1,\"name\":\"案件编号：\"},{\"rowStart\":2,\"colStart\":0,\"rowEnd\":2,\"colEnd\":1,\"name\":\"入案管时间：\"},{\"rowStart\":3,\"colStart\":0,\"rowEnd\":3,\"colEnd\":1,\"name\":\"案件名称\"},{\"rowStart\":4,\"colStart\":0,\"rowEnd\":6,\"colEnd\":1,\"name\":\"\"},{\"rowStart\":7,\"colStart\":0,\"rowEnd\":7,\"colEnd\":1,\"name\":\"嫌疑人姓名\"},{\"rowStart\":8,\"colStart\":0,\"rowEnd\":10,\"colEnd\":1,\"name\":\"某某\"},{\"rowStart\":1,\"colStart\":2,\"rowEnd\":2,\"colEnd\":4,\"name\":\"承办单位：\"},{\"rowStart\":3,\"colStart\":2,\"rowEnd\":3,\"colEnd\":2,\"name\":\"出案管中心时间\"},{\"rowStart\":3,\"colStart\":3,\"rowEnd\":3,\"colEnd\":3,\"name\":\"事由\"},{\"rowStart\":3,\"colStart\":4,\"rowEnd\":3,\"colEnd\":4,\"name\":\"经办人\"},{\"rowStart\":1,\"colStart\":5,\"rowEnd\":2,\"colEnd\":6,\"name\":\"主办人：\"},{\"rowStart\":3,\"colStart\":5,\"rowEnd\":3,\"colEnd\":5,\"name\":\"进案管中心时间\"},{\"rowStart\":3,\"colStart\":6,\"rowEnd\":3,\"colEnd\":6,\"name\":\"经办人\"}]},\"list\":{\"min\":2,\"max\":9,\"content\":10,\"list\":[{\"name\":\"出案管中心时间\",\"field\":\"b_id\"},{\"name\":\"事由\",\"field\":\"c_id\"},{\"name\":\"经办人\",\"colNum\":4,\"field\":\"d_id\"},{\"name\":\"进案管中心时间\",\"field\":\"e_id\"},{\"name\":\"经办人\",\"colNum\":6,\"field\":\"f_id\"}]}}]";
        String json2="[{\"a_id\":\"a_id\",\"b_id\":\"b_id\",\"c_id\":\"c_id\",\"f_id\":\"f_id\"},{\"a_id\":\"a_id2\",\"b_id\":\"b_id2\",\"c_id\":\"c_id2\",\"f_id\":\"f_id2\"}]";
        List<Map<String,Object>>  set = JSONSerializer.deserialize(json,List.class);
        List<Map<String,Object>>  data = JSONSerializer.deserialize(json2,List.class);
        Map<String,Object> list= ExcelUtil2.createExecl1("111",set,data);
        renderJson( Util.getResultMap(Util.state_ok,"成功",list,Util.level_info,Util.model_dev));
    }
}
