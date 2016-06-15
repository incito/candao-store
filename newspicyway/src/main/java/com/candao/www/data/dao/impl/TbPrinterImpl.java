package com.candao.www.data.dao.impl;


import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.print.entity.TbPrinter;
import com.candao.www.data.dao.TbPrinterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbPrinterImpl implements TbPrinterDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbPrinter get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public TbPrinter findOne(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".findOne", params);
	}
	
	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public <T, K, V> List<T> findAll(Map<K, V> params) {
		return dao.find(PREFIX + ".findAll", params);
	}

	public <T, K, V> List<T> getCount(Map<K, V> params) {
		return dao.find(PREFIX+".count",params);
	}
	/*public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}*/
	@Override
	public int insert(TbPrinter tbPrinter) {
		return dao.insert(PREFIX + ".insert", tbPrinter);
	}

	@Override
	public int update(TbPrinter tbPrinter) {
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
	
	public List<Map<String, Object>> getTableTag() {
		return dao.find(PREFIX+".getTableTag");
	}
	
	@Override
	public TbPrinter findByCode(String printerCode){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("printerno", printerCode);
		return dao.get(PREFIX + ".findByCode", params);
	}
	
	@Override
	public Map<String, Object> queryPrintIsExsit(String customerPrinterIp, String customerPrinterPort) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerPrinterIp", customerPrinterIp);
		params.put("customerPrinterPort", customerPrinterPort);
		return dao.get(PREFIX + ".queryPrintIsExsit", params);
	}

	@Override
	public int updateWorkstate(String ip, short workStatus) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("ip", ip);
		params.put("workstatus", workStatus);
		return dao.insert(PREFIX + ".insertOrUpdateState", params);
	}

	@Override
	public List<Map<String, Object>> queryPrinterWorkStatus() {
		return dao.find(PREFIX + ".queryPrinterWorkStatus",null);
	}
}


