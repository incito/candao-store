package com.candao.print.listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.listener.template.ListenerTemplate;

@Service
public class WeighDishListener extends AbstractQueueListener{

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
		System.out.println("WeighDishListener receive message");

		String billName = object.getBillName();
		List<PrintDish> printDishList = object.getList();
		
		socketOut.write(PrinterConstant.getFdDoubleFont());

			// 单号
			writer.write("　　　　" + StringUtils.bSubstring2(billName, 6)
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
			if(body != null){
				for (int i = 0; i < body.length; i++) {
					if( i == 0){
						writer.write(StringUtils.bSubstring2("服务员:",4) + body[i]+"\r\n");						
					} else {
						writer.write(StringUtils.getStr(7) + body[i] + "\r\n");
					}
				}
			}

			writer.write("------------------------------------------\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFdDoubleFont());

			String[] tableName = {object.getTableNo()};
			Integer[] tableLength = {20};
			String[] table = StringUtils.getLineFeedText(tableName, tableLength);
			if(table != null){
				for (int i = 0; i < table.length; i++) {
					writer.write("　　" + table[i]+"\r\n");
				}
			}
			
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			
			writer.write(StringUtils.bSubstring2("品项 ", 12)
					+ StringUtils.bSubstring2("预估重量", 5));
			writer.write( StringUtils.bSubstring2("单位", 2) + "\r\n");
			
			for (PrintDish it : printDishList) {
				it.setDishName(StringUtils.split2(it.getDishName(), "#"));
				it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
			}

			Object[] text = getPrintText(object, 23, 10, 8);

			for (int i = 0; i < text.length; i++) {
				writer.write(text[i].toString()+"\r\n");
			}
			
			writer.flush();// 
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(StringUtils.bSubstring2((printDishList.get(0)
					.getAbbrname() == null ? "　" : printDishList.get(0)
					.getAbbrname()), 4));

			// 只显示出时分秒
			writer.write(StringUtils.bSubstring3(String.valueOf(object.getOrderseq()== 0 ? "　" : "第"+object
					.getOrderseq()+"张"), 8));
			writer.flush();// 
			socketOut.write(PrinterConstant.getClear_font());				
			writer.write( StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
							.format(Calendar.getInstance().getTime()), 8)
					+ "\r\n");

			writer.write("------------------------------------------\r\n");
//		writer.flush();//  
//		socketOut.write(PrinterConstant.getFdDoubleFont());
//		writer.write(special + "\r\n");
	}
	
	public PrintData receiveMessage(PrintObj object,ListenerTemplate template) throws Exception {

		return prepareData(object,new PrintData(),template);
	}
	
	private Object[] getPrintText(PrintObj object, int num1, int num2, int num3) throws Exception {
		Object[] res = null;
		
		List<PrintDish> list = object.getList();

		for (PrintDish it : list) {
			// 校验名称
			String dishName = it.getDishName() == null ? "" : it.getDishName();
			String dishNum = it.getDishNum() == null ? "" : it.getDishNum();
			String dishUnit = it.getDishUnit() == null ? "" : it.getDishUnit();

			String[] name = { dishName, dishNum, dishUnit };
			Integer[] len = { num1, num2, num3 };

			String[] temp = StringUtils.getLineFeedText(name, len);
			String[] weight = {" 实际重量："};
			Integer[] length = {num1 + num2 + num3};
			String[] actualWeight = StringUtils.getLineFeedText(weight,length);

			res = ArrayUtils.addAll(ArrayUtils.addAll(res, temp), actualWeight);
		}

		return res;
	}

}

