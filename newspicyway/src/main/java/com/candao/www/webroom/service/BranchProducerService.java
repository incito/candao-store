package com.candao.www.webroom.service;

import javax.jms.Destination;

import com.candao.www.webroom.model.SynSqlObject;


public interface BranchProducerService {

	
	/**
	 * @param destination
	 * @param message
	 */
	public void sendMessage(String message,Destination branchQueue);
	
	/**
	 * @param destination
	 * @param obj
	 */
	public void sendMessage(final  SynSqlObject obj);
}
