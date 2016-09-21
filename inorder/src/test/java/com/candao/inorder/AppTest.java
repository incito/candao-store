package com.candao.inorder;

import java.util.ArrayList;

import com.candao.inorder.utils.print.PrintQueueUtil;
import com.candao.inorder.utils.print.InorderPrintThread;

/**
 * Unit test for simple App.
 */
public class AppTest

{
	public static void main(String[] args) throws InterruptedException {
		// ApplicationContext context = new
		// ClassPathXmlApplicationContext("spring-context-payment.xml");
		// context.getBean("", arg1);
		
		 ArrayList<String> strList = new ArrayList<String>();
		 strList.add("1");
		 strList.add("2");
		 strList.add("3");
		 strList.add("4");
		 strList.add("5");
		 strList.add("6");
		 strList.add("7");
		 strList.add("8");
		 strList.add("912");

		 Thread.sleep(3000);
//		PrintQueueUtil.getInstance().addQueue(strList);
//		PrintQueueUtil.getInstance().addQueue(strList);
//		PrintQueueUtil.getInstance().addQueue(strList);
//		PrintQueueUtil.getInstance().addQueue(strList);
//		PrintQueueUtil.getInstance().addQueue(strList);
		

		 InorderPrintThread printThread = new InorderPrintThread();
		 new Thread(printThread).start();

	}

}
