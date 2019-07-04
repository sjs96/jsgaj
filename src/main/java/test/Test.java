package test;

import com.jfinal.core.Controller;
import fixed.util.JxlsUtil;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Test extends Controller {

    public static void main(String[] args) throws Exception {
        // 模板路径和输出流
        String templatePath = "E:/新建文件夹/jsgaj.xls";
        OutputStream os = new FileOutputStream("E:/新建文件夹/out.xls");
        // 定义一个Map，往里面放入要在模板中显示数据
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("a1", "1");
        model.put("a2", "2");
        model.put("a3", "3");
        model.put("a4", "4");
        model.put("a5", "5");
        model.put("a6", "6");
        model.put("a7", "7");
        model.put("a8", "8");
        model.put("a9", "9");
        model.put("a10", "10");
        //调用之前写的工具类，传入模板路径，输出流，和装有数据Map
        JxlsUtil.exportExcel(templatePath, os, model);
        os.close();
        System.out.println("完成");
    }
}
