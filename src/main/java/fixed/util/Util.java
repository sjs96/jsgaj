package fixed.util;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    //默认值
    public static  int ROWS=10;                                   //分页行数
    public static  int PAGE=1;                                    //分页页数
    public static  int ZERO=0;                                    //默认值

    //返回值状态
    public static final int state_ok = 100;             //正常
    public static final int state_err = 200;            //错误
    public static final int state_err_jurisdiction = 201;            //权限不足
    public static final int state_err_not_logged = 202;            //未登录
    public static final int state_err_bypage = 203;            //分页数据为空
    public static final int state_err_byid = 204;            //查询单个数据
    public static final int state_err_add = 205;            //添加
    public static final int state_err_edit = 206;            //更新
    public static final int state_err_del = 207;            //删除

    //返回
    public static final int model_dev_text = 0;                 //开发测试
    public static final String model_dev = "dev";                      //开发
    public static final String model_prod = "prod";                     //正式
    //返回
    public static final String level_info = "info";
    public static final String level_warning = "warning";
    public static final String level_error = "error";
    public static final String level_success = "success";

    // 设置参数
    public static final void setParaVal(Kv kv,String key,Object value)  {
        if(value==null||value.toString().length()<1){
            return;
        }
        kv.set(key,value);
    }

    // 获取当前时间
    public static final String  getCurrentTime()  {
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(day);
    }

    // 分页
    public static final Map<String, Object>  getResultMapByPage(Page<Record> page)  {
        if(page.getList()!=null&&page.getList().size()>0){
            return getResultMap(state_ok,"有数据",page,level_info,model_dev);
        }
        return getResultMap(state_err_bypage,"暂无数据",page,level_info,model_prod);
    }
    // 全部
    public static final Map<String, Object>  getResultMapByAll(List<Record> list)  {
        if(list!=null&&list.size()>0){
            return getResultMap(state_ok,"有数据",list,level_info,model_dev);
        }
        return getResultMap(state_err_bypage,"暂无数据",list,level_info,model_prod);
    }
    // 查询单个记录
    public static final Map<String, Object>  getResultMapByID(Record page)  {
        if(page!=null){
            return getResultMap(state_ok,"有数据",page,level_info,model_dev);
        }
        return getResultMap(state_err_byid,"暂无数据",page,level_info,model_prod);
    }
    // 添加
    public static final Map<String, Object>  getResultMapByAdd(boolean bool,Object obj)  {
        if(bool){
            return getResultMap(state_ok,"添加成功",obj,level_info,model_prod);
        }
        return getResultMap(state_err_add,"添加失败",obj,level_info,model_prod);
    }
    // 更新
    public static final Map<String, Object>  getResultMapByEdit(boolean bool)  {
        if(bool){
            return getResultMap(state_ok,"更新成功",null,level_info,model_prod);
        }
        return getResultMap(state_err_edit,"更新失败",null,level_info,model_prod);
    }
    // 删除
    public static final Map<String, Object>  getResultMapByDel( int count )  {
        if(count>0){
            return getResultMap(state_ok,"删除成功",null,level_info,model_prod);
        }
        return getResultMap(state_err_del,"删除失败",null,level_info,model_prod);
    }
    //下载
    public static final Map<String, Object>  getResultMapByExecl(Map<String, Object> name)  {
        if(name!=null&& ValueUtile.getInteger("state",name)==100){
            return getResultMap(state_ok,"下载成功",name,level_info,model_prod);
        }
        return getResultMap(state_err,"下载失败",name,level_info,model_prod);
    }
    // 返回值封装
    public static final Map<String, Object>  getResultMap(int state,String msg,Object json,String level,String model)  {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("message", msg);
        resultMap.put("state", state);
        resultMap.put("json", json);
        resultMap.put("level", level);
        resultMap.put("model", model);
        return resultMap;
    }
    // 判断状态是否正常  状态值为100 为正常
    public static final boolean isOk(Map<String, Object> resultMap)  {
        boolean state = false;
        if(Integer.parseInt(resultMap.get("state")+"")==100) {
            state = true;
        }
        return state;
    }
    public static void SOP(Exception e){
        System.out.println(e.toString());
    }
    public static void println(String str){
        System.out.println(str);
    }
    public static void SOP(Exception e,String name){
        System.out.println(name+e.toString());
    }

}
