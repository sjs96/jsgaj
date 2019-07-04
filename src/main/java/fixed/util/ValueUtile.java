package fixed.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ValueUtile {
    /**
     * 判断是否为空
     * @param ret
     * @return
     */
    public static boolean isEmptyOrNull(Object ret) {

        if (ret == null) {
            return true;
        } else if (ret.toString().trim().length() == 0) {
            return true;
        }

        return false;
    }
    /**
     * 判断是否为空
     * @param map
     * @param key
     * @return
     */
    public static boolean isEmptyOrNull(HashMap<String, Object> map, String key) {

        if (map == null) {
            return true;
        } else if (!map.containsKey(key)) {
            return true;
        } else if (map.get(key) == null) {
            return true;
        } else if (map.get(key).toString().trim().length() == 0) {
            return true;
        }

        return false;
    }
    /**
     * 判断是否为空
     * @param map
     * @param key
     * @return
     */
    public static boolean isEmptyOrNull(Map<String, Object> map, String key) {

        if (map == null) {
            return true;
        } else if (!map.containsKey(key)) {
            return true;
        } else if (map.get(key) == null) {
            return true;
        } else if (map.get(key).toString().trim().length() == 0) {
            return true;
        }

        return false;
    }
    /**
     * 获取String值
     * @param key
     * @param request
     * @return
     */
    public static String getString(String key, HttpServletRequest request) {

        String ret = "";

        if (request.getParameter(key) != null && request.getParameter(key).trim().length() > 0) {
            try {
                ret = request.getParameter(key);
            } catch (NumberFormatException e) {
            }
        }

        return ret;
    }
    /**
     * 获取String值
     * @param key
     * @param map
     * @return
     */
    public static String getString(String key, Map<String, Object> map) {

        if (!isEmptyOrNull(map, key)) {
            try {
                return (String) map.get(key);
            } catch (NumberFormatException e) {
            }
        }

        return "";
    }

    /**
     * 获取int值
     *
     * @param key
     * @param request
     * @return
     */
    public static int getInteger(String key, HttpServletRequest request) {

        int ret = 0;

        if (request.getParameter(key) != null && request.getParameter(key).trim().length() > 0) {
            try {
                ret = Integer.parseInt(request.getParameter(key));
            } catch (NumberFormatException e) {
            }
        }

        return ret;
    }
    /**
     * 获取int值
     * @param key
     * @param map
     * @return
     */
    public static int getInteger(String key, Map<String, Object> map) {

        if (!isEmptyOrNull(map, key)) {
            try {
                return Integer.parseInt(map.get(key) + "");
            } catch (NumberFormatException e) {
                Util.SOP(e);
                try {
                    if(map.get(key)!=null&&!"undefined".equals(map.get(key))){
                        double a=(Double)map.get(key);
                        return (int)a;
                    }
                } catch (Exception e1) {
                    Util.SOP(e1);
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }


            }
        }

        return 0;
    }
    /**
     * 获取double值
     * @param key
     * @param request
     * @return
     */
    public static double getDouble(String key, HttpServletRequest request) {

        double ret = 0f;

        if (request.getParameter(key) != null && request.getParameter(key).trim().length() > 0) {
            try {
                ret = Double.parseDouble(request.getParameter(key));
            } catch (Exception e) {
            }
        }

        return ret;
    }
    /**
     * 获取double值
     * @param key
     * @param map
     * @return
     */
    public static double getDouble(String key, Map<String, Object> map) {

        if (!isEmptyOrNull(map, key)) {
            try {
                return Double.parseDouble(map.get(key) + "");
            } catch (NumberFormatException e) {
            }
        }

        return 0;
    }
}
