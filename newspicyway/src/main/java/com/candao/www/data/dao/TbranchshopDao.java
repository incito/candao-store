package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Tbranchshop;

public interface TbranchshopDao {
	 public final static String PREFIX = TbranchshopDao.class.getName();
	    
		public Tbranchshop get(java.lang.String id);
		
		public <K, V> Map<K, V> findOne(java.lang.String id);
		
		public <T, K, V> List<T> find(Map<K, V> params);
		
		public int insert(Tbranchshop tbranchshop);
		
		public int update(Tbranchshop tbranchshop);
		
		public int delete(java.lang.String id );

		public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

		public List<Tbranchshop> findAll();

}
