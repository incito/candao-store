package com.candao.www.data.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TsettlementDetail;

public interface TsettlementDetailMapper {
	
	   public final static String PREFIX = TsettlementDetailMapper.class.getName();
	   
		public TsettlementDetail get(java.lang.String id);
		
		public <K, V> Map<K, V> findOne(java.lang.String id);
		
		public <T, K, V> List<T> find(Map<K, V> params);
		
		public int insert(TsettlementDetail tsettlement);
		
		public int update(TsettlementDetail tsettlement);
		
		public int delete(java.lang.String id );

		public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

		public int  insertOnce(ArrayList<TsettlementDetail> listInsert);

		public void deleteBySettleId(String settleid);

		public void calDebitAmount(Map<String, Object> orderDetailMap);
		
}