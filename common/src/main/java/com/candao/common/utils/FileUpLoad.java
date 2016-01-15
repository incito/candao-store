package com.candao.common.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FileUpLoad {
	// 文件允许格式
private String[] allowFiles = { ".swf", ".wmv", ".flv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg", ".ogg", ".mov", ".wmv", ".mp4"};
private String url = "";
private String state = "";
private String originalName = "";
public void upload(MultipartFile file,String url){
	String fileName = file.getOriginalFilename();
	if(!checkFileType(fileName)){//非法上传格式
		return;
	}
	fileName=getName(fileName);
	String folderUrl=getFolder(url);
	SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
	this.url += "upload/" + formater.format(new Date())+"/"+fileName;
	File outFile = new File(folderUrl+File.separatorChar+fileName);
	try {
		file.transferTo(outFile);
	} catch (IllegalStateException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

/**
 * 获取文件扩展名
 * 
 * @return string
 */
private  String getFileExt(String fileName) {
	return fileName.substring(fileName.lastIndexOf("."));
}

/**
 * 文件类型判断
 * 
 * @param fileName
 * @return
 */
private boolean checkFileType(String fileName) {
	Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
	while (type.hasNext()) {
		String ext = type.next();
		if (fileName.toLowerCase().endsWith(ext)) {
			return true;
		}
	}
	return false;
}


/**
 * 依据原始文件名生成新文件名
 * 
 * @return
 */
private String getName(String fileName) {
	Random random = new Random();
	return  random.nextInt(10000)
			+ System.currentTimeMillis() + this.getFileExt(fileName);
}


/**
 * 根据字符串创建本地目录 并按照日期建立子目录返回
 * 
 * @param path
 * @return
 */

private String getFolder(String path) {
	SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
	path += File.separatorChar + formater.format(new Date());
	File dir = new File(path);
	if (!dir.exists()) {
		try {
			dir.mkdirs();
		} catch (Exception e) {
			return "";
		}
	}
	return path;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}

public String getOriginalName() {
	return originalName;
}

public void setOriginalName(String originalName) {
	this.originalName = originalName;
}

}
