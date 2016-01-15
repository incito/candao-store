package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailSimple;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbOrderDetailDaoImpl implements TorderDetailMapper {
    @Autowired
    private DaoSupport dao;
	@Override
	public TorderDetail get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public TorderDetail getOrderDetailByPrimaryKey(java.lang.String primarykey) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("primarykey", primarykey);
		return dao.get(PREFIX + ".getOrderDetailByPrimaryKey", params);
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
	public int insert(TorderDetail torderDetail) {
		return dao.insert(PREFIX + ".insert", torderDetail);
	}
	
	@Override
	public int insertOnce(List<TorderDetail> torderDetails){
		return dao.insert(PREFIX + ".insertOnce", torderDetails);
	}

	@Override
	public int update(TorderDetail torderDetail) {
		return dao.update(PREFIX + ".update", torderDetail);
	}

	@Override
	public int delete(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", id);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public int updateDiscardDish(Map<String, Object > map) {
		return dao.update(PREFIX + ".updateDiscardDish", map);
	}
 
	@Override
	public TorderDetail findByOrderNoAndDishNo(Map<String, String> mapDetail){
		return dao.get(PREFIX + ".findByOrderNoAndDishNo", mapDetail);
	}

	@Override
	public int getMaxByDishId(String dishid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dishid", dishid);
		return dao.get(PREFIX + ".getMaxByDishId", params);
	
	}
	
	@Override
	public int updateOrderid(Map<String, Object> detailMap){
		 return  dao.update(PREFIX + ".updateOrderid", detailMap);
	}
	
	@Override
	public int updateOrderDetailDishNum(Map<String, Object> map){
		 return  dao.update(PREFIX + ".updateOrderDetailDishNum", map);
	}
	
	@Override
	public int insertTempOnce(List<TorderDetail> list){
		return dao.insert(PREFIX + ".insertTempOnce", list);
	}
	
	@Override
	public int  deleteDish(Map<String, Object> map){
		 
		return dao.delete(PREFIX + ".deleteDish", map);
	}
	
	@Override
	public int deleteDishById(Map<String, Object> deleteMap){
		return dao.delete(PREFIX + ".deleteDishById", deleteMap);
	}
	
	@Override
	public int insertDiscardDish(TorderDetail deleteDetail){
		return dao.insert(PREFIX + ".insertDiscardDish", deleteDetail);
	}

	@Override
	public int updateOrderDetail(TorderDetail torderDetail) {
		// TODO Auto-generated method stub
		return dao.update(PREFIX + ".updateOrderDetail", torderDetail);
	}

	@Override
	public int updateOrderDetailWithPreferential(
			List<Map<String, Object>> list) { 
		if( null==list || list.size()<1){
			return 0;
		}
		return dao.update(PREFIX + ".updateOrderDetailWithPreferential", list);
	}

	@Override
	public int cancelPreferentialItemInOrder(Map params) {
		return dao.update(PREFIX + ".cancelPreferentialItemInOrder", params);
	}

	@Override
	public <T, K, V> List<T> findOrderDetailPad(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findOrderDetailPad", params);
	}

	@Override
	public void insertDiscardDishOnce(String orderId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderId);
		 dao.update(PREFIX + ".insertDiscardDishOnce", params);
	}

	@Override
	public void updateDiscardUserId(TorderDetail orderDetail) {
	   dao.update(PREFIX + ".insertDiscardDishOnce", orderDetail);
	}
	

	@Override
	public int updateDishWeight(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dao.update(PREFIX + ".updateDishWeight", params);
	}

	@Override
	public void updateDiscardDishUserIdOnce(TorderDetail orderDetail) {
		 dao.update(PREFIX + ".updateDiscardDishUserIdOnce", orderDetail);
	}

	@Override
	public void deletefishpot(Map<String, Object> params) {
		// TODO Auto-generated method stub
		dao.update(PREFIX + ".deletefishpot", params);
	}

	@Override
	public void insertDiscardfishpot(Map<String, Object> params) {
		// TODO Auto-generated method stub
		 dao.update(PREFIX + ".insertDiscardfishpot", params);
	}
 

	@Override
	public void insertDiscardDishSetOnce(Map<String, Object> params) {
		 dao.update(PREFIX + ".insertDiscardDishSetOnce", params);
	}

	@Override
	public void updateDiscardDishSetUserId(TorderDetail orderDetailDel) {
		 dao.update(PREFIX + ".updateDiscardDishSetUserId", orderDetailDel);
		
	}
 

	@Override
	public int updateDishCall(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dao.update(PREFIX + ".updateDishCall", params);
	}

	@Override
	public void updateDetailByPrimaryKey(Map<String, Object> map0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFishpotReason(TorderDetail orderDetailDel) {
		// TODO Auto-generated method stub
		 dao.update(PREFIX + ".updateFishpotReason", orderDetailDel);
		
	}

	@Override
	public List<TorderDetailSimple> getOrderDetailByOrderId(String orderid) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("orderid", orderid);
		return dao.find(PREFIX + ".getOrderDetailByOrderId", params);
	}
 
}
 
