package com.candao.print.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.jms.Destination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import com.candao.common.utils.Constant;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrintObjException;

/**
 * 后台所有单据打印的抽象类，具有处理打印异常的消息的完整机制
 * 
 * @author zhangjijun
 *
 */
public abstract class AbstractPrintListener implements PrintListener {

	/**
	 * 打印机返回正常的状态
	 */
	private static final String NORMAL = "10100^0^0^1111^";

	Log logger = LogFactory.getLog(AbstractPrintListener.class);

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
	 */
	private boolean checkPrinter(OutputStream socketOut, final Socket socket) {
		String hostAddress = socket.getInetAddress().getHostAddress();
		InputStream inputStream;
		byte[] rs = new byte[4];
		try {
			socketOut.write(new byte[] { 29, 97, 1 });
			inputStream = socket.getInputStream();
			inputStream.read(rs);

			String rs_str = "";
			for (byte b : rs) {
				rs_str += Integer.toBinaryString(b) + "^";
			}
			if (!rs_str.equals(NORMAL)) {
				logger.error("打印机状态异常，IP：" + hostAddress + "，状态码：" + rs_str, null);
				return false;
			}
		} catch (IOException e) {
			logger.error("查询打印机状态失败，IP：" + hostAddress, e);
			return false;
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
		OutputStream socketOut = null;
		OutputStreamWriter writer = null;
		Socket socket = null;
		try {
			if (object != null && listenerID != null && !"".equals(listenerID)) {
				logger.info("------------------------");
				logger.info(listenerID + " 收到打印对象:\n" + JacksonJsonMapper.objectToJson(object));
			}
			int print_port;
			print_port = Integer.parseInt(object.getCustomerPrinterPort());

			socket = new Socket();
			socket.connect(new InetSocketAddress(ipAddress, print_port), 5000);//建立连接5秒超时
			socket.setKeepAlive(true);  
	        socket.setSoTimeout(5 * 1000);      //从inputStream中读打印机状态返回值的超时时间
			socketOut = socket.getOutputStream();
			writer = new OutputStreamWriter(socketOut, Constant.PRINTERENCODE);

			boolean flag = PropertiesUtils.getValue(Constant.CHECKSTATE) == null || "Y".equals(PropertiesUtils.getValue(Constant.CHECKSTATE).trim().toUpperCase());
			
			//打印之前检查打印机状态
			if(flag){
				boolean checkPrinter = checkPrinter(socketOut, socket);
				if(!checkPrinter){
					resolve(object, ipstr, ipAddress, isMainPrint);
					//关闭连接
					writer.close();
					socketOut.close();
					socket.close();
					return;
				}				
			}
			
			socketOut.write(27);
			socketOut.write(27);
			writer.flush();//

			//调用各监听器的实现类打印具体的内容
			printBusinessData(object, socketOut, writer);
			
			// 下面指令为打印完成后自动走纸
			writer.write(27);
			writer.write(100);
			writer.write(4);
			writer.write(10);
			writer.flush();//
			socketOut.write(new byte[] { 0x1B, 0x69 });// 切纸

//			if(flag){
//				//打印完成后检查打印机状态
//				boolean checkPrinter = checkPrinter(socketOut, socket);
//				if(!checkPrinter){
//					resolve(object, ipstr, ipAddress, isMainPrint);
//				}				
//			}

		} catch (IOException se) {
			logger.error("------------------------", null);
			logger.error("打印异常(socket异常)，ip："+ipAddress+"，订单号：" + object.getOrderNo() + se.getMessage(), se);
			if (isMainPrint && hasBackupPrinter(ipstr)) {
				// 备用机打印
				ipAddress = getBackupPrinter(ipstr);
				logger.error("------------------------", null);
				logger.error("调用备用打印机，订单号：" + object.getOrderNo()+"ip地址："+ipAddress,null);
				printForm(object, ipstr, ipAddress, false);
				return;
			}
			// 连接不上打印机（主打印机连接不上同时又没配置备用打印机|备用打印机连接不上），加入异常队列
			joinExceptionQueue(object, listenerID);
		} catch (Exception e) {
			logger.error("------------------------", null);
			logger.error("打印异常，订单号：" + object.getOrderNo() + e.getMessage(), e);
		} finally {
			//关闭连接
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("关闭writer失败，IP:" + ipAddress, e);
				}
			}
			if(socketOut != null){
				try {
					socketOut.close();
				} catch (IOException e) {
					logger.error("关闭socketOut失败，IP:" + ipAddress, e);
				}
			}
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					logger.error("关闭socket失败，IP:" + ipAddress, e);
				}
			}
		}
	}

	/**
	 * 打印出现异常的后续处理
	 * @param object
	 * @param ipstr
	 * @param ipAddress
	 * @param isMainPrint
	 */
	private void resolve(PrintObj object, String ipstr, String ipAddress, boolean isMainPrint) {
		if (isMainPrint) {
			if(hasBackupPrinter(ipstr)){
				// 启用备用打印机
				ipAddress = getBackupPrinter(ipstr);
				printForm(object, ipstr, ipAddress, false);
			}else{
				//没有配置备用打印机，但主打印机状态异常
				logger.error("------------------------", null);
				logger.error("打印机状态异常，IP:" + ipAddress + ",订单号：" + object.getOrderNo(), null);
				joinExceptionQueue(object, listenerID);
			}
		} else {
			//备用打印机状态异常，则加入异常队列
			joinExceptionQueue(object, listenerID);
		}
	}

	protected abstract void printBusinessData(PrintObj object, OutputStream socketOut, OutputStreamWriter writer)
			throws Exception;
}
