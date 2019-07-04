package fixed.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class EventListenner {
    public SocketIOClientCache clientCache = new SocketIOClientCache();

    /**
     * 有人连接时，保存到当前登陆的列表
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        clientCache.addClient(client);
    }
    /**
     * 有人退出连接时，删除当前登陆的列表的数据
     * @param client
     */
    @OnDisconnect
    public void OnDisconnect(SocketIOClient client) {
        System.out.println("有人退出");
        System.out.println(clientCache.getClientAll().size());
        clientCache.remove(client);
        System.out.println(clientCache.getClientAll().size());
    }

    /**
     * 聊天
     * @param client
     * @param map
     */
    @OnEvent("chat")
    public void onChat(SocketIOClient client, Map<String, Object> map) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy MM dd HH mm ss");
        map.put("time", time.format(new Date()));
        Map<String, SocketIOClient> clientMap = clientCache.getClientAll();
        for (Map.Entry<String, SocketIOClient> entry : clientMap.entrySet()) {
            try {
                entry.getValue().sendEvent("chat", map);
            } catch (Exception e) {
                System.out.print(e.toString());
            }
        }

    }

    public void sendEvent(SocketIOClient client, MsgBean bean) {
        System.out.println("推送消息");
        client.sendEvent("OnMSG", bean);
    }


    @OnEvent("OnMSG")
    public void onSync(SocketIOClient client, Map<String, Object> map) {
        System.out.println("OnMSG");
        System.out.println(map.get("msg"));

        Map<String, SocketIOClient> clientMap = clientCache.getClientAll();
        for (Map.Entry<String, SocketIOClient> entry : clientMap.entrySet()) {
            try {
                System.out.println(entry.getKey().equals(map.get("from")) + ":" + entry.getKey() + ":" + map.get("from"));
                if (!entry.getKey().equals(map.get("from"))) {
                    map.put("a", URLDecoder.decode("那家", "UTF-8"));
                    map.put("a2", URLDecoder.decode("那家2", "GBK"));
                    entry.getValue().sendEvent("MessageController", map);
                    entry.getValue().sendEvent("MessageController", "j分割jj");
                }
            } catch (Exception e) {
                System.out.print(e.toString());
            }
        }

/*
            clientCache.addClient(client, bean);
            SocketIOClient ioClients = clientCache.getClient(bean.getTo());
            System.out.println("clientCache");
            if (ioClients == null) {
                System.out.println("你发送消息的用户不在线");
                return;
            }
            socketIOResponse.sendEvent(ioClients,bean);*/
    }



}
