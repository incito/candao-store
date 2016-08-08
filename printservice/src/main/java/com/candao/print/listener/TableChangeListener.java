package com.candao.print.listener;

import org.springframework.stereotype.Service;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.listener.template.ListenerTemplate;

@Service
public class TableChangeListener extends AbstractQueueListener{
	

	/**
	 * 
	 * @param message
	 * @return
	 */
	public String receiveMessage(String message) {
		return null;
	}

	@Override
	protected void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer,ListenerTemplate template) throws Exception {
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

	}
	
	public PrintData receiveMessage(PrintObj object,ListenerTemplate template) throws Exception {
		return prepareData(object,new PrintData(),template);
	}

}
