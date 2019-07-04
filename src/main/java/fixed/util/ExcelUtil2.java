package fixed.util;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * EXCEL报表工具类.
 *
 * @author Jeelon
 */
public class ExcelUtil2 {

	private HSSFWorkbook wb = null;
	private HSSFSheet sheet = null;
	public static String DOUBLE ="double";
	public static String STRING ="String";
	public static String INT ="int";

	private static final String PATH = File.separator+ PropKit.get("execl");
	private static final String RootPath = PathKit.getWebRootPath();
	/*************读取execl*************/

	/**
	 * 第一步获取execl
	 */
	public static Map<String,Object> readExecl1(File file,List<Map<String,Object>> list){
		Map<String,Object> returnList= new HashMap<>();
		String filename = file.getName();
		if(!filename.endsWith(".xls")&&!filename.endsWith(".xlsx")){
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
				for(int ii=0;ii<list.size();ii++){
					returnList = readExecl2(wookbook.getSheetAt(Integer.decode(list.get(ii).get("page")+"")),list.get(ii));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnList;
	}
	/**
	 * 第二步获取工作表
	 */
	public static Map<String,Object> readExecl2(Sheet sheet, Map<String,Object> set) {
		Map<String,Object> returnMap = new HashMap<>();
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		// 获取map
		map = readExecl3(sheet,(Map)set.get("map"));
		//获取list
		list = readExecl4(sheet,(Map)set.get("list"));
		returnMap.put("map",map);
		returnMap.put("list",list);
		return returnMap;
	}
	/**
	 * 第三步获取数据(map)
	 */
	public static Map<String,Object> readExecl3(Sheet sheet,Map<String,Object> set) {
		Map<String,Object> map = new HashMap<>();
		List<Map<String,Object>> list = (List)set.get("list");
		for(int i=0;i<list.size();i++){
			Row row = sheet.getRow((int)list.get(i).get("row"));
			short col = (short)((int)list.get(i).get("col"));
			Cell cell = row.getCell((short)((int)list.get(i).get("col")));
			map.put(list.get(i).get("field")+"",getVal(cell,list.get(i).get("type")+""));
		}
		return map;
	}
	/**
	 * 第四步获取数据(list)
	 */
	public static List<Map<String,Object>> readExecl4(Sheet sheet,Map<String,Object> set) {
		List<Map<String,Object>> list = new ArrayList<>();
		List<Map<String,Object>> setlist = (List)set.get("list");
		int min = (int)set.get("min");
		int max = (int)set.get("max");
		int content = (int)set.get("content");

		//获取表头
		List<Map<String,Object>> newList = fieldOrder(sheet,min,max,setlist);
		//获取内容
		list = getList(sheet, content,newList);
		return list;
	}


	/**
	 *	第四步 获取表头
	 * */
	public static List<Map<String,Object>> fieldOrder(Sheet sheet,int min,int max,List<Map<String,Object>> list){
		int mymin=min;
		//新的参数列表
		List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();
		for(;min<	max;min++){
			Row row = sheet.getRow(min);
			if(row!=null){
				for(int l=0;l<row.getPhysicalNumberOfCells();l++){
					//获取数据的列名
					try {
						String val = getRightTypeCell(row.getCell((short)l)).toString().trim();
						for(int k=0;k<list.size();k++){
							Map<String,Object> key = list.get(k);
							String name = ValueUtile.getString("name", key);
							//判断列名和参数中的映射关系
							if(mymin==min&&key.containsKey("colNum")){
								newList.add(key);
							}else if(name.equals(val)&&!key.containsKey("colNum")){
								key.put("colNum", l);
								newList.add(key);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
		return newList;
	}
	/**
	 *	第四步 获取list
	 * */
	public static List<Map<String,Object>> getList(Sheet sheet,int content,List<Map<String,Object>> list){
		List<Map<String,Object>> returnList= new ArrayList<Map<String,Object>>();
		//获得数据的总行数
		int totalRowNum = sheet.getLastRowNum();
		//循环数据
		for(; content <= totalRowNum ; content++){
			//判断一行有没有数据
			boolean bool = false;
			//获得第i行对象
			Row row = sheet.getRow(content);
			Map<String,Object> newMap = new HashMap<String,Object>();
			for(int l=0;l<list.size();l++){
				Map<String,Object> key = list.get(l);
				Cell cell = row.getCell((short)ValueUtile.getInteger("colNum", key));
				Object va = getVal(cell,key.get("type")+"");
				if((va+"").length()>0){
					bool= true;
				}
				newMap.put(key.get("field")+"", va);
			}
			//只要有一个字段有数据则有数据
			if(bool){
				newMap.put("save_state",0);
				returnList.add(newMap);
			}
		}
		return returnList;
	}

	/**
	 *	从单元格获取数据
	 * */
	public static Object getVal(Cell cell,String type) {
		if(cell==null){
			return "";
		}
		String var = getRightTypeCell(cell).toString();
		if(var!=null){
			if(STRING.equals(type)){
				return var;
			}
			if(INT.equals(type)){
				try {
					return (int)Double.parseDouble(var);
				} catch (Exception e) {
					return 0;
				}
			}
			if(DOUBLE.equals(type)){
				try {
					return Double.parseDouble(var);
				} catch (Exception e) {
					return 0;
				}
			}
		}
		return var;
	}
	public static Object getRightTypeCell(Cell cell){
		Object object = null;
		if(cell==null){
			return "";
		}
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


	/*************生成execl*************/

	/**
	 *	第一步 创建execl
	 * */
	public static Map<String,Object> createExecl1(String name,List<Map<String,Object>> set,List<Map<String,Object>> data){
		String fileName = name+".xls";
		File filePath = new File(RootPath+PATH);
		if (!filePath.exists() && !filePath.isDirectory()) {
			filePath.mkdirs();
		}
		File file  = new File(RootPath+PATH+fileName);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("state",200);
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFCellStyle cellStyle = createStyle(wb);
			for(Map<String,Object> setMap :set){
				createExecl2(wb,setMap,data);
			}
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
	/**
	 *	第二步 创建工作簿
	 * */
	public static void createExecl2(HSSFWorkbook wb,Map<String,Object> setMap,List<Map<String,Object>> data) {
		HSSFSheet sheet = wb.createSheet();

		createExecl3(wb,sheet,(Map)setMap.get("title"));
		createExecl4(wb,sheet,(Map)setMap.get("map"));
		createExecl5(wb,sheet,(Map)setMap.get("list"),data);
	/*	// 创建报表头部
		setTitle(exportExcel, title,fieldList.size()-1);
		//第一行列名和内容
		setRow(2, sheet, cellStyleTitle, valueList,fieldList,titleList);*/
	}

	/**
	 *	第三步 创建标题
	 * */
	public static void createExecl3(HSSFWorkbook wb,HSSFSheet sheet,Map<String,Object> title) {
		HSSFRow row = sheet.createRow(0);
		// 设置第一行
		HSSFCell cell = row.createCell(0);
		// row.setHeight((short) 1000);

		// 定义单元格为字符串类型
		cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
		cell.setCellValue(new HSSFRichTextString(title.get("title")+""));

		// 指定合并区域
		sheet.addMergedRegion(new Region((int)title.get("rowStart"), (short)(int)title.get("colStart"), (int)title.get("rowEnd"),(short)(int)title.get("colEnd")));

		HSSFCellStyle cellStyle = createStyleTitle(wb);
		cell.setCellStyle(cellStyle);
	}
	/**
	 *	第四步 创建map
	 * */
	public static void createExecl4(HSSFWorkbook wb,HSSFSheet sheet,Map<String,Object> mapSet) {
		HSSFCellStyle cellStyle = createStyleTitle(wb);
		List<Map<String,Object>> list = (List)mapSet.get("list");
		Map<String,HSSFRow>rows = new HashMap<>();
		for(Map<String,Object> map :list){
			HSSFRow row = null;
			if(rows.containsKey("row"+(int)map.get("rowStart"))){
				row =rows.get("row"+(int)map.get("rowStart"));
			}else{
				row = sheet.createRow((int)map.get("rowStart"));
				rows.put("row"+(int)map.get("rowStart"),row);
			}
			HSSFCell cell = row.createCell((int)map.get("colStart"));
			cell.setCellStyle(cellStyle);
			cell.setCellValue(new HSSFRichTextString(map.get("name")+""));
			sheet.addMergedRegion(new Region((int)map.get("rowStart"), (short)(int)map.get("colStart"), (int)map.get("rowEnd"),(short)(int)map.get("colEnd")));
		}
	}
	/**
	 *	第无步 创建list
	 * */
	public static void createExecl5(HSSFWorkbook wb,HSSFSheet sheet,Map<String,Object> listSet,List<Map<String,Object>> Data) {
		int min = (int)listSet.get("min");
		int max = (int)listSet.get("max");
		int content = (int)listSet.get("content");
		List<Map<String,Object>> list = (List)listSet.get("list");
		List<Map<String,Object>> newList = fieldOrder(sheet,min,max,list);
		HSSFCellStyle cellStyle = createStyleTitle(wb);
		Map<String,HSSFRow>rows = new HashMap<>();
		int totalRowNum = sheet.getLastRowNum();
		for(Map<String,Object> dataMap :Data){
			HSSFRow row = null;
			if(totalRowNum>=content){
				row = sheet.getRow(content);
			}else{
				row = sheet.createRow(content);
			}
			if(row==null){
				row = sheet.createRow(content);
			}
			for(Map<String,Object> setMap :newList){
				HSSFCell cell = row.createCell((int)setMap.get("colNum"));
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(dataMap.get(setMap.get("field"))+""));
			}
			content++;
		}
	}
	/**
	 *	第一步 创建样式
	 * */
	public static HSSFCellStyle createStyle(HSSFWorkbook wb){
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定当单元格内容显示不下时自动换行

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		return cellStyle;
	}
	/**
	 *	第一步 创建样式
	 * */
	public static HSSFCellStyle createStyleTitle(HSSFWorkbook wb){
		// 定义单元格格式，添加单元格表样式，并添加到工作簿
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行
		return cellStyle;
	}
}