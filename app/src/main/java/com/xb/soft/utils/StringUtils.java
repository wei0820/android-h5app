package com.xb.soft.utils;


public class StringUtils {

	public static final boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty() || str.equals("null");
	}
}
