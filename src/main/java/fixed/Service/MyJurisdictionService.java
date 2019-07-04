package fixed.Service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.SessionKit;
import fixed.util.Util;

/**
 * Created by 15205834418 on 2018/10/22.
 */
public class MyJurisdictionService {
    static UserService service = new UserService();
    public void  jurisdiction(Kv kv ){
System.out.println(SessionKit.get().get("user_id"));
        Record role = Db.findFirst("select ifnull(dp_name,\"\")dp_name,u.dp_id  from fixed_sys_user u left join fixed_sys_department dp on u.dp_id = dp.dp_id where ui_id=?",  SessionKit.get().get("user_id"));
        String name = role.getStr("dp_name");
        String id = role.getStr("dp_id");
        if(name==null||!"嘉善县公安局".equals(name)){
            Util.setParaVal(kv,"my_dp_id",id);
        }
    }
}
