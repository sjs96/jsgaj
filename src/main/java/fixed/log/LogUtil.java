package fixed.log;

import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import fixed.util.RedisUtil;
import fixed.util.SessionKit;
import fixed.util.Util;
import fixed.util.ValueUtile;

import java.util.*;

public class LogUtil {
    public static void log(Invocation inv) {
        Controller ctrl = inv.getController();
        String log_url= ctrl.getRequest().getRequestURL().toString();
        Map<String,Object> parmMap=new HashMap<String,Object>();
        Map<String,String[]> map= ctrl.getParaMap();
        Set<String> key=map.keySet();
        //参数迭代器
        Iterator<String> iterator = key.iterator();
        while(iterator.hasNext()){
            String k=iterator.next();
            parmMap.put(k, map.get(k)[0]);
        }
        int u_id = 0;
        String token = "";
        try {
            u_id = (int)SessionKit.get().get("user_id");
            token = ValueUtile.getString("token",parmMap);
        }catch (Exception e){
            Util.SOP(e);
        }

        String log_ip = ctrl.getRequest().getRemoteAddr();
        String log_parm = parmMap.toString();
        String log_model = inv.getViewPath().replace("/","");
        String log_method = inv.getMethodName();

        String sql = "insert into fixed_sys_log (u_id,log_ip,log_url,log_parm,log_model,log_method,create_time) values (?,?,?,?,?,?,?)";
        Db.update(sql,u_id,log_ip,log_url,log_parm,log_model,log_method, Util.getCurrentTime());

        System.out.println(u_id);
        System.out.println(log_ip);
        System.out.println(log_url);
        System.out.println(log_parm);
        System.out.println(log_model);
        System.out.println(log_method);
        Map<String,Object> userMap = RedisUtil.getValue(RedisUtil.Login,token);
        if(userMap!=null){
            userMap.put("time",new Date());
            RedisUtil.setValue(RedisUtil.Login,token,userMap,60*60*24*7);
        }
    }

}
