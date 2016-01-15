package com.candao.print.listener;

//import groovy.transform.Synchronized;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.candao.print.service.impl.NormalDishPrintService;

@Service
public class WeighDishListener {

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
		System.out.println("WeighDishListener receive message");

		OutputStream socketOut = null;
		OutputStreamWriter writer = null;
		Socket socket = null;
		String ipAddress = null;
		int print_port;
//		int printType = object.getPrintType();
//		int printWay = object.getPrintway();
		String billName = "";
		List<PrintDish> printDishList = object.getList();

		// synchronized (printDishList) {

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

			writer.write(StringUtils.bSubstring2("账单号:" + object.getOrderNo(),
					27)
					+ StringUtils.bSubstring2(object.getTimeMsg(), 10)
					+ "\r\n");

			writer.write(StringUtils.bSubstring2("服务员:" + object.getUserName(),
					9)
					+ StringUtils.bSubstring2(object.getTableArea(), 8)
					+ StringUtils.bSubstring2(
							object.getTimeMsg().substring(11), 8) + "\r\n");

		
			writer.write("------------------------------------------\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(StringUtils.bSubstring2(
					"　　" + object.getTableNo(), 10)
					+ "\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			
			writer.write(StringUtils.bSubstring2("品项 ", 13)
					+ StringUtils.bSubstring2("预估重量", 4));
			writer.write(" ");
			writer.write( StringUtils.bSubstring2("单位", 2) + "\r\n");
			
			
			writer.write("     " + "\r\n");

			for (PrintDish printDish : printDishList) {
				
				String dishName2 = StringUtils.bSubstring2(StringUtils.BtoQ(
						printDish.getDishName()), 12);
				String dishNum2 = StringUtils.bSubstring3(
						printDish.getDishNum(), 4);
				String dishunit2 = StringUtils.bSubstring2(printDish.getDishUnit(),
						2);
				writer.flush();// 
				socketOut.write(PrinterConstant.getFd8Font());
				writer.write(dishName2 );
				writer.flush();// 
				socketOut.write(PrinterConstant.getFdDoubleFont());
				writer.write( " "+dishNum2 + dishunit2
						+ "                    \r\n");
//				writer.write( " 实际重量："+  StringUtils.bSubstring2(StringUtils.BtoQ(
//						printDish.getSperequire()), 3)  
//						+printDish.getDishUnit()+ "       \r\n");
				writer.write( " 实际重量："+ "       \r\n");
//				writer.write( " 实际重量："+"       "  
//						+printDish.getDishUnit()+ "       \r\n");

			}
			writer.flush();// 
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(StringUtils.bSubstring2((printDishList.get(0)
					.getAbbrname() == null ? "　" : printDishList.get(0)
					.getAbbrname()), 4));

//			String special = "";
//			special = StringUtils.bSubstring2(String.valueOf(printDishList.get(0)
//					.getOrderseq()), 30);
//			if (special == null || "null".equals(special)) {
//				special = "";
//			}
			// 只显示出时分秒
			writer.write(StringUtils.bSubstring3(String.valueOf(Integer.toString(printDishList.get(0)
							.getOrderseq())), 8));
			writer.flush();// 
			socketOut.write(PrinterConstant.getClear_font());				
			writer.write( StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
							.format(Calendar.getInstance().getTime()), 8)
					+ "\r\n");

			writer.write("------------------------------------------\r\n");
//			writer.flush();//  
//			socketOut.write(PrinterConstant.getFdDoubleFont());
//			writer.write(special + "\r\n");

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
			jmsTemplate.convertAndSend(destination, object);
		} finally {

		}
		return null;
	}

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("weightQueue")
	private Destination destination;
}

