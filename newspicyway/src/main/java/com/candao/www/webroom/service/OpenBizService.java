package com.candao.www.webroom.service;

import com.candao.www.data.model.TbOpenBizLog;

public interface OpenBizService {

	public   TbOpenBizLog getOpenBizLog();
	
	public   int  deleteOpenBizLog();
	
	
	public  int  insertOpenBizLog( TbOpenBizLog tbOpenBizLog);
	
	
}
