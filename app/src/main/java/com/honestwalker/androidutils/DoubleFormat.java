package com.honestwalker.androidutils;

import java.text.DecimalFormat;

/**
 * �������ʽ��
 * @author langel
 *
 */
public class DoubleFormat {
	public static String format(Double price) {
		if(price == null) {
			return "0.0";
		} else {
			DecimalFormat df = new DecimalFormat("0.00");
			return df.format(price);
		}
	}
}
