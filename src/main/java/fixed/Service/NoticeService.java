package fixed.Service;

import java.util.HashMap;
import java.util.Map;

public class NoticeService extends BaseService{

    public void saveMessage(int n_type, String n_content){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("n_type",n_type );
        data.put("n_content",n_content );

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","n_id");
        map.put("id","");
        map.put("tableName","fixed_sys_notice");
        map.put("data",data);

        super.save(map);
    }
}
