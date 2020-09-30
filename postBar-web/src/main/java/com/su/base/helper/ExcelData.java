package com.su.base.helper;

import java.lang.reflect.Field;

/**
 * 导出excel的帮助工具类
 * 
 * @Description:
 * @author hxw xinwei.huang@wescxx.com
 * @date 2017年7月14日 下午3:05:18
 * @version V1.0
 */
public class ExcelData {
	private Field field;// 字段名字
	private Integer order;// 列的位置
	private Integer width;// 列宽
	private boolean istran;// 是否转换
	private Double tranOra;// 转换源数据
	private String tranTar;// 目标数据
	private short align;// 靠左靠右 默认居中
	private double times;// 倍数
	private boolean isPercent;// 是否百分比
	private int fixPoint;// 保留小数位
	private int dateType;// 日期类型 0:yyyy/MM/dd 1:yyyy-MM-dd
	private boolean hasParent = false;// 是否有父列头
	private String charDefault;// 字符，日期，以及布尔的默认值
	private String numDefault;// 数字类型的默认值
	private String guide;
	private boolean isMergeByValue;
	private String[] dataColors;
	private String[] backGroundColors;
	
	public String[] getBackGroundColors() {
		return backGroundColors;
	}
	public String[] getDataColors() {
		return dataColors;
	}
	public void setDataColors(String[] dataColors) {
		this.dataColors = dataColors;
	}
	public void setBackGroundColors(String[] backGroundColors) {
		this.backGroundColors = backGroundColors;
	}
	public boolean isMergeByValue() {
		return isMergeByValue;
	}
	
	public void setMergeByValue(boolean isMergeByValue) {
		this.isMergeByValue = isMergeByValue;
	}
	
	public String getGuide() {
		return guide;
	}
	
	public void setGuide(String guide) {
		this.guide = guide;
	}
	
	public String getCharDefault() {
		return charDefault;
	}
	
	public void setCharDefault(String charDefault) {
		this.charDefault = charDefault;
	}
	
	public String getNumDefault() {
		return numDefault;
	}
	
	public void setNumDefault(String numDefault) {
		this.numDefault = numDefault;
	}
	
	public int getDateType() {
		return dateType;
	}
	
	public void setDateType(int dateType) {
		this.dateType = dateType;
	}
	
	public double getTimes() {
		return times;
	}
	
	public void setTimes(double times) {
		this.times = times;
	}
	
	public short getAlign() {
		return align;
	}
	
	public void setAlign(short align) {
		this.align = align;
	}
	
	public boolean isIstran() {
		return istran;
	}
	
	public void setIstran(boolean istran) {
		this.istran = istran;
	}
	
	public Double getTranOra() {
		return tranOra;
	}
	
	public void setTranOra(Double tranOra) {
		this.tranOra = tranOra;
	}
	
	public String getTranTar() {
		return tranTar;
	}
	
	public void setTranTar(String tranTar) {
		this.tranTar = tranTar;
	}
	
	public Field getField() {
		return field;
	}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	public Integer getOrder() {
		return order;
	}
	
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public Integer getWidth() {
		return width;
	}
	
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	public boolean isPercent() {
		return isPercent;
	}
	
	public void setPercent(boolean isPercent) {
		this.isPercent = isPercent;
	}
	
	public int getFixPoint() {
		return fixPoint;
	}
	
	public void setFixPoint(int fixPoint) {
		this.fixPoint = fixPoint;
	}
	
	public boolean isHasParent() {
		return hasParent;
	}
	
	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}
}
