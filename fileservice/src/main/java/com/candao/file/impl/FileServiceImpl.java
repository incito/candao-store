//package com.candao.file.impl;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.apache.commons.io.IOUtils;
//import org.springframework.stereotype.Service;
//
//import com.candao.file.common.Constant;
//import com.candao.file.fastdfs.StorageClient1;
//import com.candao.file.fastdfs.StorageServer;
//import com.candao.file.fastdfs.TrackerServer;
//import com.candao.file.fastdfs.pool.ConnectionPool;
//import com.candao.file.fastdfs.service.FileService;
//
//@Service
//public class FileServiceImpl implements FileService{
//
//	private String connnectString;
//
//	private int port = 22122;
//	
//	private int size;
//
//	private int waitTimes = 2000;
//
//	private ConnectionPool pool = null;
//	
//	private static int defaultPort = 22122;
//
//	/**
//	 * 
//	 * @param connnectString
//	 *            域名地址或IP
//	 * @param port
//	 *            端口
//	 * @param size
//	 *            连接池大小
//	 */
//	public FileServiceImpl(String connnectString, int port, int size) {
//		this.connnectString = connnectString;
//		this.port = port;
//		this.size = size;
//		//pool = new ConnectionPool(connnectString, port, this.size);
//	}
//	
//	public FileServiceImpl() {
// 
//		//pool = new ConnectionPool();
//	}
//
//	/**
//	 * 
//	 * @param connnectString
//	 *            域名地址或IP
//	 * @param port
//	 *            端口
//	 */
//	public FileServiceImpl(String connnectString, int port) {
//		this(connnectString, port, 2);
//	}
//
//	/**
//	 * 
//	 * @param connnectString
//	 *            域名地址或IP 端口默认
//	 */
//	public FileServiceImpl(String connnectString) {
//		this(connnectString, defaultPort);
//	}
//
//	public String uploadFile(File file) throws IOException, Exception {
//		return uploadFile(file, getFileExtName(file.getName()));
//	}
//	
//	/**
//	 * 
//	 * @author tom
//	 * @return File id
//	 * @throws IOException
//	 * @throws Exception
//	 */
//	public String uploadFile(InputStream fis,String fileExtName) throws IOException, Exception {
//		byte[] bs = getFileByte(fis);
//		return uploadFile(bs, fileExtName);
//	}
//
//	public String uploadFile(File file, String suffix) throws IOException,
//			Exception {
//		byte[] fileBuff = getFileBuffer(file);
//		return send(fileBuff, suffix);
//	}
//
//	public String uploadFile(byte[] fileBuff, String suffix)
//			throws IOException, Exception {
//		return send(fileBuff, suffix);
//	}
//
//	private String send(byte[] fileBuff, String fileExtName)
//			throws IOException, Exception {
//		String upPath = null;
//		TrackerServer trackerServer = null;
//		try {
//			trackerServer = ConnectionPool.checkout(waitTimes);
//
//			StorageServer storageServer = null;
//			
//			//verify which storage server would be stored through the file extends name
//			StorageClient1 client1 = new StorageClient1(trackerServer,
//					storageServer);
//			upPath = client1.upload_file1(verifyFileType(fileExtName)?Constant.VIDEO_GROUP:Constant.IMG_GROUP,fileBuff, fileExtName, null);
//			ConnectionPool.checkin(trackerServer);
//		} catch (InterruptedException e) {
//			throw e;
//		} catch (NullPointerException e) {
//			throw e;
//		} catch (Exception e) {
//			e.printStackTrace();
//			ConnectionPool.drop(trackerServer);
//			throw e;
//		}
//		return upPath;
//	}
//	
//	private boolean verifyFileType(String fileSuffix){
//		if(fileSuffix == null || fileSuffix.endsWith("mp4")){
//			return true;
//		}
//		return false;
//	}
//
//	private String getFileExtName(String name) {
//		String extName = null;
//		if (name != null && name.contains(".")) {
//			extName = name.substring(name.lastIndexOf(".") + 1);
//		}
//		return extName;
//	}
//	
//	
//	private byte[] getFileByte(InputStream fis){
//		
//		
//		try {
//			return IOUtils.toByteArray(fis);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		return null;
//		
////		byte[] fileByte = null;
////		try { 
////			fileByte = new byte[fis.available()];
////			fis.read(fileByte);
////			fis.close();
////		} catch (FileNotFoundException e) {
////			e.printStackTrace();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////		return fileByte;
//	}
//
//	private byte[] getFileBuffer(File file) {
//		byte[] fileByte = null;
//		try {
//			FileInputStream fis = new FileInputStream(file);
//			fileByte = new byte[fis.available()];
//			fis.read(fileByte);
//			fis.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return fileByte;
//	}
//
//	@Override
//	public String getConnnectString() {
//		return connnectString;
//	}
//
//	@Override
//	public int getPort() {
//		return port;
//	}
//
//	public int getSize() {
//		return size;
//	}
//
//	public int getWaitTimes() {
//		return waitTimes;
//	}
//
//	public void setWaitTimes(int waitTimes) {
//		this.waitTimes = waitTimes;
//	}
//
//	@Override
//	public boolean deleteFile(String fileId) throws IOException, Exception {
//		boolean result=false;
//		TrackerServer trackerServer =null;
//		try {
//			trackerServer = pool.checkout(waitTimes);
//			StorageServer storageServer = null;
//			StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
//			result = client1.delete_file1(fileId) == 0?true:false;
//			pool.checkin(trackerServer);
//		} catch (Exception e) {
//			pool.drop(trackerServer);
//			e.printStackTrace();
//			throw e;
//		}
//		return result;
//	}
//
//	@Override
//	public byte[] getFileByID(String fileId)throws IOException,Exception{
//		byte[] result = null;
//		TrackerServer trackerServer = null;
//		try {
//			trackerServer = pool.checkout(waitTimes);
//			StorageServer storageServer = null;
//			StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
//			result = client1.download_file1(fileId);
//			pool.checkin(trackerServer);
//		} catch (Exception e) {
//			pool.drop(trackerServer);
//			e.printStackTrace();
//			throw e;
//		}
//		return result;
//	}
//}
