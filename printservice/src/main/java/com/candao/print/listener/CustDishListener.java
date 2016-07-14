package com.candao.print.listener;

import java.util.List;

import org.springframework.stereotype.Service;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.listener.template.ListenerTemplate;
 

@Service
public class CustDishListener extends AbstractQueueListener{

	/**
	 * 
	 * @param message
	 * @return
	 */
	public String receiveMessage(String message) {
		return null;
	}
	
	@Override
	protected void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer, ListenerTemplate template)
			throws Exception {
		System.out.println("CustDishListener receive message");

		String billName = object.getBillName();
		List<PrintDish> printDishList = object.getList();

		socketOut.write(PrinterConstant.getFdDoubleFont());
		// 居中
		socketOut.write(template.setAlignCenter());
		// 单号
		writer.write(StringUtils.bSubstring2(billName, billName.length()));
		writer.write("\r\n");
		
//		Object[] portName = template.getPrinterPortMsg(object);
//		this.write(writer, portName);

		writer.flush();
		// 居中
		socketOut.write(template.setAlignLeft());
		socketOut.write(template.setClearfont());

		writer.write("==========================================\r\n");

		String[] name = { object.getOrderNo(), object.getTimeMsg().substring(0, 10) };
		// 最多显示34个字符
		Integer[] len = { 22, 10 };
		String[] header = StringUtils.getLineFeedText(name, len);
		if (header != null) {
			for (int i = 0; i < header.length; i++) {
				if (i == 0) {
					writer.write(StringUtils.bSubstring2("账单号:", 4) + header[i] + "\r\n");
				} else {
					writer.write(StringUtils.getStr(7) + header[i] + "\r\n");
				}
			}
		}

		String[] username = { object.getUserName(), object.getTableArea(), object.getTimeMsg().substring(11) };
		Integer[] length = { 12, 10, 8 };
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

		// -------------------------------分割点-----------------------------------
		socketOut.write(template.getBodyFont());

		// 居中
		socketOut.write(template.setAlignCenter());
		String[] table = template.getTableMsg(object);
		this.write(writer, table);
		// 左对齐
		socketOut.write(template.setAlignLeft());

		writer.flush();//
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("------------------------------------------\r\n");
		writer.write(StringUtils.bSubstring2("品项 ", 13) + StringUtils.bSubstring2("数量", 4)
				+ StringUtils.bSubstring2("单价", 2) + "\r\n");
		writer.flush();//

		socketOut.write(template.getBodyFont());

		Object[] text = template.getBodyMsg(object);

		for (int i = 0; i < text.length; i++) {
			writer.write(text[i].toString() + "\r\n");
		}

		writer.flush();//
		socketOut.write(PrinterConstant.getClear_font());

		writer.write("------------------------------------------\r\n");
		socketOut.write(template.setAlignCenter());
		writer.write("欢迎品尝       谢谢惠顾\r\n");
		socketOut.write(template.setAlignLeft());
	}

	public PrintData receiveMessage(PrintObj object , ListenerTemplate template) throws Exception {
		return prepareData(object,new PrintData(),template);
	}

}
