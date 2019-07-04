package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadController  extends Controller{
    public void defaultUpload() {
        String defaultUrl = File.separator+PropKit.get("defaultDir");
        String path = getRequest().getServletContext().getRealPath("/");
        createPath(path+defaultUrl);// 如果没有，创建路径
        List<UploadFile> upFiles = getFiles();
        String dir = getPara("dir","");
        String type = getPara("type","");
        List<Map<String, Object>> fileName = defaultUpload(upFiles,defaultUrl,dir,type);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", getPara("name"));
        map.put("list", fileName);
        renderJson(map);
    }
    public List<Map<String, Object>> defaultUpload(List<UploadFile> upFiles,String defaultUrl,String dir,String type) {
        String path = getRequest().getServletContext().getRealPath("/");
        List<Map<String, Object>> fileName = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < upFiles.size(); i++) {
            UploadFile uf = upFiles.get(i);
            if(dir.length()>0){
                createPath(path+defaultUrl+File.separator+dir);// 如果没有，创建路径
                RemoveFile(path+defaultUrl +File.separator+ uf.getFileName(),path+defaultUrl+File.separator+dir);
                dir = defaultUrl+File.separator+dir;
            }else{
                dir = defaultUrl;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", uf.getFileName());
            map.put("url", dir +File.separator+ uf.getFileName());
            fileName.add(map);
            if("img".equals(type)){
                try {
                    createPath(path+dir+File.separator+"thumbnailUrl");
                    Thumbnails.of(path+dir +File.separator+ uf.getFileName()).size(300, 300).toFile(path+dir +File.separator+"thumbnailUrl"+File.separator+ uf.getFileName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }
    public  void createPath(String dir){
        File file = new File(dir);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }
    private boolean RemoveFile(String fileName,String destinationFloderUrl)
    {
        File file = new File(fileName);
        File destFloder = new File(destinationFloderUrl);
        //检查目标路径是否合法
        if(destFloder.exists())
        {
            if(destFloder.isFile())
            {
                return false;
            }
        }else
        {
            if(!destFloder.mkdirs())
            {
                return false;
            }
        }
        //检查源文件是否合法
        if(file.isFile() &&file.exists())
        {
            String destinationFile = destinationFloderUrl+"/"+file.getName();
            if(!file.renameTo(new File(destinationFile)))
            {
                return false;
            }
        }else
        {
            return false;
        }
        return true;
    }
}
