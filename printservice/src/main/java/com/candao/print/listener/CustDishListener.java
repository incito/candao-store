package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.jms.Destination;

import org.apache.commons.lang.ArrayUtils;
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
			writer.write("　　" + StringUtils.bSubstring2(billName, 9)
					+ " \r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("==========================================\r\n");
			
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
			
			String[] username = {object.getUserName(),object.getTableArea(),object.getTimeMsg().substring(11)};
			Integer[] length = {12,10,8};
			String[] body = StringUtils.getLineFeedText(username, length);
			if (body != null) {
				for (int i = 0; i < body.length; i++) {
					if (i == 0) {
						writer.write(StringUtils.bSubstring2("服务员:", 4) + body[i] + "\r\n");
					} else {
						writer.write(StringUtils.getStr(7) + body[i] + "\r\n");
					}
				}
			}

			writer.write("------------------------------------------\r\n");
		
			writer.flush();//  
			socketOut.write(PrinterConstant.getFdDoubleFont());
			
			String[] tableName = {object.getTableNo()};
			Integer[] tableLength = {15};
			String[] table = StringUtils.getLineFeedText(tableName, tableLength);
			if(table != null){
				for (int i = 0; i < table.length; i++) {
					writer.write("　　" + table[i]+"\r\n");
				}
			}
			
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			writer.write(StringUtils.bSubstring2("品项 ", 13)
					+ StringUtils.bSubstring2("数量", 4)
					+ StringUtils.bSubstring2("单价", 2) + "\r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			
			for (PrintDish it : printDishList) {
				it.setDishName(StringUtils.split3(it.getDishName(), "#"));
				it.setDishUnit(StringUtils.split3(it.getDishUnit(), "#"));
			}
			
			Object[] text = getPrintText(printDishList, 24, 7, 8);
			
			for (int i = 0; i < text.length; i++) {
				writer.write(text[i].toString()+"\r\n");
			}
		
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			
			writer.write("------------------------------------------\r\n");
			writer.write("          欢迎品尝       谢谢惠顾\r\n");
	}

	public String receiveMessage(PrintObj object) {
		printForm(object);
		return null;
	}
	
	private Object[] getPrintText(List<PrintDish> list, int num1, int num2, int num3) throws Exception {
		Object[] res = null;

		for (PrintDish it : list) {
			// 校验名称
			String dishName = it.getDishName() == null ? "" : it.getDishName();
			String dishNum = it.getDishNum() == null ? "" : it.getDishNum();
			String dishPrice = it.getDishPrice() == null ? "" : it.getDishPrice().toString();
			String dishUnit = it.getDishUnit() == null ? "" : "(" + it.getDishUnit() + ")";
			dishName += dishUnit;

			String[] name = { dishName, dishNum, dishPrice };
			Integer[] len = { num1, num2, num3 };

			String[] temp = StringUtils.getLineFeedText(name, len);

			res = ArrayUtils.addAll(res, temp);
		}

		return res;
	}


	@Autowired
	@Qualifier("custDishQueue")
	private Destination destination;

}
