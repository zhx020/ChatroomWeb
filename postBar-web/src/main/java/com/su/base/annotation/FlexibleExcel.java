package com.su.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
 * 用于灵活报表导出注解
 * 
 * @Description: 暂时可支持特殊文字替换，YYYY和MM会被替换成出入日期对应的年和月份
 * @author hxw 
 * @date 2017年7月14日 下午1:36:49
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlexibleExcel {
	/**
	 * 列头名称
	 * 
	 * @return
	 */
	public String column() default "";
	
	/**
	 * 在excel中的位置，必填，从零开始（因为api对应的起始值是0）
	 * 
	 * @return
	 */
	public int order();
	/**
	 * 是否开启转换，开启（数字才能用这功能）的话，下面两个属性才有效
	 * 
	 * @return
	 */
	public boolean isTran() default false;
	/**
	 * 转换的源数据
	 * 
	 * @return
	 */
	public double tranOra() default 10000000000.00;
	
	/**
	 * 转换的目标数据：如果改字段的值为10000000000的话，下载的时候为""
	 */
	public String tranTar() default "";
	
	/**
	 * 列头不写入到表格，主要解决合并明细列头的问题（具体可参考W20001009）
	 * 
	 * @return
	 */
	public boolean noWrite() default false;
	/**
	 * 父列头,多级列头，从上到下配置
	 * 
	 * @return
	 */
	public String[] ancestors() default {};
	/**
	 * 祖先列跨度，每一个父级列头的所占列，顺序要和ancestors一致
	 * 
	 * @return
	 */
	public int[] acceColumnRange() default {};
	
	/**
	 * 祖先行跨度，每一个父级列头的所占行，顺序要和ancestors一致
	 * 
	 * @return
	 */
	public int[] acceRowRange() default {};
	
	/**
	 * 列宽
	 * 
	 * @return
	 */
	public int width() default 25;
	
	/**
	 * 是否百分号展示，如果是0.1 则导出是后展示为10%
	 * 
	 * @return
	 */
	public boolean isPercent() default false;
	/**
	 * 可扩展的功能，一般会和参数关联，主要是针对不不同公司对统一字段的特殊处理，目前只扩展了两个
	 * 1.是否百分号显示，具体参考W20001001
	 * 2.字段选择导出（A公司C列显示field1，B公司要C列显示field2），具体参考月报核心表
	 * @return
	 */
	public String specialPercent() default "";
	
	/**
	 * 导出的保留小数位数，前提这个字段是数字才会考虑（如果需要乘以100的话则会乘以100后保留两位）前提这个字段是double类型
	 * 值为-1时，表示不处理小数位
	 * 
	 * @return
	 */
	public int fixPoint() default 2;
	
	/**
	 * 列跨度
	 * 
	 * @return
	 */
	public int columnRange() default 1;
	
	/**
	 * 行跨度
	 * 
	 * @return
	 */
	public int rowRange() default 1;
	
	/**
	 * 数据靠左靠右设置，默认靠右
	 * 
	 * @return
	 */
	public short align() default HSSFCellStyle.ALIGN_RIGHT;
	
	/**
	 * 数字的话，是否要乘倍数展示，主要解决单位问题
	 * 
	 * @return
	 */
	public double times() default 1;
	
	/**
	 * 日期显示格式，0 yyyy/MM/dd 1 yyyy-MM-dd 2 yyyy-MM-dd HH:mm:ss 3 YYYY.MM
	 */
	public int dateType() default 0;
	
	/**
	 * 该字段是否导出，主要也是为了解决公司个性化的问题。
	 */
	public boolean add() default true;
	
	/**
	 * 字符和日期类型字段为空时的默认值
	 */
	public String charDefault() default "";
	
	/**
	 * 数字字段为空时的默认值
	 */
	public String numDefault() default "0";
	/**
	 * 是否导出协会指引
	 * 
	 * @return
	 */
	public String guide() default "";
	
	/**
	 * 是否存在针对某张报表这个导出，具体可以参考报表枚举的hasLoseFied设置说明
	 */
	public boolean isLoseField() default false;
	/**
	 * 是否通过值合并，如果为true的话，相邻行如果该列值且mainKey（FlexibleExcelHelper的和并列参数中有）对应的值都一样的话，则会合并
	 */
	public boolean isMergeByValue() default false;
	
	/**
	 * 是否跳过此字段：和specialPercent扩展点2一样，只不过这个是用作模板导出时候使用（两个导出是两个人做的）
	 */
	public boolean isFilterField() default false;
	/**
	 * 字体颜色：可通过表达来配置：<0:10 的意思为如果这个值<0的则 给红色(默认的)，目前支持的解析可以查看CaculateColorUtils
	 */
	public String[] dataColors() default {"<0:10"};//IndexedColors.RED.getIndex() = 10 DownLoadExcelUtils.LESS
	/**
	 * 背景色:和字体规则一样
	 */
	public String[] backGroundColors() default {};//IndexedColors.RED.getIndex() = 10
}
