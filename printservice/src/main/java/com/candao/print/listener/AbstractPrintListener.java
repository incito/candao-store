package com.candao.print.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.Constant;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrintObjException;

/**
 * 后台所有单据打印的抽象类，具有处理打印异常的消息的完整机制
 * 
 * @author zhangjijun
 *
 */
public abstract class AbstractPrintListener implements PrintListener {

	LoggerHelper logger = LoggerFactory.getLogger(AbstractPrintListener.class);

	@Autowired
	@Qualifier("exceptionQueue")
	private Destination exceptionDestination;

	@Autowired
	private JmsTemplate jmsTemplate;
	
	private String listenerID;
	

	public AbstractPrintListener(String listenerID) {
		super();
		this.listenerID = listenerID;
	}

	/**
	 * 获取主打印机IP
	 * 
	 * @param ip
	 * @return
	 */
	private String getMainPrinter(String ip) {
		if (ip.contains(",")) {
			String[] ips = ip.split(",");
			return ips[0];
		}
		return ip;
	}

	/**
	 * 获取备用打印机IP
	 * 
	 * @param ip
	 * @return
	 */
	private String getBackupPrinter(String ip) {
		if (ip.contains(",")) {
			String[] ips = ip.split(",");
			if (ips.length > 1 && ips[1] != null && !ips[1].isEmpty()) {
				return ips[1];
			}
		}
		return null;
	}

	/**
	 * 判断是否配置备用打印机
	 * 
	 * @param ip
	 * @return
	 */
	private boolean hasBackupPrinter(String ip) {
		return getBackupPrinter(ip) != null;
	}

	/**
	 * 检查打印机的状态
	 * 
	 * @param socketOut
	 * @param socket
	 * @param object
	 * @throws IOException
	 */
	private boolean checkPrinter(OutputStream socketOut, final Socket socket, PrintObj object) throws IOException {
		byte[] rs;
		InputStream inputStream;
		socketOut.write(new byte[] { 29, 97, 1 });
		rs = new byte[4];
		try {
			inputStream = socket.getInputStream();
			inputStream.read(rs);

			String rs_str = "";
			for (byte b : rs) {
				rs_str += Integer.toBinaryString(b) + "^";
			}
			if (!rs_str.equals("10100^0^0^1111^")) {
				logger.error("订单号：" + object.getOrderNo() + ",打印状态：" + rs_str, "");
				return false;
			}
		} catch (IOException e) {
			String dishNames = "";
			for (PrintDish dish : object.getpDish()) {
				dishNames += dish.getDishName() + "^";
			}
			logger.error("查询打印机状态失败，订单号：" + object.getOrderNo() + ",IP：" + ",:", e, "");
		}
		return true;
	}

	/**
	 * 打印出现异常的消息加入异常队列
	 * 
	 * @param object
	 * @param listenerID
	 */
	protected void joinExceptionQueue(PrintObj object, String listenerID) {
		jmsTemplate.convertAndSend(exceptionDestination, new PrintObjException(object, listenerID));
	}

	protected void printForm(PrintObj object) {
		String ipstr = object.getCustomerPrinterIp();
		String ipAddress = getMainPrinter(ipstr);
		printForm(object, ipstr, ipAddress, true);
	}

	private void printForm(PrintObj object, String ipstr, String ipAddress, boolean isMainPrint) {
		try {
			OutputStream socketOut;
			OutputStreamWriter writer;
			Socket socket;
			int print_port;
			print_port = Integer.parseInt(object.getCustomerPrinterPort());

			socket = new Socket(ipAddress, print_port);
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);
			socketOut.write(27);
			socketOut.write(27);

			boolean checkPrinter = checkPrinter(socketOut, socket, object);
			if (isMainPrint && !checkPrinter && hasBackupPrinter(ipstr)) {
				// 启用备用打印机
				ipAddress = getBackupPrinter(ipstr);
				printForm(object, ipstr, ipAddress, false);
				return;
			}
			
			//调用各监听器的实现类打印具体的内容
			printBusinessData(object, socketOut, writer);

			// 下面指令为打印完成后自动走纸
			writer.write(27);
			writer.write(100);
			writer.write(4);
			writer.write(10);
			writer.flush();//
			socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸

			checkPrinter = checkPrinter(socketOut, socket, object);
			if (isMainPrint && !checkPrinter && hasBackupPrinter(ipstr)) {
				// 启用备用打印机
				ipAddress = getBackupPrinter(ipstr);
				printForm(object, ipstr, ipAddress, false);
				return;
			}

			writer.close();
			socketOut.close();
			socket.close();
			logger.error("-----------------------------", "");
			logger.error("打印完成，订单号：" + object.getOrderNo(), "");

		} catch (SocketException se) {
			if (isMainPrint && hasBackupPrinter(ipstr)) {
				// 备用机打印
				ipAddress = getBackupPrinter(ipstr);
				printForm(object, ipstr, ipAddress, false);
				return;
			}
			// 备用打印机出现异常，加入异常队列
			joinExceptionQueue(object, listenerID);
		} catch (Exception e) {
			logger.error("------------------------", "");
			logger.error("打印异常，订单号：" + object.getOrderNo() + e.getMessage(), e, "");
		} finally {

		}
	}

	protected abstract void printBusinessData(PrintObj object, OutputStream socketOut, OutputStreamWriter writer)
			throws Exception;
}
