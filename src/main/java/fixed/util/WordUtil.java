package fixed.util;

import java.io.*;

import java.util.Map;


import freemarker.template.Configuration;

import freemarker.template.DefaultObjectWrapper;

import freemarker.template.Template;

import freemarker.template.TemplateException;

import freemarker.template.TemplateExceptionHandler;
import sun.misc.BASE64Encoder;

public class WordUtil {
    private Configuration configuration = null;


    public WordUtil() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
    }

    public void createDoc( Map<String, Object> dataMap,String directory,String template,String fileName ) throws UnsupportedEncodingException {

        Configuration configuration = new Configuration();

        Template t=null;

        try {

            configuration.setDirectoryForTemplateLoading(new File(directory));

            configuration.setDefaultEncoding("utf-8");

            configuration.setObjectWrapper(new DefaultObjectWrapper());

            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            t = configuration.getTemplate(template);

        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();

        }



        File outFile = new File(fileName);

        Writer out = null;

        FileOutputStream fos=null;

        try {

            fos = new FileOutputStream(outFile);

            OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");

            out = new BufferedWriter(oWriter);

        } catch (FileNotFoundException e) {
            Util.SOP(e);
            e.printStackTrace();

        }

        try {

            t.process(dataMap, out);

            out.close();

            fos.close();

        } catch (TemplateException e) {
            Util.SOP(e);
            e.printStackTrace();

        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();

        }

    }

    public static String getImageStr(String image)  {
        try {
            InputStream is = new FileInputStream(image);
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] data = new byte[is.available()];
            is.read(data); is.close();
            return encoder.encode(data);
        }catch (Exception e){
            Util.SOP(e);
        }
        return "";
    }

    public static File downloadWord(Map<String, Object> dataMap,String directory,String template,String fileName){
        try {
            WordUtil mdoc = new WordUtil();
            mdoc.createDoc(dataMap,directory,template,fileName );
            return new File(fileName);
        }catch (Exception  e){
            Util.SOP(e);
        }
        return null;
    }

    /*




    public void exportSimpleWord() throws Exception{
        // 要填充的数据, 注意map的key要和word中${xxx}的xxx一致
        MapController<String,String> dataMap = new HashMap<String,String>();
        dataMap.put("username", "张三");
        dataMap.put("sex", "男");

        //Configuration用于读取ftl文件
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");

      *//*以下是两种指定ftl文件所在目录路径的方式, 注意这两种方式都是
       * 指定ftl文件所在目录的路径,而不是ftl文件的路径
       *//*
        //指定路径的第一种方式(根据某个类的相对路径指定)
        //configuration.setClassForTemplateLoading(this.getClass(),"");

        //指定路径的第二种方式,我的路径是C:/a.ftl
        configuration.setDirectoryForTemplateLoading(new File("C:/"));


        // 输出文档路径及名称
        File outFile = new File("D:/test.doc");

        //以utf-8的编码读取ftl文件
        Template t =  configuration.getTemplate("a.ftl","utf-8");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"),10240);
        t.process(dataMap, out);
        out.close();
    }*/
}
