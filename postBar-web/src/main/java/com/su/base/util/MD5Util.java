package com.su.base.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	public final static String KEY = "ASKLDJAD";
	public static String md5(String text) {
		if(text == null) { return null;}
		return DigestUtils.md5Hex(text + KEY);
	}
}
