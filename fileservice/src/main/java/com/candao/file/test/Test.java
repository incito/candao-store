///**
//* Copyright (C) 2008 Happy Fish / YuQing
//*
//* FastDFS Java Client may be copied only under the terms of the GNU Lesser
//* General Public License (LGPL).
//* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
//**/
//
//package org.csource.fastdfs.test;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import org.csource.common.MyException;
//import org.csource.common.NameValuePair;
//import org.csource.fastdfs.ClientGlobal;
//import org.csource.fastdfs.StorageClient;
//import org.csource.fastdfs.StorageClient1;
//import org.csource.fastdfs.StorageServer;
//import org.csource.fastdfs.TrackerClient;
//import org.csource.fastdfs.TrackerServer;
//
///**
//* client test
//* @author Happy Fish / YuQing
//* @version Version 1.18
//*/
//public class Test
//{
//	private Test()
//	{
//	}
//	
//	/**
//	* entry point
//	* @param args comand arguments
//	*     <ul><li>args[0]: config filename</li></ul>
//	*     <ul><li>args[1]: local filename to upload</li></ul>
//	*/
//  public static void main(String args[])
//  {
////  	if (args.length < 2)
////  	{
////  		System.out.println("Error: Must have 2 parameters, one is config filename, "
////  		                 + "the other is the local filename to upload");
////  		return;
////  	}
//  	
//  	System.out.println("java.version=" + System.getProperty("java.version"));
//  	  
//  	
//  	String conf_filename = "E:\\workspace\\fastdfs\\src\\fdfs_client.conf";
//   //	String local_filename = "F:\\software\\ActivePerl-5.16.3.1604-MSWin32-x64-298023.msi";
//  	
//  	String download_filename = "E:\\workspace\\fastdfs\\src\\历史_3";
//  	
//  	
////  	String conf_filename = args[0];
////  	String local_filename = args[1];
////  	
//  	try
//  	{
//  		ClientGlobal.init(conf_filename);
//  		System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
//  		System.out.println("charset=" + ClientGlobal.g_charset);
// 
//        TrackerClient tracker = new TrackerClient();
//        TrackerServer trackerServer = tracker.getConnection();
//        StorageServer storageServer = null;
//       final StorageClient1 client = new StorageClient1(trackerServer, storageServer);
//       File  file = new File("E:\\测试环境图片\\");
//       recuFile(client,file);
// 
//  		trackerServer.close();
//  	}
//  	catch(Exception ex)
//  	{
//  		ex.printStackTrace();
//  	}
//  }
//  
//  static void recuFile(final StorageClient1 client,final File file) throws IOException{
//	  if(file.isDirectory()){
//   	   File[] files = file.listFiles();
//   	   for(File f : files ){
//   		   if(f.isDirectory()){
//   			recuFile(client,f);
//   		   }else {
//   			start(client,f.getCanonicalPath()); 
//		 }
//   	   }
//      } else {
//    	  start(client,file.getCanonicalPath()); 
//	}
//  }
//  
//  static  void start(final StorageClient1 client ,final String local_filename){
//      new Runnable() {
//			
//		@Override
//		public void run() {
//	          String fileId;
//			try {
//				 
//				fileId = client.upload_file1(local_filename, null, null);
//				 System.out.println("upload success. file id is: " + fileId);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (MyException e) {
//				e.printStackTrace();
//			}
//			
//		}
//	}.run();
//  }
//  
//  private static void run(final StorageClient1 client,final String local_filename,final NameValuePair[] metaList){
//	  
//	  
//	  ThreadPoolExecutor tpe = new ThreadPoolExecutor(3, 6, 1, TimeUnit.DAYS,
//              new LinkedBlockingQueue<Runnable>(),new ThreadPoolExecutor.DiscardOldestPolicy());
//
//      /**
//       * 循环加线程
//       */
//      for (int i = 0; i < 20; i++) {
//          // 把线程加到线程容器里
//          tpe.execute(new Runnable() {
//
//              @Override
//              public void run() {
//                  // TODO Auto-generated method stub
//            	  
//            	  String fileId;
//				try {
//					fileId = client.upload_file1(local_filename, null, metaList);
//					 System.out.println("upload success. file id is: " + fileId);
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (MyException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//                 
//                  try {
//                      // 睡眠1秒钟
//                      Thread.sleep(1000);
//                  } catch (InterruptedException e) {
//                      // TODO Auto-generated catch block
//                      e.printStackTrace();
//                  }
//
//                  System.out.println(String.format("thread　%d　finished",  this.hashCode()));
//              }
//          });
//      }
//
//      /**
//       * 调用shutdown() 方法之后，主线程就马上结束了，而线程池会继续运行直到所有任务执行完才会停止。如果不调用 shutdown()
//       * 方法，那么线程池会一直保持下去，以便随时添加新的任务。(shutdown() 方法不会阻塞)
//       */
//      tpe.shutdown();
//  }
//  
// 
//}
