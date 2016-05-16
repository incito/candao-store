package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
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

		socketOut.write(27);
		socketOut.write(27);
		//
		writer.flush();//
		socketOut.write(PrinterConstant.getFdDoubleFont());
		// 单号
		writer.write("　　　" + StringUtils.bSubstring2(billName, 9) + " \r\n");
		writer.flush();//
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("==========================================\r\n");
		// 一行最多能容下42个字符

		writer.write(StringUtils.bSubstring2("账单号:" + object.getOrderNo(), 27) + StringUtils.bSubstring2(object.getTimeMsg(), 10) + "\r\n");

		writer.write(StringUtils.bSubstring2("服务员:" + object.getUserName(), 9) + StringUtils.bSubstring2(object.getTableArea(), 8)
				+ StringUtils.bSubstring2(object.getTimeMsg().substring(11), 8) + "\r\n");

		writer.write("------------------------------------------\r\n");

		writer.flush();//
		socketOut.write(PrinterConstant.getFdDoubleFont());
		writer.write(StringUtils.bSubstring2("　　" + object.getTableNo(), 10) + "\r\n");
		writer.flush();//
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("------------------------------------------\r\n");
		writer.write(StringUtils.bSubstring2("品项 ", 12) + StringUtils.bSubstring2("数量", 4) + StringUtils.bSubstring2("单价", 5) + "\r\n");
		writer.flush();//
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("     " + "\r\n");

		for (PrintDish it : printDishList) {
			it.setDishName(StringUtils.split3(it.getDishName(), "#"));
			it.setDishUnit(StringUtils.split3(it.getDishUnit(), "#"));
		}

		String[] text = getPrintText(printDishList, 11, 3, 5);

		for (int i = 0; i < text.length; i++) {
			writer.write(text[i] + "\r\n");
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
	
	private String[] getPrintText(List<PrintDish> list, int num1, int num2, int num3) throws Exception {
		List<String> res = new LinkedList<String>();
		List<String> name = null;
		List<String> price = null;
		String dishnum = null;

		int rows = 0;
		StringBuffer buffer = new StringBuffer();
		for (PrintDish it : list) {
			name = StringUtils.subString2(StringUtils.BtoQ(it.getDishName()), num1);
			price = StringUtils.subString2(StringUtils.BtoQ(it.getDishPrice() == null?"":it.getDishPrice().toString()), num3);
			dishnum = StringUtils.bSubstring2(StringUtils.BtoQ(it.getDishNum()), num2);

			rows = name.size() >price.size()?name.size():price.size() ;
			for (int i = 0; i < rows; i++) {
				String name2 = i + 1 > name.size() ? StringUtils.getStr(num1) : name.get(i);
				String price2 = i + 1 > price.size() ? StringUtils.getStr(num3) : price.get(i);
				String dishnum2 = i == 0 ? dishnum : StringUtils.getStr(num2);
				buffer.setLength(0);
				buffer.append(name2).append("  ").append(dishnum2).append("  ").append(price2);
				res.add(buffer.toString());
			}
		}

		return res.toArray(new String[res.size()]);
	}
	
	@Autowired
	@Qualifier("custDishQueue")
	private Destination destination;

}
