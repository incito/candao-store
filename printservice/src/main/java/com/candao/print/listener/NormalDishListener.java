package com.candao.print.listener;

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

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.Constant;
import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.service.impl.NormalDishPrintService;

@Service
public class NormalDishListener {
	LoggerHelper logger = LoggerFactory.getLogger(NormalDishListener.class);
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
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
			socketOut.write(27);
			socketOut.write(27);
			
			PrintDish printDish = object.getpDish().get(0);//TODO 临时处理
			writer.flush();//
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
				
				String[] tableName = {subTableMsg + subMsg + prefixMsg};
				Integer[] tableLength = {15};
				String[] table = StringUtils.getLineFeedText(tableName, tableLength);
				if(table != null){
					for (int i = 0; i < table.length; i++) {
						writer.write(table[i]+"\r\n");
					}
				}
				
			    if(special.endsWith(",")){
			    	special = special.substring(0, special.indexOf("[")).concat(special.substring(special.indexOf("]")+2,special.length()));
			    }else{
			    	special = special.substring(0, special.indexOf("[")).concat(special.substring(special.indexOf("]")+1,special.length()));
			    }
				
			}else{
				String[] tableName = {object.getTableNo()};
				Integer[] tableLength = {10};
				String[] table = StringUtils.getLineFeedText(tableName, tableLength);
				if(table != null){
					for (int i = 0; i < table.length; i++) {
						writer.write("　　" + table[i]+"\r\n");
					}
				}
			}
			
		
			writer.flush();//  
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			
			writer.write(StringUtils.bSubstring2("品项 ", 13)
					+ StringUtils.bSubstring2("数量", 4));
			writer.write(" ");
			writer.write( StringUtils.bSubstring2("单位", 2) + "\r\n");
			writer.flush();// 
			logger.error("------------------------","");
			logger.error("打印菜品，订单号："+object.getOrderNo()+"*菜品数量：" + (object.getpDish() == null ? 0 : object.getpDish().size()), "");
			for (PrintDish it : object.getpDish()) {
				it.setDishName(StringUtils.split2(it.getDishName(), "#"));
				it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
			}
			
			socketOut.write(PrinterConstant.getFd8Font());
			Object[] text = getPrintText(object, 24, 8, 11);

			for (int i = 0; i < text.length; i++) {
				writer.write(text[i].toString()+"\r\n");
			}
			writer.flush();
			
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			writer.flush();//  
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write(StringUtils.bSubstring2((printDish
					.getAbbrname() == null ? "　" : printDish
					.getAbbrname()), 4));
			
			//菜品套餐信息
			String parentDishName = "";
			List<String> buffer = new LinkedList<>();
			for (PrintDish it : object.getpDish()) {
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
			
			//合并打印的忌口处理
			boolean isSame = true; //判断所有菜品的备注信息是否一样
			String preSperequire = object.getpDish().get(0).getSperequire();
			for (PrintDish singleDish : object.getpDish()) {
				if(preSperequire != singleDish.getSperequire() && (preSperequire != null && !preSperequire.equals(singleDish.getSperequire()))){
					isSame = false;
				}
				preSperequire = singleDish.getSperequire();
			}
			//非全单备注
			if(!isSame){
				special = "";
				for (PrintDish singleDish : object.getpDish()) {
					if(singleDish.getSperequire() != null && !singleDish.getSperequire().isEmpty()){
						special += singleDish.getDishName() + "：" + singleDish.getSperequire() + "\r\n";
					}
				}
			}
			
			if (special == null || "null".equals(special)) {
				special = "";
			}
			if(!special.isEmpty() && isSame && object.getpDish().size() > 1){//合并打印时全单备注特殊处理
				special = "全单" + special;
			}
			// 只显示出时分秒
//			writer.write(StringUtils.bSubstring3(String.valueOf(Integer.toString(printDishList.get(0)
//							.getMaxDishCount())), 8));
			writer.write(StringUtils.bSubstring3(String.valueOf(object.getOrderseq()== 0 ? "　" : "第"+object
					.getOrderseq()+"张"), 8));
			writer.flush();// 
			socketOut.write(PrinterConstant.getClear_font());				
			writer.write( StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
							.format(Calendar.getInstance().getTime()), 8)
					+ "\r\n");

			writer.write("------------------------------------------\r\n");
			writer.flush();// 
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
			writer.flush();// 
			socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸
			writer.close();
			socketOut.close();
			socket.close();
			logger.error("-----------------------------", "");
			logger.error("打印完成，订单号：" + object.getOrderNo(), "");

		} catch (Exception e) {
			logger.error("------------------------","");
			logger.error("打印异常，订单号："+object.getOrderNo()+e.getMessage(), e, "");
			
			jmsTemplate.convertAndSend(destination, object);
		} finally {

		}
		return null;
		// }

	}
	
	private Object[] getPrintText(PrintObj object, int num1, int num2, int num3) throws Exception {
		Object[] res = null;
		
		List<PrintDish> list = object.getpDish();

		for (PrintDish it : list) {
			// 校验名称
			String dishName = it.getDishName() == null ? "" : it.getDishName();
			String dishNum = it.getDishNum() == null ? "" : it.getDishNum();
			String dishUnit = it.getDishUnit() == null ? "" : it.getDishUnit();
			logger.error("------------------------","");
			logger.error("订单号："+object.getOrderNo()+"*打印菜品：" + it.getDishName(),"");
			
			if (2 == it.getDishtype()) {
				dishName = "（套）"+dishName;
			}

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
	@Qualifier("normalDishQueue")
	private Destination destination;
	@Autowired
	NormalDishPrintService normalDishPrintService;

}
