package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbTableDao;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Tdish;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbTableDaoImpl implements TbTableDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbTable get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public TbTable getByTableNO(String tableNo){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableNo", tableNo);
		return dao.get(PREFIX + ".getTableNo", params);
	}
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public int insert(TbTable tbTable) {
		return dao.insert(PREFIX + ".insert", tbTable);
	}

	@Override
	public int update(TbTable tbTable) {
		return dao.update(PREFIX + ".update", tbTable);
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
	public List<Map<String, Object>> getPrinterTag() {
		return dao.find(PREFIX+".getPrinterTag");
	}
	public List<Map<String, Object>> getbuildingNoANDTableTypeTag() {
		return dao.find(PREFIX+".getbuildingNoANDTableTypeTag");
	}
	public List<Map<String, Object>> getTableTag3() {
		return dao.find(PREFIX+".getTableTag3");
	}
	
	public int updateStatus(TbTable tbTable){
		return dao.update(PREFIX+".updateStatus",tbTable);
	}
	
	public <T, K, V> List<T> findIds(List<String> listIds){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", listIds);
		return dao.find(PREFIX+".findIds",map);
	}
	
	@Override
	public int updateAllStatus(Map<String, Object> tableMap){
		return dao.update(PREFIX + ".updateAllStatus", tableMap);
	}
	
	@Override
	public int   updateStatusByNo(TbTable table){
		return dao.update(PREFIX+".updateStatusByNo",table);
	}
	
	@Override	
	public TbTable findTableNoAndAreaNameById(String tableId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableId", tableId);
		return dao.get(PREFIX+".findTableNoAndAreaNameById",map);
	}
	
	@Override
	public int updateCleanStatus(TbTable tbTable){
		return dao.update(PREFIX+".updateCleanStatus",tbTable);
	}
	
	@Override
	public int  updateSettleStatus(TbTable tbTable){
		return dao.update(PREFIX+".updateSettleStatus",tbTable);
	}
	
	@Override
	public int updateSettleOrderNull(TbTable tbTable){
		return dao.update(PREFIX+".updateSettleOrderNull",tbTable);
	}

	@Override
	public List<Map<String,Object>> findDetail(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX+".findDetail",params);
	}

	@Override
	public List<Map<String, Object>> getTableTag2() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public List<TbTable> getTablesByTableType(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX+".findTableDetail",params);
	}

	@Override
	public int deleteTablesByAreaid(String areaid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("areaid", areaid);
		return dao.delete(PREFIX + ".deleteTablesByAreaid", params);
	}

	@Override
	public TbTable findByOrder(Map<String, Object> map) {
		return dao.get(PREFIX+".findByOrder",map);
	}

	@Override
	public TbTable findTableByOrder(String orderid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderid);
		return dao.get(PREFIX + ".findTableByOrder", params);
	}

	@Override
	public int updateByOrderNo(String sourceOrderid, String targetOrderid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sourceOrderid", sourceOrderid);
		params.put("targetOrderid", targetOrderid);
		return dao.update(PREFIX + ".updateByOrderNo", params);
	}
	
}


