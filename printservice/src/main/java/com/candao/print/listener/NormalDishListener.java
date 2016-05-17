package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;

@Service
public class NormalDishListener extends AbstractPrintListener {
	
	LoggerHelper logger = LoggerFactory.getLogger(NormalDishListener.class);

	@Autowired
	@Qualifier("normalDishQueue")
	private Destination destination;
	
	public NormalDishListener() {
		super("normalDishListener");
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public String receiveMessage(PrintObj object) {
		System.out.println("NormalDishListener receive message");
		
		printForm(object);
		
		return null;

	}

	protected void printBusinessData(PrintObj object, OutputStream socketOut, OutputStreamWriter writer) throws Exception
	 {

		String billName = object.getBillName();
		PrintDish printDish = object.getpDish().get(0);//TODO 临时处理

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
				9)
				+ StringUtils.bSubstring2(object.getTableArea(), 8)
				+ StringUtils.bSubstring2(
						object.getTimeMsg().substring(11), 8) + "\r\n");
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
			writer.write(StringUtils.bSubstring2(subTableMsg + subMsg + prefixMsg, 15)+ "\r\n");
			
		    if(special.endsWith(",")){
		    	special = special.substring(0, special.indexOf("[")).concat(special.substring(special.indexOf("]")+2,special.length()));
		    }else{
		    	special = special.substring(0, special.indexOf("[")).concat(special.substring(special.indexOf("]")+1,special.length()));
		    }
			
		}else{
			writer.write(StringUtils.bSubstring2("　　" + object.getTableNo(), 10)+ "\r\n");
		}
		

		writer.flush();//  
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("------------------------------------------\r\n");
		
		writer.write(StringUtils.bSubstring2("品项 ", 13)
				+ StringUtils.bSubstring2("数量", 4));
		writer.write(" ");
		writer.write( StringUtils.bSubstring2("单位", 2) + "\r\n");
		
		writer.write("     " + "\r\n");
		writer.flush();// 
		logger.error("------------------------","");
		logger.error("打印菜品，订单号："+object.getOrderNo()+"*菜品数量：" + (object.getpDish() == null ? 0 : object.getpDish().size()), "");
		for (PrintDish it : object.getpDish()) {
			it.setDishName(StringUtils.split2(it.getDishName(), "#"));
			it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
		}
		
		//合并打印
		for (PrintDish singleDish : object.getpDish()) {
			logger.error("------------------------","");
			logger.error("订单号："+object.getOrderNo()+"*打印菜品：" + singleDish.getDishName(),"");
			socketOut.write(PrinterConstant.getFd8Font());
			String dishNum2 = StringUtils.bSubstring3(
					singleDish.getDishNum(), 4);
			String dishunit2 = StringUtils.bSubstring2(singleDish.getDishUnit(),
					2);
			
			int spaceNum = 12;
			if (2 == printDish.getDishtype()){
				spaceNum = 9;
				writer.write("（套）");
			}
			String dishName2 = StringUtils.bSubstring2(StringUtils.BtoQ(
					singleDish.getDishName()), spaceNum );
			writer.write(dishName2);
			writer.flush();//  
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write( " "+dishNum2 + dishunit2				+ "                    \r\n");
			writer.flush();//  
		}
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("------------------------------------------\r\n");
		writer.flush();//  
		socketOut.write(PrinterConstant.getFdDoubleFont());
		writer.write(StringUtils.bSubstring2((printDish
				.getAbbrname() == null ? "　" : printDish
				.getAbbrname()), 4));
		
		
//			if(printDish.getSperequire()!=null){
//				special = StringUtils.bSubstring2(printDish
//						.getSperequire(), 30);
//			}
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
		writer.write(StringUtils.bSubstring3(String.valueOf(object.getOrderseq()== 0 ? "　" : object
				.getOrderseq()), 8));
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
			writer.write("备注：" + parentDishName + "\r\n");
		}
		
		socketOut.write(PrinterConstant.getFdDoubleFont());
		writer.write(special + "\r\n");

	}

}
