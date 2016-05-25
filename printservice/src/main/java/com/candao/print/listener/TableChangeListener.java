package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.service.PrinterService;

@Service
public class TableChangeListener {
	/**
	 * 
	 * @param message
	 * @return
	 */
	public String receiveMessage(String message) {

		return null;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public String receiveMessage(PrintObj object) {
		System.out.println("TableChangeListener receive message");
		OutputStream socketOut = null;
		OutputStreamWriter writer = null;
		Socket socket = null;
		String ipAddress = null;
		int print_port;
//		int printType = object.getPrintType();
		String billName = "";
	
		
		
		try {
			ipAddress = object.getCustomerPrinterIp();
			billName = object.getBillName();
			print_port = Integer.parseInt(object.getCustomerPrinterPort());// Integer.parseInt(address[1]);

			socket = new Socket(ipAddress, print_port);
//			socket = new Socket("192.168.40.138", 9100);
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
			socketOut.write(27);
			socketOut.write(27);
			
			writer.flush();//
			socketOut.write(PrinterConstant.getFdDoubleFont());
			// 单号
			writer.write("　　　　" + StringUtils.bSubstring2(billName, 6)
					+ " \r\n");
			writer.flush();//
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("==========================================\r\n");
			writer.flush();// 
			
			String[] name = {object.getOrderNo(),object.getTimeMsg().substring(0,10)};
			//最多显示34个字符
			Integer[] len = {22,10};
			String[] header = StringUtils.getLineFeedText(name, len);
			if(header != null){
				for (int i = 0; i < header.length; i++) {
					if( i == 0){
						writer.write( StringUtils.bSubstring2("账单号:",4) + header[i] +"\r\n" );
					} else {
						writer.write( StringUtils.getStr(7) + header[i] + "\r\n" );						
					}
				}
			}

			String[] username = {object.getUserName(),object.getTimeMsg().substring(11)};
			Integer[] length = {12,8};
			String[] body = StringUtils.getLineFeedText(username, length);
			if(body != null){
				for (int i = 0; i < body.length; i++) {
					if( i == 0){
						writer.write(StringUtils.bSubstring2("服务员:",4) + body[i]+"\r\n");						
					} else {
						writer.write(StringUtils.getStr(7) + body[i] + "\r\n");
					}
				}
			}
			
			writer.write(StringUtils.bSubstring2("授权人:" + object.getDiscardUserId(),
					20)
					+"\r\n");
			
			writer.write("------------------------------------------\r\n");
			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write("\r\n");
			
			writer.write(
//					StringUtils.bSubstring2(object.getTableArea(), 8)
					StringUtils.bSubstring3("　" + object.getTableNo(), 8)
					+StringUtils.bSubstring2(object.getAbbrbillName(), 2)
					+StringUtils.bSubstring3(object.getWelcomeMsg(), 5)
					+ "\r\n");
			
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());

			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());


			// 下面指令为打印完成后自动走纸
			writer.write(27);
			writer.write(100);
			writer.write(4);
			writer.write(10);
			writer.flush();// 
			socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸
			writer.close();
			socketOut.close();
			socket.close();

		} catch (Exception e) {
			//查询object下的打印机ip与端口是否存在，如果数据库中存在，表示打印机故障，重新加入队列等待打印机修复
			int result=printerService.queryPrintIsExsit(object.getCustomerPrinterIp(),object.getCustomerPrinterPort());
			if(result>0){
				//该数据存在，重新加入队列等待打印机修复
				jmsTemplate.convertAndSend(destination, object);
			}
			//不存在则表示垃圾数据直接清除
		} finally {

		}
		return null;
		// }

	}

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("tableSwitchQueue")
	private Destination destination;
	@Autowired
	PrinterService    printerService;

}
