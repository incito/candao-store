package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.print.entity.TbPrinter;

public interface TbPrinterDao {
public final static String PREFIX = TbPrinterDao.class.getName();
    
	public TbPrinter get(java.lang.String id);
	
	public  TbPrinter findOne(java.lang.String id);
	

	public <T, K, V> List<T> find(Map<K, V> params);
	public <T, K, V> List<T> findAll(Map<K, V> params);
	
	public <T, K, V> List<T> getCount(Map<K, V> params);
	
	public int insert(TbPrinter tbPrinter);
	
	public int update(TbPrinter tbPrinter);
	
	public int delete(java.lang.String id );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
	/**
	 * 取得数据分类
	 * @return
	 */
	
	
	public List<Map<String,Object>> getTableTag();

	public TbPrinter findByCode(String printerCode);

	public Map<String, Object> queryPrintIsExsit(String customerPrinterIp, String customerPrinterPort);
	public int updateWorkstate(String ip,short workStatus);
	public List<Map<String,Object>> queryPrinterWorkStatus();
}
