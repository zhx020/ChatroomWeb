package com.su.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumUtil {
	/**
	 * 两数相除
	 * @param temp
	 * @param times
	 * @return
	 */
	public static Double div(Double temp, double times) {
		BigDecimal d1 = new BigDecimal(temp);
		BigDecimal d2 = new BigDecimal(times);
		return d1.divide(d2,2,RoundingMode.HALF_UP).doubleValue();
	}

}
