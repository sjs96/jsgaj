package fixed.websocket;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.jfinal.kit.PropKit;

import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebSocket {
    public static EventListenner eventListenner = new EventListenner();
    public static void socket()  {
        Configuration config = new Configuration();
        String port = PropKit.get("websocketPort");
        //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口v
        config.setPort(Integer.parseInt(port));
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        config.setSocketConfig(socketConfig);
       // config.setHostname("localhost");
        config.setHostname("0.0.0.0");
        SocketIOServer server = new SocketIOServer(config);
        server.addListeners(eventListenner);
        server.start();
        System.out.println("WebSocket启动正常");
    }

    /**
     * 向单个登陆用户发送信息(测试)
     */
    public static void sendEvent() {
        Map<String,Object> map2 = new HashMap<String,Object>();
        SimpleDateFormat time = new SimpleDateFormat("yyyy MM dd HH mm ss");
        map2.put("time", time.format(new Date()));
        map2.put("img", "https://ss0.baidu.com/73F1bjeh1BF3odCf/it/u=374501421,1594665386&fm=85&s=5EB28645EE316E0D14A03CD50100D0E2");
        map2.put("sender", "");
        map2.put("sender_name", "");
        map2.put("recipient",  "");
        map2.put("recipient_name",  "");
        map2.put("msg","ccccccc");
      //  eventListenner.clientCache.getClient("U0289").sendEvent("MessageController", map2);
    }
}
