package com.candao.www.data.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TsettlementMapper;
import com.candao.www.data.model.Tsettlement;
import com.candao.www.webroom.model.SettlementInfo;

@Repository
public class TsettlementDaoImpl implements TsettlementMapper {

	@Autowired
    private DaoSupport dao;
	
	
//	@Override
//	public int insertOnce(List<Tsettlement> tsettlements){
//		return dao.insert(PREFIX + ".insertOnce", tsettlements);
//	}
	
	public int insertOnce(List<Tsettlement> tsettlements){
		return dao.insert(PREFIX + ".insertOnce", tsettlements);
	}
 
	@Override
	public Tsettlement get(java.lang.String id) {
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
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	 

	@Override
	public int delete(java.lang.String id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", id);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public int insert(Tsettlement tsettlement) {
		return dao.insert(PREFIX + ".insert", tsettlement);
	}

	@Override
	public int update(Tsettlement tsettlement) {
		return dao.update(PREFIX + ".update", tsettlement);
	}

	@Override
	public int reverseOrder(Tsettlement tsettlement) {
		return dao.update(PREFIX + ".reverseOrder", tsettlement);
		
	}

	@Override
	public Tsettlement getTotalAmount(Date loginTime){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("beginTime", loginTime);
//		Tsettlement settlement = new Tsettlement();
//		settlement.setInserttime(loginTime);
		return dao.get(PREFIX + ".getTotalAmount", params);
	}
 
	@Override
	public  List<Tsettlement> deleteByOrderNo(String orderId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderId);
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public void insertRebackSettle(SettlementInfo settlementInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertRebackSettleDetail(SettlementInfo settlementInfo) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 根据订单号查询反结次数
	 * @author weizhifang
	 * @since 2016-1-12
	 * @param orderId
	 * @return
	 */
	public String queryAgainSettleNums(String orderId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderId);
		return dao.get(PREFIX + ".queryAgainSettleNums", params);
	}
	
	/**
	 * 根据订单号修改反结算次数
	 * @author weizhifang
	 * @since 2016-1-12
	 * @param orderId
	 * @param againSettleNums
	 * @return
	 */
	public int updateSettlementHistory(String orderId,int againSettleNums,String authorizerName,String reason){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", orderId);
		params.put("againSettleNums", againSettleNums);
		params.put("authorizerName", authorizerName);
		params.put("reason", reason);
		return dao.update(PREFIX + ".updateSettlementHistory", params);
	}
	
	/**
	 * 往结算表里写数据
	 * @author weizhifang
	 * @since 2016-01-14
	 * @param id
	 * @param reason
	 * @param againSettleNums
	 * @param authorizerName
	 * @return
	 */
	public int insertSettlementHistory(java.lang.String id ,String reason,int againSettleNums,String authorizerName,Double inflated){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", id);
		params.put("reason", reason);
		params.put("againSettleNums", againSettleNums);
		params.put("authorizerName", authorizerName);
		params.put("inflated", inflated);
		dao.insert(PREFIX + ".insertHistory", params);
		dao.insert(PREFIX + ".insertDetailHistory", params);
		dao.insert(PREFIX + ".insertOrderHistory", params);
		dao.insert(PREFIX + ".insertOrderDetailHistory", params);
		return 0;
	}
	
	/**
	 * 得到会员消费虚增值
	 * @author weizhifang
	 * @since 2016-1-19
	 * @param orderid
	 * @return
	 */
	public Double getMemberInflated(String orderid){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", orderid);
		return dao.getSqlSessionTemplate().selectOne(PREFIX + ".getMemberInflated", params);
	}
	
	/**
	 * 反结算时修改会员消费虚增值为0
	 * @author weizhifang
	 * @since 2016-1-26
	 * @param orderid
	 * @return
	 */
	public int updateTorderMember(String orderid){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", orderid);
		return dao.update(PREFIX + ".updateTorderMember", params);
	}
	
	@Override
	public int selectIsPayWeixin(Map<String, String> dataMap) {
		return dao.get(PREFIX + ".selectIsPayWeixin", dataMap);
	}

	@Override
	public Map<String, Object> fingHistory(Map<String, Object> param) {
		return dao.get(PREFIX + ".fingHistory", param);
	}

	@Override
	public boolean rePay(String orderid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", orderid);
		long count =dao.get(PREFIX+ ".rePay", params);
		return count==0?false:true;
	}
}
