package fixed.util;
import com.chinamobile.openmas.client.Sms;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import javax.xml.namespace.QName;


/**
 *
 */
public class MessageUtil2 {
    public static void main(String[] args) {
    }
    public static boolean send(String mobile,String message)  {
        try {
            Sms sms = new Sms(PropKit.get("dx_api"));
            String extendCode = PropKit.get("dx_extendCode"); //自定义扩展代码（模块）
            String applicationId= PropKit.get("dx_applicationId");
            String password = PropKit.get("dx_password");
            String dx_send = PropKit.get("dx_send");
            String dx_dev = PropKit.get("dx_dev");
            if("true".equals(dx_dev)){
                mobile = PropKit.get("dx_dev_mobile");
            }

            if(message!=null&&message.length()>0&&mobile!=null&&mobile.length()>0&&"true".equals(dx_send)){
                System.out.println(mobile);
                String messageid = sms.SendMessage(new String[]{mobile}, message, extendCode, applicationId, password);
            }
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}