package kaiying;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Test {
	
	public static void main(String [] args){
		
	/*	String sb = "好的认识";
		System.out.println(sb.length());
		byte[] result =  compress(sb.toString()); 
		System.out.println( result.length);*/
		

		// TODO Auto-generated method stub
		InetAddress ia=null;
		try {
			ia=ia.getLocalHost();
			
			String localname=ia.getHostName();
			String localip=ia.getHostAddress();
			System.out.println("本机名称是："+ localname);
			System.out.println("本机的ip是 ："+localip);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
//		   List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		   Map<String, Object> map = new HashMap<String, Object>();
//		   map.put("result", "0");
//		   map.put("loginTime", DateFormat.getDateTimeInstance().format(new Date()));
//		  // list.add(map);
//		   map.put("tables", list);
//		   System.out.println( JacksonJsonMapper.objectToJson(map));
//		   
		   
//		Properties p = System.getProperties();
//	    Enumeration<Object> e = p.keys();
//	    for (;e.hasMoreElements();) {
//	    	Object key = e.nextElement();
//			System.out.println(key + "=" + p.get(key));
//		}
//		File f = new java.io.File("C:\\Users\\tom\\Desktop\\lib");
//		File [] files = f.listFiles();
//		for(File file : files){
//			System.out.println(file.getName());
//		}
	}
//	os.name
	
	/**
	* 压缩字符串为 byte[]
	* 储存可以使用new sun.misc.BASE64Encoder().encodeBuffer(byte[] b)方法
	* 保存为字符串
	*
	* @param str 压缩前的文本
	* @return
	*/
	public static final byte[] compress(String str) {
			if(str == null) {
			  return null;
			}
		
			byte[] compressed;
			ByteArrayOutputStream out = null;
			ZipOutputStream zout = null;
		
			try {
				out = new ByteArrayOutputStream();
				zout = new ZipOutputStream(out);
				zout.putNextEntry(new ZipEntry("0"));
				zout.write(str.getBytes());
				zout.closeEntry();
				compressed = out.toByteArray();
			} catch(IOException e) {
					e.printStackTrace();
					compressed = null;
			} finally {
					if(zout != null) {
						try{zout.close();} catch(IOException e){}
						}
					if(out != null) {
							try{out.close();} catch(IOException e){}
						 
					}
//				
//					if(compressed != null) {
//					 return Base64.encodeBase64String(compressed);
			}
		
			return compressed;
	} 
	
}
