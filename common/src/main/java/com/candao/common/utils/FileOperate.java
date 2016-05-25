package com.candao.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.candao.common.exception.SysException;

/**
 * @Description: 文件操作类
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年5月6日下午6:48:05
 * @version 1.0
 */
public class FileOperate {
	/**
	 * 
	 * @Description:创建File
	 * @create: 余城序
	 * @Modification:
	 * @param path
	 *            文件存放路径
	 * @param fileName
	 *            文件名称
	 * @return File 文件file
	 * @throws IOException
	 */
	public static File createFile(String path, String fileName)
			throws SysException {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		path = path + fileName;
		file = new File(path);
		return file;
	}

	/**
	 * 
	 * @Description:是否存在该路径
	 * @create: 余城序
	 * @Modification:
	 * @param path
	 *            绝对路径
	 * @return boolean
	 */
	public static boolean isExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static void main(String args[]) throws IOException {
		/*
		 * File file = createFile("D:/data","/data.gz"); String str =
		 * "fsdsaaaagsdfgrgfsddsgsdf发放速度跟房东说方式方式方式大概是否大商股份是fdsafasdjkkikjhfds";
		 * ByteArrayOutputStream byOut = new ByteArrayOutputStream();
		 * GZIPOutputStream zipOut = new GZIPOutputStream(new
		 * FileOutputStream(file)); File file2 = new File("D:/data/test.txt");
		 * InputStream in = new FileInputStream(file2); int index; while((index
		 * = in.read()) != -1){ zipOut.write(index); } zipOut.close(); byte[] by
		 * = byOut.toByteArray(); byOut.close();
		 */

		/*
		 * File file2 = new File("D:/data/test.txt"); long lon =
		 * file2.lastModified(); Date date = new Date(lon); SimpleDateFormat sdf
		 * = new SimpleDateFormat("yyyyMMdd");
		 * System.out.println(sdf.format(date));
		 */

		System.out.println(isExist("D:/data/testdsa.txt"));

	}

}
