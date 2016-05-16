package com.candao.print.listener;

//import groovy.transform.Synchronized;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.jms.Destination;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.service.PrinterService;
import com.candao.print.service.impl.NormalDishPrintService;

@Service
public class MultiDishListener {

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
		System.out.println("MultiDishListener receive message");

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

			String[] name = {object.getOrderNo(),object.getTimeMsg().substring(0,10)};
			//最多显示34个字符
			Integer[] len = {22,10};
			String[] header = StringUtils.getLineFeedText(name, len);
			if(header != null){
				header[0] = StringUtils.bSubstring2("账单号:",4) + header[0];
				for (int i = 0; i < header.length; i++) {
					writer.write(header[i]+"\r\n");
				}
			}

			String[] username = {object.getUserName(),object.getTableArea(),object.getTimeMsg().substring(11)};
			Integer[] length = {12,10,8};
			String[] body = StringUtils.getLineFeedText(username, length);
			if(body != null){
				body[0] = StringUtils.bSubstring2("服务员:",4) + body[0];
				for (int i = 0; i < body.length; i++) {
					writer.write(body[i]+"\r\n");
				}
			}
			
			if(object.getDiscardUserId()!=null&&!"".equals(object.getDiscardUserId())){
				writer.write(StringUtils.bSubstring2("授权人:" + object.getDiscardUserId(),
						9)
						 + "\r\n");
			}
		
			writer.write("------------------------------------------\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFdDoubleFont());

			String[] tableName = {object.getTableNo()};
			Integer[] tableLength = {10};
			String[] table = StringUtils.getLineFeedText(tableName, tableLength);
			if(table != null){
				for (int i = 0; i < table.length; i++) {
					writer.write("　　" + table[i]+"\r\n");
				}
			}
			
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			
			writer.write(StringUtils.bSubstring2("品项 ", 13)
					+ StringUtils.bSubstring2("数量", 4));
			writer.write(" ");
			writer.write( StringUtils.bSubstring2("单位", 2) + "\r\n");
			
			for (PrintDish it : printDishList) {
				it.setDishName(StringUtils.split2(it.getDishName(), "#"));
				it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
			}
			
			writer.flush();
			socketOut.write(PrinterConstant.getFd8Font());
			
			Object[] text = getPrintText(object, 24, 8, 11);
			for (int i = 0; i < text.length; i++) {
				writer.write(text[i].toString()+"\r\n");
			}
			
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
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
			special = printDishList.get(0).getSperequire() == null ? "" : printDishList.get(0).getSperequire();
			if (special == null || "null".equals(special)) {
				special = "";
			}
			// 只显示出时分秒
//			writer.write(StringUtils.bSubstring3(String.valueOf(Integer.toString(printDishList.get(0)
//							.getMaxDishCount())), 8));
			
			writer.write(StringUtils.bSubstring3(String.valueOf(object.getOrderseq()== 0 ? "　" : "第"+object
					.getOrderseq()+"张"), 8));
			
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());				
			writer.write( StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
							.format(Calendar.getInstance().getTime()), 8)
					+ "\r\n");

			writer.write("------------------------------------------\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFdDoubleFont());
			//填写菜品套餐信息
			if (parentDishName != null && !"".equals(parentDishName)) {
				//套餐备注换行
				String[] dishName = {parentDishName};
				Integer[] dishLength = {20};
				String[] parentDishNameLineFeed = StringUtils.getLineFeedText(dishName, dishLength);
				parentDishNameLineFeed[0] = "备注："+parentDishNameLineFeed[0];
				for (int j = 0; j < parentDishNameLineFeed.length; j++) {
					writer.write( parentDishNameLineFeed[j] + "\r\n");					
				}
			} else {
				if (special != null && !"".equals(special))
					special = "备注：" + special;
			}
			
			//忌口信息
			String[] specialName = {special};
			Integer[] specialLength = {20};
			String[] specialLineFeed = StringUtils.getLineFeedText(specialName, specialLength);
			for (int j = 0; j < specialLineFeed.length; j++) {
				writer.write( specialLineFeed[j] + "\r\n");		
			}

			writer.flush();
			socketOut.write(PrinterConstant.getClear_font());
			// 下面指令为打印完成后自动走纸
			writer.write(27);
			writer.write(100);
			writer.write(4);
			writer.write(10);
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸
			writer.close();
			socketOut.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
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

			res = ArrayUtils.addAll(res, temp);
		}

		return res;
	}

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("multiDishQueue")
	private Destination destination;
	@Autowired
	NormalDishPrintService normalDishPrintService;
	@Autowired
	PrinterService    printerService;

}
