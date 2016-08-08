package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbMessageInstrument;

public interface TbMessageInstrumentDao {
	 public final static String PREFIX = TbMessageInstrumentDao.class.getName();
	    
		public TbMessageInstrument get(Map<String,Object> params);
		
		public <K, V> Map<K, V> findOne(java.lang.String id);
		
		public <T, K, V> List<T> find(Map<K, V> params);
		
		public int insert(TbMessageInstrument tbMessageInstrument);
		
		public int update(TbMessageInstrument tbMessageInstrument);
		
		public int delete(Map params);

		public int updateStatus(TbMessageInstrument tbMessageInstrument);



		

}
