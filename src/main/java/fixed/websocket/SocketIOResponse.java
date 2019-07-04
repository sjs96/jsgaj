package fixed.websocket;

import com.corundumstudio.socketio.SocketIOClient;

public class SocketIOResponse {
    public void sendEvent(SocketIOClient client, MsgBean bean) {
        System.out.println("推送消息");
        client.sendEvent("OnMSG", bean);
    }
}
