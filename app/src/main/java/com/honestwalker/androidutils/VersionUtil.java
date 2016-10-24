package com.honestwalker.androidutils;

import com.honestwalker.androidutils.exception.VersionException;

public class VersionUtil {
	public static Boolean versionNewer(String version1,String version2) throws VersionException {
		String version1Str = getVersionString(version1);
		String version2Str = getVersionString(version2);
		String[] vChar1	   = version1Str.split("[.]");
		String[] vChar2	   = version2Str.split("[.]");
		int[] vInt1 = new int[vChar1.length];
		int[] vInt2 = new int[vChar2.length];
		for(int i=0;i<vChar1.length;i++) {
			try {
				vInt1[i] = Integer.parseInt(vChar1[i]);
			} catch (Exception e) {
				throw new VersionException();
			}
		}
		for(int i=0;i<vChar2.length;i++) {
			try {
				vInt2[i] = Integer.parseInt(vChar2[i]);
			} catch (Exception e) {
				throw new VersionException();
			}
		}
		Boolean result = false;
		for(int i=0;i<vInt1.length;i++) {
			if(i < vInt2.length) {
				if(vInt1[i] > vInt2[i]) {
					result = true;
				}
			}
		}
		return result;
	}
	
	private static String getVersionString(String version) throws VersionException {
		if(version == null || version.length() == 0 || version.startsWith(".") || version.endsWith(".")) {
			throw new VersionException();
		} else {
			
			StringBuffer sb = new StringBuffer("");
			int startPoint  = version.indexOf(".");			// 第一个.位置
			int endPoint    = version.lastIndexOf(".");	// 最后一个.位置
			char[] chs = version.toCharArray();
			int vStart = 0;
			int vEnd   = version.length() - 1;
			for(int i=startPoint-1;i>=0;i--) {	// 从第一个.前找数字
				try{
					Integer.parseInt(chs[i] + "");
					vStart = i;
				} catch (Exception e) {
					break;
				}
			}
			for(int i=endPoint + 1;i<version.length();i++) {	// 从第一个.前找数字
				try{
					Integer.parseInt(chs[i] + "");
					vEnd = i;
				} catch (Exception e) {
					System.out.println(chs[i] + "不能转换" + " i=" + i);
					break;
				}
			}
			return version.substring(vStart,vEnd + 1);
		}
	}
}
