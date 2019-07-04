package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.MD5Util;
import fixed.util.RedisUtil;
import fixed.util.Util;

import java.util.*;

public class UserService extends BaseService {
    public  static  final  int role_xtgly=1;
    public  static  final  int role_ejxtgly=2;
    public  static  final  int role_fd=3;
    public  static  final  int role_fdgly=4;
    public  static  final  int role_fk=5;
    public  static  final  int role_error=-1;
    public Map<String, Object> savePassword( Object... obj){

        int i = Db.update(Db.getSql("user.savePassword"),  obj);
        if ( i>0){
            return Util.getResultMap(Util.state_ok,"更新密码成功",null,Util.level_info,Util.model_prod);
        }else{
            return Util.getResultMap(Util.state_err,"更新密码失败",null,Util.level_info,Util.model_prod);
        }
    }
    public Map<String, Object> updatePassword( Object... obj){
        int i = Db.update(Db.getSql("user.updatePassword"),  obj);
        if ( i>0){
            return Util.getResultMap(Util.state_ok,"更新密码成功",null,Util.level_info,Util.model_prod);
        }else{
            return Util.getResultMap(Util.state_err,"更新密码失败",null,Util.level_info,Util.model_prod);
        }
    }
    public Map<String, Object> updateInfo( Object... obj){
        int i = Db.update(Db.getSql("user.updateInfo"),  obj);
        if ( i>0){
            return Util.getResultMap(Util.state_ok,"更新信息成功",null,Util.level_info,Util.model_dev);
        }else{
            return Util.getResultMap(Util.state_err,"更新信息失败",null,Util.level_info,Util.model_dev);
        }
    }
    public Map<String, Object> unlock(List<Map<String,Object>> list ){
        int success = 0;
        int error = 0;
        String msg = "";
        for(Map<String,Object> map :list ){
            if(msg.length()>0){
                msg+=",";
            }
            Object ui_id =  map.get("ui_id");
            int  i = Db.update(Db.getSql("user.unlock"),  ui_id);
            if(i>0){
                success++;
            }else{
                error++;
            }
        }
        if(success>0){
            return Util.getResultMap(Util.state_ok,"解锁成功",null,Util.level_info,Util.model_prod);
            //  return Util.getResultMap(Util.state_ok,"成功:"+success+",失败:"+error+"。"+msg,null);
        }else{
            return Util.getResultMap(Util.state_err,"解锁失败",null,Util.level_info,Util.model_prod);
            // return Util.getResultMap(Util.state_err,"操作失败."+msg,null);
        }
    }
    public Map<String, Object> resetPassword(List<Map<String,Object>> list ){
        int success = 0;
        int error = 0;
        String msg = "";
        for(Map<String,Object> map :list ){
            if(msg.length()>0){
                msg+=",";
            }
            Object ui_id =  map.get("ui_id");
            int  i = Db.update(Db.getSql("user.resetPassword"),  MD5Util.MD5("123456"), ui_id);
            if(i>0){
                success++;
            }else{
                error++;
            }
        }
        if(success>0){
            return Util.getResultMap(Util.state_ok,"重置密码成功",null,Util.level_info,Util.model_prod);
          //  return Util.getResultMap(Util.state_ok,"成功:"+success+",失败:"+error+"。"+msg,null);
        }else{
            return Util.getResultMap(Util.state_err,"重置密码失败",null,Util.level_info,Util.model_prod);
           // return Util.getResultMap(Util.state_err,"操作失败."+msg,null);
        }
    }
    public Map<String, Object> saveInfo( Object... obj){

        int i = Db.update(Db.getSql("user.saveInfo"),  obj);
        if ( i>0){
            return Util.getResultMap(Util.state_ok,"更新成功",null,Util.level_info,Util.model_prod);
        }else{
            return Util.getResultMap(Util.state_err,"更新失败",null,Util.level_info,Util.model_prod);
        }
    }
    public Map<String, Object> login(String QueryByIdSql, Object... obj){

        //  验证码 boolean boll = CaptchaRender.validate(this,getPara("validate"));
        // 登录
        Record user = Db.findFirst(QueryByIdSql,obj[0],obj[1]);
        Record lockout = Db.findFirst(Db.getSql("user.lockout"),obj[0]);
        Record base = Db.findFirst(Db.getSql("base.queryByOne"));
        //判断帐号是否锁定
        if(lockout!=null){
            return Util.getResultMap(Util.state_err,"24小时后将自动解锁或请联系系统管理员",null,Util.level_info,Util.model_prod);
        }
        int num = 1;
        if(user!=null){
            // 获取权限
            Map<String,Object> jurisdiction = new HashMap<String,Object>();
            //返回值
            Map<String,Object> userMap = new HashMap<String,Object>();
            userMap.put("ui_name",user.get("ui_name"));
            userMap.put("user_id",user.get("ui_id"));
            userMap.put("jurisdiction",jurisdiction);
            userMap.put("r_name",user.get("r_name"));
            userMap.put("r_id",user.get("r_id"));
            userMap.put("ui_isuser",user.get("ui_isuser"));
            userMap.put("ui_code",user.get("ui_code"));
            userMap.put("ui_landlord_name",user.get("ui_landlord_name"));
            userMap.put("ui_landlord_id",user.get("ui_landlord_id"));
            userMap.put("isCookie",obj[2]);
            userMap.put("time",new Date());
            userMap.put("user_id",user.get("ui_id"));
            String token = UUID.randomUUID().toString();
            userMap.put("token",token);
            RedisUtil.setValue(RedisUtil.Login,token,userMap,60*60*24*7);
            Map<String,Object> userBase = new HashMap<String,Object>();
            userBase.put("role_xtgly",base.get("b_role1"));
            userBase.put("role_ejxtgly",base.get("b_role2"));
            userBase.put("role_fd",base.get("b_role3"));
            userBase.put("role_fdgly",base.get("b_role4"));
            userBase.put("role_fk",base.get("b_role5"));
            userBase.put("role_error",-1);
            userMap.put("base",userBase);
            return Util.getResultMap(Util.state_ok,"登录成功",userMap,Util.level_info,Util.model_prod);

        }else{

            Map<String,Object> error = RedisUtil.getValue(RedisUtil.AccountError,""+obj[0]);
            if(error==null){
                error = new HashMap<String,Object>();
            }
            if(error.containsKey("num")){
                num = Integer.parseInt(error.get("num")+"")+1;
            }
            if(num>4){
                Db.update(Db.getSql("user.AccountError"),  obj[0]);
                RedisUtil.del(RedisUtil.AccountError,""+obj[0]);
            }else{
                error.put("num",num);
                RedisUtil.setValue(RedisUtil.AccountError,""+obj[0],error,60*1);
            }
        }
        return Util.getResultMap(Util.state_err,"帐号或密码错误(错误："+num+"次,剩余"+(5-num)+"次)",null,Util.level_info,Util.model_prod);
    }
    public Map<String, Object> binding(String QueryByIdSql, Object... obj){
        if(Db.update(QueryByIdSql, obj)>0){
            return Util.getResultMap(Util.state_ok,"绑定成功",null,Util.level_info,Util.model_prod);
        }else{
            return Util.getResultMap(Util.state_err,"绑定失败",null,Util.level_info,Util.model_prod);
        }
    }
    public Map<String, Object> weixin_login(String QueryByIdSql, Object... obj){

        //  验证码 boolean boll = CaptchaRender.validate(this,getPara("validate"));
        // 登录
        Record user = Db.findFirst(QueryByIdSql,obj[0]);

        if(user!=null){
            // 获取权限
            Map<String,Object> jurisdiction = new HashMap<String,Object>();
            //返回值
            Map<String,Object> userMap = new HashMap<String,Object>();
            userMap.put("ui_name",user.get("ui_name"));
            userMap.put("jurisdiction",jurisdiction);
            userMap.put("r_name",user.get("r_name"));
            userMap.put("r_id",user.get("r_id"));
            userMap.put("ui_landlord_name",user.get("ui_landlord_name"));
            userMap.put("ui_landlord_id",user.get("ui_landlord_id"));
            userMap.put("isCookie",false);
            userMap.put("time",new Date());
            userMap.put("user_id",user.get("ui_id"));
            String token = UUID.randomUUID().toString();
            userMap.put("token",token);
            RedisUtil.setValue(RedisUtil.Login,token,userMap,60*60*24*7);
            Map<String,Object> userBase = new HashMap<String,Object>();
            userBase.put("role_error",-1);
            userMap.put("base",userBase);
            return Util.getResultMap(Util.state_ok,"登录成功",userMap,Util.level_info,Util.model_prod);

        }
        return Util.getResultMap(Util.state_err,"未绑定帐号",null,Util.level_info,Util.model_prod);
    }


    public Map<String, Object>  save(Map<String,Object> map ,Map<String,Object> map2 ,String ui_code){
        synchronized(this) {
            Map<String, Object> resultMap = super.save(map);
            Record record = Db.findFirst("select * from fixed_sys_directories where d_code=?", ui_code);
            if (record == null) {
                super.save(map2);
            }
            return resultMap;
        }
    }


    public Map<String, Object>  save(Map<String,Object> map,List<Map<String,Object>> list ){
        String id= (String) map.get("id");
            synchronized(this) {
            // 保存
            Map<String, Object> resultMap = super.save(map);
            if(id==null||id.length()<1){
                //获取刚添加的id
                Record user = Db.findFirst(Db.getSql("manager.queryByNew"));
                id = ""+user.get("ui_id");
            }else{
                //删除原来所有数据
                Db.update(Db.getSql("manager.deletePost"),  id);
            }
            for (int i=0;i<list.size();i++){
                Map<String,Object> map1= list.get(i);
                Record record = new Record();
                record.set("ui_id", id);
                record.set("dp_id", map1.get("dp_id"));
                record.set("po_id", map1.get("po_id"));
                Db.save("fixed_sys_user_post", record);
            }
            //保存职务数据

            return resultMap;
        }
    }

}
