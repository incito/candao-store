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

public class Test8 {

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
		object.setAbbrbillName("");
	    object.setBillName(billName);
	    object.setCustomerPrinterIp("");
	    object.setCustomerPrinterPort("");
	    object.setDataMsg("");
//	    object.setPrintnum(2);
	    object.setOrderNo("H2015060611222009");
	    object.setTimeMsg("2011-01-20 18:30:03");
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
	    pDish.setPrintport("9100");
	    pDish.setDishNum("2");
	    
	    
	    printDishList.add(pDish);
		
		try {
			ipAddress = printDishList.get(0).getPrintipaddress();
			billName = object.getBillName();
			print_port = Integer.parseInt(printDishList.get(0).getPrintport());// Integer.parseInt(address[1]);

			socket = new Socket(ipAddress, print_port);
//			socket = new Socket("192.168.40.138", 9100);
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
			socketOut.write(27);
			socketOut.write(27);
			writer.flush();//
			socketOut.write(PrinterConstant.getFd12Font());

			PrintDish printDish = printDishList.get(0);
			// 单号
			writer.write("　　　　" + StringUtils.bSubstring2(billName, 6)
					+ " \r\n");
			writer.flush();//
			socketOut.write(PrinterConstant.getClear_font());
			writer.write("==========================================\r\n");

			writer.write(StringUtils.bSubstring2("账单号:" + object.getOrderNo(),
					24)
					+ StringUtils.bSubstring2(object.getTimeMsg(), 10)
					+ "\r\n");

			writer.write(StringUtils.bSubstring2("服务员:" + object.getUserName(),
					9)
					+ StringUtils.bSubstring2(object.getTableArea(), 8)
					+ StringUtils.bSubstring2(
							object.getTimeMsg().substring(11), 8) + "\r\n");

			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFd12Font());
			writer.write("------------------------------------------\r\n");
			writer.write(StringUtils.bSubstring2(
					"　　" + StringUtils.BtoQ(object.getTableNo()), 10)
					+ "\r\n");
			writer.write("------------------------------------------\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getClear_font());
			writer.write(StringUtils.bSubstring2("品项 ", 11)
					+ StringUtils.bSubstring2("数量", 6)
					+ StringUtils.bSubstring2("单位", 5) + "\r\n");
			
			writer.write("     " + "\r\n");
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFd8Font());
			String dishName2 = StringUtils.bSubstring2(
					StringUtils.BtoQ(printDish.getDishName()), 10);
			String dishNum2 = StringUtils.bSubstring2(
					StringUtils.BtoQ(printDish.getDishNum()), 3);
			String dishunit2 = StringUtils.bSubstring2(printDish.getDishUnit(),
					2);

			writer.write(dishName2 );
			writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
			socketOut.write(PrinterConstant.getFd12Font());
			writer.write( " "+dishNum2 + dishunit2
					+ "                    \r\n");

			writer.write("------------------------------------------\r\n");

			writer.write(StringUtils.bSubstring2((printDishList.get(0)
					.getAbbrname() == null ? "　" : printDishList.get(0)
					.getAbbrname()), 4));

			String special = "";
			special = StringUtils.bSubstring2(StringUtils.BtoQ(printDishList.get(0)
					.getSperequire()), 30);
			if (special == null || "null".equals(special)) {
				special = "";
			}
			// 只显示出时分秒
			writer.write(StringUtils.bSubstring2(String.valueOf(StringUtils
					.BtoQ(Integer.toString(printDishList.get(0)
							.getMaxDishCount()))), 4)
					+ StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
							.format(Calendar.getInstance().getTime()), 8)
					+ "\r\n");

			writer.write("------------------------------------------\r\n");
			writer.write(special + "\r\n");

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
		} finally {

		}

	}

}
