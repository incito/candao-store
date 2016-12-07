package com.candao.inorder.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

/**
 * 
 * @author liangdong Linux scp 文件上传
 */
public class LiunxSSHScpclientUtil {
	
	private Logger logger = LoggerFactory.getLogger(LiunxSSHScpclientUtil.class);
	private static String empPrintbuffer;

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static LiunxSSHScpclientUtil instance = new LiunxSSHScpclientUtil();
	}

	private LiunxSSHScpclientUtil() {
		printModel();
	}

	public static LiunxSSHScpclientUtil getInstance() {

		return SingletonHolder.instance;
	}

	public void putFile(String remoteFileName, String remoteTargetDirectory, String mode, List<String> printList) {
		Connection conn = new Connection("10.66.21.86", 22);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword("root", "cd_cp_123456");
			if (isAuthenticated == false) {
				System.err.println("authentication failed");
			}
			SCPClient client = new SCPClient(conn);
			if ((mode == null) || (mode.length() == 0)) {
				mode = "0600";
			}
			// String formatStr = String.format(printModel(),
			// printList.toArray());
			// byte[] bytesWrite = formatStr.getBytes(StandardCharsets.UTF_8);
			// SCPOutputStream outputStream = client.put(remoteFileName,
			// bytesWrite.length, remoteTargetDirectory, mode);
			// outputStream.write(bytesWrite);
			// outputStream.flush();
			// outputStream.close();
			//
			// conn.close();
		} catch (IOException ex) {
			// Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE,
			// null,ex);s
		}
	}

	public void upPrintFile(Map<String, String> strMes) {
		FileOutputStream fileout = null;
		for (String key : strMes.keySet()) {
			try {
				logger.info("------------",strMes.get(key));
				byte[] bytesWrite = strMes.get(key).getBytes("GB2312");
				File file =new File(key);
				fileout = new FileOutputStream(file);
				fileout.write(bytesWrite);
				fileout.flush();
			} catch (Exception e) {
				logger.error("------吉旺打印---》",e);
				e.printStackTrace();
			} finally {
				if (fileout != null) {
					try {
						fileout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	};

	public byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream(1024 * 1024);
			byte[] b = new byte[1024 * 1024];
			int i;
			while ((i = fis.read(b)) != -1) {
				byteArray.write(b, 0, i);
			}
			fis.close();
			byteArray.close();
			buffer = byteArray.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	private void printModel() {

		if (empPrintbuffer!=null&&!empPrintbuffer.trim().isEmpty()) {
			return;
		}
		StringBuffer empsbf = new StringBuffer();
		try {
			// 是否切换
	        ClassLoader classLoader = getClass().getClassLoader();  
	          InputStream resource = classLoader.getResourceAsStream("printModel");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				empsbf.append(line);
				empsbf.append("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		empPrintbuffer = empsbf.toString();
	}

	public static String getEmpPrintbuffer() {
		return empPrintbuffer;
	}

}
