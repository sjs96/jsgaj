package fixed.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fixed.util.command.MergeCommand;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

/**
 * @描述：jxls模版
 * @author lk
 */
public class JxlsUtil {
	static{
		//添加自定义指令（可覆盖jxls原指令）
        XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
	}
	/** jxls模版文件目录 */
	private final static String TEMPLATE_PATH = "jxlsTemplate";
	/**
	 * 导出excel
	 * @param is - excel文件流
	 * @param os - 生成模版输出流
	 * @param beans - 模版中填充的数据
	 * @throws IOException 
	 */
	public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> beans) throws IOException {
		Context context = new Context();
		if (beans != null) {
			for (String key : beans.keySet()) {
				context.putVar(key, beans.get(key));
			}
		}
		JxlsHelper jxlsHelper = JxlsHelper.getInstance();
		Transformer transformer  = jxlsHelper.createTransformer(is, os);
		JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
		Map<String, Object> funcs = new HashMap<String, Object>();
		funcs.put("jx", new JxlsUtil());	//添加自定义功能
	    evaluator.getJexlEngine().setFunctions(funcs);
		jxlsHelper.processTemplate(context, transformer);
	}

	/**
	 * 导出excel
	 * @param xlsPath excel文件
	 * @param outPath 输出文件
	 * @param beans 模版中填充的数据
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void exportExcel(String xlsPath, String outPath, Map<String, Object> beans) throws FileNotFoundException, IOException {
			exportExcel(new FileInputStream(xlsPath), new FileOutputStream(outPath), beans);
	}
	public static void exportExcel(String templateName, OutputStream os, Map<String, Object> model) throws FileNotFoundException, IOException {
		File template = getTemplate(templateName);
		if(template!=null){
			exportExcel(new FileInputStream(template), os, model);
		}
	}

	/**
	 * 导出excel
	 * @param xls excel文件
	 * @param out 输出文件
	 * @param beans 模版中填充的数据
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void exportExcel(File xls, File out, Map<String, Object> beans) throws FileNotFoundException, IOException {
			exportExcel(new FileInputStream(xls), new FileOutputStream(out), beans);
	}
	/**
	 * 获取jxls模版文件
	 */
	public static File getTemplate(String name){
		//String templatePath = JxlsUtil.class.getClassLoader().getResource(TEMPLATE_PATH).getPath();
		File template = new File( name);
		if(template.exists()){
			return template;
		}
		return null;
	}

	// 日期格式化
	public String dateFmt(Date date, String fmt) {
		if (date == null) {
			return null;
		}
		try {
			SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
			return dateFmt.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 返回第一个不为空的对象
	public Object defaultIfNull(Object... objs) {
		for (Object o : objs) {
			if (o != null)
				return o;
		}
		return null;
	}

	// if判断
	public Object ifelse(boolean b, Object o1, Object o2) {
		return b ? o1 : o2;
	}
}