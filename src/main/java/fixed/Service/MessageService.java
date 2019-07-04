package fixed.Service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.util.MessageUtil;
import fixed.util.SessionKit;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/8/22.
 */
public class MessageService extends BaseService {

    //　保存
    public Map<String, Object> save(Map<String,Object> map , List<String> area){
        Map<String,Object> data = (Map<String,Object>) map.get("data");
        String primaryKey = (String) map.get("primaryKey");
        String id = (String) map.get("id");
        String tableName = (String) map.get("tableName");
        if(id==null||id.length()<1){
            Map<String,Object> myid = create(data,tableName,primaryKey);
            Map<String,Object> json = (Map)myid.get("json");
            return createInfo(area,json.get("id")+"",data.get("m_content")+"",data.get("m_time")+"");
        }
        return Util.getResultMap(Util.state_err,"短信无更新功能",null, Util.level_info, Util.model_prod);
    }
    //　保存
    public Map<String, Object> getNum(String name,String content,String time,List<String> area ){
        if(area.size()<1){
            return Util.getResultMap(Util.state_err,"无基站选中",null, Util.level_info, Util.model_prod);
        }
        Kv kv = Kv.create();
        Util.setParaVal(kv, "area",area);
        SqlPara query = Db.getSqlPara("message.queryByMSG", kv);
        List<Record> list = Db.find(query.getSql(), query.getPara());
        Map<String,Object> map = new HashMap<>();
        map.put("msg","任务名称:"+name+"</br>覆盖:"+area.size()+"个基站"+"</br>覆盖:"+list.size()+"个用户"+"</br>发送时间:"+time+"</br>短信内容:"+content);
        if(list.size()<1){
            return Util.getResultMap(Util.state_err,"无用户选中",map, Util.level_info, Util.model_prod);
        }
        return Util.getResultMap(Util.state_ok,"成功",map, Util.level_info, Util.model_dev);
    }

    //添加
    public Map<String, Object>  createInfo(List<String> area ,String m_id, String msg, String time){
        boolean bool = false;
        Kv kv = Kv.create();
        Util.setParaVal(kv, "area",area);
        SqlPara query = Db.getSqlPara("message.queryByMSG", kv);
        List<Record> list = Db.find(query.getSql(), query.getPara());
        for(int i=0;i<list.size();i++){
            Record record = new Record();
            record.set("u_id", list.get(i).get("u_id"));
            record.set("m_id", m_id);
            record.set("i_state", 0);
            record.set("create_time", Util.getCurrentTime());
            record.set("create_user", SessionKit.get().get("user_id"));
            Db.save("sys_message_info", record);
            if(!MessageUtil.send(record.get("id")+"",list.get(i).get("u_name")+"",msg,time)){
                Db.update(Db.getSql("messageInfo.updataStateErr"),  record.get("id")+"");
            }
            bool = true;
        }
        if(list.size()<1){
            return Util.getResultMap(Util.state_err,"",null, Util.level_info, Util.model_prod);
        }
        if(bool){
            return Util.getResultMap(Util.state_ok,"任务开始执行,发送人数"+list.size()+"名",null, Util.level_info, Util.model_prod);
        }
        return Util.getResultMap(Util.state_err_add,"发送失败",null, Util.level_info, Util.model_prod);
    }

}
