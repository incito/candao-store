package com.candao.www.data.dao;

import com.candao.www.data.model.TbOpenBizLog;

public interface  TbOpenBizLogDao {

	
   public final static String PREFIX = TbOpenBizLogDao.class.getName();
   
   public TbOpenBizLog findOpenBizDate();
   
   public int insertOpenBizLog(TbOpenBizLog bizLog);
   
   public int deleteOpenBizLog();
}
