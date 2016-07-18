package com.candao.common.compress;

import java.io.File;
import java.io.IOException;

import com.candao.common.exception.SysException;

/**
 * 
 * @Description: 压缩数据的接口
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年5月4日上午10:44:49
 * @version 1.0
 */
public interface ICompress {
	/**
	 * 
	 * @Description:压缩字符串方法
	 * @create: 余城序
	 * @Modification:
	 * param:需要被压缩的字符串
	 * @return String 被压缩过后的byte数组
	 */
	byte[] getCompress(String str) throws SysException;
	/**
	 * 
	 * @Description:解压被压缩的字符串
	 * @create: 余城序
	 * @Modification:
	 * @param by 被压缩过的byte数组
	 * @return String 解压过后的字符串
	 */
	String getUnCompress(byte[] bt) throws SysException;
	/**
	 * 
	 * @Description:解压被压缩的字符串
	 * @create: 余城序
	 * @Modification:
	 * @param file file对象
	 * @return String 解压过后的字符串
	 */
	String getUnCompress(File file) throws SysException;
	/**
	 * 
	 * @Description:压缩字符串到某个路径包下(格式gzip)
	 * @create: 余城序
	 * @Modification:
	 * @param file对象
	 * @param path 要保存的路径
	 * @return byte[] 被压缩过后的byte数组
	 * @throws IOException 
	 */
	void compressTo(String sql,File file) throws SysException;

}
