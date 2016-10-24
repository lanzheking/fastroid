package com.honestwalker.androidutils.IO;

import java.io.File;
import java.io.FileOutputStream;

import com.honestwalker.androidutils.equipment.SDCardUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;

public class LogTxt {
	
	public static String defaultPath = SDCardUtil.getSDRootPath() + "kancart/";
	
	/**
	 * 写入文本，覆盖
	 * @param fileName 格式： api/log.txt 或 log.txt 输出目录位于 .KanCart\app名\ fileName
	 * @param content
	 */
	public static void write(String dir,String fileName,String content) {
		write(dir,fileName,content,false);
	}
	
	/**
	 * 写入本文，追加
	 * @param fileName 格式： api/log.txt 或 log.txt , 输出目录位于 .KanCart\app名\ fileName
	 * @param content
	 */
	public static void appent(String dir,String fileName, String content) {
		write(dir,fileName, content,true);
	}
	
	private static void write(String dir,String fileName,String content,Boolean appent) {
		if(dir == null) {
			dir = "";
		}
		if(!dir.endsWith("/")) {
			dir += "/";
		}
		if(LogCat.showLog) {
			if(content == null) {
				content = "";
			}
			if(!content.endsWith("\r\n")) {
				content += "\r\n";
			}
			if(fileName.contains("/")) {	// 如果fileName 包含/ 作为路径分隔，最后一个是文件名
				String[] filePath = fileName.split("/");
				for(int i=0;i<filePath.length;i++) {
					if(i != filePath.length - 1) {
						dir += filePath[i] + "/";
					} else {
						fileName = filePath[i];
					}
				}
			}
			try {
				SDCardUtil.createFolder(dir);
				File file = new File(dir + fileName+".txt");
				if(!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos;
				fos = new FileOutputStream(file,appent);
				fos.write(content.getBytes());
				fos.flush();
				fos.close();
			} catch (Exception e) {
				ExceptionUtil.showException(e);
			}
		}
	}
}