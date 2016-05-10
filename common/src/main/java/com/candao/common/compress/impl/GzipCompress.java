package com.candao.common.compress.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.common.compress.ICompress;
import com.candao.common.enums.ErrorMessage;
import com.candao.common.enums.Module;
import com.candao.common.exception.SysException;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.FileOperate;
import com.candao.common.utils.PropertiesUtils;

/**
 * @Description: gzip压缩和解压方式类
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年5月4日下午2:31:38
 * @version 1.0
 */
public class GzipCompress implements ICompress{
	
	Logger logger = LoggerFactory.getLogger(GzipCompress.class);
	
	/**
	 * 
	 * @Description:压缩字符串方法
	 * @create: 余城序
	 * @Modification:
	 * param:需要被压缩的字符串
	 * @return String 被压缩过后的字符串
	 */
	@Override
	public byte[] getCompress(String str) throws SysException {
		logger.info("getCompress-start:"+str);
		
		GZIPOutputStream gzip = null;
		ByteArrayOutputStream out = null;
		
		try {
			out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			//压缩文件
			gzip.write(str.toString().getBytes());
		} catch (IOException e) {
			logger.error("解压出现异常",e);
			throw new SysException(ErrorMessage.GINZIP_READ, Module.LOCAL_SHOP);
		}finally{
			try{
				if(gzip != null)
					gzip.close();
				if(out != null)
					out.close();
			}catch(IOException e){
				logger.error("关闭数据流失败",e);
				throw new SysException(ErrorMessage.IO_CLOSE,Module.LOCAL_SHOP);
			}
		}
		
		byte[] value = out.toByteArray();
		
		logger.info("getCompress-end:"+value);
		return value;
	}
	/**
	 * 
	 * @Description:解压被压缩的字符串
	 * @create: 余城序
	 * @Modification:
	 * @param by 被压缩过的byte数组
	 * @return String 解压过后的字符串
	 */
	@Override
	public String getUnCompress(byte[] bt) throws SysException{
		logger.info("getUnCompress-start:"+bt);
		ByteArrayInputStream in = new ByteArrayInputStream(bt);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream inGzip = null;
		try {
			inGzip = new GZIPInputStream(in);
			byte[] by = new byte[1024];
			int index = -1;
			while((index = inGzip.read(by)) != -1){
				out.write(by,0,index);
				out.flush();
			}
			String value = out.toString();
			logger.info("getUnCompress-end:"+value);
			return value;
		} catch (IOException e) {
			logger.error("解压出现异常",e);
			throw new SysException(ErrorMessage.GINZIP_READ, Module.LOCAL_SHOP);
		}finally{
			try{
				if(inGzip != null)
					inGzip.close();
				out.close();
			}catch(IOException e){
				logger.error("关闭数据流失败",e);
				throw new SysException(ErrorMessage.IO_CLOSE,Module.LOCAL_SHOP);
			}
		}
	}
	
	@Override
	public String getUnCompress(File file) throws SysException {
		logger.info("getUnCompress-start:"+file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream inGzip = null;
		try {
			inGzip = new GZIPInputStream(new FileInputStream(file));
			byte[] by = new byte[1024];
			int index = -1;
			while((index = inGzip.read(by)) != -1){
				out.write(by,0,index);
				out.flush();
			}
			String value = out.toString();
			logger.info("getUnCompress-end:"+value);
			return value;
		} catch (FileNotFoundException e){
			logger.error("没找到对应文件",e);
			throw new SysException(ErrorMessage.NO_EXIST_SYN_GZIP, Module.LOCAL_SHOP);
		} catch (IOException e) {
			logger.error("解压出现异常",e);
			throw new SysException(ErrorMessage.GINZIP_READ, Module.LOCAL_SHOP);
		}finally{
			try{
				if(inGzip != null)
					inGzip.close();
				out.close();
			}catch(IOException e){
				logger.error("关闭数据流失败",e);
				throw new SysException(ErrorMessage.IO_CLOSE,Module.LOCAL_SHOP);
			}
		}
	}
	
	@Override
	public void compressTo(String sql,File file) throws SysException {
		ByteArrayInputStream byteIn = null;
		GZIPOutputStream zipOut = null;
		BufferedOutputStream out = null;
		try{
			byteIn = new ByteArrayInputStream(sql.getBytes());
			out = new BufferedOutputStream(new FileOutputStream(file));
			zipOut = new GZIPOutputStream(out);
			byte[] by = new byte[1024];
			int index = -1;
			while((index = byteIn.read(by)) != -1){
				zipOut.write(by,0,index);
				zipOut.flush();
			}
		} catch (FileNotFoundException e){
			logger.error("没找到对应文件",e);
			throw new SysException(ErrorMessage.NO_EXIST_SYN_GZIP, Module.LOCAL_SHOP);
		} catch (IOException e) {
			logger.error("压缩出现异常",e);
			throw new SysException(ErrorMessage.GINZIP_WRITE, Module.LOCAL_SHOP);
		}finally{
			try{
				if(byteIn != null)
					byteIn.close();
				if(zipOut != null)
					zipOut.close();
			}catch(IOException e){
				logger.error("关闭数据流失败",e);
				throw new SysException(ErrorMessage.IO_CLOSE,Module.LOCAL_SHOP);
			}
		}
	}
	

}
