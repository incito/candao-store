package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
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
import com.candao.print.service.PrinterService;
import com.candao.print.service.impl.NormalDishPrintService;

@Service
public class NormalDishListener {
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
		System.out.println("NormalDishListener receive message");
		OutputStream socketOut = null;
		OutputStreamWriter writer = null;
		Socket socket = null;
		String ipAddress = null;
		int print_port;
//		int printType = object.getPrintType();
		String billName = "";
//		List<PrintDish> printDishList = object.getList();
//		PrintDish printDish=object.getpDish();
		
		
		try {
			ipAddress = object.getCustomerPrinterIp();
			billName = object.getBillName();
			print_port = Integer.parseInt(object.getCustomerPrinterPort());// Integer.parseInt(address[1]);

			socket = new Socket(ipAddress, print_port);
//			socket = new Socket("192.168.40.215", 9100);
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
			socketOut.write(27);
			socketOut.write(27);
			
			PrintDish printDish = object.getpDish();
			writer.flush();//
			socketOut.write(PrinterConstant.getFdDoubleFont());
			// 单号
			writer.write("　　　　" + StringUtils.bSubstring2(billName, 6)
					+ " \r\n");
			writer.flush();//
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("==========================================\r\n");
			writer.flush();// 
			writer.write(StringUtils.bSubstring2("账单号:" + object.getOrderNo(),
					27)
					+ StringUtils.bSubstring2(object.getTimeMsg(), 10)
					+ "\r\n");

			writer.write(StringUtils.bSubstring2("服务员:" + object.getUserName(),
					9)
					+ StringUtils.bSubstring2(object.getTableArea(), 8)
					+ StringUtils.bSubstring2(
							object.getTimeMsg().substring(11), 8) + "\r\n");
			if(object.getDiscardUserId()!=null&&!"".equals(object.getDiscardUserId())){
			writer.write(StringUtils.bSubstring2("授权人:" + object.getDiscardUserId(),
					9)
					 + "\r\n");

			}
			
			writer.write("------------------------------------------\r\n");
			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());
			
			//处理送菜的问题 [2-3] 表示从2桌子送菜到3号桌子 ，在桌号前加送字
			String special = printDish.getSperequire();
//			String giftMsg = "(礼)-";
			
			if(!org.springframework.util.StringUtils.isEmpty(special) && special.contains("[")){
				String[] arraySpec = special.split(",");
				String subMsg = "";
				String subTableMsg = "  桌号：";
				for(String spec : arraySpec){
					if(!spec.contains("[")){
					  	continue;
					}else{
						subMsg = spec.substring(1, spec.length() -1);
					   break;
					}
				}
				String prefixMsg = " ("+ subMsg.split("-")[0] + "台送)";
				subMsg =   subMsg.split("-")[1] ;
				writer.write(StringUtils.bSubstring2(subTableMsg + subMsg + prefixMsg, 12)+ "\r\n");
				
			    if(special.endsWith(",")){
			    	special = special.substring(0, special.indexOf("[")).concat(special.substring(special.indexOf("]")+2,special.length()));
			    }else{
			    	special = special.substring(0, special.indexOf("[")).concat(special.substring(special.indexOf("]")+1,special.length()));
			    }
				
			}else{
				writer.write(StringUtils.bSubstring2("　　" + object.getTableNo(), 10)+ "\r\n");
			}
			
		
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			
			writer.write(StringUtils.bSubstring2("品项 ", 13)
					+ StringUtils.bSubstring2("数量", 4));
			writer.write(" ");
			writer.write( StringUtils.bSubstring2("单位", 2) + "\r\n");
			
			writer.write("     " + "\r\n");
			writer.flush();// 
			socketOut.write(PrinterConstant.getFd8Font());
			String dishName2 = StringUtils.bSubstring2(StringUtils.BtoQ(
					printDish.getDishName()), 12);
			String dishNum2 = StringUtils.bSubstring3(
					printDish.getDishNum(), 4);
			String dishunit2 = StringUtils.bSubstring2(printDish.getDishUnit(),
					2);

			writer.write(dishName2 );
			writer.flush();//  
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write( " "+dishNum2 + dishunit2				+ "                    \r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(StringUtils.bSubstring2((printDish
					.getAbbrname() == null ? "　" : printDish
					.getAbbrname()), 4));
			
			
//			if(printDish.getSperequire()!=null){
//				special = StringUtils.bSubstring2(printDish
//						.getSperequire(), 30);
//			}
			
			if (special == null || "null".equals(special)) {
				special = "";
			}
			// 只显示出时分秒
//			writer.write(StringUtils.bSubstring3(String.valueOf(Integer.toString(printDishList.get(0)
//							.getMaxDishCount())), 8));
			writer.write(StringUtils.bSubstring3(String.valueOf(object.getOrderseq()== 0 ? "　" : object
					.getOrderseq()), 8));
			writer.flush();// 
			socketOut.write(PrinterConstant.getClear_font());				
			writer.write( StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
							.format(Calendar.getInstance().getTime()), 8)
					+ "\r\n");

			writer.write("------------------------------------------\r\n");
			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(special + "\r\n");

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
	@Qualifier("normalDishQueue")
	private Destination destination;

	@Autowired
	NormalDishPrintService normalDishPrintService;

	@Autowired
	// @Qualifier("producerService")
	private NormalDishProducerService producerService;
	@Autowired
	PrinterService    printerService;
}
