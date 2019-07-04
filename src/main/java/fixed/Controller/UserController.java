package fixed.Controller;

import com.jfinal.captcha.CaptchaRender;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import fixed.Service.UserService;
import fixed.parameter.Parameter;
import fixed.util.*;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController extends Controller{
	static UserService service = new UserService();
	public void toOauth(){
		String calbackUrl=PropKit.get("domain");
		String url= SnsAccessTokenApi.getAuthorizeURL(PropKit.get("appId"), calbackUrl, "111",false);
		redirect(url);
	}


	public void query(){
		String dir = getRequest().getServletContext().getRealPath("/");
		System.out.println(dir);
		System.out.println(getRequest().getServerName());
		Kv kv = Kv.create();
		Util.setParaVal(kv,"ui_code",getPara("ui_code"));
		Util.setParaVal(kv,"ui_name",getPara("ui_name"));
		Util.setParaVal(kv,"ui_telno",getPara("ui_telno"));
		Util.setParaVal(kv,"r_id",getPara("r_id"));
		Util.setParaVal(kv,"dp_id",getPara("dp_id"));
		Util.setParaVal(kv, "tableControlShow", JSONSerializer.deserialize(getPara("tableControlShow"),List.class));
		Util.setParaVal(kv, "fuzzy_query", getPara("fuzzy_query"));
		SqlPara count = Db.getSqlPara("user.queryCount",kv);
		SqlPara query = Db.getSqlPara("user.query",kv);
		renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),query.getPara() ));
	}
	public void queryById(){
		renderJson(service.queryById(Db.getSql("user.queryById"),getPara("default_id_")));
	}
	public void queryBaseById(){
		renderJson(service.queryById(Db.getSql("user.queryBaseById"),SessionKit.get().get("user_id")));
	}

	public void save(){
		Map<String,Object> data2 = new HashMap<String,Object>();
		data2.put("d_name",getPara("ui_name","") );
		data2.put("dp_id",getParaToInt("dp_id",0) );
		data2.put("d_phone",getPara("ui_telno","") );
		data2.put("d_code",getPara("ui_code","") );
		data2.put("d_post","民警");
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("primaryKey","d_id");
		map2.put("id",getPara("d_id"));
		map2.put("tableName","fixed_sys_directories");
		map2.put("data",data2);

		Map<String,Object> data = new HashMap<String,Object>();
		data.put("ui_code", getPara("ui_code","") );
		data.put("ui_nickname", getPara("ui_nickname","") );
		data.put("ui_name", getPara("ui_name","") );
		data.put("ui_telno", getPara("ui_telno","") );
		data.put("ui_fictel", getPara("ui_fictel","") );
		data.put("ui_inside", getPara("ui_inside","") );
		data.put("ui_email", getPara("ui_email","") );
		data.put("ui_sort", getPara("ui_sort","") );
		data.put("ui_isuser", getPara("ui_isuser","") );
		data.put("ui_valid", getPara("ui_valid","") );
		data.put("ui_note", getPara("ui_note","") );
		data.put("ui_photo", getPara("ui_photo","") );
		data.put("dp_id", getParaToInt("dp_id",0) );
		data.put("ui_isuser", getPara("ui_isuser","") );
		data.put("r_id", getParaToInt("r_id",0) );
		if(getPara("ui_id")==null||getPara("ui_id").length()<1){
			data.put("ui_password", MD5Util.MD5(getPara("ui_password","123456")));
			Record user = Db.findFirst(Db.getSql("user.isExistence"),getPara("ui_code",""));
			if(user!=null){
				renderJson(Util.getResultMap(Util.state_err,"帐号已存在",null,Util.level_info,Util.model_prod));
				return;
			}
		}else{
			Record user = Db.findFirst(Db.getSql("user.isExistence2"),getPara("ui_code",""),getPara("ui_id",""));
			if(user!=null){
				renderJson(Util.getResultMap(Util.state_err,"帐号已存在",null,Util.level_info,Util.model_prod));
				return;
			}
		}

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("primaryKey","ui_id");
		map.put("id",getPara("ui_id"));
		map.put("tableName","fixed_sys_user");
		map.put("data",data);

		renderJson(service.save(map,map2,getPara("ui_code","")));
	}
	public void resetPassword(){
		List<Map<String,Object>> ids = JSONSerializer.deserialize(getPara("ui_id"),List.class);
		renderJson(service.resetPassword(ids));
	}
	public void unlock(){
		List<Map<String,Object>> ids = JSONSerializer.deserialize(getPara("ui_id"),List.class);
		renderJson(service.unlock(ids));
	}
	public void getYzm(){
		int yzm = (int)((Math.random()*9+1)*100000);
		Map<String,Object> map = new HashMap<>();
		map.put("yzm",yzm);
		RedisUtil.setValue(RedisUtil.Login,"yzm",map,60*5);
		System.out.println(new Date().getTime());
		MessageUtil.sendImmediately("-1",PropKit.get("yzmPhone"),PropKit.get("yzmMsg")+yzm);
		if(yzm>0){
			renderJson(Util.getResultMap(Util.state_ok,"验证码发送成功",null,Util.level_info,Util.model_prod));
		}else{
			renderJson(Util.getResultMap(Util.state_err,"验证码发送失败",null,Util.level_info,Util.model_prod));
		}
	}
	public void savePassword(){
		synchronized(this){
			Record user = Db.findFirst(Db.getSql("user.isExistence2"),getPara("ui_code",""),SessionKit.get().get("user_id"));
			if(user!=null){
				renderJson(Util.getResultMap(Util.state_err,"帐号已存在",null,Util.level_info,Util.model_prod));
				return;
			}

			String ui_password = getPara("ui_password","");
			Map<String,Object> map = null;
			if(ui_password.length()>0){
				map = service.savePassword(getPara("ui_code",""),getPara("ui_telno",""), getPara("ui_name",""),MD5Util.MD5(getPara("ui_password","")),SessionKit.get().get("user_id"),MD5Util.MD5(getPara("ui_password_old","")));
			}else{
				map = service.saveInfo(getPara("ui_code",""),getPara("ui_telno",""), getPara("ui_name",""),SessionKit.get().get("user_id"),MD5Util.MD5(getPara("ui_password_old","")));
			}
			renderJson(map);
		}
	}
	public void updatePassword(){
		renderJson(service.updatePassword(MD5Util.MD5(getPara("ui_password","")),SessionKit.get().get("user_id"),MD5Util.MD5(getPara("ui_password_old",""))));
	}

	public void updateInfo(){
		renderJson(service.updateInfo(getPara("ui_telno",""), getPara("ui_name",""),SessionKit.get().get("user_id")));
	}
	@Parameter.Must({"default_id_"})
	public void delete(){
		renderJson(service.delete( Db.getSql("user.setDeleteInfo"), Db.getSql("user.delete"),JSONSerializer.deserialize(getPara("default_id_"),List.class)));

	}

	@Parameter.Must({"ui_code","ui_password"})
	public void login(){
		Map<String,Object> map = RedisUtil.getValue(RedisUtil.Login,"yzm");
		renderJson(service.login(Db.getSql("user.login") ,getPara("ui_code"), MD5Util.MD5(getPara("ui_password")),getParaToBoolean("isCookie")));
	}
	public void VerifyCode(){
		CaptchaRender r = new CaptchaRender();
		render(r);
	}
	public void queryByAll(){
		renderJson(service.queryByAll(Db.getSql("user.queryByAll")));
	}
	public String readToString(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (IOException e) {
			Util.SOP(e);
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			Util.SOP(e);
			e.printStackTrace();
			return null;
		}
	}
}
