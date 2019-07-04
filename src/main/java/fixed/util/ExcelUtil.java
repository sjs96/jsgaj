package fixed.util;

import org.apache.poi.ss.usermodel.Cell;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * EXCEL报表工具类.
 *
 * @author Jeelon
 */
public class ExcelUtil {

	private HSSFWorkbook wb = null;
	private HSSFSheet sheet = null;
	public static String DOUBLE ="double";
	public static String STRING ="String";
	public static String INT ="int";
	public static String DATE ="Date";
	public static String DATE_STRING ="DATE_STRING";
	/**
	 * @param wb
	 * @param sheet
	 */
	public ExcelUtil(HSSFWorkbook wb, HSSFSheet sheet) {
		// super();
		this.wb = wb;
		this.sheet = sheet;
	}

	/**
	 * 创建通用EXCEL头部
	 *
	 * @param headString
	 *            头部显示的字符
	 * @param colSum
	 *            该报表的列数
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public void createNormalHead(String headString, int colSum) {
		HSSFRow row = sheet.createRow(0);
		// 设置第一行
		HSSFCell cell = row.createCell(0);
		// row.setHeight((short) 1000);

		// 定义单元格为字符串类型
		cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
		cell.setCellValue(new HSSFRichTextString(headString));

		// 指定合并区域
		/**
		 * public Region(int rowFrom, short colFrom, int rowTo, short colTo)
		 */
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) colSum));

		// 定义单元格格式，添加单元格表样式，并添加到工作簿
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格水平对齐类型
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// font.setFontName("宋体");
		// font.setFontHeight((short) 600);
		// cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * 创建通用报表第二行
	 *
	 * @param params
	 *            统计条件数组
	 * @param colSum
	 *            需要合并到的列索引
	 */
	@SuppressWarnings("deprecation")
	public void createNormalTwoRow(String[] params, int colSum) {
		// 创建第二行
		HSSFRow row1 = sheet.createRow(1);

		row1.setHeight((short) 400);

		HSSFCell cell2 = row1.createCell(0);

		cell2.setCellType(HSSFCell.ENCODING_UTF_16);
		cell2.setCellValue(new HSSFRichTextString("时间：" + params[0] + "至"
				+ params[1]));

		// 指定合并区域
		/**
		 * public Region(int rowFrom, short colFrom, int rowTo, short colTo)
		 */
		sheet.addMergedRegion(new Region(1, (short) 0, 1, (short) colSum));

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);

		cell2.setCellStyle(cellStyle);
	}

	/**
	 * 设置报表标题
	 *
	 * @param columHeader
	 *            标题字符串数组
	 */
	public void createColumHeader(String[] columHeader) {

		// 设置列头 在第三行
		HSSFRow row2 = sheet.createRow(2);

		// 指定行高
		row2.setHeight((short) 600);

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);

		// 设置单元格背景色
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCell cell3 = null;

		for (int i = 0; i < columHeader.length; i++) {
			cell3 = row2.createCell(i);
			cell3.setCellType(HSSFCell.ENCODING_UTF_16);
			cell3.setCellStyle(cellStyle);
			cell3.setCellValue(new HSSFRichTextString(columHeader[i]));
		}
	}

	/**
	 * 创建内容单元格
	 *
	 * @param wb
	 *            HSSFWorkbook
	 * @param row
	 *            HSSFRow
	 * @param col
	 *            short型的列索引
	 * @param align
	 *            对齐方式
	 * @param val
	 *            列值
	 */
	public void cteateCell(HSSFWorkbook wb, HSSFRow row, int col, short align,
						   String val) {
		HSSFCell cell = row.createCell(col);
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(new HSSFRichTextString(val));
		HSSFCellStyle cellstyle = wb.createCellStyle();
		cellstyle.setAlignment(align);
		cell.setCellStyle(cellstyle);
	}

	/**
	 * 创建合计行
	 *
	 * @param colSum
	 *            需要合并到的列索引
	 * @param cellValue
	 */
	@SuppressWarnings("deprecation")
	public void createLastSumRow(int colSum, String[] cellValue) {

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		// 获取工作表最后一行
		HSSFRow lastRow = sheet.createRow((short) (sheet.getLastRowNum() + 1));
		HSSFCell sumCell = lastRow.createCell(0);

		sumCell.setCellValue(new HSSFRichTextString("合计"));
		sumCell.setCellStyle(cellStyle);
		// 合并 最后一行的第零列-最后一行的第一列
		sheet.addMergedRegion(new Region(sheet.getLastRowNum(), (short) 0,
				sheet.getLastRowNum(), (short) colSum));// 指定合并区域

		for (int i = 2; i < (cellValue.length + 2); i++) {
			// 定义最后一行的第三列
			sumCell = lastRow.createCell(i);
			sumCell.setCellStyle(cellStyle);
			// 定义数组 从0开始。
			sumCell.setCellValue(new HSSFRichTextString(cellValue[i - 2]));
		}
	}

	/**
	 * 输入EXCEL文件
	 *
	 * @param fileName
	 *            文件名
	 */
	public void outputExcel(String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(fileName));
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the sheet
	 */
	public HSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet
	 *            the sheet to set
	 */
	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return the wb
	 */
	public HSSFWorkbook getWb() {
		return wb;
	}

	/**
	 * @param wb
	 *            the wb to set
	 */
	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

	private static final String PATH = File.separator+ PropKit.get("execl");
	private static final String RootPath = PathKit.getWebRootPath();
	public static String getTitle(){
		File thumbnail = new File(RootPath+PATH);
		if (!thumbnail.exists() && !thumbnail.isDirectory()) {
			thumbnail.mkdirs();
		}
		Date date = new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date)+"_统计报表.xls";
	}
	//导出
	public static Map<String,Object> toExecl(String path,String title,List<Map<String, Object>> valueList,List<String> fieldList,List<String>titleList ){
		String fileName = path+".xls";
		File filePath = new File(RootPath+PATH);
		if (!filePath.exists() && !filePath.isDirectory()) {
			filePath.mkdirs();
		}
		File file  = new File(RootPath+PATH+fileName);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("state",200);
		try {



			HSSFWorkbook wb = new HSSFWorkbook();

			// 创建单元格样式
			HSSFCellStyle cellStyleTitle = wb.createCellStyle();
			// 指定单元格居中对齐
			cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 指定单元格垂直居中对齐
			cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 指定当单元格内容显示不下时自动换行
			cellStyleTitle.setWrapText(true);
			// ------------------------------------------------------------------
			HSSFCellStyle cellStyle = wb.createCellStyle();
			// 指定单元格居中对齐
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 指定单元格垂直居中对齐
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 指定当单元格内容显示不下时自动换行
			cellStyle.setWrapText(true);
			// ------------------------------------------------------------------
			// 设置单元格字体
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeight((short) 200);
			cellStyleTitle.setFont(font);


			HSSFSheet sheet = wb.createSheet();
			ExcelUtil exportExcel = new ExcelUtil(wb, sheet);


			// 创建报表头部
			setTitle(exportExcel, title,fieldList.size()-1);
			//第一行列名和内容
			setRow(2, sheet, cellStyleTitle, valueList,fieldList,titleList);

			try {

				FileOutputStream fileOutputStreane = new FileOutputStream(file);
				wb.write(fileOutputStreane);
				fileOutputStreane.flush();
				fileOutputStreane.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Output   is   closed ");
			} finally {
			}
			returnMap.put("state",100);
		} catch (Exception e) {
			System.out.println(e.toString());
			// TODO: handle exception
		}

		returnMap.put("url",PATH+fileName);
		returnMap.put("fileName",fileName);

		return returnMap;
	}
	//导出
	public static ByteArrayOutputStream toExecl2(String title,List<Map<String, Object>> valueList,List<String> keyNameList,List<String>keyStrList ,String fileName ){
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			fileName = fileName+".xls";
			fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
			HSSFWorkbook wb = new HSSFWorkbook();

			// 创建单元格样式
			HSSFCellStyle cellStyleTitle = wb.createCellStyle();
			// 指定单元格居中对齐
			cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 指定单元格垂直居中对齐
			cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 指定当单元格内容显示不下时自动换行
			cellStyleTitle.setWrapText(true);
			// ------------------------------------------------------------------
			HSSFCellStyle cellStyle = wb.createCellStyle();
			// 指定单元格居中对齐
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 指定单元格垂直居中对齐
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 指定当单元格内容显示不下时自动换行
			cellStyle.setWrapText(true);
			// ------------------------------------------------------------------
			// 设置单元格字体
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeight((short) 200);
			cellStyleTitle.setFont(font);


			HSSFSheet sheet = wb.createSheet();
			ExcelUtil exportExcel = new ExcelUtil(wb, sheet);


			// 创建报表头部
			setTitle(exportExcel, title,keyNameList.size()-1);
			//第一行列名和内容
			setRow(2, sheet, cellStyleTitle, valueList,keyNameList,keyNameList);
			try {
				wb.write(stream);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Output   is   closed ");
			} finally {
			}
			return stream;
		} catch (Exception e) {
			System.out.println(e.toString());
			// TODO: handle exception
		}
		return null;
	}

	//设置内容
	public static void setRow(int index,HSSFSheet sheet,HSSFCellStyle cellStyleTitle,List<Map<String, Object>> valueList,List<String> fieldList,List<String>titleList){
		if(valueList==null||valueList.size()<1){
			return;
		}
		int row_num = valueList.size();
		setColumnName(sheet.createRow(1), cellStyleTitle, titleList);
		int col_num = fieldList.size();
		for(int i=0;i<row_num;i++){
			HSSFRow row = sheet.createRow(i + 2);
			Map<String, Object> valueMap = valueList.get(i);
			for(int j=0;j<col_num;j++){
				setColumn(row.createCell(j), cellStyleTitle, valueMap.get(fieldList.get(j))+"" );
			}
		}

	}
	/*	//设置内容
        public void setRow(int index,HSSFSheet sheet,HSSFCellStyle cellStyleTitle,List<MapController<String, Object>> valueList){
            if(valueList==null||valueList.size()<1){
                return;
            }
            int row_num = valueList.size();
            List<String> keyList = new ArrayList<String>();
            MapController<String, Object> valueMapOne = valueList.get(0);
            for (String keyName : valueMapOne.keySet()) {
                keyList.add(keyName);
            }
            setColumnName(sheet.createRow(1), cellStyleTitle, keyList);
            int col_num = keyList.size();
            for(int i=0;i<row_num;i++){
                HSSFRow row = sheet.createRow(i + 2);
                MapController<String, Object> valueMap = valueList.get(i);
                for(int j=0;j<col_num;j++){
                    setColumn(row.createCell(j), cellStyleTitle, getString(keyList.get(j), valueMap) );
                }
            }

        }
    */	//设置第一行
	public static void setColumnName(HSSFRow row,HSSFCellStyle cellStyleTitle,List<String> nameList){
		int col_num = nameList.size();
		for(int i=0;i<col_num;i++){
			setColumn(row.createCell(i), cellStyleTitle, nameList.get(i));
		}
	}
	//设置列
	public static void setColumn(HSSFCell cell,HSSFCellStyle cellStyleTitle,String name){
		cell.setCellStyle(cellStyleTitle);
		cell.setCellValue(new HSSFRichTextString(name));
	}
	// 创建报表头部
	public static void setTitle(ExcelUtil exportExcel, String worksheetTitle, int length){
		exportExcel.createNormalHead(worksheetTitle, length);
	}

	//读取导入并且值映射
//读取导入并且值映射
	public static List<Map<String,Object>> readExecl(File file,Map<String,Object> setting,Map<String,Object> valMap){
		List<Map<String,Object>> list  = (List<Map<String,Object>>)valMap.get("setting");
		int num  = Integer.parseInt(valMap.get("num")+"")-1;
		boolean auto  = (boolean)valMap.get("auto");
		List<Map<String,Object>> returnList= new ArrayList<Map<String,Object>>();
		String filename = file.getName();
		if(!filename.endsWith(".xls")&&!filename.endsWith(".xlsx"))
		{
			System.out.println("文件不是excel类型");
			return null;
		}
		if (filename!=null){
			try {
				//判断是否为excel类型文件
				if(!filename.endsWith(".xls")&&!filename.endsWith(".xlsx"))
				{
					System.out.println("文件不是excel类型");
				}
				FileInputStream fis =new FileInputStream(file);
				Workbook wookbook = null;
				try
				{
					//2003版本的excel，用.xls结尾
					wookbook = new HSSFWorkbook(fis);//得到工作簿

				}
				catch (Exception ex)
				{
					//ex.printStackTrace();
					try
					{
						//2007版本的excel，用.xlsx结尾
						FileInputStream fis1 =new FileInputStream(file);
						wookbook = new XSSFWorkbook(fis1);//得到工作簿
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//得到一个工作表
				Sheet sheet = wookbook.getSheetAt(0);
				//获得表头
				Row rowHead = sheet.getRow(0);
				//判断表头是否正确
				if(rowHead.getPhysicalNumberOfCells() != 3)
				{
					//System.out.println("表头的数量不对!");
				}
				List<Map<String,Object>> newList = null;
				//新的参数列表
				if(auto){
					newList = fieldOrder(sheet, list);
				}else{
					newList = list;
				}
				//根据表格获取参数
				returnList = getList(sheet, newList,valMap,num);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnList;
	}
	//封装返回数据
	public static List<Map<String,Object>> getList(Sheet sheet,List<Map<String,Object>> newList,Map<String,Object> valMap,int num){
		Map<String,Object> copyMap = new HashMap<String,Object>();
		List<Map<String,Object>> returnList= new ArrayList<Map<String,Object>>();
		//获得数据的总行数
		int totalRowNum = sheet.getLastRowNum();
		//循环数据
		for(int i = num ; i <= totalRowNum ; i++)
		{
			//判断一行有没有数据
			boolean bool = false;
			//获得第i行对象
			Row row = sheet.getRow(i);

			Map<String,Object> newMap = new HashMap<String,Object>();
			for(int l=0;l<newList.size();l++){
				Map<String,Object> key = newList.get(l);
				boolean mybool = false;
				boolean copy = (boolean)key.get("copy");
				Cell cell = row.getCell((short)ValueUtile.getInteger("colNum", key));
				if(cell==null){
					continue;
				}
				//判断是否存在值
				String valStr = getRightTypeCell(cell).toString().trim();
				if(valStr!=null&&valStr.length()>0){
					bool= true;
					mybool = true;
				}
				//如果返回的参数类型为int
				if(INT.equals(ValueUtile.getString("type", key))){
					int val = getIntegerVal(valMap, cell, key);
					if(copy&&(!mybool)){
						val = ValueUtile.getInteger(ValueUtile.getString("field", key), copyMap);
					}
					newMap.put(ValueUtile.getString("field", key), val);
					copyMap.put(ValueUtile.getString("field", key), val);
				}
				//如果返回的参数类型为String
				else if(STRING.equals(ValueUtile.getString("type", key))){
					String val =getStringVal(valMap, cell, key);
					if(copy&&(!mybool)){
						val = ValueUtile.getString(ValueUtile.getString("field", key), copyMap);
					}
					newMap.put(ValueUtile.getString("field", key), val);
					copyMap.put(ValueUtile.getString("field", key), val);
				}
				//如果返回的参数类型为double
				else if(DOUBLE.equals(ValueUtile.getString("type", key))){
					double val =getDoubleVal(valMap, cell, key);
					if(copy&&(!mybool)){
						val = ValueUtile.getDouble(ValueUtile.getString("field", key), copyMap);
					}
					newMap.put(ValueUtile.getString("field", key), val);
					copyMap.put(ValueUtile.getString("field", key), val);
				}
				//如果返回的参数类型为Date
				else if(DATE.equals(ValueUtile.getString("type", key))){
					Date val =getDateVal(valMap, cell, key);
					newMap.put(ValueUtile.getString("field", key), val);
				}
				//如果返回的参数类型为Date
				else if(DATE_STRING.equals(ValueUtile.getString("type", key))){
					String val =getDateStringVal(valMap, cell, key);
					newMap.put(ValueUtile.getString("field", key), val);
				}
			}
			//只要有一个字段有数据则有数据
			if(bool){
				newMap.put("save_state",0);
				returnList.add(newMap);
			}
		}
		return returnList;
	}
	//获取int值
	public static int getIntegerVal(Map<String,Object> valMap,Cell cell,Map<String,Object> key){
		int val =0;
		try {
			if(valMap !=null&&valMap.containsKey(ValueUtile.getString("field", key))){
				Object obj = getVal(valMap, cell, key);
				if(obj!=null){
					val = Integer.parseInt(obj+"");
				}
			}else{
				val = (int)Double.parseDouble(getRightTypeCell(cell).toString());
			}
		} catch (Exception e) {
		}
		return val;
	}
	//获取double值
	public static double getDoubleVal(Map<String,Object> valMap,Cell cell,Map<String,Object> key){
		double val =0;
		try {
			if(valMap !=null&&valMap.containsKey(ValueUtile.getString("field", key))){
				Object obj = getVal(valMap, cell, key);
				if(obj!=null){
					val = Double.parseDouble(obj+"");
				}
			}else{
				val = Double.parseDouble(getRightTypeCell(cell).toString());
			}
		} catch (Exception e) {
		}
		return val;
	}
	//获取String值
	public static String getStringVal(Map<String,Object> valMap,Cell cell,Map<String,Object> key){
		String val ="";
		try {
			if(valMap !=null&&valMap.containsKey(ValueUtile.getString("field", key))){
				Object obj = getVal(valMap, cell, key);
				if(obj!=null){
					val = (String)obj;
				}
			}else{
				val = getRightTypeCell(cell).toString();
			}
		} catch (Exception e) {
		}
		return val;
	}
	//获取DateString值
	public static String getDateStringVal(Map<String,Object> valMap,Cell cell,Map<String,Object> key){
		String val ="";
		try {
			if(valMap !=null&&valMap.containsKey(ValueUtile.getString("field", key))){
				Object obj = getVal(valMap, cell, key);
				if(obj!=null){
					val = (String)obj;
					val = val.replace("年", "-");
					val = val.replace("月", "-");
					val = val.replace("日", "");
				}
			}else{
				val = getRightTypeCell(cell).toString();
				val = val.replace("年", "-");
				val = val.replace("月", "-");
				val = val.replace("日", "");
			}
		} catch (Exception e) {
		}
		return val;
	}
	//获取Date值
	public static Date getDateVal(Map<String,Object> valMap,Cell cell,Map<String,Object> key){
		Date val =null;
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			if(valMap !=null&&valMap.containsKey(ValueUtile.getString("field", key))){
				Object obj = getVal(valMap, cell, key);
				if(obj!=null){
					String dateStr = (String)obj;
					if (dateStr.indexOf("/") > 0) {
						dateStr = dateStr.replace("/", "-");
					}
					val = sdf.parse(dateStr);
				}
			}else{
				String dateStr = getRightTypeCell(cell).toString();
				if (dateStr.indexOf("/") > 0) {
					dateStr = dateStr.replace("/", "-");
				}
				val = sdf.parse(dateStr);
			}
		} catch (Exception e) {
		}
		return val;
	}
	//获取值
	public static Object getVal(Map<String,Object> valMap,Cell cell,Map<String,Object> key){
		Object val = null;
		Map<String, Object> infoMap = (Map<String, Object>) valMap.get(ValueUtile.getString("field", key));

		//原值key
		String oldKey = ValueUtile.getString("oldKey", infoMap);

		//现值key
		String newKey = ValueUtile.getString("newKey", infoMap);

		//数据
		if(infoMap.get("list")!=null){
			List<Map<String,Object>> valList = (List<Map<String, Object>>) infoMap.get("list");
			for(Map<String,Object> map: valList){
				String var = "";
				if(STRING.equals(infoMap.get("oldType"))){
					var = getRightTypeCell(cell).toString();
				}
				if(INT.equals(infoMap.get("oldType"))){
					var = ""+(int)Double.parseDouble(getRightTypeCell(cell).toString());
				}
				if(DOUBLE.equals(infoMap.get("oldType"))){
					var = ""+Double.parseDouble(getRightTypeCell(cell).toString());
				}
				if(var.equals(map.get(oldKey).toString())){
					val = map.get(newKey);
					break;
				}
			}
		}
		return val;
	}
	//字段排序
	public static List<Map<String,Object>> fieldOrder(Sheet sheet,List<Map<String,Object>> list){
		//表头
		Row row = sheet.getRow(0);
		//新的参数列表
		List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();
		//遍历头
		for(int l=0;l<row.getPhysicalNumberOfCells();l++){
			//获取数据的列名
			Cell cell = row.getCell((short)l);
			if(cell==null){
				continue;
			}
			String val = "";
			try {
				val = getRightTypeCell(cell).toString().trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for(int k=0;k<list.size();k++){
				Map<String,Object> key = list.get(k);
				String name = ValueUtile.getString("name", key);
				//判断列名和参数中的映射关系
				if(name.equals(val)){
					Map<String, Object> map = list.get(k);
					map.put("colNum", l);
					newList.add(map);
					break;
				}
			}
		}
		return newList;
	}
	/**
	 * 导入时基础映射
	 * @param list  	封装到的结果集合
	 * @param name		表头名称
	 * @param field		字段名
	 * @param type		类型
	 */
	public static void setMapping(List<Map<String,Object>> list,String name,String field,String type){
		Map<String,Object> parame = new HashMap<String,Object>();
		parame.put("name", name);
		parame.put("field", field);
		parame.put("type", type);
		list.add(parame);
	}

	/**
	 * 导出时基础映射
	 */
	public static void setMapping2(List<String> fieldList,List<String> titleList,String field,String title){
		fieldList.add(field);
		titleList.add(title);
	}

	/**
	 * 导入时，如果有转化时的映射
	 * @param returnMap  	封装到的结果集合
	 * @param oldKey	 	list 中原来的字段名
	 * @param newKey	 	list 中需要的字段名
	 * @param oldType		原来的类型
	 * @param field			要保存的字段名称
	 * @param list			要映射的数据
	 */
	public static void setSpecialMapping(Map<String,Object> returnMap,String oldKey,String newKey,String oldType,String field,List<Map<String, Object>> list){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("oldKey",oldKey);
		map.put("newKey", newKey);
		map.put("oldType", oldType);
		map.put("list", list);
		returnMap.put(field, map);
	}

	public static Object getRightTypeCell(Cell cell){
		Object object = null;
		switch(cell.getCellType())
		{
			case Cell.CELL_TYPE_STRING :
			{
				object=cell.getStringCellValue();
				break;
			}
			case Cell.CELL_TYPE_NUMERIC :
			{
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				DecimalFormat df = new DecimalFormat("0");
				object = df.format(cell.getNumericCellValue());
				break;
			}
			case Cell.CELL_TYPE_FORMULA :
			{
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				object=cell.getNumericCellValue();
				break;
			}
			case Cell.CELL_TYPE_BLANK :
			{
				cell.setCellType(Cell.CELL_TYPE_BLANK);
				object=cell.getStringCellValue();
				break;
			}
		}
		return object;
	}



}