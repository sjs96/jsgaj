package fixed.util;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.*;
import org.apache.axis.*;
import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.rmi.RemoteException;



/**
 *   yunmax
 */
public class MessageUtil {
    public static void main(String[] args) {
    }
    public static boolean send(String msg_id,String phone,String msg,String time)  {
        try {
            String password=Integer.parseInt(phone.substring(phone.length()-4,phone.length()))*3+1121+"";
            String src_tele_num="106573063310";
            String message= add_msg(msg_id,password,src_tele_num,phone,msg,time);
            String ret = send_msg_st( "jsxqxj_jx063310", message);
            if (ret.indexOf("CDATA[0]")!=-1){
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static boolean sendImmediately(String msg_id,String phone,String msg)  {
        try {
            String password=Integer.parseInt(phone.substring(phone.length()-4,phone.length()))*3+1121+"";
            String src_tele_num="106573063310";
            String message= add_msg(msg_id,password,src_tele_num,phone,msg);
            String ret = send_msg( "jsxqxj_jx063310", message);
            if (ret.indexOf("CDATA[0]")!=-1){
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static boolean sendOk(String msg_id)  {
        try {
            String ret =rt_receipt("jsxqxj_jx063310",msg_id);
            if (ret.indexOf("发送成功")!=-1){
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String send_msg(String dbuser,String message) throws Exception {
        String endpoint = "http://111.1.31.119/webservice/services/sendmsg";
        Service service = new Service();
        Call call = null;
        call = (Call) service.createCall();
        call.setOperationName(new QName("http://111.1.31.119/webservice/services/sendmsg", "sendmsg"));
        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        String ret = (String) call.invoke(new Object[] {dbuser,message});
        return ret;
    }

    public static String send_msg_st(String dbuser,String message) throws Exception {
        String endpoint = "http://111.1.31.119/webservice/services/sendmsg";
        Service service = new Service();
        Call call = null;
        call = (Call) service.createCall();
        call.setOperationName(new QName("http://111.1.31.119/webservice/services/sendmsg", "sendmsg_st"));
        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        String ret = (String) call.invoke(new Object[] {dbuser,message});
        return ret;
    }

    public static String deliver(String dbuser) throws Exception {
        String endpoint = "http://111.1.31.119/webservice/services/deliver";
        Service service = new Service();
        Call call = null;
        call = (Call) service.createCall();
        call.setOperationName(new QName("http://111.1.31.119/webservice/services/deliver", "deliver"));
        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        String ret = (String) call.invoke(new Object[] {dbuser});
        return ret;
    }


    public static String receipt_all(String dbuser) throws Exception {
        String endpoint = "http://111.1.31.119/webservice/services/rt_receipt_all";
        Service service = new Service();
        Call call = null;
        call = (Call) service.createCall();
        call.setOperationName(new QName("http://111.1.31.119/webservice/services/rt_receipt_all", "receipt"));
        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        String ret = (String) call.invoke(new Object[] {dbuser});
        return ret;
    }



    public static String rt_receipt(String dbuser,String msg_id) throws Exception {
        String endpoint = "http://111.1.31.119/webservice/services/rt_receipt";
        Service service = new Service();
        Call call = null;
        call = (Call) service.createCall();
        call.setOperationName(new QName("http://111.1.31.119/webservice/services/rt_receipt", "receipt"));
        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        String ret = (String) call.invoke(new Object[] {dbuser,msg_id});
        return ret;
    }

    public static String add_msg(String msg_id,String password,String src_tele_num,String dest_tele_num,String msg) throws Exception {
        String message="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
                "<infos>"+
                "<info>"+
                "<msg_id><![CDATA["+msg_id+"]]></msg_id>"+
                "<password><![CDATA["+password+"]]></password>"+
                "<src_tele_num><![CDATA["+src_tele_num+"]]></src_tele_num>"+
                "<dest_tele_num><![CDATA["+dest_tele_num+"]]></dest_tele_num>"+
                "<msg><![CDATA["+msg+"]]></msg>"+
                "</info>"+
                "</infos>";
        return message;
    }
    public static String add_msg(String msg_id,String password,String src_tele_num,String dest_tele_num,String msg,String time) throws Exception {
        String message="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
                "<infos>"+
                "<info>"+
                "<msg_id><![CDATA["+msg_id+"]]></msg_id>"+
                "<password><![CDATA["+password+"]]></password>"+
                "<src_tele_num><![CDATA["+src_tele_num+"]]></src_tele_num>"+
                "<dest_tele_num><![CDATA["+dest_tele_num+"]]></dest_tele_num>"+
                "<msg><![CDATA["+msg+"]]></msg>"+
                "<send_time><![CDATA["+time+"]]></send_time>"+
                "</info>"+
                "</infos>";
        return message;
    }

}