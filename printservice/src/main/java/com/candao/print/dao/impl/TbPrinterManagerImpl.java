package com.candao.print.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.TbPrinterArea;
import com.candao.print.entity.TbPrinterDetail;
import com.candao.print.entity.TbPrinterManager;


/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbPrinterManagerImpl implements TbPrinterManagerDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbPrinterManager get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public TbPrinterManager findOne(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".findOne", params);
	}
	
	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		
		return dao.find(PREFIX + ".find", params);
	}
	public <T, K, V> List<T> getCount(Map<K, V> params) {
		return dao.find(PREFIX+".count",params);
	}
	/*public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}*/
	@Override
	public int insert(TbPrinterManager tbPrinter) {
		return dao.insert(PREFIX + ".insert", tbPrinter);
	}

	@Override
	public int update(TbPrinterManager tbPrinter) {
		return dao.update(PREFIX + ".update", tbPrinter);
	}

	@Override
	public int delete(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}
	@Override
	public List<Map<String, Object>> getAreaslistTag(String printerid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("printerid", printerid);
		return dao.find(PREFIX + ".getAreaslistTag", params);
	}
	@Override
	public List<Map<String, Object>> getDishTypeslistTag(String printerid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("printerid", printerid);
		return dao.find(PREFIX + ".getDishTypeslistTag", params);
	}
	@Override
	public TbPrinterManager findByCode(String printerCode){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("printerno", printerCode);
		return dao.get(PREFIX + ".findByCode", params);
	}
	@Override
	public int addPrinterTables(List<Map<String, Object>> tdu) {
		// TODO Auto-generated method stub
		return dao.insert(PREFIX + ".addPrinterTables", tdu);
	}
	@Override
	public int addPrinterDishes(List<Map<String, Object>> tdu) {
		// TODO Auto-generated method stub
		return dao.insert(PREFIX + ".addPrinterDishes", tdu);
	}
	@Override
	public List<TbPrinterArea> findArea(Map<String,Object> params) {
		return dao.find(PREFIX + ".findAreaByPrinterid", params);
	}

	@Override
	public List<Map<String, Object>> findDishes(Map<String,Object> params) {
		return dao.find(PREFIX + ".findDishesByPrinterid", params);
	}

	@Override
	public int deleteTablesByPrinterd(String printerid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("printerid", printerid);
		return dao.delete(PREFIX + ".deleteTablesByPrinterd", params);
	}

	@Override
	public int deleteDishesByPrinterd(String printerid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("printerid", printerid);
		return dao.delete(PREFIX + ".deleteDishesByPrinterd", params);
	}

	@Override
	public List<Map<String, Object>> findPrinternameByTableids(
			Map<String, Object> params) {
		return dao.find(PREFIX + ".findPrinternameByTableids", params);
	}

	@Override
	public List<Map<String, Object>> findPrinternameByDishids(
			Map<String, Object> paramsDish) {
		return dao.find(PREFIX + ".findPrinternameByDishids", paramsDish);
	}

	@Override
	public List<TbPrinterManager> findNoDishPrinter(Map<String, Object> paramMap) {
		return dao.find(PREFIX + ".findNoDishPrinter", paramMap);
	}

	@Override
	public List<TbPrinterManager> findDishPrinter(Map<String, Object> paramMap) {
		return dao.find(PREFIX + ".findDishPrinter", paramMap);
	}

	@Override
	public List<TbPrinterManager> findPrintByType(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findPrintByType", paramMap);
	}

	@Override
	public int updateDishGroup(Map<String, Object> paramMap) {
		return dao.update(PREFIX + ".updateDishGroup", paramMap);
	}

	@Override
	public List<TbPrinterDetail> findPrintDetail(Map<String, Object> paramMap) {
		return dao.find(PREFIX + ".findPrintDetail", paramMap);
	}

	@Override
	public int cleanDishGroup(String printerid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("printerid", printerid);
		return dao.update(PREFIX + ".cleanDishGroup", params);
	}
	
}


