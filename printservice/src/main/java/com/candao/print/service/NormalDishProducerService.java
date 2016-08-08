package com.candao.print.service;

import com.candao.print.entity.PrintObj;

public interface NormalDishProducerService {

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
