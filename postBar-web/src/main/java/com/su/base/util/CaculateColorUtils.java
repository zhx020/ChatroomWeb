package com.su.base.util;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.IndexedColors;

public class CaculateColorUtils {
	public static final short DEFAULT_DATA_COLOR = IndexedColors.BLACK.getIndex();
	public static final short DEFAULT_BACKAGE_COLOR = IndexedColors.WHITE.getIndex();
	public final static String LESS = "<";
	public final static String LESS_EQUAL = "<=";
	public final static String EQUAL = "=";
	public final static String GREATER_EQUAL = ">=";
	public final static String GREATERL = ">";
	public final static String NO_EQQUAL = "!=";
	public final static String COLON = ":";
	public final static String AND = "&&";
	public final static String OR = "OR";
	public final static String MUL = "*";
	public final static String ADD = "+";
	/**
	 * 解析表达式获取值
	 * @param express
	 * @param value
	 * @return
	 */
	public static Short getColor(String[] express,Object value,Object modle){
		if(value == null){
			value = "0";
		}
		if(express != null){
			try {
				for(String dataColorRule : express){//<0:10
					String[] dataColor = dataColorRule.split(COLON);
					String data = dataColor[0];//设置的表达式
					short color = Short.parseShort(dataColor[1]);//展示的颜色
					if(value.getClass().equals(String.class)){//TODO  可以考虑优化为工厂模式或者模板方法
						 if(checkStringExpressColor(value, data)){
							 return color;
						 }
					} else {
						if(checkNumberExpressColor(value, modle,data)){
							 return color;
						 }
					}
				}
			} catch (Exception e) {}
		}
		return null;
	}
	//解析字符串表达式color
	private static boolean checkStringExpressColor(Object value, String data) {
		String sreal = value.toString();
		if(data.contains(EQUAL)){
			String compareData = data.split(EQUAL)[1];
			if(sreal.equals(compareData)){
				return true;
			}
		}
		return false;
	}
	//解析数据表达式获取颜色
	private static boolean checkNumberExpressColor(Object value, Object modle, String data) {
		double real = Double.parseDouble(value.toString());//列的值
		String[] strCheckDatas = null;//比较的值
		if(data.contains(LESS)){//如果是小于的话
			strCheckDatas = data.split(LESS)[1].split("\\" +MUL);
		} else if(data.contains(GREATER_EQUAL)){//如果是大于的话
			strCheckDatas = data.split(GREATER_EQUAL)[1].split("\\" + MUL);
		} else if(data.contains(GREATERL)){//如果是大于的话
			strCheckDatas = data.split(GREATERL)[1].split("\\" + MUL);
		}
		double[] values = replaceValue(strCheckDatas,modle);//替换值
		double compareData = 1.0;//计算比较之后的值
		for(double d : values){
			compareData *= d;
		}
		if(data.contains(LESS)){
			if(real < compareData){
				return true;
			}
		} else if(data.contains(GREATER_EQUAL)){
			if(real >= compareData){
				return true;
			}
		} else if(data.contains(GREATERL)){//add by yixin
			if (real > compareData) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 替换字段值
	 * @param strCheckDatas
	 * @param modle
	 * @return
	 */
	private static double[] replaceValue(String[] strCheckDatas, Object modle) {
		if(strCheckDatas == null){
			strCheckDatas = new String[0];
		}
		double[] results = new double[strCheckDatas.length];
		if(modle != null){
			Class<?> clazz = modle.getClass();
			int i = 0;
			for (String strCheckData:strCheckDatas) {
				try {
					Field field = clazz.getDeclaredField(strCheckData);
					field.setAccessible(true);
					Object object = field.get(modle);
					if(object != null){
						strCheckDatas[i] = object.toString();
					} else {
						strCheckDatas[i] = "0";
					}
				} catch (Exception e) {}
				try {
					double d = Double.parseDouble(strCheckDatas[i]);
					results[i] = d;
				} catch (Exception e) {}
				i ++;
			}
		}
		
		return results;
	}
	
}
