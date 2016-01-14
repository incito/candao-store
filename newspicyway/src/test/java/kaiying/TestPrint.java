package kaiying;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.candao.print.entity.PrinterConstant;
import com.candao.www.constant.Constant;

public class TestPrint {

	public static void main(String[] args) {

		Map<String, String> mapip = new HashMap<String, String>();
		mapip.put("0", "192.168.1.251:9100");
		mapip.put("1", "192.168.1.251:9100");
		 
		OutputStream socketOut = null;
		OutputStreamWriter writer = null;
		Socket  socket = null;
 
		String print_ip = "192.168.40.215";
		int print_port = 9100;
		//TODO 把socket 放到队列中使用 ，此处要优化
		
		 byte[] PRINT_CODE = new byte[9];
		 PRINT_CODE[0] = 0x1B;
		 PRINT_CODE[1] = 0x69;
		 PRINT_CODE[2] =0x06;
		 PRINT_CODE[3] =0x07;
	 
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
			   socketOut.write(PRINT_CODE);
			   writer.write("-----------桌号：A区  厅 012 ----------------------\r\n");
			   socketOut.write(PrinterConstant.getClear_font());
			   writer.write(27);
			   writer.close();
			   socketOut.close();
			   socket.close();
			   
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

	}

}
