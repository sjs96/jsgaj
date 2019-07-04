package fixed.websocket;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MsgBean {
    private String from;
    private String to;
    private String content;
    private String title;
    private String message;

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = getString(from);
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = getString(to);
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = getString(content);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = getString(title);
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = getString(message);
    }
    @Override
    public String toString() {
        return "MsgBean [from=" + from + ", to=" + to + ", content=" + content + "]";
    }
    public String getString(String message){
        try {
            return URLEncoder.encode(message, "UTF-8").replace("+","%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
