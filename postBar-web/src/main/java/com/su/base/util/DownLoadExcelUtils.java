package com.su.base.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;

import com.su.base.annotation.FlexibleExcel;
import com.su.base.constant.DateConstant;
import com.su.base.helper.ExcelData;
import com.su.base.helper.ExcelHeader;
import com.su.base.helper.FlexibleExcelHelper;
import com.su.base.helper.FlexibleExcelHelper.Attached;
import com.su.base.helper.FlexibleExcelHelper.Column;
import com.su.base.helper.FlexibleExcelHelper.DataMerge;
import com.su.base.helper.FlexibleExcelHelper.DropDownList;
import com.su.base.helper.FlexibleExcelHelper.SpecialCell;
import com.su.base.helper.FlexibleExcelHelper.SpecialRow;
import com.su.base.helper.FlexibleExcelHelper.Title;
/**
 * 下载excel文件的工具类
 * 
 * @Description:
 * @author hxw 
 * @date 2017年7月14日 上午10:53:55
 * @version V1.0
 */
public class DownLoadExcelUtils {
	private final static String SPLIT = "&";//列头连接符order&columnName 解决同样columnName问题
	private final static int MAX_ROW = 15;//报表导入时判断最大行（寻找起始行和列要用）
	private final static int MAX_COLUMN = 10;//报表导入时判断最大列（寻找起始行和列要用）
	private final static String ROW_INDEX = "ROW_INDEX";//起始行key
	private final static String COLUMN_INDEX = "COLUMN_INDEX";//起始列key
	private final static String KEY_REAL_VALUE = "REAL_VALUE";//真实值key
	private final static String KEY_DATA_TYPE = "DATA_TYPE";//数据类型的key
	public final static Map<String, Boolean> hasGuides = new HashMap<>();//是否有指引？主要是用来判断导入之前有没有导出
	/**
	 * 为了统一传参数，限制死了参数的key，每个key对应一个单元格样式，单元格样式可以在这里扩展
	 */
	public static enum STYLE_KEY {
		TITLE// 标题
		, COLUMN// 列头居中
		, COLUMN_LEFT// 列头靠左
		, COLUMN_RIGHT// 列头靠右
		, DATA_STRING_CENTER// 字符串数据格式居中
		, DATA_STRING_LEFT// 字符串数据格式靠左
		, DATA_STRING_RIGHT// 字符串数据格式靠右
		, DATA_NUMBER_NORMAL_LEFT// 数字数据模式数据(小数位不处理)
		, DATA_NUMBER_NORMAL_CENTER// 数字数据模式数据(小数位不处理)
		, DATA_NUMBER_NORMAL_RIGHT// 数字数据模式数据(小数位不处理)
		, DATA_NUMBER_LEFT// 数字数据2位模式数据
		, DATA_NUMBER_CENTER// 数字数据2位模式数据
		, DATA_NUMBER_RIGHT// 数字数据2位模式数据
		, DATA_NUMBER_2_LEFT// 数字数据2位模式数据
		, DATA_NUMBER_2_CENTER// 数字数据2位模式数据
		, DATA_NUMBER_2_RIGHT// 数字数据2位模式数据
		, DATA_NUMBER_3_LEFT// 数字数据3位模式数据
		, DATA_NUMBER_3_CENTER// 数字数据3位模式数据
		, DATA_NUMBER_3_RIGHT// 数字数据3位模式数据
		, DATA_NUMBER_4_LEFT// 数字数据4位模式数据
		, DATA_NUMBER_4_CENTER// 数字数据4位模式数据
		, DATA_NUMBER_4_RIGHT// 数字数据4位模式数据
		, DATA_NUMBER_6_LEFT// 数字数据4位模式数据
		, DATA_NUMBER_6_CENTER// 数字数据4位模式数据
		, DATA_NUMBER_6_RIGHT// 数字数据4位模式数据
		, DATA_NUMBER_8_LEFT// 数字数据8位模式数据
		, DATA_NUMBER_8_CENTER// 数字数据8位模式数据
		, DATA_NUMBER_8_RIGHT// 数字数据8位模式数据
		, DATA_NUMBER_10_LEFT// 数字数据10位模式数据
		, DATA_NUMBER_10_CENTER// 数字数据10位模式数据
		, DATA_NUMBER_10_RIGHT// 数字数据10位模式数据
		, DATA_PERCENT_LEFT// 百分号型2位
		, DATA_PERCENT_CENTER// 百分号型2位
		, DATA_PERCENT_RIGHT// 百分号型2位
		, DATA_PERCENT_2_LEFT// 百分号型2位
		, DATA_PERCENT_2_CENTER// 百分号型2位
		, DATA_PERCENT_2_RIGHT// 百分号型2位
		, BORDER_THIN// 细线框，用来合并
		;
	}
	/**
	 * 数据类型
	 */
	private enum DATA_TYPE {
		STRING, INTEGER, DOUBLE;
	}
	//附属表，主要用来导出公司代码，上报机构，报告日期啥的，很多地方用到
	public static class AttachedMap {
		private String key;//报表日期（key）:2018-10-31(value)
		private String value;
		private Integer keyColNum;//key占行
		private Integer valColNum;//value占行
		private boolean isDouble;//是否数据，模板导出用到，用来控制样式
		
		public AttachedMap() {
			super();
		}
		
		public AttachedMap(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}
		
		public AttachedMap(String key, String value , boolean isDouble) {
			super();
			this.key = key;
			this.value = value;
			this.setDouble(isDouble);
		}
		
		public String getKey() {
			return key;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public Integer getKeyColNum() {
			return keyColNum;
		}
		
		public void setKeyColNum(Integer keyColNum) {
			this.keyColNum = keyColNum;
		}
		
		public Integer getValColNum() {
			return valColNum;
		}
		
		public void setValColNum(Integer valColNum) {
			this.valColNum = valColNum;
		}

		public boolean getIsDouble() {
			return isDouble;
		}

		public void setDouble(boolean isDouble) {
			this.isDouble = isDouble;
		}
		
	}
	/**
	 * 设置基础样式
	 * @param styles  所有的样式（前面已经初始化好了）
	 * @param columnStyle  每列对应的样式（主要一个缓存的功能）
	 * @param row  excel的行
	 * @param columnDetail 该列的配置信息（对应到flexibleExcel的配置信息）  
	 * @param j  excel的列  row + j 确定到了一个cell
	 * @param type 数据类型
	 * @param isColumn 是否列头
	 */
	private static void setStyle(Map<STYLE_KEY, HSSFCellStyle> styles, Map<String, HSSFCellStyle> columnStyle, HSSFRow row,
			ExcelData columnDetail, int j, DATA_TYPE type,boolean isColumn) {
		if (columnStyle == null) {
			columnStyle = new HashMap<>();
		}
		if(isColumn){//是列头的话 直接设置属性
			row.getCell(j).setCellStyle(styles.get(STYLE_KEY.COLUMN));
			return;
		}
		if(columnStyle.get(columnDetail.getField().getName()) == null){
			if (DATA_TYPE.INTEGER == type) {
				if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
					row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_LEFT));
				} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
					row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_CENTER));
				} else {
					row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_RIGHT));
				}	
			} else if (DATA_TYPE.DOUBLE == type) {
				if (columnDetail.isPercent()) {
					if (columnDetail.getFixPoint() == 4) {
						// TODO
					} else if (columnDetail.getFixPoint() == 2) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_PERCENT_2_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_PERCENT_2_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_PERCENT_2_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == 0) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_PERCENT_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_PERCENT_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_PERCENT_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == 10) {
						// TODO
					}
				} else {
					if (columnDetail.getFixPoint() == 4) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_4_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_4_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_4_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == 2) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_2_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_2_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_2_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == 3) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_3_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_3_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_3_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == 0) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == 10) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_10_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_10_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_10_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == 6) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_6_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_6_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_6_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == 8) {
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_8_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_8_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_8_RIGHT));
						}
					} else if (columnDetail.getFixPoint() == -1) {// 不处理小数位
						if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_NORMAL_LEFT));
						} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_NORMAL_CENTER));
						} else {
							row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_NUMBER_NORMAL_RIGHT));
						}
					}
				}
			} else {
				if (HSSFCellStyle.ALIGN_LEFT == columnDetail.getAlign()) {
					row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_LEFT));
				} else if (HSSFCellStyle.ALIGN_CENTER == columnDetail.getAlign()) {
					row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_CENTER));
				} else {
					row.getCell(j).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_RIGHT));
				}
			}
			if(columnDetail.getField().getType().toString().toUpperCase().contains(type.toString())){
				columnStyle.put(columnDetail.getField().getName(), row.getCell(j).getCellStyle());
			}
		} else {
			row.getCell(j).setCellStyle(columnStyle.get(columnDetail.getField().getName()));
		}
		
	}
	/**
	 * 初始化默认样式
	 * @param styles 
	 * @param hssfWorkbook
	 */
	public static void initDefaultStyle(Map<STYLE_KEY, HSSFCellStyle> styles, HSSFWorkbook hssfWorkbook) {
		if (styles == null) {
			styles = new HashMap<STYLE_KEY, HSSFCellStyle>();
		}
		for (STYLE_KEY value : STYLE_KEY.values()) {
			if (styles.get(value) == null) {//
				styles.put(value, getDefaultStyle(value, hssfWorkbook));
			}
		}
	}
	/**
	 * 获取默认的参数
	 * @param value 样式的key
	 * @param hssfWorkbook workBook对象
	 * @return
	 */
	@SuppressWarnings("static-access")
	private static HSSFCellStyle getDefaultStyle(STYLE_KEY value, HSSFWorkbook hssfWorkbook) {
		HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
		if (value == STYLE_KEY.TITLE) {// 居中加粗
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 居中
			HSSFFont font = hssfWorkbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
			font.setFontHeightInPoints((short) 18);// 字体大小
			cellStyle.setFont(font);
		} else if (value == STYLE_KEY.COLUMN) {// 自动换行 + 居中 样式
			HSSFFont font = hssfWorkbook.createFont();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
			// add by hqb fix BUG #10516
			font.setCharSet(HSSFFont.DEFAULT_CHARSET);
			font.setFontName("宋体");
			cellStyle.setWrapText(true);// 换行
			cellStyle.setFont(font);
		} else if (value == STYLE_KEY.COLUMN_LEFT) {// 自动换行 + 居中 样式
			HSSFFont font = hssfWorkbook.createFont();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
			// add by hqb fix BUG #10516
			font.setCharSet(HSSFFont.DEFAULT_CHARSET);
			font.setFontName("宋体");
			cellStyle.setWrapText(true);// 换行
			cellStyle.setFont(font);
		} else if (value == STYLE_KEY.COLUMN_RIGHT) {// 自动换行 + 居中 样式
			HSSFFont font = hssfWorkbook.createFont();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
			// add by hqb fix BUG #10516
			font.setCharSet(HSSFFont.DEFAULT_CHARSET);
			font.setFontName("宋体");
			cellStyle.setWrapText(true);// 换行
			cellStyle.setFont(font);
		} else if (value == STYLE_KEY.DATA_STRING_LEFT) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 靠左
		} else if (value == STYLE_KEY.DATA_STRING_CENTER) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 靠左
		} else if (value == STYLE_KEY.DATA_STRING_RIGHT) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 靠左
		} else if (value == STYLE_KEY.DATA_NUMBER_LEFT) {// 靠右千分位，不保留小数位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_NUMBER_CENTER) {// 靠右千分位，不保留小数位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_NUMBER_RIGHT) {// 靠右千分位，不保留小数位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_NUMBER_2_LEFT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.00"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_NUMBER_2_CENTER) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.00"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_NUMBER_2_RIGHT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.00"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_NUMBER_3_LEFT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_NUMBER_3_CENTER) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_NUMBER_3_RIGHT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_NUMBER_4_LEFT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.0000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_NUMBER_4_CENTER) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.0000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_NUMBER_4_RIGHT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.0000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_NUMBER_6_LEFT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_NUMBER_6_CENTER) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_NUMBER_6_RIGHT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_NUMBER_8_LEFT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.00000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_NUMBER_8_CENTER) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.00000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_NUMBER_8_RIGHT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.00000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_NUMBER_10_LEFT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.0000000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_NUMBER_10_CENTER) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.0000000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_NUMBER_10_RIGHT) {// 靠右千分位，保留两位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("#,##0.0000000000"));// 千分位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_PERCENT_LEFT) {// 带百分号0位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getBuiltinFormat("0%"));// 百分号
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_PERCENT_CENTER) {// 带百分号0位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getBuiltinFormat("0%"));// 百分号
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_PERCENT_RIGHT) {// 带百分号0位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getBuiltinFormat("0%"));// 百分号
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_PERCENT_2_LEFT) {// 带百分号2位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getBuiltinFormat("0.00%"));// 百分号
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_PERCENT_2_CENTER) {// 带百分号2位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getBuiltinFormat("0.00%"));// 百分号
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_PERCENT_2_RIGHT) {// 带百分号2位
			HSSFDataFormat format = hssfWorkbook.createDataFormat();
			cellStyle.setDataFormat(format.getBuiltinFormat("0.00%"));// 百分号
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		} else if (value == STYLE_KEY.DATA_NUMBER_NORMAL_LEFT) {// 靠左千分位，不处理小数位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		} else if (value == STYLE_KEY.DATA_NUMBER_NORMAL_CENTER) {// 居中千分位，不处理小数位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		} else if (value == STYLE_KEY.DATA_NUMBER_NORMAL_RIGHT) {// 靠右千分位，不处理小数位
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		}
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		return cellStyle;
	}
	/**
	 * 导出
	 * @param hssfWorkbook
	 * @param helper 参数对象
	 * @return 上一个报表的列数
	 */
	public static void export(HSSFWorkbook hssfWorkbook, FlexibleExcelHelper helper) {
		if (helper == null || helper.getClazz() == null) {
			throw new IllegalArgumentException("数据导出时clazz不能为空");
		}
		if (hssfWorkbook == null) {
			hssfWorkbook = new HSSFWorkbook();
		}
		int startRow = 0; // 起始行
		String sheetName = helper.getSheetName();
		if (StringUtils.isEmpty(sheetName)) {
			sheetName = "sheet" + Math.ceil((Math.random() * 100 + Math.random() * 100));
		}
		Map<STYLE_KEY, HSSFCellStyle> styles = helper.getStyles();
		if (styles == null) {
			styles = new HashMap<>();
			helper.setStyles(styles);
		}
		boolean isAdd = helper.isAdd();
		// 创建sheet页
		HSSFSheet hssfSheet = hssfWorkbook.getSheet(sheetName);
		//检验是否合法并且获取到开始行
		startRow = checkIsAddAndGetStartRow(helper, startRow, isAdd, hssfSheet);
		if (hssfSheet == null) {
			hssfSheet = hssfWorkbook.createSheet(sheetName);
		}
		//调整数据（后续可能吧所有和参数有关的数据全部调整,目前主要是处理因为导出序号问题而把写死的和并列往后推）
		helper.fixMergeDatas();
		Class<?> clazz = helper.getClazz();
		//设置行高
		hssfSheet.setDefaultRowHeightInPoints(helper.getDefaultRowHight());
		// 1.根绝类型下载统计数据
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length > 0) {
			Map<Integer, ExcelData> dataSetting = new HashMap<>();
			// 初始化样式
			initDefaultStyle(styles, hssfWorkbook);
			// 初始化报表配置信息
			Map<String, ExcelHeader> columnSetting = initSetting(dataSetting, fields, startRow, helper);
			// 导出逻辑
			export( hssfWorkbook,hssfSheet, startRow, dataSetting, columnSetting, helper);
		}
	}
	/**
	 * 检验是否合法并且获取到开始行
	 * @param helper
	 * @param startRow
	 * @param isAdd
	 * @param hssfSheet
	 * @return
	 */
	private static int checkIsAddAndGetStartRow(FlexibleExcelHelper helper, int startRow, boolean isAdd,
			HSSFSheet hssfSheet) {
		if (hssfSheet == null) {
			if (isAdd) {
				throw new IllegalArgumentException("主报表信息未导入");
			}
		} else {
			if (!isAdd) {
				throw new IllegalArgumentException("该报表不存在副表");
			}
			if (helper.isVertical()) {// 上下排版
				startRow = hssfSheet.getLastRowNum() + 1 + helper.getSplitVerticalRow();
			}
			if (helper.isHorizontal()) {// 左右排版
				helper.setStartColumn(helper.getStartColumn() + helper.getSplitHorizontalColumn() + helper.getBeforeColumnCount());
			}
		}
		return startRow;
	}
	
	// 初始化配置信息并且父列头的信息
	private static Map<String, ExcelHeader> initSetting(Map<Integer, ExcelData> setting, Field[] fields, int startRow,
			FlexibleExcelHelper helper) {
		Map<String, ExcelHeader> headers = new HashMap<String, ExcelHeader>();// 使用hashset去重
		boolean isNo = helper.isNo();// 是否有序号
		int type = helper.getType();// 样式类型
		String companyType = helper.getCompanyType();
		int inintStartColumn = helper.getStartColumn();// 开始列
		boolean isGetAll = helper.isGetALl();// 是否导出所有
		ExcelData subDetail;//字段配置信息，和flexible非ancestors 信息对应
		int addColumn = isNo ? 1 : 0;// 如果是带需要导出的话 则自动把所有的column的order+1
		Short defaultAlig = 1 == type ? HSSFCellStyle.ALIGN_CENTER : (2 == type ? HSSFCellStyle.ALIGN_LEFT : (3 == type ? HSSFCellStyle.ALIGN_RIGHT : -99));
		// 判断标题
		Title title = helper.getTitle();
		if (title != null) {
			if (StringUtils.isNotEmpty(title.getTitle())) {// 如果有标题的话则加两行，并且空一行
				startRow += title.getTotalRows();
			}
		}
		// 判断附属信息
		Attached attached = helper.getAttached();
		if (attached != null) {
			if (attached.isHasAttached()) {
				startRow += attached.getTotalRows();
			}
		}
		// 判断列头信息
		Column column = helper.getColumn();
		if (column != null) {
			Map<Integer, Boolean> hides = helper.getHides();// 隐藏列;
			startRow += column.getSplitRow();
			int curRow = startRow;
			ExcelHeader header;//父列信息，和flexible ancestors 信息对应
			int columnCount = 0;
			boolean needAdd;
			for (Field field : fields) {
				// 1.判断字段是否有FlexibleExcel注解
				FlexibleExcel excel = field.getAnnotation(FlexibleExcel.class);
				if (excel == null) {
					continue;
				}
				needAdd = excel.add();
				if (StringUtils.isNoneBlank(companyType) && StringUtils.isNoneBlank(excel.specialPercent())) {
					needAdd = companyType.equals(excel.specialPercent());
				}
				if (!(isGetAll || needAdd)) {// 如果不用导出并且不用获取所有时过滤掉改字段
					continue;
				}
				columnCount++;
				// 2.读取和保存信息
				Integer order = excel.order() + inintStartColumn + addColumn;// 从0开始，获取该字段的位置，并且此字段的位置只能保证有一个
				for(Entry<Integer, Boolean> entry : hides.entrySet()){//如果value为true的话  则考虑为隐藏  并且不往后移动
					if(!entry.getValue() && entry.getKey() <= order){
						order++;
					}
				}
				// 填充数据列信息
				subDetail = new ExcelData();
				subDetail.setField(field);// 字段信息
				subDetail.setWidth(excel.width());// 列宽度
				subDetail.setPercent(excel.isPercent());// 是否百分比
				subDetail.setFixPoint(excel.fixPoint());// 保留位数
//				if (!excel.isPercent() && StringUtils.isNotBlank(excel.specialPercent())) {
//					if (EnumConst.YES_NO.YES.getSCode().equals(SysConfigUtil.readProperty(excel.specialPercent()))) {
//						subDetail.setPercent(true);// 是否百分比//
//						subDetail.setFixPoint(subDetail.getFixPoint() - 2);// 保留位数
//					}
//				}
				subDetail.setIstran(excel.isTran());//是否转换
				subDetail.setTranOra(excel.tranOra());//转换元数据
				subDetail.setTranTar(excel.tranTar());//转换目标数据
				subDetail.setTimes(excel.times());//倍数
				subDetail.setCharDefault(excel.charDefault());//字符和日期默认值
				subDetail.setNumDefault(excel.numDefault());//数字默认值
				subDetail.setAlign(defaultAlig == -99 ? excel.align() : defaultAlig.shortValue());//靠左靠右样式
				subDetail.setDateType(excel.dateType());//日期显示格式
				subDetail.setGuide(excel.guide());//是否导出指引
				subDetail.setMergeByValue(excel.isMergeByValue());//是否通过值合并
				subDetail.setDataColors(excel.dataColors());//字体样式表达式
				subDetail.setBackGroundColors(excel.backGroundColors());//背景色表达式
				subDetail.setOrder(order);
				setting.put(order, subDetail);// 将位置和配置信息绑定起来
				// 优先记录父列头信息并且记录当前列的行宽度
				String[] ancestors = excel.ancestors();
				if (ancestors != null && ancestors.length > 0) {
					int level = ancestors.length;// 记录祖先的长度，确保祖先的长度和祖先配置信息一致
					// 获取父列对应的占宽
					int[] acceRowRange = excel.acceRowRange();// 一般ancestors和acceRowRange的长度是一样的
					int[] acceColumnRange = excel.acceColumnRange();
					if (acceColumnRange.length != level || acceRowRange.length != level) {
						throw new IllegalArgumentException("祖先列对应的配置信息不全");
					}
					for (int i = 0; i < level; i++) {// 遍历祖先从最上级祖先开始
						// 把特殊的字符替换掉YYYY和MM
						if (ancestors[i].contains("<YYYY>")) {
							ancestors[i] = ancestors[i].replaceAll("<YYYY>", helper.getYear() + "");
						}
						if (ancestors[i].contains("<MM>")) {
							ancestors[i] = ancestors[i].replaceAll("<MM>", helper.getMonth() + "");
						}
						if (ancestors[i].contains("<QQ>")) {
							ancestors[i] = ancestors[i].replaceAll("<QQ>", helper.getSeason() + "");
						}
						if (ancestors[i].contains("<DD>")) {
							ancestors[i] = ancestors[i].replaceAll("<DD>", helper.getDay() + "");
						}
						for (Entry<String, String> entry : helper.getReplaceMap().entrySet()) {
							if (ancestors[i].contains(entry.getKey())) {
								ancestors[i] = ancestors[i].replaceAll(entry.getKey(), entry.getValue());
							}
						}
						if (headers.keySet().contains(ancestors[i])) {// 如果该组先列被配置过多个，则取起始列最小
							header = headers.get(ancestors[i]);
							header.setStartColumn(Math.min(header.getStartColumn(), order));// 如果之前放了的话 起始列要最小的
						} else {
							header = new ExcelHeader();
							header.setColumn(ancestors[i]);// 名字
							header.setStartColumn(order);// 起始列，当前的order
							header.setColumnRange(acceColumnRange[i]);// 列宽
							header.setStartRow(curRow);//开始行
							header.setRowRange(acceRowRange[i]);// 行宽
							headers.put(ancestors[i], header);
						}
						// 让当前行等于当前header的endRow
						curRow = header.getEndRow() + 1;
					}
				}
				// 记录最底层列头信息
				header = new ExcelHeader();//
				String columnName = excel.column();
				if (columnName.contains("<YYYY>")) {
					columnName = columnName.replaceAll("<YYYY>", helper.getYear() + "");
				}
				if (columnName.contains("<MM>")) {
					columnName = columnName.replaceAll("<MM>", helper.getMonth() + "");
				}
				if (columnName.contains("<QQ>")) {
					columnName = columnName.replaceAll("<QQ>", helper.getSeason() + "");
				}
				if (columnName.contains("<DD>")) {
					columnName = columnName.replaceAll("<DD>", helper.getDay() + "");
				}
				for (Entry<String, String> entry : helper.getReplaceMap().entrySet()) {
					if (columnName.contains(entry.getKey())) {
						columnName = columnName.replaceAll(entry.getKey(), entry.getValue());
					}
				}
				if(StringUtils.isNotEmpty(columnName)){
					header.setColumn(columnName);//列头名
					header.setStartColumn(order);//开始列
					header.setColumnRange(excel.columnRange());//列宽
					header.setStartRow(curRow);//起始行
					header.setRowRange(excel.rowRange());//行宽
					headers.put(header.getColumn() + SPLIT + order, header);//split主要解决相同父列名问题
				}
				// 每一列最初的当前行都是起始行
				curRow = startRow;
			}
			for(Entry<Integer, Boolean> entry : hides.entrySet()){
				if(!entry.getValue()){
					columnCount++;
				}
			}
			columnCount += addColumn;
			helper.setNowColumnCount(columnCount);
		}
		return headers;
	}
	
	// 下载逻辑
	private static <T> void export(HSSFWorkbook hssfWorkbook,HSSFSheet sheet, int initStartRow, Map<Integer, ExcelData> dataSetting,
			Map<String, ExcelHeader> headers, FlexibleExcelHelper helper) {
		int initStartColumn = helper.getStartColumn();// 起始列
		Title titleInfo = helper.getTitle();// 标题信息
		Map<Integer, Boolean> hides = helper.getHides();// 隐藏列信息
		Attached attached = helper.getAttached();// 附属信息
		int nowRow = initStartRow;//当前所在行
		Map<STYLE_KEY, HSSFCellStyle> styles = helper.getStyles();
		int size = helper.getNowColumnCount();//总共多少列
		// 1.隐藏需要隐藏的列
		for(Entry<Integer, Boolean> entry : hides.entrySet()){
			sheet.setColumnHidden(entry.getKey(), true);//设置隐藏列
		}
		HSSFRow row;
		for(Integer i : dataSetting.keySet()){
			sheet.setColumnWidth(i, dataSetting.get(i).getWidth() * 256);//设置列宽
		}
		// 1.导出标题
		if (titleInfo != null && StringUtils.isNotEmpty(titleInfo.getTitle())) {
			styles.get(STYLE_KEY.TITLE).setAlignment(titleInfo.getAlign());
			int titleColumnRange = titleInfo.getColumnRange() == null ? size : titleInfo.getColumnRange();
			String title = titleInfo.getTitle() == null ? "" : titleInfo.getTitle();
			int titleRowRange = titleInfo.getRowRange();
			nowRow += titleInfo.getSplitRow();
			if (title.contains("<YYYY>")) {
				title = title.replaceAll("<YYYY>", helper.getYear() + "");
			}
			if (title.contains("<MM>")) {
				title = title.replaceAll("<MM>", helper.getMonth() + "");
			}
			if (title.contains("<QQ>")) {
				title = title.replaceAll("<QQ>", helper.getSeason() + "");
			}
			if (title.contains("<DD>")) {
				title = title.replaceAll("<DD>", helper.getDay() + "");
			}
			row = sheet.getRow(nowRow);
			if (row == null) {
				row = sheet.createRow(nowRow);// 创建一行
			}
			// 先合并
			sheet.addMergedRegion(new CellRangeAddress(nowRow, nowRow + titleRowRange - 1, initStartColumn,initStartColumn + titleColumnRange - 1));
			if (title.contains("\r\n")){
				row.setHeight((short) 1200);
			}
			// 设置样式
			for (int j = 0; j < titleRowRange; j++) {
				row = sheet.getRow(nowRow + j);
				if (row == null) {
					row = sheet.createRow(nowRow + j);// 创建一行
				}
				for (int i = initStartColumn; i < titleColumnRange + initStartColumn; i++) {// 设置其他格为无边款模式
					row.createCell(i).setCellStyle(styles.get(STYLE_KEY.BORDER_THIN));
				}
			}
			// 赋值
			row = sheet.getRow(nowRow);
			if (row == null) {
				row = sheet.createRow(nowRow);// 创建一行
			}
			if (title.contains("\r\n")) {
				HSSFCellStyle titleStyle = styles.get(STYLE_KEY.TITLE);
				titleStyle.setWrapText(true);
				styles.put(STYLE_KEY.TITLE, titleStyle);
				row.createCell(initStartColumn).setCellValue(new HSSFRichTextString(title));
			} else {
				row.createCell(initStartColumn).setCellValue(title);
			}
			row.getCell(initStartColumn).setCellStyle(styles.get(STYLE_KEY.TITLE));
			nowRow += titleRowRange;
		}
		// 2.填充附属信息
		if (attached != null && attached.isHasAttached()) {// 如果是新框架的话 需要再加一行
			nowRow += attached.getSplitRow();
			row = sheet.getRow(nowRow);
			if (row == null) {
				row = sheet.createRow(nowRow);// 创建第二行
			}
			List<AttachedMap> attachedDatas = attached.getDatas();
			// 填充公司信息（此栏不需要列头）
			if (!CollectionUtils.isEmpty(attachedDatas)) {
				for (AttachedMap attachedMap : attachedDatas) {
					row = sheet.getRow(nowRow);
					if (row == null) {
						row = sheet.createRow(nowRow);
					}
					Integer keyColNum = null != attachedMap.getKeyColNum() ? attachedMap.getKeyColNum() : 0;// key字段占的列数
					Integer valColNum = null != attachedMap.getValColNum() ? attachedMap.getValColNum() : 0;// val字段占的列数
					if (null != keyColNum && keyColNum > 0) {
						HSSFCell cell = row.createCell(initStartColumn);
						cell.setCellValue(attachedMap.getKey());
						row.getCell(initStartColumn).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_LEFT));
						for (int i = initStartColumn + 1; i < keyColNum + initStartColumn; i++) {
							row.createCell(i).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_LEFT));
						}
						sheet.addMergedRegion(new CellRangeAddress(nowRow, nowRow, initStartColumn, initStartColumn + keyColNum - 1));
					} else {
						row.createCell(initStartColumn).setCellValue(attachedMap.getKey());
						row.getCell(initStartColumn).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_LEFT));
					}
					int last = row.getLastCellNum();
					int valCellColSerial = initStartColumn + 1 + (keyColNum > 0 ? keyColNum - 1 : keyColNum) + (valColNum > 0 ? valColNum - 1 : valColNum);
					if (null != valColNum && valColNum > 0) {
						HSSFCell cell = row.createCell(last);
						cell.setCellValue(attachedMap.getKey());
						row.getCell(last).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_LEFT));
						for (int i = last + 1; i < valColNum + last; i++) {
							row.createCell(i).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_LEFT));
						}
						sheet.addMergedRegion(new CellRangeAddress(nowRow, nowRow, last, last + valColNum - 1));
					} else {
						row.createCell(valCellColSerial).setCellValue(attachedMap.getValue());
						row.getCell(valCellColSerial).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_LEFT));
					}
					nowRow++;
				}
			}
		}
		// 3.填充列头
		nowRow = fixColumn(sheet, headers, size, nowRow, helper);
		// 4.填充数据
		nowRow = fixDatas(hssfWorkbook,sheet, dataSetting, size, nowRow, helper);
		// 5.补充特殊行
		fixSpecialColumn(sheet, helper);
		// 6.设置下拉框
		addDropDownList(sheet, helper);
	}
	/**
	 * 加入下拉框
	 */
	private static void addDropDownList(HSSFSheet sheet,FlexibleExcelHelper helper){
		List<DropDownList> dropDownLists = helper.getDropDownList();
		if (dropDownLists!=null && dropDownLists.size()>0) {
			if (helper.getIsCascade()) {
				//级联下拉框创建新的sheet并隐藏显示,外部处理
			}else{
				for (DropDownList dropDownData : dropDownLists) {
					// 加载下拉列表内容
					DVConstraint constraint = DVConstraint.createExplicitListConstraint(dropDownData.getTextList());
					// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
					CellRangeAddressList regions = new CellRangeAddressList(dropDownData.getStartRow(),dropDownData.getEndRow(), dropDownData.getStartCol(), dropDownData.getEndCol());
					// 数据有效性对象
					HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
					sheet.addValidationData(data_validation_list);
				}
			}
		}
	}
	
	/**
	 * 加入特殊行
	 */
	private static void fixSpecialColumn(HSSFSheet sheet, FlexibleExcelHelper helper) {
		List<SpecialRow> specialRows = helper.getSpecialRows();
		Map<STYLE_KEY, HSSFCellStyle> styles = helper.getStyles();
		List<SpecialCell> specialCells = helper.getSpecialCells();
		if (specialRows != null && specialRows.size() > 0) {
			for (SpecialRow specialRow : specialRows) {
				HSSFRow spRow = sheet.getRow(specialRow.getSplitRow());
				if (spRow == null){
					spRow = sheet.createRow(specialRow.getSplitRow());
				}
				HSSFCell spCell = null;
				int currentCol = specialRow.getSplitCol();
				HSSFCellStyle cellStyle = specialRow.getCellStyle();
				if (null != specialRow.getRowData()) {
					for (String val : specialRow.getRowData()) {
						spCell = spRow.createCell(currentCol);
						spCell.setCellValue(val);
						if (specialRow.getIsCellStyle()) {
							if (cellStyle != null) {
								spCell.setCellStyle(cellStyle);
							} else {
								spCell.setCellStyle(styles.get(STYLE_KEY.DATA_STRING_LEFT));
							}
						}
						currentCol++;
					}
				}
			}
			
		}
		if(CollectionUtils.isEmpty(specialCells)){
			HSSFRow row;
			HSSFCell cell;
			for(SpecialCell specialCell : specialCells){
				row = sheet.getRow(specialCell.getRow());
				if(row == null){
					row = sheet.createRow(specialCell.getRow());
				}
				cell = row.createCell(specialCell.getColumn());
				cell.setCellValue(specialCell.getValue());
				//合并列
				sheet.addMergedRegion(new CellRangeAddress(specialCell.getRow(), specialCell.getEndRow(), specialCell.getColumn(), specialCell.getEndColumn()));
				for(int i = specialCell.getRow();i <= specialCell.getEndRow(); i ++){
					row = sheet.getRow(i) == null ? sheet.createRow(i) : sheet.getRow(i);
					for(int j = specialCell.getColumn();j <= specialCell.getEndColumn(); j ++){
						cell = row.getCell(j) == null ? row.createCell(j) : row.getCell(j);
						cell.setCellStyle(styles.get(specialCell.getStyleKey()));
					}
				}
			}
		}
	}
	// 填充列头
	private static int fixColumn(HSSFSheet sheet, Map<String, ExcelHeader> headers, int size, int nowRow,
			FlexibleExcelHelper helper) {
		Column column = helper.getColumn();
		boolean isNo = helper.isNo();
		int maxRow = nowRow;
		int initStartColumn = helper.getStartColumn();// 起始列
		int maxColumn = initStartColumn;
		Map<STYLE_KEY, HSSFCellStyle> styles = helper.getStyles();
		if (column != null) {
			nowRow += column.getSplitRow();
			if(!column.isNoColumn()){
				Map<Integer, HSSFRow> record = new HashMap<>();
				HSSFRow row;
				Integer startRow = null;// 并不是记录当前行 而是记录每一个单元格开始的行
				ExcelHeader header;
				HSSFCell cell;
				// for循环设置列头的字样和属性
				for (Entry<String, ExcelHeader> entry : headers.entrySet()) {
					header = entry.getValue();
					startRow = header.getStartRow();
					if (record.get(startRow) != null) {
						row = record.get(startRow);
					} else {
						row = sheet.getRow(startRow);
						if (row == null) {
							row = sheet.createRow(startRow);
						}
						record.put(startRow, row);
					}
					if (!header.isNoWrite()) {
						row.createCell(header.getStartColumn()).setCellValue(header.getColumn().split(SPLIT)[0].trim());
						sheet.addMergedRegion(new CellRangeAddress(startRow, header.getEndRow(), header.getStartColumn(),
								header.getEndColumn()));
					}
					maxRow = Math.max(maxRow, header.getEndRow());
					maxColumn = Math.max(maxColumn, header.getEndColumn());
				}
				if (isNo) {// 是否有序号
					String noName = StringUtils.isEmpty(helper.getNoName()) ? "序号" : helper.getNoName();
					if (record.get(nowRow) != null) {
						row = record.get(nowRow);
					} else {
						row = sheet.getRow(nowRow);
						if (row == null) {
							row = sheet.createRow(nowRow);
						}
						record.put(nowRow, row);
					}
					row.createCell(initStartColumn).setCellValue(noName);
					sheet.addMergedRegion(new CellRangeAddress(nowRow, maxRow, initStartColumn, initStartColumn));
				}
				// for循环设置列头的style
				for (int i = nowRow; i <= maxRow; i++) {
					row = record.get(i) == null ? sheet.createRow(i) : record.get(i);
					for (int j = initStartColumn; j <= maxColumn ; j++) {
						cell = row.getCell(j) == null ? row.createCell(j) : row.getCell(j);
						cell.setCellStyle(styles.get(STYLE_KEY.COLUMN));
					}
				}
				return maxRow + 1;
			}
			return nowRow;
		} else {
			return maxRow;
		}
		
	}
	
	// 填充数据
	private static int fixDatas(HSSFWorkbook hssfWorkbook,HSSFSheet sheet, Map<Integer, ExcelData> setting, int size, int nowRow,
			FlexibleExcelHelper helper) {
		HSSFRow row;
		List<?> datas = helper.getDatas();
		boolean isNo = helper.isNo();
		int initStartColumn = helper.getStartColumn();
		Map<STYLE_KEY, HSSFCellStyle> styles = helper.getStyles();
		Field keyField = helper.getMainKeyField();//获取到比较主键
		nowRow = fixRemarkInfo(sheet, size, nowRow, helper, initStartColumn);//填充备注
		nowRow = fixGuide(sheet, setting, size, nowRow, helper, isNo, initStartColumn, styles);//填充协会指引
		if (datas != null && datas.size() > 0) {
			List<DataMerge> hasMergedDatas =new ArrayList<>();//字段已经合并的单元格
			List<Integer> noHasMergedDatas = new ArrayList<>();//序号和并列
			Map<String, List<Object>> keyValues = new HashMap<>();// 需要合并的列的有值的
			Map<String, DataMerge> keyValueMergeDetail = new HashMap<>();// 列值对应的合并信息
			Map<String, HSSFCellStyle> columnStyles = new HashMap<String, HSSFCellStyle>();// 每列的样式
			int num = 1;
			Field field;
			String fieldName;
			HSSFCell cell = null;
			Object keyValue;
			boolean writeNo = true;
			boolean isColumn;
			for (int i = 0; i < datas.size(); i++) {// 循环查询的数据生成行
				row = sheet.getRow(nowRow);
				if (row == null) {
					row = sheet.createRow(nowRow);// 创建一行
				}
				keyValue = getFieldValue(datas.get(i), keyField) == null?"":getFieldValue(datas.get(i), keyField);
				isColumn = false;
				for (ExcelData columnDetail : setting.values()) {// 循环配置生成列
					if (columnDetail != null) {// 为空的直接跳过
						int j = columnDetail.getOrder();
						try {
							cell = row.createCell(j);// 创建个单元格
							field = columnDetail.getField();// 获取到字段
//							boolean isFixColor = field.getType() == Integer.class || field.getType() == Double.class;
							fieldName = field.getName();// 获取到字段名称
							// 获取字段对应的value
							Object object = getFieldValue(datas.get(i), field);
							if(!checkNeedSave(nowRow, helper, initStartColumn, columnDetail, hasMergedDatas,
									keyValues, keyValueMergeDetail, fieldName, keyValue, j, object)){//判断是否保存
								continue;
							}
							if(helper.getColumn().isHasColumnData() && helper.getColumn().getColumnNames().contains(object)){
								isColumn = true;
							}
							writeNo = helper.isWriteNo();
							Map<String, Object> valueAndType = setValue(cell,columnDetail,object);
							setStyle(styles, columnStyles, row, columnDetail, j, (DATA_TYPE) valueAndType.get(KEY_DATA_TYPE),isColumn);
							Object realValue = valueAndType.get(KEY_REAL_VALUE);
//							if(isFixColor){
								fixColor(hssfWorkbook,columnDetail,cell,realValue,columnStyles,datas.get(i));//填充颜色
//							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(isNo){
					//填充序号值
					num = fixNo(sheet, nowRow, helper, row,initStartColumn, styles, noHasMergedDatas,keyValueMergeDetail, columnStyles, num, writeNo);
				}
				nowRow++;
			}
			if (keyField != null) {
				keyField.setAccessible(false);
			}
			if(helper.isMerge()){
				//合并单元格
				mergeCell(sheet, setting, helper, initStartColumn, styles, hasMergedDatas, noHasMergedDatas,keyValueMergeDetail, columnStyles);
			}
		}  
		//生成默认
		nowRow = createDefaultRow(sheet, setting, nowRow, helper, datas, isNo, initStartColumn, styles);
		return nowRow;
	}
	//设置值(并且获取真实值和对应样式)
	private static Map<String,Object> setValue(HSSFCell cell,ExcelData columnDetail,Object object ) {
		Map<String, Object> result = new HashMap<>();
		result.put(KEY_REAL_VALUE, object);
		result.put(KEY_DATA_TYPE, DATA_TYPE.STRING);
		Field field = columnDetail.getField();
		if (field.getType() == String.class) {// 根据字段类型判断格式
			object = object == null ? columnDetail.getCharDefault() : object.toString();// String直接赋值
			cell.setCellValue((String) object);
		} else if (field.getType() == Date.class) {// 日期格式化后赋值
			DateConstant dateFormat = DateConstant.YMD_ORA;
			if (columnDetail.getDateType() == 1) {
				dateFormat = DateConstant.Y_M_D;
			} else if (columnDetail.getDateType() == 2) {
				dateFormat = DateConstant.WHOLE_TIME;
			} else if (columnDetail.getDateType() == 3) {
				dateFormat = DateConstant.Y_M;
			}
			try {
				object= object == null ? columnDetail.getCharDefault() : DateUtil.dateConvertStr((Date) object, dateFormat);
			} catch (Exception e) {}
			String dateStr = (String) object;
			if (columnDetail.getDateType() == 3 && dateStr.contains("-")) {
				dateStr = dateStr.replace("-", ".");
			}
			cell.setCellValue(dateStr);
		} else if (field.getType() == Integer.class) {// 整形直接赋值
			object = object == null ? columnDetail.getNumDefault() : object;
			if (columnDetail.isIstran() && columnDetail.getTranOra().toString().equals(object.toString())) {// 如果走转换路线的话 则直接zouString类型
				object = columnDetail.getTranTar();
				result.put(KEY_REAL_VALUE, object);
				cell.setCellValue(object.toString());
			} else {
				try {// 如果默认值转数字正常的话 则直接按数字逻辑处理
					cell.setCellValue(Integer.parseInt(object.toString()) * columnDetail.getTimes());
					result.put(KEY_DATA_TYPE, DATA_TYPE.INTEGER);
				} catch (NumberFormatException e) {// 出错的话则按string处理
					cell.setCellValue(object.toString());
				}
				
			}
		} else if (field.getType() == Double.class) {// double型考虑百分号和小数位
			object = object == null ? columnDetail.getNumDefault() : object;
			if (columnDetail.isIstran() && columnDetail.getTranOra().toString().equals(object.toString())) {
				object = columnDetail.getTranTar();
				result.put(KEY_REAL_VALUE, object);
				cell.setCellValue(object.toString());
			} else {
				try {// 如果默认值转数字正常的话 则直接按数字逻辑处理
					cell.setCellValue(Double.parseDouble(object.toString()) * columnDetail.getTimes());
					result.put(KEY_DATA_TYPE, DATA_TYPE.DOUBLE);
				} catch (Exception e) {
					cell.setCellValue(object.toString());
				}
				
			}
		}
		return result;
	}
	/**
	 * 检测是否需要保存数据（如果是存在合并列的话  则存在不需要设置地方）,有些和并列的只需要写一个值就好了
	 * @return
	 */
	private static boolean checkNeedSave(int nowRow, FlexibleExcelHelper helper, int initStartColumn,ExcelData columnDetail, List<DataMerge> hasMergedDatas, 
			Map<String, List<Object>> keyValues,Map<String, DataMerge> keyValueMergeDetail, String fieldName, Object keyValue, int columnIndex,
			Object object) {
		boolean needSetValue = true;
		boolean writeNo = true;
		if (helper.isMerge()) {// 如果是合并导出的话，则需要判断这一行需不需要合并这个字段是不是合并的
			//处理通过值合并列情况
			if (columnDetail.isMergeByValue()) {// 如果是通过值进行合并的话
				if (fieldName.equals(helper.getMainKey())) {
					writeNo = false;
				}
				String key = fieldName + keyValue;//字段值+主键值 :  nav000001
				// 1.判断该值是不是已经合并过
				if (object != null) {
					if (keyValues.get(key) == null) {
						keyValues.put(key, new ArrayList<>());
					}
					if (keyValues.get(key).contains(object)) {//如果已经合并了的话
						DataMerge dataMerge2 = keyValueMergeDetail.get(key + object.toString());
						dataMerge2.addRowRange(1);
						if (fieldName.equals(helper.getMainKey())) {
							DataMerge dataMerge3 = keyValueMergeDetail.get(initStartColumn + keyValue.toString());
							dataMerge3.addRowRange(1);
						}
						needSetValue = false;
					} else {//如果是第一次遇到的话
						keyValues.get(key).add(object);
						DataMerge dataMerge2 = new DataMerge();
						dataMerge2.setStartColumn(columnIndex);
						dataMerge2.setStartRow(nowRow);
						keyValueMergeDetail.put(key + object.toString(), dataMerge2);//nav0000010.01
						// 把序号放进去
						if (fieldName.equals(helper.getMainKey())) {// 说明是主数据
							writeNo = !writeNo;
							DataMerge dataMerge3 = new DataMerge();
							dataMerge3.setStartColumn(initStartColumn);
							dataMerge3.setStartRow(nowRow);
							keyValueMergeDetail.put(initStartColumn + keyValue.toString(), dataMerge3);
						}
					}
				} else if (keyValues.get(helper.getMainKey() + keyValue) != null && keyValues.get(helper.getMainKey() + keyValue).contains(keyValue)) {// 如果主值已写，则过滤
					if (keyValueMergeDetail.get(key) == null) {
						DataMerge dataMerge2 = new DataMerge();
						dataMerge2.setStartColumn(columnIndex);
						dataMerge2.setStartRow(nowRow);
						keyValueMergeDetail.put(key, dataMerge2);
					} else {
						DataMerge dataMerge2 = keyValueMergeDetail.get(key);
						dataMerge2.addRowRange(1);
					}
				}
			} else {
				for(DataMerge dataMerge : helper.getDataMerges().keySet()){
					if(dataMerge.checkNeedMerge(nowRow,columnIndex)){
						needSetValue = fieldName.equals(helper.getDataMerges().get(dataMerge)) && nowRow == dataMerge.getStartRow();
						if(needSetValue){
							dataMerge.setShowValue(object);
							hasMergedDatas.add(dataMerge);
						}
					}
				}
			}
		}
		helper.setWriteNo(writeNo);
		return needSetValue;
	}
	//填充序号
	private static int fixNo(HSSFSheet sheet, int nowRow, FlexibleExcelHelper helper, HSSFRow row,int initStartColumn, 
			Map<STYLE_KEY, HSSFCellStyle> styles, List<Integer> noHasMergedDatas,Map<String, DataMerge> keyValueMergeDetail, 
			Map<String, HSSFCellStyle> columnStyles, int num,boolean writeNo) {
		//合并序号
		if (helper.isMerge()) {
			if (CollectionUtils.isEmpty(keyValueMergeDetail.values())) {
				if (writeNo) {
					row.createCell(initStartColumn).setCellValue(num);
					num++;
					row.getCell(initStartColumn).setCellStyle(styles.get(STYLE_KEY.COLUMN));
					columnStyles.put(initStartColumn+"", styles.get(STYLE_KEY.COLUMN));
				}
			} else {
				if (!noHasMergedDatas.contains(nowRow)) {// 合并列是否包含该列，不包含则产生序号
					int noRowRange = helper.getNoRowRange();// 获取序号的合并列数
					for (int index = nowRow; index < nowRow + noRowRange; index++) {// 把需要合并的列全部放到序号合并集合中
						noHasMergedDatas.add(index);
					}
					row.createCell(initStartColumn).setCellValue(num);
					num++;
					sheet.addMergedRegion(new CellRangeAddress(nowRow, nowRow + noRowRange - 1, initStartColumn,initStartColumn));
				}
			}
		} else {
			row.createCell(initStartColumn).setCellValue(num);
			num++;
			row.getCell(initStartColumn).setCellStyle(styles.get(STYLE_KEY.COLUMN));
		}
		return num;
	}
	//合并单元格
	private static void mergeCell(HSSFSheet sheet, Map<Integer, ExcelData> setting, FlexibleExcelHelper helper,
			int initStartColumn, Map<STYLE_KEY, HSSFCellStyle> styles,List<DataMerge> hasMergedDatas,
			List<Integer> noHasMergedDatas, Map<String, DataMerge> keyValueMergeDetail,
			Map<String, HSSFCellStyle> columnStyles) {
		HSSFRow row;
		HSSFCell cell;
		//合并序号设置格式(序号前面已经合并了,所以这里只设置样式)
		for (int index = 0; index < noHasMergedDatas.size(); index++) {
			row = sheet.getRow(noHasMergedDatas.get(index)) == null ? sheet.createRow(noHasMergedDatas.get(index)) : sheet.getRow(noHasMergedDatas.get(index));
			cell = row.getCell(initStartColumn) == null ? row.createCell(initStartColumn) : row.getCell(initStartColumn);
			cell.setCellStyle(styles.get(STYLE_KEY.COLUMN));
		}
		//合并写死数据
		for (DataMerge dataMerge : hasMergedDatas) {// 循环设置合并的样式
			sheet.addMergedRegion(new CellRangeAddress(dataMerge.getStartRow(), dataMerge.getEndRow(),dataMerge.getStartColumn(), dataMerge.getEndColumn()));
			for (int index = dataMerge.getStartRow(); index <= dataMerge.getEndRow(); index++) {
				row = sheet.getRow(index) == null ? sheet.createRow(index) : sheet.getRow(index);
				for (int n = dataMerge.getStartColumn(); n <= dataMerge.getEndColumn(); n++) {
					cell = row.getCell(n) == null ? row.createCell(n) : row.getCell(n);
					if(index == dataMerge.getStartRow() && n == dataMerge.getStartColumn()){
						setValue(cell, setting.get(n), dataMerge.getShowValue());
					}
					if(dataMerge.getAlign() == HSSFCellStyle.ALIGN_LEFT){
						cell.setCellStyle(styles.get(STYLE_KEY.COLUMN_LEFT));
					} else if(dataMerge.getAlign() == HSSFCellStyle.ALIGN_RIGHT){
						cell.setCellStyle(styles.get(STYLE_KEY.COLUMN_RIGHT));
					} else {
						cell.setCellStyle(styles.get(STYLE_KEY.COLUMN));
					}
				}
			}
		}
		//合并通过value合并
		if (keyValueMergeDetail != null && keyValueMergeDetail.size() > 0) {
			for (DataMerge dataMergeDetail : keyValueMergeDetail.values()) {
				sheet.addMergedRegion(new CellRangeAddress(dataMergeDetail.getStartRow(), dataMergeDetail.getEndRow(),dataMergeDetail.getStartColumn(), dataMergeDetail.getEndColumn()));
				for (int i = dataMergeDetail.getStartRow(); i <= dataMergeDetail.getEndRow(); i++) {
					row = sheet.getRow(i) == null ? sheet.createRow(i) : sheet.getRow(i);
					for (int j = dataMergeDetail.getStartColumn(); j <= dataMergeDetail.getEndColumn(); j++) {
						cell = row.getCell(j) == null ? row.createCell(j) : row.getCell(j);
						cell.setCellStyle(columnStyles.get(setting.get(j) == null ? j + "": setting.get(j).getField().getName()));
					}
				}
			}
		}
	}
	//获取字段值
	private static Object getFieldValue(Object model, Field field) {
		Object value = null ;
		if (field != null) {
			field.setAccessible(true);
			try {
				value = field.get(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
			field.setAccessible(false);
		}
		return value;
	}
	//填充备注
	private static int fixRemarkInfo(HSSFSheet sheet, int size, int nowRow, FlexibleExcelHelper helper,
			int initStartColumn) {
		HSSFRow row;
		Map<String, Object> remarkInfoMap = helper.getRemarkInfoMap();
		if (null != remarkInfoMap) {
			String remark = (String) remarkInfoMap.get("title");
			HSSFCellStyle styleBorderNone = (HSSFCellStyle) remarkInfoMap.get("style");
			row = sheet.getRow(nowRow);
			if (row == null) {
				row = sheet.createRow(nowRow);// 创建一行
			}
			row.createCell(0).setCellValue(remark);
			sheet.addMergedRegion(new CellRangeAddress(nowRow, nowRow, initStartColumn, initStartColumn + size - 1));
			row.getCell(initStartColumn).setCellStyle(styleBorderNone);
			nowRow++;
		}
		return nowRow;
	}

	private static int createDefaultRow(HSSFSheet sheet,Map<Integer, ExcelData> setting, int nowRow, FlexibleExcelHelper helper,
			List<?> datas, boolean isNo, int initStartColumn, Map<STYLE_KEY, HSSFCellStyle> styles) {
		HSSFRow row;
		if (CollectionUtils.isEmpty(datas) && helper.isHasDefaultRow()) {// 如果没有数据则创建一个空行
			HSSFCell cell;
			int num = 1;
			row = sheet.getRow(nowRow);
			if (row == null) {
				row = sheet.createRow(nowRow);// 创建一行
			}
			for (int j : setting.keySet()) {
				cell = row.getCell(j);
				if (cell == null) {
					cell = row.createCell(j);
					cell.setCellStyle(styles.get(STYLE_KEY.COLUMN));
				}
			}
			if (isNo) {// 如果排序的话，把第一列设置为序号
				row.createCell(initStartColumn).setCellValue(num);
				num++;
				row.getCell(initStartColumn).setCellStyle(styles.get(STYLE_KEY.DATA_STRING_CENTER));
			}
			nowRow++;
		}
		return nowRow;
	}
	//填充指引
	private static int fixGuide(HSSFSheet sheet, Map<Integer, ExcelData> setting, int size, int nowRow,
			FlexibleExcelHelper helper, boolean isNo, int initStartColumn, Map<STYLE_KEY, HSSFCellStyle> styles) {
		HSSFRow row;
		if (helper.isGuide()) {// 判断是否导出协会指引
			row = sheet.getRow(nowRow);
			if (row == null) {
				row = sheet.createRow(nowRow);// 创建一行
			}
			row.setHeight((short) (10 * 256));
			if (isNo) {// 如果排序的话，把第一列设置为序号
				row.createCell(initStartColumn).setCellValue("");
				row.getCell(initStartColumn).setCellStyle(styles.get(STYLE_KEY.COLUMN));
			}
//			for (int j = 0; j < size + initStartColumn; j++) {// 循环配置生成列
//				columnDetail = setting.get(j);// 通过order获取对应的配置信息
//				if (columnDetail != null) {// 为空的直接跳过
//					row.createCell(j);// 创建个单元格
//					row.getCell(j).setCellValue(columnDetail.getGuide());
//					row.getCell(j).setCellStyle(styles.get(STYLE_KEY.COLUMN));
//				}
//			}
			for (ExcelData columnDetail : setting.values()) {// 循环配置生成列
				HSSFCell cell = row.createCell(columnDetail.getOrder());// 创建个单元格
				cell.setCellValue(columnDetail.getGuide());
				cell.setCellStyle(styles.get(STYLE_KEY.COLUMN));
			}
			nowRow++;
		}
		return nowRow;
	}
	//填充颜色
	private static void fixColor(HSSFWorkbook hssfWorkbook,ExcelData columnDetail, HSSFCell cell, Object object,Map<String, HSSFCellStyle> columnStyles,Object model) {
			Short dataColor = CaculateColorUtils.getColor(columnDetail.getDataColors(), object,model);
//			if (!PalBaseCache.compareWith(CacheConst.EXPORT_FILED_WARNING, EnumConst.YES_NO.YES.getSCode())){
//				dataColor=null;
//			}
			Short backGroudColor = CaculateColorUtils.getColor(columnDetail.getBackGroundColors(), object,model);
//			if (backGroudColor!=null && backGroudColor==13 && PalBaseCache.compareWith(CacheConst.IS_OPEN_RATE_WARNING, EnumConst.YES_NO.NO.getSCode())) {//story #2837 
//				backGroudColor = null;//如果配置为否的话，就不设置颜色
//			}
			if(dataColor != null || backGroudColor != null){
				dataColor = dataColor == null ? CaculateColorUtils.DEFAULT_DATA_COLOR : dataColor;
				backGroudColor = backGroudColor == null ? CaculateColorUtils.DEFAULT_BACKAGE_COLOR : backGroudColor;
				String key = columnDetail.getField().getName() + "_"+ backGroudColor + "_" +dataColor;
				if(columnStyles.get(key) == null){
					HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
					cellStyle.cloneStyleFrom(cell.getCellStyle());
					cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); // 填充单元格
					cellStyle.setFillForegroundColor(backGroudColor);
					HSSFFont font = hssfWorkbook.createFont();
					font.setColor(dataColor);
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
					columnStyles.put(key, cellStyle);
				} else {
					cell.setCellStyle(columnStyles.get(key));
				}
			}
		
	}

	/**
	 * excel文件转对象
	 * 
	 * @param clazz class对象
	 * @param workbook excel对象
	 * @param dataRow 数据行
	 * @param dataColumn 数据列
	 * @param reportId 报表Id
	 * @param reportIdField 报表Id对应的字段（默认是reportScheduleId）
	 * @param companyType 公司类型，主要用来判断2选1字段取值
	 * @param hasLoseField 是否过滤字段loseField = true字段
	 * @param isCheckTemp 是否检测模板 如果一个模板没有与之对应的实体则无法进行检验模板
	 * @return
	 */
	public static <T> List<T> importExcel(Class<T> clazz, Workbook workbook, Integer dataRow, Integer dataColumn,
			String reportId, String reportIdField, String companyType, boolean hasLoseField, boolean isCheckTemp) {
		List<T> resutl = new ArrayList<>();
		if (workbook != null && clazz != null) {
			if (hasGuides.get(clazz.toString()) == null) {
				throw new RuntimeException("请先下载文件！");
			}
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet != null) {
				Map<String, Integer> rowAndColumn = new HashMap<>();
				rowAndColumn.put(ROW_INDEX, dataRow);
				rowAndColumn.put(COLUMN_INDEX, dataColumn);
				// 1.根据clazz获取每一列值对应的字段
				Map<Integer, Field> columnOrderMaps = initColumnOrderMaps(clazz, companyType, hasLoseField);
				// 2.确定起始行和列
				getBeginColumn(columnOrderMaps, sheet, rowAndColumn);
				if (isCheckTemp) {
					checkTemplate(sheet, columnOrderMaps, rowAndColumn);
				}
				// 3.读数据返回
				resutl = readWorkBook(sheet, columnOrderMaps, rowAndColumn, clazz, reportId, reportIdField);
			}
		}
		return resutl;
	}
	
	/**
	 * 检测模板的正确性
	 * 
	 * @param sheet
	 * @param columnOrderMaps
	 * @param rowAndColumn
	 */
	private static void checkTemplate(Sheet sheet, Map<Integer, Field> columnOrderMaps,
			Map<String, Integer> rowAndColumn) {
		if (rowAndColumn.get(ROW_INDEX) == null || rowAndColumn.get(COLUMN_INDEX) == null) {
			throw new RuntimeException("模板不对！");
		} else {
			// 挨个比较列
			int rowIndex = rowAndColumn.get(ROW_INDEX) - 1;
			Cell cell;
			Integer beginColumn = rowAndColumn.get(COLUMN_INDEX);
			Row row;
			Object value = null;
			for (Entry<Integer, Field> entry : columnOrderMaps.entrySet()) {
				FlexibleExcel annotation = entry.getValue().getAnnotation(FlexibleExcel.class);
				int rowrange = annotation.rowRange();
				for (int i = rowIndex; i >= rowIndex - rowrange + 1; i--) {
					row = sheet.getRow(i);
					if(row == null){
						continue;
					}
					cell = row.getCell(entry.getKey() + beginColumn);
					value = getValue(cell, entry.getValue());
					if (value != null) {
						break;
					}
				}
				if (value != null && !annotation.column().equals(value) && annotation.add()) {
					throw new RuntimeException("模板不对！");
				}
			}
		}
	}
	
	/**
	 * 读取数据
	 * 
	 * @param sheet
	 * @param columnOrderMaps
	 * @param rowAndColumn
	 * @param clazz
	 * @return
	 */
	private static <T> List<T> readWorkBook(Sheet sheet, Map<Integer, Field> columnOrderMaps,
			Map<String, Integer> rowAndColumn, Class<T> clazz, String reportId, String reportIdField) {
		List<T> result = new ArrayList<>();
		try {
			Field reportScheduleId = null;
			if (StringUtils.isNotEmpty(reportId)) {
				reportScheduleId = clazz.getDeclaredField(StringUtils.isEmpty(reportIdField) ? "reportScheduleId" : reportIdField);
				reportScheduleId.setAccessible(true);
			}
			Integer beginRow = rowAndColumn.get(ROW_INDEX);
			Integer beginColumn = rowAndColumn.get(COLUMN_INDEX);
			if (hasGuides.get(clazz.toString())) {
				beginRow++;
			}
			int lastRowNum = sheet.getLastRowNum();
			Field field;
			Cell cell;
			Row row;
			int columnNoDatas;// 记录连续多少列没有数据 ，如果超过5列连续没数据的话 则认为此行没数据
			int rowNoDatas = 0;// 记录连续多少行没有数据，超过5没有没有则不往下读啦
			// 1.从开始行读取数据
			for (int rowIndex = beginRow; rowIndex <= lastRowNum; rowIndex++) {
				columnNoDatas = 0;
				row = sheet.getRow(rowIndex);
				if(row == null){
					rowNoDatas++;
					if (rowNoDatas > 5) {
						break;
					}
					continue;
				}
				T t = clazz.newInstance();
				short lastCellNum = row.getLastCellNum();
				if (lastCellNum == -1) {
					columnNoDatas = 5;
					rowNoDatas++;
					continue;
				}
				lastCellNum = (short) (beginColumn + columnOrderMaps.size());
				for (int columnIndex = beginColumn; columnIndex < lastCellNum; columnIndex++) {
					field = columnOrderMaps.get(columnIndex - beginColumn);
					if (field == null) {
						continue;
					}
					FlexibleExcel annotation = field.getAnnotation(FlexibleExcel.class);
					cell = row.getCell(columnIndex);
					if (cell == null) {
						if (annotation.add()) {
							columnNoDatas++;
						}
						if (columnNoDatas == 5) {
							break;
						}
						continue;
					}
					Object value = getValue(cell, field);
					if (value == null || StringUtils.isEmpty(value.toString())) {
						if (annotation.add()) {
							columnNoDatas++;
						}
						if (columnNoDatas == 5) {
							break;
						}
						continue;
					}
					columnNoDatas = 0;
					try {
						if (String.class.equals(field.getType()) && annotation.charDefault().equals(value.toString())) {// 处理默认值问题
							value = null;
						}
						if ((Double.class.equals(field.getType()) || Integer.class.equals(field.getType()))
								&& annotation.numDefault().equals(value.toString())) {// 处理默认值问题
							value = null;
						}
						if (value != null) {
							if (Date.class.equals(field.getType())) {// 处理日期
								if (Double.class.equals(value.getClass())) {
									try {
										value = DateUtil.getAfterNDate(
												DateUtil.strConvertDate("1900-01-01", DateConstant.Y_M_D),
												(int) ((Double) value - 2));
									} catch (Exception e) {}
								} else {
									try {
										value = DateUtil.strConvertDate(value.toString(), DateConstant.Y_M_D);
									} catch (Exception e) {}
									try {
										value = DateUtil.strConvertDate(value.toString(), DateConstant.YMD_ORA);
									} catch (Exception e) {}
									try {
										value = DateUtil.strConvertDate(value.toString(), DateConstant.Y_M_D_CN);
									} catch (Exception e) {}
								}
							}
							if (Double.class.equals(field.getType())) {
								Double temp = new Double(value.toString());
								if (annotation.isPercent()) {
									// value = new
								}
								if (annotation.times() != 1.0 && annotation.times() != 0.0) {
									temp = NumUtil.div(temp, annotation.times());
								}
								value = temp;
							}
							if (Integer.class.equals(field.getType())) {
								Integer temp = new Double(value.toString()).intValue();
								if (annotation.isPercent()) {
									// value = new
								}
								if (annotation.times() != 1.0 && annotation.times() != 0.0) {
									temp = (int) (temp / annotation.times());
								}
								value = temp;
							}
							if (annotation.isTran() && annotation.tranTar().equals(value.toString())) {
								value = annotation.tranOra();
							}
							if (String.class.equals(field.getType())) {
								field.set(t, value.toString());
							} else {
								field.set(t, value);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (columnNoDatas < 5) {
					if (reportScheduleId != null) {
						reportScheduleId.set(t, reportId);
					}
					result.add(t);
					rowNoDatas = 0;
				} else {
					rowNoDatas++;
					if (rowNoDatas > 5) {
						break;
					}
				}
			}
			for (Field field2 : columnOrderMaps.values()) {
				field2.setAccessible(false);
			}
			if (reportScheduleId != null) {
				reportScheduleId.setAccessible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据类别获取值
	 * 
	 * @param cell
	 * @param field
	 * @return
	 */
	private static Object getValue(Cell cell, Field field) {
		Object value = null;
		try {
			if (cell != null) {
				int cellType = cell.getCellType();
				if (Cell.CELL_TYPE_NUMERIC == cellType) {
					value = cell.getNumericCellValue();
					if (value != null) {
						if (field != null && String.class.equals(field.getType())) {// 如果是字符串的话
							value = DoubleToStr((Double)value, 4);
						}
					}
				} else if (Cell.CELL_TYPE_STRING == cellType) {
					value = cell.getStringCellValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	 /**
     * double 转字符串
     * @author lgh
     * @param b
     * @param scale
     * @return
     */
    private static String DoubleToStr(Double b, int scale) {
		
		BigDecimal big = new BigDecimal("0");
		try {
			big = BigDecimal.valueOf(b);
			big.setScale(scale,BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return big.toPlainString();
    }
    
	/**
	 * 获取起始列:读取到和最早的column一样的列头
	 * 
	 * @param columnOrderMaps
	 * @param hssfWorkbook
	 * @return
	 */
	private static void getBeginColumn(Map<Integer, Field> columnOrderMaps, Sheet sheet,
			Map<String, Integer> rowAndColumn) {
		try {
			int rowIndex = 0;
			Field firstField = getFirstField(columnOrderMaps);
			FlexibleExcel excel = firstField.getAnnotation(FlexibleExcel.class);
			int rowRange = excel.rowRange();
			String column = excel.column();
			Row row;
			Cell cell;
			boolean out = false;
			while (rowIndex <= MAX_ROW) {
				row = sheet.getRow(rowIndex);
				if (row != null) {
					int columnIndex = 0;
					while (columnIndex <= MAX_COLUMN) {
						cell = row.getCell(columnIndex);
						if (cell != null) {
							Object value = getValue(cell, null);
							if (column.equals(value)) {
								if (rowAndColumn.get(COLUMN_INDEX) == null) {
									rowAndColumn.put(COLUMN_INDEX, columnIndex);
								}
								out = true;
								break;
							}
						}
						columnIndex++;
					}
					if (out) {
						if (rowAndColumn.get(ROW_INDEX) == null) {
							rowAndColumn.put(ROW_INDEX, rowIndex + rowRange);
						}
						break;
					}
				}
				rowIndex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取第一个元素
	 * 
	 * @param columnOrderMaps
	 * @return
	 */
	private static Field getFirstField(Map<Integer, Field> columnOrderMaps) {
		int index = 0;
		Field firstField = null;
		while (index <= MAX_COLUMN) {// 确定初始元素
			if (columnOrderMaps.get(index) != null) {
				firstField = columnOrderMaps.get(index);
				index = 0;
				break;
			}
			index++;
		}
		return firstField;
	}
	
	/**
	 * 初始化clazz对字段所在列
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	private static Map<Integer, Field> initColumnOrderMaps(Class<?> clazz, String companyType, boolean hasLoseField) {
		Map<Integer, Field> result = new HashMap<>();
		try {
			Field[] declaredFields = clazz.getDeclaredFields();
			Set<Integer> hasLoseFields = new HashSet<>();
			Integer realOrder;
			// 1.记录需要隐藏的field
			if (hasLoseField) {
				for (Field field : declaredFields) {
					FlexibleExcel annotation = field.getAnnotation(FlexibleExcel.class);
					if (annotation != null && annotation.isLoseField()) {
						hasLoseFields.add(annotation.order());
					}
				}
			}
			for (Field field : declaredFields) {
				FlexibleExcel annotation = field.getAnnotation(FlexibleExcel.class);
				if (annotation != null) {
					if (hasLoseFields.contains(annotation.order())) {
						continue;
					}
					if (StringUtils.isNoneBlank(annotation.specialPercent())) {// 处理根据不同公司到出不同列的选择
//						if (annotation.specialPercent().equals(CacheConst.ASSET_GROWTH_RATE_PERCENT)) {// 特殊处理十分百分号
//							field.setAccessible(true);
//							result.put(annotation.order(), field);
//						}
						if (!annotation.specialPercent().equals(companyType)) {// 特殊处理是否导出该字段
							continue;
						}
					}
					realOrder = getRealOrder(annotation.order(), hasLoseFields);
					field.setAccessible(true);
					result.put(realOrder, field);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** 获取真实的order */
	private static Integer getRealOrder(int order, Set<Integer> hasLoseFields) {
		if (CollectionUtils.isEmpty(hasLoseFields)) {
			for (Integer hasLoseField : hasLoseFields) {
				if (hasLoseField < order) {
					order--;
				}
			}
		}
		return order;
	}
}
