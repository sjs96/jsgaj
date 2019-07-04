package fixed.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import fixed.jurisdiction.JurisdictionUtil;
import fixed.log.LogUtil;
import fixed.parameter.ParameterUtil;
import fixed.util.Util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
public class TokenInterceptor extends PrototypeInterceptor {
    public void doIntercept(Invocation inv) {
        System.out.println("拦截器");
        Controller ctrl = inv.getController();
        //日志
        LogUtil.log(inv);

        //权限验证
        Map<String, Object> jurisdiction = JurisdictionUtil.check(inv);

        //参数验证
        Map<String, Object> parameter = ParameterUtil.noEmpty(inv);

        if(!Util.isOk(jurisdiction)){ //身份认证失败
            ctrl.renderJson(jurisdiction);
        }else if(!Util.isOk(parameter)){//参数验证失败
            ctrl.renderJson(parameter);
        }else{
            //认证通过，执行Controller
            inv.invoke();
        }
    }


}
