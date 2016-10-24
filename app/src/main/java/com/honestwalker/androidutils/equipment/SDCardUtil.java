package com.honestwalker.androidutils.equipment;

import java.io.File;

import android.os.Environment;

/**
 * SD卡工具类
 * 
 * @author Administrator
 * 
 */
public class SDCardUtil {

	/**
	 * 判断指定文件在SD卡上是否存在 （无序指定SD卡ROOT路径如 /image/file.txt 代表SD卡下的image/file.txt文件） 
	 * @param filePath
	 * @return
	 */
	public static Boolean exists(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 获取SD卡跟路径，路径最后已经包括了 /
	 * 
	 * @return
	 */
	public static String getSDRootPath() {
		return Environment.getExternalStorageDirectory().getPath() + "/";
	}

	/**
	 * 是否存在sd卡
	 * @return
	 */
	public static Boolean existsSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 建立文件夹
	 * @param path
	 * @return
	 */

	public static Boolean createFolder(String path) {
		File file = new File(path);
		return file.mkdirs();
	}

	/**
	 * �?��SD卡是否存�?	 * 
	 * @return
	 */
	public static Boolean hasSDCard() {
		File file = new File(getSDRootPath());
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除指定路径下的文件
	 * @param filepath  文件夹路径
	 * @param deleteDir 是否联通文件夹一起删除
	 */
	public static void delAllFile(String filepath , boolean deleteDir) {   
		File f = new File(filepath);//定义文件路径          
		if(f.exists() && f.isDirectory()){//判断是文件还是目录   
			if(f.listFiles().length==0 && deleteDir){//若目录下没有文件则直接删除   
				f.delete();   
			}else{//若有则把文件放进数组，并判断是否有下级目录   
				File delFile[]=f.listFiles();   
				int i =f.listFiles().length;   
				for(int j=0;j<i;j++){   
					if(delFile[j].isDirectory()){   
						delAllFile(delFile[j].getAbsolutePath() , false);//递归调用del方法并取得子目录路径   
					}
					delFile[j].delete();//删除文件   
				}   
		    }
		}      
	}
	
}
