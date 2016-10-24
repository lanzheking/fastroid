package com.honestwalker.androidutils;

import java.math.BigDecimal;

public class DoubleUtil {

	
	private DoubleUtil(){}
	
	public static double div(double v1,double v2,int scale) {
    	if(scale <=0 ) {
    		return (int)v1/v2;
    	}
    	BigDecimal b1 = new BigDecimal(v1);
    	BigDecimal b2 = new BigDecimal(v2);
    	return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
