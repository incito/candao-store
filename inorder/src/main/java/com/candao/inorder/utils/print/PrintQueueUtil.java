package com.candao.inorder.utils.print;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PrintQueueUtil {
	private static ArrayBlockingQueue<Map<String, String>> printMesQueue = null;
	// 打印线程
	private static ExecutorService singleThreadExecutor = Executors.newCachedThreadPool();
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static PrintQueueUtil instance = new PrintQueueUtil();
	}

	private PrintQueueUtil() {
		printMesQueue = new ArrayBlockingQueue<Map<String, String>>(2000);
		singleThreadExecutor.execute(new InorderPrintThread());
	}

	public static PrintQueueUtil getInstance() {

		return SingletonHolder.instance;

	}

	public  ArrayBlockingQueue<Map<String, String>> getPrinMesQueue()
	{
		return printMesQueue;
	}
	public void addQueue(Map<String, String> strList){
		synchronized (printMesQueue) {
			try {
				getPrinMesQueue().put(strList);
				getPrinMesQueue().notifyAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
