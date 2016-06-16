package com.candao.print.listener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.candao.print.entity.PrintData;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;

@Service
public class NormalDishListener extends AbstractQueueListener {
	
	LoggerHelper logger = LoggerFactory.getLogger(NormalDishListener.class);

	public PrintData receiveMessage(PrintObj object) throws Exception {
		System.out.println("NormalDishListener receive message");
		return prepareData(object,new PrintData());
	}

	protected void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer) throws Exception
	 {

		String billName = object.getBillName();
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
				Integer[] tableLength = {15};
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
			writer.write( StringUtils.bSubstring2("单位", 2) + "\r\n");
			writer.flush();// 
			logger.error("------------------------","");
			logger.error("打印菜品，订单号："+object.getOrderNo()+"*菜品数量：" + (object.getpDish() == null ? 0 : object.getpDish().size()), "");
			for (PrintDish it : object.getpDish()) {
				it.setDishName(StringUtils.split2(it.getDishName(), "#"));
				it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
			}
			
			socketOut.write(PrinterConstant.getFd8Font());
			Object[] text = getPrintText(object, 24, 7, 8);

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
					break;
				}
			}
			
			List<String> bufferList = new ArrayList<>();
			if (special != null && !special.isEmpty()) {
				bufferList.add(special);
			}
			//非全单备注
			if(!isSame){
				special = "";
				bufferList.clear();
				for (PrintDish singleDish : object.getpDish()) {
					if(singleDish.getSperequire() != null && !singleDish.getSperequire().isEmpty()){
						bufferList.add(singleDish.getDishName() + "：" + singleDish.getSperequire());
					}
				}
			}
			
			if (special == null || "null".equals(special)) {
				special = "";
			}
			if(!special.isEmpty() && isSame && object.getpDish().size() > 1){//合并打印时全单备注特殊处理
				bufferList.clear();
				bufferList.add("全单" + special);
			}
			// 只显示出时分秒
//			writer.write(StringUtils.bSubstring3(String.valueOf(Integer.toString(printDishList.get(0)
//							.getMaxDishCount())), 8));
			writer.write(StringUtils.bSubstring3(String.valueOf(object.getOrderseq()== 0 ? "　" : "第"+object
					.getOrderseq()+"张"), 7));
			writer.flush();// 
			socketOut.write(PrinterConstant.getClear_font());				
			writer.write( StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
							.format(Calendar.getInstance().getTime()), 8)
					+ "\r\n");

			writer.write("------------------------------------------\r\n");
			writer.flush();// 
			socketOut.write(PrinterConstant.getFd8Font());
			//填写菜品套餐信息
			if (parentDishName != null && !"".equals(parentDishName)) {
				//套餐备注换行
				String[] dishName = {parentDishName};
				Integer[] dishLength = {38};
				String[] parentDishNameLineFeed = StringUtils.getLineFeedText(dishName, dishLength);
				parentDishNameLineFeed[0] = "备注："+parentDishNameLineFeed[0];
				for (int j = 0; j < parentDishNameLineFeed.length; j++) {
					writer.write( parentDishNameLineFeed[j] + "\r\n");					
				}
			} else {
				if (bufferList != null && !bufferList.isEmpty()){
					String temp = bufferList.get(0);
					bufferList.set(0,"备注:" + temp);
				}
			}
			
			//忌口信息
			if(bufferList != null && !bufferList.isEmpty()){
				for (String it : bufferList) {
					String[] specialName = {it};
					Integer[] specialLength = {38};
					String[] specialLineFeed = StringUtils.getLineFeedText(specialName, specialLength);
					for (int j = 0; j < specialLineFeed.length; j++) {
						writer.write( specialLineFeed[j] + "\r\n");		
					}									
				}
			}
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

}
