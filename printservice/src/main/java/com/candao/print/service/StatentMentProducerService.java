package com.candao.print.service;

import com.candao.print.entity.PrintObj;

/**
 * 结账单
 * @author snail
 *
 */
public interface StatentMentProducerService {
	/**
	 * @param destination
	 * @param message
	 */
	public void sendMessage(String message);
	
	/**
	 * @param destination
	 * @param obj
	 */
	public void sendMessage(final PrintObj obj);
}
