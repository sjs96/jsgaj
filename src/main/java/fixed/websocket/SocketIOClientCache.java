package fixed.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import fixed.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class SocketIOClientCache{
    //String：EventType类型
    private Map<String,SocketIOClient> clients=new ConcurrentHashMap<String,SocketIOClient>();

    //用户登录时添加
    public void addClient(SocketIOClient client){
        String ui_id = client.getHandshakeData().getSingleUrlParam("ui_id");
        String token = client.getHandshakeData().getSingleUrlParam("token");

      /*  if(clients.containsKey(client.getHandshakeData().getSingleUrlParam("clientid"))  ){
            clients.get(client.getHandshakeData().getSingleUrlParam("clientid")).sendEvent("MessageController","有其他人登录");
        }*/
        System.out.println("ui_id:  "+ui_id+"token:  "+token+"登录");
        clients.put(ui_id+"_:::_"+token,client);
    }

    //用户退出时移除
    public void remove(SocketIOClient client) {
        String ui_id = client.getHandshakeData().getSingleUrlParam("ui_id");
        String token = client.getHandshakeData().getSingleUrlParam("token");
        clients.remove(ui_id+"_:::_"+token);
    }
    //获取所有
    public Map<String,SocketIOClient> getClientAll() {
        return clients;
    }
    public List<SocketIOClient> getClient() {
        List<SocketIOClient> list = new ArrayList<>();
        for (Map.Entry<String,SocketIOClient> entry : clients.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }
    //获取发生的对象
    public  List<SocketIOClient>  getClient(String ui_id) {
        System.out.println("getClient开始");
        List<SocketIOClient> list = new ArrayList<>();
        for (Map.Entry<String,SocketIOClient> entry : clients.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().toString());
            String key =entry.getKey();
            String [] keys = key.split("_:::_");
            if(ui_id.equals(keys[0])){
                list.add(entry.getValue());
            }

        }
        System.out.println("getClient结束");
        return list;
    }
    //获取发生的对象
    public  List<SocketIOClient> getClient(String ui_id,String token) {
        List<SocketIOClient> list = new ArrayList<>();
        list.add(clients.get(ui_id+"_:::_"+token));
        return list;
    }

    public  void send (NotifyBean obj) {
        String ui_id = obj.getUi_id();
        String token = obj.getToken();
        String type  = obj.getType();
        List<SocketIOClient> list = null;
        System.out.println(ui_id);
        System.out.println(token);
        System.out.println(type);
        if(ui_id!=null&&ui_id.length()>0&&token!=null&&token.length()>0){
            System.out.println("111");
            list = getClient(ui_id,token);
        }else if(ui_id!=null&&ui_id.length()>0){
            System.out.println("222");
            list = getClient(ui_id);
        }else{
            System.out.println("333");
            list = getClient();
        }
        System.out.println(list.size());
        System.out.println(list.toString());
        for(int i = 0;i<list.size();i++){
            try {
                System.out.println("发送");
                list.get(i).sendEvent(type, obj);
                System.out.println("发送成功");
            }catch (Exception e){
                System.out.println("发送失败");
                Util.SOP(e);
            }
        }
    }
    public  void send (SignOutBean obj) {
        String ui_id = obj.getUi_id();
        String token = obj.getToken();
        String type  = obj.getType();
        List<SocketIOClient> list = null;
        if(ui_id!=null&&ui_id.length()>0&&token!=null&&token.length()>0){
            list = getClient(ui_id,token);
        }else if(ui_id!=null&&ui_id.length()>0){
            list = getClient(ui_id);
        }else{
            list = getClient();
        }

        for(int i = 0;i<list.size();i++){
            try {
                list.get(i).sendEvent(type, obj);
            }catch (Exception e){
                //Util.SOP(e);
            }
        }
    }


}
