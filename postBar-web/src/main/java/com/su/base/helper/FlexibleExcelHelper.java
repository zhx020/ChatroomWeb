package com.su.base.helper;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import com.su.base.constant.DateConstant;
import com.su.base.util.DateUtil;
import com.su.base.util.DownLoadExcelUtils;
import com.su.base.util.DownLoadExcelUtils.AttachedMap;
import com.su.base.util.DownLoadExcelUtils.STYLE_KEY;

public class FlexibleExcelHelper {
	/** 一个sheet页多个表格参数 */
	private boolean isAdd;// 是否新增一个报表
	private boolean isVertical = true;// 垂直增加
	private int splitVerticalRow;// 垂直分割行
	private boolean isHorizontal;// 是否水平
	private int splitHorizontalColumn;// 水平分割行
	private int beforeColumnCount;// 上一个报表多少列，因为要往右加，需要记录上一个多少列
	/** 排序参数 */
	private boolean isNo = true;// 是否有序号
	private String noName;// 序号名称，默认为序号
	private int noRowRange = 1;// 序号所占行
	/** 标题信息 */
	private Title title;
	/** 附属信息 */
	private Attached attached;
	/** 特殊行信息 */
	private List<SpecialRow> specialRows;// 自定义数据1（一行一列）
	private List<SpecialCell> specialCells;// 自定义数据2（行列可以控制，样式也可以）
	/** 下拉框 */
	private List<DropDownList> dropDownList;// 下拉框
	private boolean isCascade = false;// 是否级联（暂不支持）
	/** 列头信息 */
	private Column column;
	/** 隐藏列信息 ,boolean值表示是否隐藏数据 true 隐藏 false不隐藏往后移 */
	private Map<Integer, Boolean> hides;
	/** 数据合并列信息 */
	private boolean isMerge;// 是否和并列
	private Map<DataMerge, String> dataMerges;// key为合并的数据（写死的列和行），value为字段名
	private String mainKey = "";// 对应到flexible的mergeByValue,如果mainKey对应的值一样时，value一样才会合并
	/** 必须基本数据 */
	private List<?> datas;// 导出数据
	private Class<?> clazz;// 数据的class
	private String sheetName;// sheet页名
	private int startColumn;// 开始列
	/** 个性化数据 */
	private int type;// 格式定义：0 ：自定格式 1：统一居中 2：统一靠左 3：统一靠右（默认统一居中）
	private boolean isGetALl;// 是否获取所有字段，和FlexibleExcel的isAdd对应
	private Date reportDate;// 报告日期
	private Map<STYLE_KEY, HSSFCellStyle> styles;// 样式信息，保留一份样式信息主要是为了解决复用问题，避免产生太多样式而导致excel版本的样式问题
	private int defaultRowHight = 35;// 行高
	private boolean isGuide;// 是否包含指引
	private Map<String, Object> remarkInfoMap;// 备注信息com.wescxx.app.palMr.constants.ReportRemarkEnum
	// 辅助参数
	private int nowColumnCount;// 该报表多少列
	private String companyType;// 公司类型，对应FlexibleExcel.specialPercent的扩展点2，根据公司类型导出不一样的报表
	private Map<String, String> replaceMap;// 列头替换的数据，入有些列头是和报告日期有关的，需要动态替换,具体可参考DR00002001
	private List<?> extraRows; // 临时的数据行(模板导出合计行)
	private boolean isLoseField = false; // 是否跳过此列 模板导出
	private boolean hasDefaultRow = true;// 如果没有数据则默认导出一个空行
	private boolean writeNo;// 该字段目前只有携带数据功能
	
	public Map<Integer, Boolean> getHides() {
		if (hides == null) {
			hides = new HashMap<>();
		}
		return hides;
	}
	
	public void setHides(Map<Integer, Boolean> hides) {
		this.hides = hides;
	}
	
	public List<SpecialCell> getSpecialCells() {
		return specialCells;
	}
	
	public void setSpecialCells(List<SpecialCell> specialCells) {
		this.specialCells = specialCells;
	}
	
	public boolean isWriteNo() {
		return writeNo;
	}
	
	public void setWriteNo(boolean writeNo) {
		this.writeNo = writeNo;
	}
	
	public boolean isHasDefaultRow() {
		return hasDefaultRow;
	}
	
	public void setHasDefaultRow(boolean hasDefaultRow) {
		this.hasDefaultRow = hasDefaultRow;
	}
	
	public void setLoseField(boolean isLoseField) {
		this.isLoseField = isLoseField;
	}
	
	public boolean getIsLoseField() {
		return isLoseField;
	}
	
	public void setIsLoseField(boolean isLoseField) {
		this.isLoseField = isLoseField;
	}
	
	public Map<String, String> getReplaceMap() {
		if (replaceMap == null) {
			replaceMap = new HashMap<>();
		}
		return replaceMap;
	}
	
	public void setReplaceMap(Map<String, String> replaceMap) {
		this.replaceMap = replaceMap;
	}
	
	public List<?> getExtraRows() {
		return extraRows;
	}
	
	public void setExtraRows(List<?> extraRows) {
		this.extraRows = extraRows;
	}
	
	public String getCompanyType() {
		return companyType;
	}
	
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	
	public int getNoRowRange() {
		return noRowRange;
	}
	
	public void setNoRowRange(int noRowRange) {
		this.noRowRange = noRowRange;
	}
	
	public boolean isMerge() {
		return isMerge;
	}
	
	public void setMerge(boolean isMerge) {
		this.isMerge = isMerge;
	}
	
	public Map<DataMerge, String> getDataMerges() {
		if (dataMerges == null) {
			dataMerges = new HashMap<>();
		}
		return dataMerges;
	}
	
	public void setDataMerges(Map<DataMerge, String> dataMerges) {
		this.dataMerges = dataMerges;
	}
	
	public int getBeforeColumnCount() {
		return beforeColumnCount;
	}
	
	public void setBeforeColumnCount(int beforeColumnCount) {
		this.beforeColumnCount = beforeColumnCount;
	}
	
	public int getNowColumnCount() {
		return nowColumnCount;
	}
	
	public void setNowColumnCount(int nowColumnCount) {
		this.nowColumnCount = nowColumnCount;
	}
	
	public boolean isVertical() {
		return isVertical;
	}
	
	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}
	
	public boolean isHorizontal() {
		return isHorizontal;
	}
	
	public void setHorizontal(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}
	
	public String getNoName() {
		return noName;
	}
	
	public void setNoName(String noName) {
		this.noName = noName;
	}
	
	public boolean isGuide() {
		return isGuide;
	}
	
	public void setGuide(boolean isGuide) {
		this.isGuide = isGuide;
	}
	
	public int getStartColumn() {
		return startColumn;
	}
	
	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}
	
	public boolean isNo() {
		return isNo;
	}
	
	public void setNo(boolean isNo) {
		this.isNo = isNo;
	}
	
	public int getDefaultRowHight() {
		return defaultRowHight;
	}
	
	public void setDefaultRowHight(int defaultRowHight) {
		this.defaultRowHight = defaultRowHight;
	}
	
	public boolean isAdd() {
		return isAdd;
	}
	
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	
	public int getSplitVerticalRow() {
		return splitVerticalRow;
	}
	
	public void setSplitVerticalRow(int splitVerticalRow) {
		this.splitVerticalRow = splitVerticalRow;
	}
	
	public int getSplitHorizontalColumn() {
		return splitHorizontalColumn;
	}
	
	public void setSplitHorizontalColumn(int splitHorizontalColumn) {
		this.splitHorizontalColumn = splitHorizontalColumn;
	}
	
	public Map<STYLE_KEY, HSSFCellStyle> getStyles() {
		return styles;
	}
	
	public void setStyles(Map<STYLE_KEY, HSSFCellStyle> styles) {
		this.styles = styles;
	}
	
	public String getSheetName() {
		return sheetName;
	}
	
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	public String getMainKey() {
		return mainKey;
	}
	
	public void setMainKey(String mainKey) {
		this.mainKey = mainKey;
	}
	
	public Title getTitle() {
		return title;
	}
	
	public void setTitle(Title title) {
		this.title = title;
	}
	
	public Attached getAttached() {
		return attached;
	}
	
	public void setAttached(Attached attached) {
		this.attached = attached;
	}
	
	public Column getColumn() {
		if (column == null) {
			column = new Column();
		}
		return column;
	}
	
	public void setColumn(Column column) {
		this.column = column;
	}
	
	public List<?> getDatas() {
		return datas;
	}
	
	public void setDatas(List<?> datas) {
		this.datas = datas;
	}
	
	public Class<?> getClazz() {
		DownLoadExcelUtils.hasGuides.put(clazz.toString(), isGuide);
		return clazz;
	}
	
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isGetALl() {
		return isGetALl;
	}
	
	public void setGetALl(boolean isGetALl) {
		this.isGetALl = isGetALl;
	}
	
	public Date getReportDate() {
		return reportDate;
	}
	
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	
	public int getYear() {
		if (reportDate == null) {
			reportDate = new Date();
		}
		return DateUtil.getYear(reportDate);
	}
	
	public int getMonth() {
		if (reportDate == null) {
			reportDate = new Date();
		}
		return DateUtil.getMonth(reportDate);
	}
	
	public int getDay() {
		if (reportDate == null) {
			reportDate = new Date();
		}
		String sdate = DateUtil.dateConvertStr(reportDate, DateConstant.Y_M_D);
		return Integer.parseInt(sdate.split("-")[2]);
	}
	
	public int getSeason() {
		if (reportDate == null) {
			reportDate = new Date();
		}
		return DateUtil.getSeason(reportDate);
	}
	
	public Map<String, Object> getRemarkInfoMap() {
		return remarkInfoMap;
	}
	
	public void setRemarkInfoMap(Map<String, Object> remarkInfoMap) {
		this.remarkInfoMap = remarkInfoMap;
	}
	
	public void fixMergeDatas() {
		if (isNo) {
			dataMerges = this.getDataMerges();
			for (DataMerge dataMerge : dataMerges.keySet()) {
				dataMerge.setStartColumn(dataMerge.getStartColumn() + 1);
			}
		}
	}
	
	/* =================================================== */
	// 提供外部获取标题信息
	public static Title getTitle(String stitle, int splitRow, int rowRange) {
		Title title = new Title();
		title.setSplitRow(splitRow);
		title.setTitle(stitle);
		title.setRowRange(rowRange);
		return title;
	}
	
	// 提供外部获取列头信息
	public static Column getColumn(boolean noColumn, int splitRow) {
		Column column = new Column();
		column.setNoColumn(noColumn);
		column.setSplitRow(splitRow);
		return column;
	}
	
	// 提供外部获取附属信息
	public static Attached getAttached(boolean hasAttached, int splitRow, List<AttachedMap> datas) {
		Attached attached = new Attached();
		attached.setHasAttached(hasAttached);
		attached.setSplitRow(splitRow);
		attached.setDatas(datas);
		return attached;
	}
	
	// 提供外部获取附属信息(模板导出时用到)
	/**
	 * 
	 * @author lgh
	 * @param hasAttached
	 * @param startRow 开始行
	 * @param startColumn 开始列
	 * @param templateFileName
	 * @param datas
	 * @return
	 */
	public static Attached getAttached(boolean hasAttached, int startRow, int startColumn, String templateFileName,
			List<AttachedMap> datas) {
		Attached attached = new Attached();
		attached.setHasAttached(hasAttached);
		attached.setSplitRow(startRow);
		attached.setDatas(datas);
		attached.setSplitColumn(startColumn);
		attached.setTemplateFileName(templateFileName);
		return attached;
	}
	
	// 提供外部获取数据合并列信息
	public static DataMerge getDataMerge(int startColumn, int columnRange, int startRow, int rowRange) {
		DataMerge dataMerge = new DataMerge();
		dataMerge.setStartColumn(startColumn);
		dataMerge.setColumnRange(columnRange);
		dataMerge.setRowRange(rowRange);
		dataMerge.setStartRow(startRow);
		return dataMerge;
	}
	
	/******************* 内部类 **************************************/
	public static class Title {// 标题类
		private String title;// 标题名
		private int splitRow;// 隔行数
		private int rowRange;// 行数
		private short align = HSSFCellStyle.ALIGN_CENTER_SELECTION;
		private Integer columnRange;// 标题宽度
		
		public short getAlign() {
			return align;
		}
		
		public void setAlign(short align) {
			this.align = align;
		}
		
		public Integer getColumnRange() {
			return columnRange;
		}
		
		public void setColumnRange(Integer columnRange) {
			this.columnRange = columnRange;
		}
		
		private Title() {
		}
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public int getSplitRow() {
			return splitRow;
		}
		
		public void setSplitRow(int splitRow) {
			this.splitRow = splitRow;
		}
		
		public int getRowRange() {
			return rowRange;
		}
		
		private void setRowRange(int rowRange) {
			this.rowRange = rowRange;
		}
		
		public int getTotalRows() {
			return this.splitRow + this.rowRange;
		}
		
	}
	
	/**
	 * 合并列
	 * 
	 * @author Administrator
	 *
	 */
	public static class DataMerge {// 数据合并
		private int startColumn;
		private int columnRange = 1;
		private int startRow;
		private int rowRange = 1;
		private Object showValue;
		private short align = HSSFCellStyle.ALIGN_CENTER;
		
		public short getAlign() {
			return align;
		}
		
		public void setAlign(short align) {
			this.align = align;
		}
		
		public Object getShowValue() {
			return showValue;
		}
		
		public void setShowValue(Object showValue) {
			this.showValue = showValue;
		}
		
		public int getStartRow() {
			return startRow;
		}
		
		public void setStartRow(int startRow) {
			this.startRow = startRow;
		}
		
		public int getColumnRange() {
			return columnRange;
		}
		
		public DataMerge() {
		}
		
		public int getStartColumn() {
			return startColumn;
		}
		
		public void setStartColumn(int startColumn) {
			this.startColumn = startColumn;
		}
		
		public int getEndColumn() {
			return this.startColumn + this.columnRange - 1;
		}
		
		public void setColumnRange(int columnRange) {
			this.columnRange = columnRange;
		}
		
		public int getRowRange() {
			return this.rowRange;
		}
		
		public void addRowRange(int rowRange) {
			this.rowRange += rowRange;
		}
		
		public void setRowRange(int rowRange) {
			this.rowRange = rowRange;
		}
		
		public int getEndRow() {
			return this.startRow + this.rowRange - 1;
		}
		
		public boolean checkNeedMerge(int nowRow, int columnIndex) {
			if (nowRow >= this.getStartRow() && nowRow <= this.getEndRow()) {
				if (columnIndex >= this.getStartColumn() && columnIndex <= this.getEndColumn()) {
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * 附属信息
	 * 
	 * @author Administrator
	 *
	 */
	public static class Attached {// 附属信息
		private boolean hasAttached;
		private List<AttachedMap> datas;// 标题名
		private Integer splitRow;// 开始行
		private Integer splitColumn;// 开始列
		private String templateFileName;// 模板名称
		
		private Attached() {
		}
		
		public boolean isHasAttached() {
			return hasAttached;
		}
		
		public void setHasAttached(boolean hasAttached) {
			this.hasAttached = hasAttached;
		}
		
		public List<AttachedMap> getDatas() {
			return datas;
		}
		
		public void setDatas(List<AttachedMap> datas) {
			this.datas = datas;
		}
		
		public Integer getSplitRow() {
			return splitRow;
		}
		
		public void setSplitRow(Integer splitRow) {
			this.splitRow = splitRow;
		}
		
		public Integer getSplitColumn() {
			return splitColumn;
		}
		
		public void setSplitColumn(Integer splitColumn) {
			this.splitColumn = splitColumn;
		}
		
		public String getTemplateFileName() {
			return templateFileName;
		}
		
		public void setTemplateFileName(String templateFileName) {
			this.templateFileName = templateFileName;
		}
		
		public int getTotalRows() {
			return this.splitRow + (CollectionUtils.isNotEmpty(this.datas) && this.hasAttached ? datas.size() : 0);
		}
	}
	
	public static class DropDownList {
		private int startRow;// 起始行
		private int endRow;// 结束行
		private int startCol;// 其实列
		private int endCol;// 结束列
		private String[] textList;
		
		public DropDownList(int startRow, int endRow, int startCol, int endCol, String[] textList) {
			this.startRow = startRow;
			this.endRow = endRow;
			this.startCol = startCol;
			this.endCol = endCol;
			this.textList = textList;
		}
		
		public int getStartRow() {
			return startRow;
		}
		
		public void setStartRow(int startRow) {
			this.startRow = startRow;
		}
		
		public int getEndRow() {
			return endRow;
		}
		
		public void setEndRow(int endRow) {
			this.endRow = endRow;
		}
		
		public int getStartCol() {
			return startCol;
		}
		
		public void setStartCol(int startCol) {
			this.startCol = startCol;
		}
		
		public int getEndCol() {
			return endCol;
		}
		
		public void setEndCol(int endCol) {
			this.endCol = endCol;
		}
		
		public String[] getTextList() {
			return textList;
		}
		
		public void setTextList(String[] textList) {
			this.textList = textList;
		}
	}
	
	/**
	 * 特殊行
	 * 
	 * @author Administrator
	 *
	 */
	public static class SpecialRow {
		/** 开始行，从0开始 */
		private int splitRow;
		/** 开始列，从0开始 */
		private int splitCol;
		/** 需要插入的数据 */
		private List<String> rowData;
		/** excel样式 */
		private HSSFCellStyle cellStyle;
		private boolean isCellStyle = true;// 是否需要格式
		
		public boolean getIsCellStyle() {
			return isCellStyle;
		}
		
		public void setIsCellStyle(boolean isCellStyle) {
			this.isCellStyle = isCellStyle;
		}
		
		public int getSplitRow() {
			return splitRow;
		}
		
		public void setSplitRow(int splitRow) {
			this.splitRow = splitRow;
		}
		
		public List<String> getRowData() {
			return rowData;
		}
		
		public void setRowData(List<String> rowData) {
			this.rowData = rowData;
		}
		
		public int getSplitCol() {
			return splitCol;
		}
		
		public void setSplitCol(int splitCol) {
			this.splitCol = splitCol;
		}
		
		public HSSFCellStyle getCellStyle() {
			return cellStyle;
		}
		
		public void setCellStyle(HSSFCellStyle cellStyle) {
			this.cellStyle = cellStyle;
		}
	}
	
	/**
	 * 列头信息
	 * 
	 * @author Administrator
	 *
	 */
	public static class Column {// 列头信息
		private boolean noColumn;// 是否包含列头
		private int splitRow;// 隔行
		private boolean hasColumnData;// 是否有列头数据
		private List<String> columnNames;// 判断列头的数据
		
		public boolean isHasColumnData() {
			return hasColumnData;
		}
		
		public void setHasColumnData(boolean hasColumnData) {
			this.hasColumnData = hasColumnData;
		}
		
		public List<String> getColumnNames() {
			return columnNames;
		}
		
		public void setColumnNames(List<String> columnNames) {
			this.columnNames = columnNames;
		}
		
		public Column() {
		}
		
		public boolean isNoColumn() {
			return noColumn;
		}
		
		public void setNoColumn(boolean noColumn) {
			this.noColumn = noColumn;
		}
		
		public int getSplitRow() {
			return splitRow;
		}
		
		public void setSplitRow(int splitRow) {
			this.splitRow = splitRow;
		}
	}
	
	// 特殊的cell
	public static class SpecialCell {
		private String value;
		private int row;// 起始行
		private int rowRange = 1;// 占行数
		private int column;// 起始列
		private int columnRange = 1;// 占列数
		private STYLE_KEY styleKey = STYLE_KEY.DATA_STRING_LEFT;// 对齐方式
		
		public STYLE_KEY getStyleKey() {
			return styleKey;
		}
		
		public void setStyleKey(STYLE_KEY styleKey) {
			this.styleKey = styleKey;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public int getRow() {
			return row;
		}
		
		public void setRow(int row) {
			this.row = row;
		}
		
		public int getRowRange() {
			return rowRange;
		}
		
		public void setRowRange(int rowRange) {
			this.rowRange = rowRange;
		}
		
		public int getColumn() {
			return column;
		}
		
		public void setColumn(int column) {
			this.column = column;
		}
		
		public int getColumnRange() {
			return columnRange;
		}
		
		public void setColumnRange(int columnRange) {
			this.columnRange = columnRange;
		}
		
		public int getEndRow() {
			return row + rowRange - 1;
		}
		
		public int getEndColumn() {
			return column + columnRange - 1;
		}
		
		public SpecialCell(String value, int row, int rowRange, int column, int columnRange) {
			super();
			this.value = value;
			this.row = row;
			this.rowRange = rowRange;
			this.column = column;
			this.columnRange = columnRange;
		}
		
		public SpecialCell(String value, int row, int rowRange, int column, int columnRange, STYLE_KEY styleKey) {
			super();
			this.value = value;
			this.row = row;
			this.rowRange = rowRange;
			this.column = column;
			this.columnRange = columnRange;
			this.styleKey = styleKey;
		}
	}
	
	/***************** 大众化的获取类型 ************************/
	/**
	 * @param clazz 类class
	 * @param rows 导出的数据
	 * @param sheetName sheet页名
	 * @param isNo 是否有序号
	 * @param isGuide 是否导出指引
	 * @param startColumn 开始列
	 * @return
	 */
	public static <T> FlexibleExcelHelper getHelper(Class<T> clazz, List<T> rows, String sheetName, boolean isNo,
			boolean isGuide, int startColumn) {
		FlexibleExcelHelper helper = new FlexibleExcelHelper();
		helper.setClazz(clazz);
		helper.setDatas(rows);
		helper.setStartColumn(startColumn);
		helper.setNo(isNo);
		helper.setGuide(isGuide);
		helper.setSheetName(sheetName);
		return helper;
	}
	
	public static FlexibleExcelHelper getHelper(Class<?> clazz, List<?> rows, String sheetName, boolean isNo,
			boolean isGuide, Integer startColumn) {
		FlexibleExcelHelper helper = new FlexibleExcelHelper();
		helper.setClazz(clazz);
		helper.setDatas(rows);
		helper.setStartColumn(startColumn);
		helper.setNo(isNo);
		helper.setGuide(isGuide);
		helper.setSheetName(sheetName);
		return helper;
	}
	
	public List<SpecialRow> getSpecialRows() {
		return specialRows;
	}
	
	public void setSpecialRows(List<SpecialRow> specialRows) {
		this.specialRows = specialRows;
	}
	
	public List<DropDownList> getDropDownList() {
		return dropDownList;
	}
	
	public void setDropDownList(List<DropDownList> dropDownList) {
		this.dropDownList = dropDownList;
	}
	
	public boolean getIsCascade() {
		return isCascade;
	}
	
	public void setIsCascade(boolean isCascade) {
		this.isCascade = isCascade;
	}
	
	/**
	 * 拆分单元格
	 * 
	 * @param sheet 需要拆分的Excel表
	 * @param refCell 需要拆分的单元格的引用地址(如：A3)
	 * @param value 拆分后每个单元格的值
	 */
	public void splitMergedCell(HSSFSheet sheet, String refCell, String value) {
		CellReference ref = new CellReference(refCell);
		// 遍历sheet中的所有的合并区域
		for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--) {
			CellRangeAddress region = sheet.getMergedRegion(i);
			HSSFRow firstRow = sheet.getRow(region.getFirstRow());
			HSSFCell firstCellOfFirstRow = firstRow.getCell(region.getFirstColumn());
			// 如果第一个单元格的是字符串
			if (firstCellOfFirstRow.getCellType() == Cell.CELL_TYPE_STRING) {
				value = firstCellOfFirstRow.getStringCellValue();
			}
			// 判断到指定的单元格才进行拆分单元格
			if (region.getFirstRow() == ref.getRow() && region.getLastColumn() == ref.getCol()) {
				sheet.removeMergedRegion(i);
			}
			
			// 设置第一行的值为，拆分后的每一行的值
			for (Row row : sheet) {
				for (Cell cell : row) {
					if (region.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(value);
					}
				}
			}
		}
	}
	
	/**
	 * 获取主键主键
	 * 
	 * @return
	 */
	public Field getMainKeyField() {
		Field keyField = null;
		try {
			if (StringUtils.isNotEmpty(this.mainKey)) {
				keyField = clazz.getDeclaredField(mainKey);
				keyField.setAccessible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyField;
	}
	
}
