package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;

@Service
public class StatementDishListener extends AbstractPrintListener{
	
	
	public StatementDishListener() {
		super("statementDishListener");
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
		System.out.println("StatamentListener receive message");
		
		String billName = object.getBillName();
		
		Map<String, Object>   ordermap=object.getOrdermap();
		
		socketOut.write(PrinterConstant.getFdDoubleFont());
		// 单号
		writer.write("　　　　" + StringUtils.bSubstring2(billName, 6)
		+ " \r\n"+ " \r\n");
		writer.flush();//
		socketOut.write(PrinterConstant.getClear_font());
		
		writer.write(StringUtils.bSubstring2("结账单号:" + object.getOrderNo(),
				27)
				//+ StringUtils.bSubstring2(object.getTimeMsg(), 10)
				+ "\r\n"+"\r\n");

		writer.write(StringUtils.bSubstring2("厅:" + object.getTableArea(),
				10)
				+ StringUtils.bSubstring2("客位: "+object.getTableNo(), 13)
				+ "\r\n");
		
		writer.write(StringUtils.bSubstring2("人数:" +getValueMap(ordermap, "custnum"),
				10)
				+ StringUtils.bSubstring2("服务员: "+object.getUserName(), 13)
				+ "\r\n");

		writer.write(StringUtils.bSubstring2("开始时间:" + getValueMap(ordermap, "begintime"),
				27)
				+ "\r\n");
		writer.write(StringUtils.bSubstring2("结束时间:" + getValueMap(ordermap, "endtime"),
				27)
				+ "\r\n");
		writer.write("------------------------------------------\r\n");
		writer.flush();
		
		writer.write(
				StringUtils.bSubstring2("品项", 7)
				+StringUtils.bSubstring2("数量", 3)
				+StringUtils.bSubstring2("单价", 3)
				+StringUtils.bSubstring2("小计", 7)
				+"\r\n");
		writer.write("------------------------------------------\r\n");
		writer.flush();//  
		//循环菜品
		List<Map<String, Object>>   list=object.getDishes();
		BigDecimal  sum=new BigDecimal("0.00");
		if(list!=null && list.size()>0){
			
			for (Map<String, Object> it : list) {
				it.put("title",StringUtils.split2(it.get("title").toString(), "#"));
			}
			
			for(Map<String, Object> printDish:list){
				BigDecimal  total=new BigDecimal("0.00");
				String  dishnum= getValueMap(printDish, "dishnum");
				String  title=getValueMap(printDish, "title");
				String  orderprice=getValueMap(printDish, "orderprice");
				BigDecimal  price=new BigDecimal(orderprice);
				total=price.multiply(new BigDecimal(dishnum));
				sum=sum.add(total);
					writer.write(
							StringUtils.bSubstring2(title+"  ", 9)
							+StringUtils.bSubstring2(dishnum+"  ", 3)
							+StringUtils.bSubstring2(price.toString()+"  ", 7)
							+StringUtils.bSubstring2(total.toString(), 7)
							+ "\r\n");
			}
		}
		writer.write("------------------------------------------\r\n");
		writer.flush();
			
		writer.write(
				StringUtils.bSubstring3("结算方式 ", 11)
				+StringUtils.bSubstring3("金额", 5)
				+StringUtils.bSubstring3("备注", 8)
				+ "\r\n");
		writer.write("------------------------------------------\r\n");
		writer.write(
				StringUtils.bSubstring3("微信支付", 11)
				+StringUtils.bSubstring3(sum.toString(), 5)
				+StringUtils.bSubstring3("    ", 8)
				+ "\r\n");
		writer.write("------------------------------------------\r\n");
		writer.flush();
			
		socketOut.write(PrinterConstant.getClear_font());

		writer.flush();// 
		socketOut.write(PrinterConstant.getFdDoubleFont());
	}

	public String receiveMessage(PrintObj object) {
		printForm(object);
		return null;
	}

	
	private String getValueMap(Map<String, Object> ordermap,String key){
		if(ordermap==null){
			return "";
		}
		SimpleDateFormat  dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if("begintime".equals(key) || "endtime".equals(key)){
			Date  date= (Date) ordermap.get(key);
			return dateFormat.format(date);
		}
		Object obj= ordermap.get(key);
		String temp=obj.toString();
		if(temp==null){
			return "";
		}else{
			return temp;
		}
	}
	
	@Autowired
	@Qualifier("statementQueue")
	private Destination destination;

}
