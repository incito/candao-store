package kaiying;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.www.constant.Constant;

public class TestCustPrint {

	public static void main(String[] args) {

		Map<String, String> mapip = new HashMap<String, String>();
		mapip.put("0", "192.168.1.251:9100");
		mapip.put("1", "192.168.1.251:9100");
		 
		System.out.println("NormalDishListener receive message");
		OutputStream socketOut = null;
		OutputStreamWriter writer = null;
		Socket socket = null;
		String ipAddress = null;
		int print_port;
	 
		String billName = "测试单";
		List<PrintDish> printDishList =  new ArrayList<PrintDish>();
		
		PrintObj object = new PrintObj();
		object.setAbbrbillName("测试");
	    object.setBillName(billName);
	    object.setCustomerPrinterIp("192.168.2.113");
	    object.setCustomerPrinterPort("9100");
	    object.setDataMsg("");
//	    object.setPrintnum(2);
	    object.setOrderNo("H2015060611222009");
	    object.setTimeMsg("2014-01-21 18:30:03");
	    object.setTableNo("大堂000");
	    object.setUserName("张三三");
	    
	    PrintDish pDish = new PrintDish();
	    pDish.setAbbrname("退");
	    pDish.setDishName("测试打印");
	    pDish.setDishPrice(new BigDecimal(10.00));
	    pDish.setDishUnit("份");
	    pDish.setMaxDishCount(4);
	    pDish.setPayAmount(new BigDecimal(10.00));
	    pDish.setPrintipaddress("192.168.2.113");
	    pDish.setSperequire("清蒸");
	    pDish.setTableNomsg("20");
	    pDish.setDishNum("3");
	    
	    
	    PrintDish pDish2 = new PrintDish();
	    pDish2.setAbbrname("退");
	    pDish2.setDishName("测试打印");
	    pDish2.setDishPrice(new BigDecimal(10.00));
	    pDish2.setDishUnit("份");
	    pDish2.setMaxDishCount(4);
	    pDish2.setPayAmount(new BigDecimal(10.00));
	    pDish2.setPrintipaddress("192.168.2.113");
	    pDish2.setSperequire("清蒸");
	    pDish2.setTableNomsg("20");
	    pDish2.setDishNum("2");
	    
	    printDishList.add(pDish);
	    printDishList.add(pDish2);
		
	    try {
			billName = object.getBillName();
			ipAddress = object.getCustomerPrinterIp();
			print_port = Integer.parseInt(object.getCustomerPrinterPort());

			
			socket = new Socket(ipAddress, print_port);
//			socket = new Socket("192.168.40.138", 9100);
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);

			socketOut.write(27);
			socketOut.write(27);
			//
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFd12Font());
			// 单号
			writer.write("　　　" + StringUtils.bSubstring2(billName, 9)
					+ " \r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("==========================================\r\n");
			// 一行最多能容下42个字符

			writer.write(StringUtils.bSubstring2("账单号:" + object.getOrderNo(),
					24)
					+ StringUtils.bSubstring2(object.getTimeMsg(), 10)
					+ "\r\n");

			writer.write(StringUtils.bSubstring2("服务员:" + object.getUserName(),
					9)
					+ StringUtils.bSubstring2(object.getTableArea(), 8)
					+ StringUtils.bSubstring2(
							object.getTimeMsg().substring(11), 8) + "\r\n");

			writer.write("------------------------------------------\r\n");
			writer.write("                ");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFd8Font());
			writer.write(StringUtils.bSubstring2(StringUtils.BtoQ(object.getTableNo()), 10)
					+ "\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("------------------------------------------\r\n");
			writer.write(StringUtils.bSubstring2("品项 ", 12)
					+ StringUtils.bSubstring2("数量", 4)
					+ StringUtils.bSubstring2("单价", 5) + "\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFd8Font());
//			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
//			socketOut.write(PrinterConstant.getLineFont());
//			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
//			socketOut.write(PrinterConstant.getLineFont());
			writer.write("     " + "\r\n");

			for (PrintDish printDish : printDishList) {
				String dishName2 = StringUtils.bSubstring2(
						StringUtils.BtoQ(printDish.getDishName()), 12);
				
				String dishNum2 = StringUtils.bSubstring2(StringUtils.BtoQ(
						printDish.getDishNum()), 4);
				String dishPrice2 = StringUtils.bSubstring2(printDish
						.getDishPrice() == null ? "" : StringUtils.BtoQ(printDish.getDishPrice()
						.toString()), 5);
				writer.write(dishName2);
			
				writer.write( dishNum2 );
				writer.write( dishPrice2 + "\r\n");
				
				
				writer.write("     " + "\r\n");
				
				
			}
		
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());
			
			writer.write("------------------------------------------\r\n");
			writer.write("       欢迎品尝       谢 谢 惠 顾\r\n");

			writer.write(27);// 重置
			writer.write(100);
			writer.write(4);
			writer.write(10);// 换行
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸
			writer.close();
			socketOut.close();
			socket.close();
		} catch (Exception e) {
			 e.printStackTrace();
			// e.printStackTrace();
		} finally {

		}
	}

}
