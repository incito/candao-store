package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.webroom.model.SynSqlObject;

public interface TSynSqlMapper {
	
	public final static String PREFIX = TSynSqlMapper.class.getName();

	public int insert(SynSqlObject record);
	
	public <E, K, V> List<E> getSynSqlDetailByparams(Map<K, V> params);
	
	public int update(SynSqlObject record) ;

	public void synData(Map<String, Object> mapParam);

	public int copyDataFromTemp();

	public void deleteDataTemp();

}