package com.candao.www.webroom.service.impl;
//package com.candao.www.webroom.service.impl;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.Socket;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.candao.newspicyway.printqueue.entity.PrinterConstant;
//import com.candao.www.constant.Constant;
//
//@Service
//public class AddDishPrintServiceImpl extends PrintService{
//	@Autowired
//	PrintService  printService;
//	
//	@Override
//	public String print(String printType, String msg) {
//		
//		OutputStream socketOut = null;
//		OutputStreamWriter writer = null;
//		Socket  socket = null;
//	 
//		String[] ipAddressStr  = printService.getPrinter(printType).split(":");;
//		
//		String print_ip = ipAddressStr[0];
//		int print_port = Integer.parseInt(ipAddressStr[1]);
//		//TODO 把socket 放到队列中使用 ，此处要优化
//		
//		  try {
//			  socket = new Socket(print_ip, print_port);
//			  socketOut = socket.getOutputStream();
//			  writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
//			  
//			   socketOut.write(PrinterConstant.getFdFont());// 字体放大
//			   socketOut.write(PrinterConstant.getFont_B());// 字体加粗
//			   
//			   writer.write(" ------ " + "厨打单" + " --------- \r\n");  
//			   writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
//			   socketOut.write(PrinterConstant.getClear_font());
//			   
//			   socketOut.write(10);
//			   writer.write(PrinterConstant.fix_set_length(1,"------------------------------------",38)  + "\n");
//			   writer.write(PrinterConstant.fix_set_length(1,"------------------------------------",38)  + "\r\n");
//			    
//		 
//			   writer.write("账单号:"+"H00040      2014-11-04 "+"\r\n");
//			   writer.write("服务员:"+"1012316  区域:A区  18:13:19  "+"\r\n");
//			   writer.write("-------------------------------------\r\n");
//			   
//			   writer.write("-----桌号：A区 厅012 ----------- \r\n");
//			   socketOut.write(PrinterConstant.getFdFont());// 
//			   socketOut.write(PrinterConstant.getFont_B());//  
//			   writer.flush();//  
//			   writer.write("-------------------------------------\r\n");
//			   writer.write("品项            数量         单位\r\n"); 
//			   
//			   
//			   writer.write("红汤/番茄双拼锅                                     \r\n"); 
//			   writer.write("  双拼锅底   1      份   \r\n"); 
//			   writer.write("  红汤梭边鱼 1   斤   \r\n"); 
//			   writer.write("  番茄龙利鱼   1 斤   \r\n"); 
//			   writer.write("   \r\n"); 
//			   writer.write("                5           19:23:00  \r\n"); 
//			   writer.write("                -                     \r\n");
//			   writer.write("------------------------------------- \r\n");
//			   writer.write("备注：少盐 ,不要太辣                                                  \r\n");
//		 
//			   
//			   writer.write(27);
//			   writer.write(100);
//			   writer.write(5);
//			   writer.write(10);
//			   writer.close();
//			   socketOut.close();
//			   socket.close();
//			   
//		   return "0";
//		  } catch (IOException e) {
//		     e.printStackTrace();
//		  }finally{
//			  
//			  try{
//				  if(writer != null){
//						writer.close();
//				  } 
//				  if(socketOut != null){
//					  socketOut.close();
//				  }
//				  if(socket != null){
//					   socket.close();
//				  }
//			  }  catch (IOException e) {
//					e.printStackTrace();
//				}
//			
//		  }
//		  return "1";
//	}
//
//}
