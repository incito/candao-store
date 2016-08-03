package com.candao.file.fastdfs.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
	/**
	 * 上传文件
	 * @param file 文件
	 *    文件扩展名通过file.getName()获得
	 * @return 文件存储路径
	 * @throws IOException
	 * @throws Exception
	 */
	public String uploadFile(File file) throws IOException, Exception ;
	/**
	 * 上传文件
	 * @param file  文件
	 * @param suffix  文件扩展名，如 (jpg,txt)
	 * @return  文件存储路径
	 * @throws IOException
	 * @throws Exception
	 */
	public String uploadFile(File file, String suffix) throws IOException, Exception ;
	
	/**
	 * 用流上传文件
	 * @author tom
	 * @param fis
	 * @param fileExtName
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public String uploadFile(InputStream fis,String fileExtName) throws IOException, Exception;
	/**
	 * 上传文件
	 * @param fileBuff 二进制数组
	 * @param suffix  文件扩展名 ，如(jpg,txt)
	 * @return
	 */ 
	public String uploadFile(byte[] fileBuff,String suffix) throws IOException, Exception;
	
	/**
	 * 得到域名或IP
	 * @return
	 */
	public String getConnnectString() ;
	/**
	 * 得到连接端口
	 * @return
	 */
	public int getPort() ;
	
	/**
	 *删除文件
	 * @param fileId   包含group及文件目录和名称信息
	 * @return true 删除成功，false删除失败
	 * @throws IOException
	 * @throws Exception
	 */
	public boolean deleteFile(String fileId) throws IOException,Exception;
	/**
	 * 查找文件内容
	 * @param fileId
	 * @return  文件内容的二进制流
	 * @throws IOException
	 * @throws Exception
	 */
	public byte[] getFileByID(String fileId)throws IOException,Exception;
}
