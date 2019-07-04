package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.MessageUtil;
import fixed.util.Util;
import fixed.Service.MessageInfoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15205834418 on 2018/8/22.
 */
public class MessageInfoController extends Controller {
    static MessageInfoService service = new MessageInfoService();
    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "m_id", getPara("m_id"));
        Util.setParaVal(kv, "u_name", getPara("u_name"));
        Util.setParaVal(kv, "i_state", getPara("i_state"));
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("messageInfo.queryCount", kv);
        SqlPara query = Db.getSqlPara("messageInfo.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), query.getPara()));
    }


    public void save() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("m_name", getPara("m_name", ""));
        data.put("m_content", getPara("m_content", ""));
        data.put("m_time", getPara("m_time", ""));
        data.put("u_id", getPara("u_id", ""));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "i_id");
        map.put("id", getPara("i_id"));
        map.put("tableName", "fixed_message_info");
        map.put("data", data);

        renderJson(service.save(map));
    }
    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("messageInfo.queryById"), getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete( Db.getSql("messageInfo.setDeleteInfo"), Db.getSql("messageInfo.delete"), JSONSerializer.deserialize(getPara("default_id_"),List.class)));
    }
    @Parameter.Must({"default_id_"})
    public void sendOk() {
        if(MessageUtil.sendOk(getPara("default_id_"))){
            renderJson(Util.getResultMap(Util.state_ok,"发送完成",null, Util.level_info, Util.model_prod));
        }else{
            renderJson(Util.getResultMap(Util.state_ok,"已提交",null, Util.level_info, Util.model_prod));
        }
    }
}
