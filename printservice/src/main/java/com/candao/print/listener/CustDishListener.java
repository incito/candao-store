package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
 

@Service
public class CustDishListener extends AbstractPrintListener{

	public CustDishListener() {
		super("custDishListener");
	}

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
	
	@Override
	protected void printBusinessData(PrintObj object, OutputStream socketOut, OutputStreamWriter writer) throws Exception {
		System.out.println("CustDishListener receive message");

		String billName = object.getBillName();
		List<PrintDish> printDishList = object.getList();

		socketOut.write(PrinterConstant.getFdDoubleFont());
		// 单号
		writer.write("　　　" + StringUtils.bSubstring2(billName, 9)
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
	}

	public String receiveMessage(PrintObj object) {
		printForm(object);
		return null;
	}
	
	@Autowired
	@Qualifier("custDishQueue")
	private Destination destination;

}
