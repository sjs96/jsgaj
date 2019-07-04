package fixed.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符创处理工具类
 * Created by Administrator on 2016/8/19.
 */
public class StrUtil {
    /** 驼峰命名和下划线命名的互转需要 */
    public static final char UNDERLINE='_';

    /**
     * 转为布尔型
     * @param param 需要转换的布尔字符串
     * @return 转换异常返回null,否则返回布尔值
     */
    public static Boolean toBoolean(String param){
        try {
            return Boolean.valueOf(param);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转为双精度浮点型
     * @param param 需要转换的数字字符串
     * @return 转换异常返回null,否则返回数值
     */
    public static Double toDouble(String param){
        try {
            return Double.valueOf(param);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转为单精度浮点型
     * @param param 需要转换的数字字符串
     * @return 转换异常返回null,否则返回数值
     */
    public static Float toFloat(String param){
        try {
            return Float.valueOf(param);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 转为长整型
     * @param param 需要转换的数字字符串
     * @return 转换异常返回null,否则返回数值
     */
    public static Long toLong(String param){
        try {
            return Long.valueOf(param);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转为整型
     * @param param 需要转换的数字字符串
     * @return 转换异常返回null,否则返回数值
     */
    public static Integer toInt(String param){
        try {
            return Integer.valueOf(param);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 转为短整型
     * @param param 需要转换的数字字符串
     * @return 转换异常返回null,否则返回数值
     */
    public static Short toShort(String param){
        try {
            return Short.valueOf(param);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 判断是否为空
     * @param param
     * @return
     */
    @SuppressWarnings("null")
	public static boolean isEmpty(String param){
        if(param!=null || !"".equals(param.trim())){
            return false;
        }
        return true;
    }

    /**
     * 判断不为空
     * @param param
     * @return
     */
    @SuppressWarnings("null")
	public static boolean isNotEmpty(String param){
        if(param!=null || !"".equals(param.trim())){
            return true;
        }
        return false;
    }

    /**
     * 驼峰式转下划线
     * @param param
     * @return
     */
    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰式 普通方式
     * @param param 下划线字符串
     * @return
     */
    public static String underlineToCamel(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (c==UNDERLINE){
                if (++i<len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰式 使用正则
     * @param param
     * @return
     */
    public static String underlineToCamel2(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        StringBuilder sb=new StringBuilder(param);
        Matcher mc= Pattern.compile("_").matcher(param);
        int i=0;
        while (mc.find()){
            int position=mc.end()-(i++);
            //String.valueOf(Character.toUpperCase(sb.charAt(position)));
            sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());
        }
        return sb.toString();
    }
}
