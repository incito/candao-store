package com.candao.inorder.utils.print;

import java.util.Map;

import com.candao.inorder.utils.LiunxSSHScpclientUtil;

/**
 * 
 * @author Candao 打印线程
 */
public class InorderPrintThread implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println(PrintQueueUtil.getInstance().getPrinMesQueue().size());
			synchronized (PrintQueueUtil.getInstance().getPrinMesQueue()) {
				while (PrintQueueUtil.getInstance().getPrinMesQueue().isEmpty()) {
					try {
						PrintQueueUtil.getInstance().getPrinMesQueue().wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						//如果失败 释放线程
						PrintQueueUtil.getInstance().getPrinMesQueue().notifyAll();
					}
				}
				System.out.println("进来");
	             Map<String, String> strMes = PrintQueueUtil.getInstance().getPrinMesQueue().poll();
				PrintQueueUtil.getInstance().getPrinMesQueue().notifyAll();
				LiunxSSHScpclientUtil.getInstance().upPrintFile(strMes);
			}
		}
	}

}
