package fixed.websocket;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NotifyBean {
    private String ui_id;
    private String token;
    private String title;
    private String message;
    private Map<String,Object> data;
    private final String type = "notify";

    public String getUi_id() {
        return ui_id;
    }

    public void setUi_id(String ui_id) {
        this.ui_id = ui_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }
    public String getType() {
        return type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotifyBean(String ui_id, String token, String title, String message,Map<String, Object> data){
        this.message = getString(message);
        this.title = getString(title);
        this.ui_id = getString(ui_id);
        this.token = getString(token);
        this.data = getString(data);
    }
    public String getString(String message){
        if(message==null){
            message="";
        }
        try {
            return URLEncoder.encode(message, "UTF-8").replace("+","%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    public Map<String, Object> getString(Map<String, Object> data){
        Map<String, Object> newData = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            newData.put(entry.getKey(),getString(""+entry.getValue()));

        }
        return newData;
    }
}