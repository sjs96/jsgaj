package fixed.Controller;


import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.MenuService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;
import fixed.websocket.NotifyBean;
import fixed.websocket.WebSocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuController extends Controller{
    static MenuService service = new MenuService();

    public void query(){
        Kv kv = Kv.create();
        Util.setParaVal(kv,"menu_parent_id",getPara("menu_parent_id"));
        Util.setParaVal(kv,"menu_valid",getPara("menu_valid"));
        Util.setParaVal(kv,"menu_name",getPara("menu_name"));
        Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
        Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
        SqlPara count = Db.getSqlPara("menu.queryCount",kv);
        SqlPara query = Db.getSqlPara("menu.query",kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("menu_name", getPara("menu_name","") );
        data.put("menu_parent_id",getParaToInt("menu_parent_id",0) );
        data.put("menu_level", getParaToInt("menu_level",1));
        data.put("menu_ifturn",getParaToInt("menu_ifturn",0));
        data.put("menu_valid",getParaToInt("menu_valid",1));
        data.put("menu_imgurl",getPara("menu_imgurl",""));
        data.put("menu_linkurl",getPara("menu_linkurl",""));
        data.put("menu_order",getParaToInt("menu_order",0));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","menu_id");
        map.put("id",getPara("menu_id"));
        map.put("tableName","fixed_sys_menu");
        map.put("data",data);

        List<Map<String,Object>> ids = JSONSerializer.deserialize(getPara("jurisdiction"),List.class);
        renderJson(service.save(map,ids));
    }

    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("menu.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        String s1 = Db.getSql("menu.setDeleteAttributeInfo");
        String s2 = Db.getSql("menu.deleteAttribute");
        String ss1 = Db.getSql("menu.setDeleteInfo");
        String ss2 = Db.getSql("menu.delete");
        service.delete( s1,s2, JSONSerializer.deserialize(getPara("default_id_"),List.class));
        renderJson(service.delete( ss1, ss2, JSONSerializer.deserialize(getPara("default_id_"),List.class)));

    }

    public void queryByFirst(){
        renderJson(service.queryByAll(Db.getSql("menu.queryByFirst")));
    }

    public void getMenuTree(){
        renderJson(service.getMenuTree(getPara("model","")));
    }

    public void getOrderInfo() {
        try {
            System.out.println("try");
            NotifyBean bean = new NotifyBean("" + getParaToInt("id", 0), "", "充值成功", "token:", new HashMap());
            WebSocket.eventListenner.clientCache.send(bean);
            System.out.println("try2");
            System.out.println(WebSocket.eventListenner.clientCache.toString());
        } catch (Exception e) {
            System.out.println("异常");
            Util.SOP(e);
        }
    }


}
