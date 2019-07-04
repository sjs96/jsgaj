package Task;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.Service.BaseService;
import fixed.util.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class MessageInfoTask extends Controller implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Record> list = Db.find(Db.getSql("files.queryByOvertime"));
        if(list!=null&&list.size()>0){
            for (Record r : list){
                if(true){
                    send(r);
                }
            }
        }
    }
    public void send(Record r){
        try{
            String m_name = r.get("f_name");
            String u_id_notify = r.get("u_id_notify");
            List<String> list = JSONSerializer.deserialize(u_id_notify,List.class);
            String tx_message = r.get("tx_message");
            String cs_message = r.get("cs_message");
            int tx_type = r.getInt("tx_type");
            String m_content = "";
            if(tx_message==null||tx_message.length()<1){
                tx_message = "您办理的"+r.get("f_name")+"即将到期，请及时办理。";
            }
            if(cs_message==null||cs_message.length()<1){
                cs_message = "您办理的"+r.get("f_name")+"已超时，请及时办理。";
            }
            if(tx_type==1){
                m_content=tx_message;
            }else{
                m_content=cs_message;
            }
            if(m_content!=null&&m_content.length()>0){
                m_content = m_content.replaceAll("_案卷名称_",r.get("f_name")+"");
            }

            Record record = new Record();
            record.set("m_name", m_name);
            record.set("m_content",m_content);
            record.set("m_time", Util.getCurrentTime());
            record.set("create_time", Util.getCurrentTime());
            record.set("create_user", 0);
            Db.save("fixed_message", record);

            for(int i=0;i<list.size();i++){
                Record user = Db.findFirst(Db.getSql("directories.queryById"),list.get(i));
                if(user!=null&&user.get("d_phone")!=null){
                    MessageUtil2.send(user.get("d_phone")+"",m_content);
                    Record record2 = new Record();
                    record2.set("u_id", list.get(i));
                    record2.set("m_id", record.get("id"));
                    record2.set("send_time", Util.getCurrentTime());
                    record2.set("i_state", 0);
                    record2.set("create_time", Util.getCurrentTime());
                    record2.set("create_user", 0);
                    Db.save("fixed_message_info", record2);
                }
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }

    }
}
