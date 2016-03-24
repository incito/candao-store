package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.service.NormalDishProducerService;
import com.candao.print.service.PrinterService;
import com.candao.print.service.impl.NormalDishPrintService;
 

@Service
public class CustDishListener {

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
		System.out.println("CustDishListener receive message");

		OutputStream socketOut = null;
		OutputStreamWriter writer = null;
		Socket socket = null;
		String ipAddress = null;
		int print_port;

		String billName = "";
		List<PrintDish> printDishList = object.getList();
		try {
			billName = object.getBillName();
			ipAddress = object.getCustomerPrinterIp();
			print_port = Integer.parseInt(object.getCustomerPrinterPort());

			
			socket = new Socket(ipAddress, print_port);
//			socket = new Socket("192.168.40.138", 9100);
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);

			socketOut.write(27);
			socketOut.write(27);
			//
			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());
			// 单号
			writer.write("　　　" + StringUtils.bSubstring2(billName, 9)
					+ " \r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("==========================================\r\n");
			// 一行最多能容下42个字符

			writer.write(StringUtils.bSubstring2("账单号:" + object.getOrderNo(),
					28)
					+ StringUtils.bSubstring2(object.getTimeMsg(), 10)
					+ "\r\n");

			writer.write(StringUtils.bSubstring2("服务员:" + object.getUserName(),
					9)
					+ StringUtils.bSubstring2(object.getTableArea(), 8)
					+ StringUtils.bSubstring2(
							object.getTimeMsg().substring(11), 8) + "\r\n");

			writer.write("------------------------------------------\r\n");
		
			writer.flush();//  
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(StringUtils.bSubstring2(
					"　　" + object.getTableNo(), 10)
					+ "\r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			writer.write(StringUtils.bSubstring2("品项 ", 12)
					+ StringUtils.bSubstring2("数量", 4)
					+ StringUtils.bSubstring2("单价", 5) + "\r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getFd8Font());
			writer.write("     " + "\r\n");

			for (PrintDish printDish : printDishList) {
				String dishName2 = StringUtils.bSubstring2(
						StringUtils.BtoQ(printDish.getDishName()), 12);
				
				String dishNum2 = StringUtils.bSubstring2(StringUtils.BtoQ(
						printDish.getDishNum()), 4);
				String dishPrice2 = StringUtils.bSubstring2(printDish
						.getDishPrice() == null ? "" : StringUtils.BtoQ(printDish.getDishPrice()
						.toString()), 5);
				writer.write(dishName2);
				writer.write( dishNum2 );
				writer.write( dishPrice2 + "\r\n");
				writer.write("     " + "\r\n");
			 }
		
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			
			writer.write("------------------------------------------\r\n");
			writer.write("       欢迎品尝       谢谢惠顾\r\n");

			writer.write(27);// 重置
			writer.write(100);
			writer.write(4);
			writer.write(10);// 换行
			writer.flush();//  
			socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸
			writer.close();
			socketOut.close();
			socket.close();
		} catch (Exception e) {
			// e.printStackTrace();
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
	@Qualifier("custDishQueue")
	private Destination destination;

	@Autowired
	NormalDishPrintService normalDishPrintService;

	@Autowired
	PrinterService    printerService;

}
