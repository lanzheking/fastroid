package com.honestwalker.androidutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import com.honestwalker.androidutils.IO.LogCat;

public class FileEncrypt {

	public static void encrypt(String fileUrl, String key) throws Exception {
		File file = new File(fileUrl);
		String path = file.getPath();
		if (!file.exists()) {
			return;
		}
		int index = path.lastIndexOf("/");
		String destFile = path.substring(0, index) + "//" + "abc";
		File dest = new File(destFile);
		InputStream in = new FileInputStream(fileUrl);
		OutputStream out = new FileOutputStream(destFile);
		byte[] buffer = new byte[1024];
		int r;
		byte[] buffer2 = new byte[1024];
		while ((r = in.read(buffer)) > 0) {
			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
				buffer2[i] = b == 255 ? 0 : ++b;
			}
			out.write(buffer2, 0, r);
			out.flush();
		}
		in.close();
		out.close();
		file.delete();
		dest.renameTo(new File(fileUrl));
		appendMethodA(fileUrl, MD5.encrypt(key));
		System.out.println("加密成功");
	}

	public static void appendMethodA(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content.getBytes() + "");
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String decrypt(String fileUrl, String tempUrl, String key)
			throws Exception {
		File file = new File(fileUrl);
		if (!file.exists()) {
			return null;
		}
		File dest = new File(tempUrl);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

		InputStream is = new FileInputStream(fileUrl);
		OutputStream out = new FileOutputStream(tempUrl);

		byte[] buffer = new byte[1024];
		byte[] buffer2 = new byte[1024];
		byte bMax = (byte) 255;
		long size = file.length() - MD5.encrypt(key).length();
		int mod = (int) (size % 1024);
		int div = (int) (size >> 10);
		int count = mod == 0 ? div : (div + 1);
		int k = 1, r;
		while ((k <= count && (r = is.read(buffer)) > 0)) {
			if (mod != 0 && k == count) {
				r = mod;
			}

			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
				buffer2[i] = b == 0 ? bMax : --b;
			}
			out.write(buffer2, 0, r);
			k++;
		}
		out.close();
		is.close();
		return tempUrl;
	}
	
	public static String decrypt(InputStream is, String tempUrl, String key)
			throws Exception {
		
		OutputStream out = new FileOutputStream(tempUrl);

		byte[] buffer = new byte[1024];
		byte[] buffer2 = new byte[1024];
		byte bMax = (byte) 255;
		long size = is.available() - ((key.getBytes() + "").length());
		int mod = (int) (size % 1024);
		int div = (int) (size >> 10);
		int count = mod == 0 ? div : (div + 1);
		int k = 1, r;
		while ((k <= count && (r = is.read(buffer)) > 0)) {
			if (mod != 0 && k == count) {
				r = mod;
			}

			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
				buffer2[i] = b == 0 ? bMax : --b;
			}
			out.write(buffer2, 0, r);
			k++;
		}
		out.close();
		is.close();
		LogCat.d("CONFIG", tempUrl + " 输出 " + new File(tempUrl).exists() );
		return tempUrl;
	}
	
}
