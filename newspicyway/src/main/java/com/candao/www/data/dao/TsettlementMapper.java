package com.candao.www.data.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Tsettlement;
import com.candao.www.webroom.model.SettlementInfo;

public interface TsettlementMapper {
	
	   public final static String PREFIX = TsettlementMapper.class.getName();
	   
		public int insertOnce(List<Tsettlement> tsettlements);
	   
		public Tsettlement get(java.lang.String id);
		
		public <K, V> Map<K, V> findOne(java.lang.String id);
		
		public <T, K, V> List<T> find(Map<K, V> params);
		
		public int insert(Tsettlement tsettlement);
		
		public int update(Tsettlement tsettlement);
		
		public int delete(java.lang.String id ,String reason);

		public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

		public int reverseOrder(Tsettlement tsettlement);

		public Tsettlement getTotalAmount(Date loginTime);

		public List<Tsettlement>  deleteByOrderNo(String orderId);

		public void insertRebackSettle(SettlementInfo settlementInfo);

		public void insertRebackSettleDetail(SettlementInfo settlementInfo);
		
}