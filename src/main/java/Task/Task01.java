package Task;

import fixed.util.RedisUtil;
import fixed.util.Util;
import fixed.websocket.SignOutBean;
import fixed.websocket.WebSocket;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Task01 implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Map<String,Object>> list =  RedisUtil.getLoginAll();
        for(int i = 0;i<list.size();i++){
            try {
                Map<String,Object> user = list.get(i);
                if(user.containsKey("time")){
                    Date date = (Date)user.get("time");
                    boolean isCookie = (boolean)user.get("isCookie");
                    if(date.getTime() + 30*60*1000<new Date().getTime()&&!isCookie){
                        String ui_id = ""+user.get("user_id");
                        String token = ""+user.get("token");
                        SignOutBean bean = new SignOutBean(ui_id,token, "长时间");
                        WebSocket.eventListenner.clientCache.send(bean);
                        RedisUtil.del(RedisUtil.Login,token);
                    }
                }
            }catch (Exception e){
             //   Util.SOP(e);
            }

        }
    }
}
