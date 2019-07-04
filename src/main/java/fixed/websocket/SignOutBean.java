package fixed.websocket;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SignOutBean {
    private String ui_id;
    private String token;
    private String message;
    private final String type = "signOut";

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


    public String getMessage() {
        return message;
    }
    public String getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SignOutBean(String ui_id, String token, String message){
        this.message = getString(message);
        this.ui_id = getString(ui_id);
        this.token = getString(token);
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

}