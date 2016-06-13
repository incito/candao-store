package kaiying;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.print.entity.PrinterConstant;
import com.candao.print.utils.PrintControl;
import com.candao.www.constant.Constant;

public class TestPrint {
	
	private static final String NORMAL = "10100^0^100000^1001111^";
	private static final String COVER_OPEN = "111100^0^100000^1001111^";
	private static final String COVER_OPEN_REVERT = "10100^0^100000^1001111^";
	private static final String PRINTING = "10100^0^1000000^1111^";
	
	

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for(int i=0;i<1;i++){
			try {
				print();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	private static void print() throws InterruptedException {
		Map<String, String> mapip = new HashMap<String, String>();
		mapip.put("0", "192.168.1.251:9100");
		mapip.put("1", "192.168.1.251:9100");

		OutputStream socketOut = null;
		PrintWriter writer = null;

		String print_ip = "10.66.18.3";
		int print_port = 9100;
		// TODO 把socket 放到队列中使用 ，此处要优化

		byte[] PRINT_CODE = new byte[9];
		PRINT_CODE[0] = 0x1B;
		PRINT_CODE[1] = 0x69;
		PRINT_CODE[2] = 0x06;
		PRINT_CODE[3] = 0x07;

		try {
			final Socket socket = new Socket();
			socket.connect(new InetSocketAddress(print_ip, print_port), 5000);//建立连接5秒超时
//			socket.setKeepAlive(true);  
//	        socket.setSoTimeout(5 * 1000);      //从inputStream中读打印机状态返回值的超时时间
			socketOut = socket.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(socketOut, Constant.PRINTERENCODE));

			
//			socketOut.write(new byte[]{0x1B, 0x73, 0x42, 0x45, (byte) 0x92, (byte) 0x9A, 0x01, 0x00, 0x5F, 0x0A});
			
			// 设置自动状态返回
//			socketOut.write(new byte[]{0x1D, 0x61, (byte) 0xFF});
//			System.out.println("第一次状态---");
//			readStatus(socket, socketOut);
			
			int printerIsReady = new PrintControl().printerIsReady(0, 5000, socketOut, socket.getInputStream());
			System.out.println("打印前状态查询：" + printerIsReady);
			
			socketOut.write(27);
			socketOut.write(64);
			

			writer.write("        厨打单     \n ");
			writer.flush();
				
				
			socketOut.write(PrinterConstant.getFdFont());//
			socketOut.write(PrinterConstant.getFont_B());//

			writer.flush();//
			socketOut.write(PrinterConstant.getClear_font());
			for(int i=0; i<3;i++){
				writer.write("-------------------------------------\r\n");
				writer.write("-------------------------------------\r\n");
				writer.write("账单号:" + "H00040      2014-11-04 " + "\r\n");
				socketOut.write(PRINT_CODE);
				writer.write("-----------桌号：A区  厅 012 ----------------------\r\n");
			}
			socketOut.write(PrinterConstant.getClear_font());

			// 下面指令为打印完成后自动走纸
						writer.write(27);
						writer.write(100);
						writer.write(4);
						writer.write(10);
						writer.flush();// 
						socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸
						
			// 打印机状态
			int checkJob = new PrintControl().CheckJob(0, 5000, socket.getInputStream());
			System.out.println("打印后状态查询：" + printerIsReady);

						
			writer.close();
			
			socketOut.close();
			socket.close();
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (writer != null) {
					writer.close();
				}
				if (socketOut != null) {
					socketOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private static boolean readStatus(final Socket socket, OutputStream socketOut) {
		InputStream inputStream;
		try {
			inputStream = socket.getInputStream();
			
//			int readDeviceStatus = new PrintControl().ReadDeviceStatus(inputStream);
//			System.out.println(readDeviceStatus);
			
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean checkPrinter1(OutputStream socketOut, final Socket socket) {
		InputStream inputStream;
		byte[] rs = new byte[1];
		try {
			socketOut.write(new byte[] { 16, 4, 1 });
			inputStream = socket.getInputStream();
			inputStream.read(rs);

			String rs_str = "";
			for (byte b : rs) {
				rs_str += Integer.toBinaryString(b) + "^";
			}
			System.out.println(rs_str);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	private static void find(){
		final Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("10.66.18.3", 9100), 5000);//建立连接5秒超时
			socket.setKeepAlive(true);  
	        socket.setSoTimeout(5 * 1000);      //从inputStream中读打印机状态返回值的超时时间
			OutputStream socketOut = socket.getOutputStream();
			socketOut.write(new byte[]{0x1D, 0x61, (byte) 0xFF});
			socketOut.write(new byte[]{0x1B, 0x61, 0x5F});
			readStatus(socket, socketOut);
		} catch (IOException e) {
		}
	}

}
