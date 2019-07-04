package fixed.parameter;

import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import fixed.util.Util;

import java.util.Map;

public class ParameterUtil {
    public static Map<String, Object> noEmpty(Invocation inv) {
        String msg="";
        int state = Util.state_ok;
        if(inv.getMethod().isAnnotationPresent(Parameter.Must.class)){
            Parameter.Must annotation = inv.getMethod().getAnnotation(Parameter.Must.class);
            Controller con = inv.getController();
            String[] value = annotation.value();
            for (String v : value) {
                String parameter = con.getPara(v);
                if (parameter == null || parameter.trim().length() == 0) {
                    msg += v+"不能为空";
                    state=Util.state_err;
                }
            }
        }

        return Util.getResultMap(state,msg,null,Util.level_info,Util.model_dev);
    }
}
