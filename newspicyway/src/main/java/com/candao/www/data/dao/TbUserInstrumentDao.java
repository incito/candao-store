package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbUserInstrument;

public interface TbUserInstrumentDao {
	 public final static String PREFIX = TbUserInstrumentDao.class.getName();
	    
		public TbUserInstrument get(Map params);
		
		public <K, V> Map<K, V> findOne(java.lang.String id);
		
		public <T, K, V> List<T> find(Map<K, V> params);
		
		public int insert(TbUserInstrument tbUserInstrument);
		
		public int update(TbUserInstrument tbUserInstrument);
		
		public int updateByid(TbUserInstrument tbUserInstrument);
		
		public int delete(Map params);

		public int updateStatus(TbUserInstrument tbUserInstrument);

		public List<TbUserInstrument> findByParams(Map<String, Object> params);

		public List<Map<String, Object>> findUseridByParams(
				Map<String, Object> params);



		

}
