package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.model.Torder;
import com.candao.www.webroom.model.TableStatus;

/**
 * 数据访问接口
 * 
 * @author mew
 *
 */
@Repository
public class TbOrderDaoImpl implements TorderMapper {
	@Autowired
	private DaoSupport dao;

	@Override
	public Torder get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}

	@Override
	public <K, V> Map<K, V> findOne(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <K, V> Map<K, V> findOneOrderInfo(Map<K, V> params) {
		return dao.get(PREFIX + ".findOneOrderInfo", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public int insert(Torder torder) {
		return dao.insert(PREFIX + ".insert", torder);
	}

	@Override
	public int update(Torder torder) {
		return dao.update(PREFIX + ".update", torder);
	}

	@Override
	public int updateInvoiceid(Torder torder) {
		return dao.update(PREFIX + ".updateInvoiceid", torder);
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
	public String getPrimaryKey() {
		Map<String, Object> params = new HashMap<String, Object>();
		return dao.get(PREFIX + ".getPrimaryKey", params);
	}

	@Override
	public int updateAllTableNo(Map<String, Object> orderMap) {
		return dao.update(PREFIX + ".updateAllTableNo", orderMap);
	}

	@Override
	public List<Torder> findByOrderNo(Map<String, Object> map) {
		return dao.get(PREFIX + ".findByOrderNo", map);
	}

	@Override
	public List<Torder> findOrdersByTableids(Map<String, Object> mapOrder) {
		return dao.find(PREFIX + ".findOrdersByTableids", mapOrder);
	}

	@Override
	public List<Torder> findontimeOrdersByTableids(Map<String, Object> mapOrder) {
		return dao.find(PREFIX + ".findontimeOrdersByTableids", mapOrder);
	}

	@Override
	public List<TableStatus> selectSwitchTable(Map<String, Object> mapParam) {
		return dao.find(PREFIX + ".selectSwitchTable", mapParam);
	}

	@Override
	public List<TableStatus> selectMergeTable(Map<String, Object> mapParam) {
		return dao.find(PREFIX + ".selectMergeTable", mapParam);
	}

	@Override
	public List<TableStatus> setOrderDish(Map<String, Object> mapParam) {
		return dao.find(PREFIX + ".setOrderDish", mapParam);
	}

	@Override
	public Torder getMaxOrderNum(String orderIdDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderDateStr", orderIdDate);
		return dao.get(PREFIX + ".getMaxOrderNum", params);
	}

	@Override
	public int updateOrderMeid(Torder torder) {
		return dao.update(PREFIX + ".updateOrderMeid", torder);
	}

	@Override
	public void executeSql(String sql) {
		dao.executeSql(sql);
	}

	@Override
	public List<Torder> verifyAllOrder() {
		return dao.find(PREFIX + ".verifyAllOrder", null);
	}

	// @Override
	// public int verifyAllCLean() {
	// return dao.get(PREFIX + "verifyAllCLean",null);
	// }
	//
	// @Override
	// public int verifyAllTableClear() {
	// return dao.get(PREFIX + "verifyAllTableClear",null);
	// }

	@Override
	public String callEndWork(String userName, String isSucess) {
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put("userName", userName);
		parameter.put("flag", isSucess);
		dao.get(PREFIX + ".callEndWork", parameter);
		return parameter.get("flag");
	}

	@Override
	public void setOrderMember(Map<String, Object> mapParam) {
		dao.get(PREFIX + ".setOrderMember", mapParam);
	}

	@Override
	public int updateOrderForMergeTable(Map<String, Object> mapParam) {
		return dao.update(PREFIX + ".updateOrderForMergeTable", mapParam);
	}

	@Override
	public int deleteByPrimaryKey(String orderId) {
		Map<String, Object> map = new HashMap<>();
		map.put("orderid", orderId);
		return dao.delete(PREFIX + ".deleteByPrimaryKey", map);
	}

	@Override
	public void updateVipPrice(String orderId) {
		Map<String, Object> map = new HashMap<>();
		map.put("i_orderid", orderId);
		dao.find(PREFIX + ".updateVipPrice", map);
	}

	@Override
	public int updateMemberno(String orderid, String memberno) {
		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put("orderid", orderid);
		mapParam.put("memberno", memberno);
		return dao.update(PREFIX + ".updateMemberno", mapParam);
	}

	@Override
	public Torder lock(String orderId) {
		Map<String, Object> map = new HashMap<>();
		map.put("orderid", orderId);
		return dao.get(PREFIX + ".selectForUpdate", map);
	}

	@Override
	public <K, V> Map<K, V> selectConsumInfo() {
		return dao.get(PREFIX + ".selectConsumInfo", null);
	}
}
