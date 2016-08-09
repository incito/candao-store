package com.candao.www.webroom.service;

import javax.jms.Destination;

import com.candao.print.entity.PrintObj;
import com.candao.www.webroom.model.SynSqlObject;

public interface PrintCommonService {
	
//	public Destination  destination = null;
//	
//	public Destination getDestination();

	/**
	 * @param destination
	 * @param message
	 */
	public void sendMessage(PrintObj obj);
	
	/**
	 * @param destination
	 * @param obj
	 */
//	public void sendMessage(final  SynSqlObject obj,Destination branchQueue);
	
}
