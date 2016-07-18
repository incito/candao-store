package com.candao.common.utils;

import java.io.File;
import java.io.IOException;

import com.candao.common.compress.ICompress;
import com.candao.common.compress.impl.GzipCompress;
import com.candao.common.exception.SysException;

/**
 * 
 * @Description: 压缩方法操作
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年5月4日上午10:40:23
 * @version 1.0
 */
public class CompressOperate {
	
	private ICompress compress;
	
	public CompressOperate(ICompress compress){
		this.compress = compress;
	}
	
	/**
	 * 
	 * @Description:压缩字符串方法
	 * @create: 余城序
	 * @Modification:
	 * param:需要被压缩的字符串
	 * @return byte[] 被压缩过后的byte数组
	 * @throws IOException 
	 */
	public byte[] getCompress(String str) throws SysException{
		return compress.getCompress(str);
	}
	
	/**
	 * 
	 * @Description:压缩字符串到某个路径包下(格式gzip)
	 * @create: 余城序
	 * @Modification:
	 * @param sql 需要被压缩的字符串
	 * @param path 要保存的路径
	 * @return void
	 * @throws IOException 
	 */
	public void compressTo(String sql,File file) throws SysException{
		compress.compressTo(sql, file);
	}
	/**
	 * 
	 * @Description:解压被压缩的字符串
	 * @create: 余城序
	 * @Modification:
	 * @param compress 被压缩过的字符串
	 * @return String 解压过后的字符串
	 * @throws IOException 
	 */
	public String getUnCompress(byte[] compressStr) throws SysException{
		return compress.getUnCompress(compressStr);
	}
	
	/**
	 * 
	 * @Description:解压被压缩的字符串
	 * @create: 余城序
	 * @Modification:
	 * @param file对象
	 * @return String 解压过后的字符串
	 * @throws IOException 
	 */
	public String getUnCompress(File file) throws SysException{
		return compress.getUnCompress(file);
	}
	
	

}
