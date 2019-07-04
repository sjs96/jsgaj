package fixed.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import fixed.util.RedisUtil;
import fixed.util.SessionKit;

import java.util.HashMap;
import java.util.Map;


public class SessionInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		Map<String,Object> map = new HashMap<>();
		String token = inv.getController().getPara("token","");
		map.put("session",inv.getController().getSession());
		if(token.length()>0){
			map.put("token",token);
			Map<String, Object> a = RedisUtil.getValue(RedisUtil.Login, token);
			map.put("user_id", RedisUtil.getValue(RedisUtil.Login,token).get("user_id"));
		}else{
			map.put("token","-1");
			map.put("user_id", "-1");
		}

		SessionKit.put(map);
		try {
			inv.invoke();
		}finally {
			SessionKit.remove();
		}
	}
}
