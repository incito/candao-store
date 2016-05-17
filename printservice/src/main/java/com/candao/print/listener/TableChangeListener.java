package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;

@Service
public class TableChangeListener extends AbstractPrintListener{
	
	
	public TableChangeListener() {
		super("TableChangeListener");
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
		System.out.println("TableChangeListener receive message");

		String billName = object.getBillName();
		
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
				17)
//					+ StringUtils.bSubstring2(object.getTableArea(), 8)
				+ StringUtils.bSubstring2(
						object.getTimeMsg().substring(11), 8) + "\r\n");
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
		
	}
	
	public String receiveMessage(PrintObj object) {
		printForm(object);
		return null;
	}

	@Autowired
	@Qualifier("tableSwitchQueue")
	private Destination destination;

}
