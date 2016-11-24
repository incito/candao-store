package com.candao.www.webroom.service;

import com.candao.www.webroom.model.SynSqlObject;


public interface BranchProducerService {

	
	
	/**
	 * @param destination
	 * @param obj
	 */
	public void sendMessage(final  SynSqlObject obj);
}
