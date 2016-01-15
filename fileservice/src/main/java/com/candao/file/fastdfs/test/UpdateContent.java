//package org.csource.fastdfs.test;
//
//import java.io.File;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.csource.common.MyException;
//import org.csource.fastdfs.ClientGlobal;
//import org.csource.fastdfs.StorageClient1;
//import org.csource.fastdfs.StorageServer;
//import org.csource.fastdfs.TrackerClient;
//import org.csource.fastdfs.TrackerServer;
//
//public class UpdateContent {
//	
//	private static String querySql = "SELECT * FROM ky.tb_article_bak t WHERE (t.content like '%%%s%%') or t.image like '%%%s%%'";
//	
//	private static String querySql2 = "select * from ky.tb_template_bak t where t.data like  '%%%s%%'";
//	
//    private static final String IMAGELABEL = "IMAGE";
//    private static final String CONTENTLABEL = "CONTENT";
//    private static final String DATALABEL = "DATA";
//    private static final String IDLABEL = "ID";
//    private static String regexStr = "%s.(mp4|jpg|png|gif|jpeg|MP4|JPG|PNG|JPEG)";
////    private static String regexStr = "src=\"?(.*?)(%s.(mp4|jpg|png|gif|jpeg|MP4|JPG|PNG|GIF|JPEG))\"";
//    private static String regexUploadStr = "upload/%s.(mp4|jpg|png|gif|jpeg|MP4|JPG|PNG|GIF|JPEG)";
//    
////    private static String regexTemplateStr = "image:\"?(.*?)(%s.(mp4|jpg|png|gif|jpeg))\"";
//    
//    
//    
// //   <img.*src=(.*?)[^>]*?>
//    //http:\"?(.*?)(\"|>|\\s+)
//    	
//    
// //  static  String conString = "<p><video class=\"edui-upload-video video-js vjs-default-skin\" controls=\"\" preload=\"none\" width=\"420\" height=\"280\" src=\"/kaiying/upload/20140814/60911407985063182.mp4\" data-setup=\"{}\"><source src=\"/kaiying/upload/20140814/60911407985063182.mp4\" type=\"video/mp4\"></source></video></p>";
////     static String conString = "upload/2323232.jpg";
//     
//	public static void main(String [] args) throws SQLException{
//		
//// 		 String htmlStr = "<p>广州 <br/><img src=\"/kaiying/upload/20140825/76801408959978475.jpg\"/>新快。<br/><video class=\"edui-upload-video video-js vjs-default-skin\" controls=\"\" preload=\"none\" width=\"420\" height=\"280\" src=\"/kaiying/upload/20140826/40031408959996806.mp4\" data-setup=\"{}\"><source src=\"/kaiying/upload/20140826/40031408959996806.mp4\" type=\"video/mp4\"></source></video><br/></p><p>很传奇。";
////		  String regEx_img = "<img.*src=(76801408959978475.(mp4|jpg|png|gif|jpeg))/76801408959978475>"; //图片链接地址     
////		  String regEx_img = "<video.*src=\"(.*?)40031408959996806.(mp4|jpg|png|gif|jpeg)*?\""; //图片链接地址     
//	//good	 
////		String regEx_img = "src=\"(.*?)(40031408959996806.(mp4|jpg|png|gif|jpeg))[^/>]*?\""; //图片链接地址     
// 
////	good bak	String regEx_img = "src=\"(.*?)(40031408959996806.(mp4|jpg|png|gif|jpeg))[^/>]*?\""; //图片链接地址     
////		  String regEx_img = "src=\"(.*?)[^/>]*?\""; //图片链接地址  
// 
// 
// 		
//// 		String regEx_img = "40031408959996806.(mp4|jpg|png|gif|jpeg|MP4|JPG|PNG|JPEG)"; //图片链接地址   
//// 		
//// 		String regEx_img2 = "76801408959978475.(mp4|jpg|png|gif|jpeg)"; //图片链接地址   
// 		
//// 		UpdateContent upload = new UpdateContent();
//// 		Pattern p = Pattern.compile(regEx_img);
//// 		Matcher matcher = p.matcher(htmlStr);
//// 		String string3 = "";
//// 		if (matcher.find()) {
//// 			int indexStart = upload.verifyContent(htmlStr,matcher.start());
//// 			string3 = htmlStr.replaceAll(htmlStr.substring(indexStart +1, matcher.end()), "test.mp4");
////		}
//// 		System.out.println(string3);
// 		
////		 p = Pattern.compile(regEx_img2);
//// 		 matcher = p.matcher(string3);
////// 		 string3 = "";
//// 		if (matcher.find()) {
//// 			int indexStart = upload.verifyContent(string3,matcher.start());
//// 			string3 = string3.replaceAll(string3.substring(indexStart +1, matcher.end()), "test.mp4");
////		}
//// 		System.out.println(string3);
// 		
// 		
//		new UpdateContent().uploadFile();
//	}
//	private int verifyContent(String content,int  firstIndex){
//		while (content.charAt(firstIndex) != '"') {
//			--firstIndex;
//		}
//		 
//		return firstIndex;
//		
//	}
//	
//	private  void matchContent(final String id,String content,final String oldFileName,String newFilename,int index){
//		Pattern pattern = null;
//		 Matcher matcher = null;
//		 
//		 
//		if(index == 2){
//			pattern = Pattern.compile(String.format(regexStr,oldFileName));
//		    matcher = pattern.matcher(content);
//			 if (matcher.find()) {
//				   //update table tb_article_bak
//				 
//		 			int indexStart = verifyContent(content,matcher.start());
//		 			final String content2 = content.replaceAll(content.substring(indexStart +1, matcher.end()), newFilename);
//				 
////				   new Thread(new Runnable() {
////						
////						@Override
////						public void run() {
//							System.out.println(oldFileName + "[2] = "+ content2);
//							 updateTable(id,content2,2);
////						}
////					}).run();
//			 }
//		}else if(index == 1){
//			pattern = Pattern.compile(String.format(regexUploadStr,oldFileName));
//			matcher = pattern.matcher(content);
//			if (matcher.find()) {
//				   //update table tb_article_bak 
//				final String content2 = matcher.replaceAll(  newFilename );
////		        new Thread(new Runnable() {
////							
////							@Override
////							public void run() {
//								System.out.println(oldFileName + "[1] = "+ content2);
//								updateTable(id,content2,1);
////							}
////						}).run();
//			}
//			
//		}else if(index == 3){
//			pattern = Pattern.compile(String.format(regexStr,oldFileName));
//			matcher = pattern.matcher(content);
//			if (matcher.find()) {
//				   //update table tb_template
//				int indexStart = verifyContent(content,matcher.start());
//	 			final String content2 = content.replaceAll(content.substring(indexStart +1, matcher.end()), newFilename);
////				new Thread(new Runnable() {
//					
////					@Override
////					public void run() {
//						 System.out.println(oldFileName + "[3] = "+ content2);
//						updateTable(id,content2,3);
////					}
////				}).run();
//			}
//			
//			 
//		}
//	}
//	
//	
//	
//	private  void updateTable(String id,String content,int index) {
//	 
//	    String sql = "";
//	    if(index == 1){
//	    	sql = "update tb_article_bak t set t.image = ? where t.id = ?";
//	    }else if (index == 2) {
//			sql  = "update tb_article_bak t set t.content = ? where t.id = ?";
//		}else if (index == 3) {
//			sql  = "update tb_template_bak t set t.data = ? where t.id = ?";
//		}
////		synchronized (this) {
//			 Connection connection = DBUtils.getConn();
//			    PreparedStatement pstmt;
//				try {
//					pstmt = connection.prepareStatement(sql);
//					pstmt.setString(1, content);
//				    pstmt.setString(2, id);
//				    pstmt.executeUpdate();
//					
////				    try {
////						Thread.sleep(2000);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
//					connection.close();
//					
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}  
////		}
//	}
// 
//   private  void uploadFile(){
//	   System.out.println("java.version=" + System.getProperty("java.version"));
//	  	String conf_filename = "E:\\workspace\\fastdfs\\src\\fdfs_client.conf";
//	  	try
//	  	{
//	  		ClientGlobal.init(conf_filename);
//	  		System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
//	  		System.out.println("charset=" + ClientGlobal.g_charset);
//	 
//	        TrackerClient tracker = new TrackerClient();
//	        TrackerServer trackerServer = tracker.getConnection();
//	        StorageServer storageServer = null;
//	       final StorageClient1 client = new StorageClient1(trackerServer, storageServer);
//	       File  file = new File("E:\\测试环境图片");
//	       recuFile(client,file);
//	       trackerServer.close();
//	  	}
//	  	catch(Exception ex)
//	  	{
//	  		ex.printStackTrace();
//	  	}
//   }	
//	
//   
//
//    void recuFile(final StorageClient1 client,final File file) throws IOException{
// 	  if(file.isDirectory()){
//    	   File[] files = file.listFiles();
//    	   for(File f : files ){
//    		   if(f.isDirectory()){
//    			recuFile(client,f);
//    		   }else {
//    			start(client,f.getCanonicalPath()); 
// 		     }
//    	   }
//       } else {
//     	  start(client,file.getCanonicalPath()); 
// 	}
//   }
//   
//     void start(final StorageClient1 client ,final String local_filename){
//       new Runnable() {
// 			
// 		@Override
// 		public void run() {
// 	          String fileId;
// 			try {
// 				 
// 				 fileId = client.upload_file1(local_filename, null, null);
// 				 System.out.println("upload "+local_filename+ ", success. file id is: " + fileId);
// 				 
// 				 
// 				String imageStr ;
// 				String contentStr;
// 				String dataStr;
// 				//上传文件返回了文件id
// 				
// 				Connection connection = DBUtils.getConn();
// 				Statement stmt = connection.createStatement();
//				
// 				String oldFileName = local_filename.substring(local_filename.lastIndexOf("\\") +1, local_filename.length());
// 				oldFileName = oldFileName.substring(0,oldFileName.lastIndexOf("."));
// 				String newFilename = fileId;
// 				ResultSet rs = stmt.executeQuery(String.format(querySql, oldFileName,oldFileName));
// 			    while (rs.next()) {
// 			    	 
// 			    	imageStr =  new String(rs.getString(IMAGELABEL).getBytes("utf-8"));
// 			    	contentStr =  new String(rs.getString(CONTENTLABEL).getBytes("utf-8"));
//// 			    	 System.out.println(contentStr);
// 			    	matchContent(rs.getString(IDLABEL),imageStr,oldFileName,newFilename,1);
// 			    	matchContent(rs.getString(IDLABEL),contentStr,oldFileName,newFilename,2);
// 				}
// 			    rs = stmt.executeQuery(String.format(querySql2, oldFileName,oldFileName));
// 			    
// 			    while (rs.next()) {
// 			    	dataStr = new String(rs.getString(DATALABEL).getBytes("utf-8"));
//// 			    	System.out.println(dataStr);
// 			    	matchContent(rs.getString(IDLABEL),dataStr,oldFileName,newFilename,3);
// 				}
// 			    
// 				rs.close();
// 				stmt.close();
// 				connection.close();
// 				
// 			} catch (IOException e) {
// 				e.printStackTrace();
// 			} catch (MyException e) {
// 				e.printStackTrace();
// 			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
// 			
// 		}
// 	}.run();
//   }
//   
// 
//   
//}
