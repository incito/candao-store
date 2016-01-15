package com.candao.print.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.print.entity.TbPrinterArea;
import com.candao.print.entity.TbPrinterDetail;
import com.candao.print.entity.TbPrinterManager;

public interface TbPrinterManagerDao {
public final static String PREFIX = TbPrinterManagerDao.class.getName();
    
	public TbPrinterManager get(java.lang.String id);
	
	public  TbPrinterManager findOne(java.lang.String id);
	

	public <T, K, V> List<T> find(Map<K, V> params);
	
	public <T, K, V> List<T> getCount(Map<K, V> params);
	
	public int insert(TbPrinterManager tbPrinterManager);
	
	public int update(TbPrinterManager tbPrinterManager);
	
	public int delete(java.lang.String id );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
	/**
	 * 取得数据分类
	 * @return
	 */
	
	
	public List<Map<String,Object>> getAreaslistTag(String printerid);

	public TbPrinterManager findByCode(String printerCode);

	
	public int addPrinterTables(List<Map<String, Object>> tdu);

	public List<TbPrinterArea> findArea(Map<String,Object> params);

	public List<TbPrinterDetail> findDishes(Map<String,Object> params);

	public List<Map<String, Object>> getDishTypeslistTag(String printerid);

	public int addPrinterDishes(List<Map<String, Object>> list);

	public int deleteTablesByPrinterd(String printerid);

	public int deleteDishesByPrinterd(String printerid);

	public List<Map<String, Object>> findPrinternameByTableids(
			Map<String, Object> params);

	public List<Map<String, Object>> findPrinternameByDishids(
			Map<String, Object> paramsDish);

	public List<TbPrinterManager> findNoDishPrinter(Map<String, Object> paramMap);

	public List<TbPrinterManager> findDishPrinter(Map<String, Object> paramMap);
	/**
	 * 根据答应机类型，找到打印机。这个打印机不关联桌子和菜品
	 * @author shen
	 * @date:2015年7月31日下午3:29:33
	 * @Description: TODO
	 */
	public List<TbPrinterManager> findPrintByType(Map<String, Object> paramMap);

}
