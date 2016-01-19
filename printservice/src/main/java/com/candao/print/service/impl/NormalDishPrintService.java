package com.candao.print.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.print.entity.PrinterConstant;

@Service
public class NormalDishPrintService extends PrintService{
	
	@Autowired
	PrintService  printService;
	
	@Override
	public String print(String printType, String msg) {
		
		OutputStream socketOut = null;
		OutputStreamWriter writer = null;
		Socket  socket = null;
	 
		String[] ipAddressStr  = printService.getPrinter(printType).split(":");;
		
		String print_ip = ipAddressStr[0];
		int print_port = Integer.parseInt(ipAddressStr[1]);
		//TODO 把socket 放到队列中使用 ，此处要优化
		
		 byte[] PRINT_CODE = new byte[9];
//	     PRINT_CODE[0] = 0x1d;
//	     PRINT_CODE[1] = 0x68;
//	     PRINT_CODE[2] = 120;
//	     PRINT_CODE[3] = 0x1d;
//	     PRINT_CODE[4] = 0x48;
//	     PRINT_CODE[5] = 0x10;
//	     PRINT_CODE[6] = 0x1d;
//	     PRINT_CODE[7] = 0x6B;
//	     PRINT_CODE[8] = 0x02;
//	     // 清除字体放大指令
//	     byte[] CLEAR_FONT = new byte[3];
//	     CLEAR_FONT[0] = 0x1c;
//	     CLEAR_FONT[1] = 0x21;
//	     CLEAR_FONT[2] = 1;
//	     // 字体加粗指令
//	     byte[] FONT_B = new byte[3];
//	     FONT_B[0] = 27;
//	     FONT_B[1] = 33;
//	     FONT_B[2] = 8;
//	     // 字体横向放大一倍
//	     byte[] FD_FONT = new byte[3];
//	     FD_FONT[0] = 0x1c;
//	     FD_FONT[1] = 0x21;
//	     FD_FONT[2] = 4;
		 
		  try {
			  socket = new Socket(print_ip, print_port);
			  socketOut = socket.getOutputStream();
			  writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
			  
			  socketOut.write(27);
			  socketOut.write(64);
			   
			   writer.write("        厨打单     \n ");  
			   writer.flush();
			   socketOut.write(PrinterConstant.getFdFont());// 
			   socketOut.write(PrinterConstant.getFont_B());//  
			   
			   writer.flush();//  
			   socketOut.write(PrinterConstant.getClear_font());
			   writer.write("-------------------------------------\r\n");
			   writer.write("-------------------------------------\r\n");
			   writer.write("账单号:"+"H00040      2014-11-04 "+"\r\n");
			   writer.write("服务员:"+"1012316            区域:A区          18:13:19  "+"\r\n");
			   writer.write("-------------------------------------\r\n");
			   
			   writer.write("-----------桌号：A区  厅 012 ----------------------\r\n");
			   socketOut.write(PrinterConstant.getFdFont());// 
			   socketOut.write(PrinterConstant.getFont_B());//  
			   writer.flush();//  
			   writer.write("-------------------------------------\r\n");
			   writer.write("品项                                             数量                        单位\r\n"); 
			   
			   
			   writer.write("红汤/番茄双拼锅                                                              \r\n"); 
			   writer.write("  双拼锅底                                  1                              份   \r\n"); 
			   writer.write("  红汤梭边鱼                              1                              斤   \r\n"); 
			   writer.write("  番茄龙利鱼                              1                              斤   \r\n"); 
			   writer.write("   \r\n"); 
			   writer.write("                5           19:23:00  \r\n"); 
			   writer.write("                -                     \r\n");
			   writer.write("------------------------------------- \r\n");
			   writer.write("备注：少盐 ，不要太辣                                                  \r\n");
			   
			    
		 
			   
			   writer.write(27);
			   writer.write(100);
			   writer.write(5);
			   writer.write(10);
			   writer.close();
			   socketOut.close();
			   socket.close();
			   
		   return "0";
		  } catch (IOException e) {
		     e.printStackTrace();
		  }finally{
			  
			  try{
				  if(writer != null){
						writer.close();
				  } 
				  if(socketOut != null){
					  socketOut.close();
				  }
				  if(socket != null){
					   socket.close();
				  }
			  }  catch (IOException e) {
					e.printStackTrace();
				}
			
		  }
		  return "1";
	}

}
