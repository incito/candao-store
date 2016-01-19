package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbInstrument;

public interface TbInstrumentDao {
	 public final static String PREFIX = TbInstrumentDao.class.getName();
	    
		public TbInstrument get(Map params);
		
		public <K, V> Map<K, V> findOne(java.lang.String id);
		
		public <T, K, V> List<T> find(Map<K, V> params);
		
		public int insert(TbInstrument tbInstrument);
		
		public int update(TbInstrument tbInstrument);
		
		public int delete(Map params);

		public int updateStatus(TbInstrument tbInstrument);



		

}
