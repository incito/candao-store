package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.jms.Destination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import com.candao.common.utils.Constant;
import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.service.NormalDishProducerService;
import com.candao.print.service.impl.NormalDishPrintService;

/**
 *套餐小票（传菜员使用）监听类 
 * @author caicai
 *
 */
public class DishSetListener {

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
		System.out.println("DishSetListener receive message");

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
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
			socketOut.write(27);
			socketOut.write(27);
			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());
			
			// 单号
			writer.write("　　　   " + StringUtils.bSubstring2(billName, 9)
					+ " \r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("==========================================\r\n");
			// 一行最多能容下42个字符

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
			writer.write(StringUtils.bSubstring2(
					"　　" + object.getTableNo(), 10)
					+ "\r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			
			writer.write("------------------------------------------\r\n");
			//注意格式与客用单保持 一致
			writer.write(StringUtils.bSubstring2("品项 ", 12)
					+ StringUtils.bSubstring2("数量", 4)
					+ StringUtils.bSubstring2("单位", 5) + "\r\n");
			
			writer.flush();//  
			socketOut.write(PrinterConstant.getFd8Font());
			writer.write("     " + "\r\n");
			
			for (PrintDish it : printDishList) {
				it.setDishName(StringUtils.split2(it.getDishName(), "#"));
				it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
			}

			for (PrintDish printDish : printDishList) {
				String dishName2 = StringUtils.bSubstring2(
						StringUtils.BtoQ(printDish.getDishName()), 12);
				
				String dishNum2 = StringUtils.bSubstring2(StringUtils.BtoQ(
						printDish.getDishNum()), 4);
				String dishunit2 = StringUtils.bSubstring2(printDish
						.getDishUnit() == null ? "" : StringUtils.BtoQ(printDish.getDishUnit()
						.toString()), 5);
				writer.write(dishName2);
				writer.write( dishNum2 );
				writer.write( dishunit2 + "\r\n");
			 }
		
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			
			writer.write("------------------------------------------\r\n");
			
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(StringUtils.bSubstring2((printDishList.get(0)
					.getAbbrname() == null ? "　" : printDishList.get(0)
					.getAbbrname()), 4));
			
			//菜品套餐信息
			String parentDishName = "";
			List<String> buffer = new LinkedList<>();
			for (PrintDish it : printDishList) {
				if(it.getParentDishName() != null && !"".equals(it.getParentDishName())){
					if(!buffer.contains(it.getParentDishName()))
						buffer.add(it.getParentDishName());
				}
			}
			for (int i = 0; i < buffer.size(); i++) {
				if (i != 0) {
					parentDishName = parentDishName.concat("，").concat(buffer.get(i));
				} else {
					parentDishName = parentDishName.concat(buffer.get(i));
				}
			}

			String special = "";
			special = StringUtils.bSubstring2(printDishList.get(0)
					.getSperequire()==null?"":printDishList.get(0)
							.getSperequire(), 30);
			if (special == null || "null".equals(special)) {
				special = "";
			}
			
			writer.write(StringUtils.bSubstring3(String.valueOf(object.getOrderseq()), 8));
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());				
			writer.write( StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
							.format(Calendar.getInstance().getTime()), 8)
					+ "\r\n");

			writer.write("------------------------------------------\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			//填写菜品套餐信息
			if (parentDishName != null && !"".equals(parentDishName)) {
				writer.write("备注：" + parentDishName + "\r\n");
			}
			
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(special + "\r\n");			
			
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
			jmsTemplate.convertAndSend(destination, object);
		} finally {

		}
		return null;

	}

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("dishSetQueue")
	private Destination destination;

	@Autowired
	NormalDishPrintService normalDishPrintService;

	@Autowired
	private NormalDishProducerService producerService;


}
