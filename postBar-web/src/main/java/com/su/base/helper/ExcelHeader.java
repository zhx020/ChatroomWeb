package com.su.base.helper;

/**
 * 导出excel祖先代实体
 * 
 * @Description:
 * @author hxw xinwei.huang@wescxx.com
 * @date 2017年7月14日 下午3:05:18
 * @version V1.0
 */
public class ExcelHeader {
	private String column;// 列名
	private int columnRange;// 列占比，一一般是用在父列上
	private int rowRange;// 行占比
	private int startColumn;// 起始列
	private int startRow;// 起始行
	private boolean noWrite;
	
	public boolean isNoWrite() {
		return noWrite;
	}
	
	public void setNoWrite(boolean noWrite) {
		this.noWrite = noWrite;
	}
	
	public String getColumn() {
		return column;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public int getColumnRange() {
		return columnRange;
	}
	
	public void setColumnRange(int columnRange) {
		this.columnRange = columnRange;
	}
	
	public int getRowRange() {
		return rowRange;
	}
	
	public void setRowRange(int rowRange) {
		this.rowRange = rowRange;
	}
	
	public int getStartColumn() {
		return startColumn;
	}
	
	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}
	
	public int getStartRow() {
		return startRow;
	}
	
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	public int getEndRow() {
		return this.startRow + this.rowRange - 1;
	}
	
	public int getEndColumn() {
		return this.startColumn + this.columnRange - 1;
	}
}
