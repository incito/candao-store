package com.candao.print.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class WeighDishListener extends AbstractPrintListener{
	
	LoggerHelper logger = LoggerFactory.getLogger(WeighDishListener.class);

	public WeighDishListener() {
		super("WeighDishListener");
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

	@Override
	protected void printBusinessData(PrintObj object, OutputStream socketOut, OutputStreamWriter writer) throws Exception {
		System.out.println("WeighDishListener receive message");
		logger.error("-------------------------------------", "");
		logger.error("打印称重单开始,订单号：" + object.getOrderNo(), "");

		String billName = object.getBillName();
		List<PrintDish> printDishList = object.getList();
		
		socketOut.write(PrinterConstant.getFdDoubleFont());

		// 单号
		writer.write("　　　　" + StringUtils.bSubstring2(billName, 6)
				+ " \r\n");
		writer.flush();//
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("==========================================\r\n");

		writer.write(StringUtils.bSubstring2("账单号:" + object.getOrderNo(),
				27)
				+ StringUtils.bSubstring2(object.getTimeMsg(), 10)
				+ "\r\n");

		writer.write(StringUtils.bSubstring2("服务员:" + object.getUserName(),
				9)
				+ StringUtils.bSubstring2(object.getTableArea(), 8)
				+ StringUtils.bSubstring2(
						object.getTimeMsg().substring(11), 8) + "\r\n");

	
		writer.write("------------------------------------------\r\n");
		writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
		socketOut.write(PrinterConstant.getFdDoubleFont());
		writer.write(StringUtils.bSubstring2(
				"　　" + object.getTableNo(), 10)
				+ "\r\n");
		writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("------------------------------------------\r\n");
		
		writer.write(StringUtils.bSubstring2("品项 ", 13)
				+ StringUtils.bSubstring2("预估重量", 4));
		writer.write(" ");
		writer.write( StringUtils.bSubstring2("单位", 2) + "\r\n");
		
		
		writer.write("     " + "\r\n");
		
		for (PrintDish it : printDishList) {
			it.setDishName(StringUtils.split2(it.getDishName(), "#"));
			it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
		}

		for (PrintDish printDish : printDishList) {
			
			String dishName2 = StringUtils.bSubstring2(StringUtils.BtoQ(
					printDish.getDishName()), 12);
			String dishNum2 = StringUtils.bSubstring3(
					printDish.getDishNum(), 4);
			String dishunit2 = StringUtils.bSubstring2(printDish.getDishUnit(),
					2);
			writer.flush();// 
			socketOut.write(PrinterConstant.getFd8Font());
			writer.write(dishName2 );
			writer.flush();// 
			socketOut.write(PrinterConstant.getFdDoubleFont());
			writer.write( " "+dishNum2 + dishunit2
					+ "                    \r\n");
			writer.write( " 实际重量："+ "       \r\n");

		}
		writer.flush();
		socketOut.write(PrinterConstant.getClear_font());
		writer.write("------------------------------------------\r\n");
		writer.flush();// 
		socketOut.write(PrinterConstant.getFdDoubleFont());
		writer.write(StringUtils.bSubstring2((printDishList.get(0)
				.getAbbrname() == null ? "　" : printDishList.get(0)
				.getAbbrname()), 4));

		// 只显示出时分秒
		writer.write(StringUtils.bSubstring3(String.valueOf(Integer.toString(printDishList.get(0)
						.getOrderseq())), 8));
		writer.flush();// 
		socketOut.write(PrinterConstant.getClear_font());				
		writer.write( StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
						.format(Calendar.getInstance().getTime()), 8)
				+ "\r\n");

		writer.write("------------------------------------------\r\n");
		
		logger.error("-------------------------------------", "");
		logger.error("打印称重单结束,订单号：" + object.getOrderNo(), "");
	}
	
	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public String receiveMessage(PrintObj object) {
		printForm(object);
		return null;
	}

	@Autowired
	@Qualifier("weightQueue")
	private Destination destination;

}

