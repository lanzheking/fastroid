package com.honestwalker.androidutils.IO;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import com.honestwalker.androidutils.exception.ExceptionUtil;

/**
 * 文本输入，输出工具类
 */
public class FileIO {
	
	/**
	 * 保存文件
	 * @param buf
	 * @param filePath
	 * @param fileName
	 */
	public static void saveFile(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			if(!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					ExceptionUtil.showException(e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					ExceptionUtil.showException(e);
				}
			}
		}
	}
	
	/**
	 * 保存文件，
	 * @param file 包含文件名的file对象
	 */
	public static void saveFile(File file) {
		saveFile(fileToBytes(file) , file.getParent().toString() , file.getName());
	}
	
	/** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] fileToBytes(File filePath){  
        byte[] buffer = null;  
        try {  
            FileInputStream fis = new FileInputStream(filePath);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);  
            byte[] b = new byte[1024];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }

	public static void fileCopy(String srcPath, String desPath) throws Exception {
		fileCopy(new File(srcPath), new File(desPath));
	}

	public static void fileCopy(File srcFile , File desFile) throws Exception {
		byte[] data = fileToBytes(srcFile);
		saveFile(data , desFile.getPath().replace(desFile.getName() , "") , desFile.getName());
	}

	public static void deleteFile(String file) {
		try {
			new File(file).delete();
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
	}

    /**
     * 读取文件
     *
     * @param filePath 文件路径
     */
    public static String readFile(String filePath) {
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                StringBuffer result = new StringBuffer();
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    result.append(lineTxt).append("\r\n");
                }
                read.close();
                bufferedReader.close();
                return result.toString();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return "";
    }

    public static void write(String fileName, String content) {
        write(fileName, content, false);
    }

    private static void write(String fileName, String content, boolean append) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            File file = new File(fileName);
            if(!file.exists()) file.createNewFile();
            FileWriter writer = new FileWriter(file, append);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法追加文件：使用FileWriter
     */
    public static void append(String fileName, String content) {
        write(fileName, content, true);
    }

    /**
     * 删除目录下所有文件和目录
     * @param path
     */
    public static void removeAllFileAndDir(String path) {
        File parentDir = new File(path);
        if(!parentDir.exists() || !parentDir.isDirectory()) return;
        File[] subFiles = parentDir.listFiles();
        for (File subFile : subFiles) {
            try {
                if(subFile.isDirectory()) {
                    deleteDir(subFile);
                } else {
                    subFile.delete();
                }
            } catch (Exception e) {

            }
        }
    }

    public static void deleteDir(File dir){
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            for(int i=0; i<files.length; i++) {
                deleteDir(files[i]);
            }
        }
        dir.delete();
    }

    public static String getMd5ByFile(InputStream is, File cacheFile) throws FileNotFoundException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            if(cacheFile.exists()) cacheFile.delete();
            cacheFile.createNewFile();
            fos = new FileOutputStream(cacheFile);
            while((nbread = is.read(data))>-1) {
                fos.write(data,0,nbread);
            }
            String fileMD5 = FileIO.getMd5ByFile(cacheFile);
            LogCat.d("FILE", cacheFile.getPath() + "  " + fileMD5);
            return fileMD5;
        } catch (Exception ex) {
        } finally{
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

}
