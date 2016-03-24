/**
 * 
 */
package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.OtherCouponDao;

/**
 * 更多优惠方式的添加
 *  @author zhouyao
 *  @serialData 2016-1-21
 */
@Repository
public class OtherCouponDaoImpl implements OtherCouponDao {
  @Autowired
  private DaoSupport dao;

  /**
  * 根据ID查询优惠活动信息表
  * @param groupon
  */
  @Override
 public List<Map<String,Object>> getActivity(String id){
	  Map<String, Object> params = new HashMap<String, Object>();
	  params.put("id", id);
	  return dao.get(PREFIX + ".getActivity", params);
 }
 /**
  * 根据ID查询优惠活动信息明细表
  * @param groupon
  */
 @Override
 public List<Map<String,Object>> getDetail(String id){
	  Map<String, Object> params = new HashMap<String, Object>();
	  params.put("id", id);
	  return dao.get(PREFIX + ".getDetail", params);
 }
 /**
  * 添加优惠活动信息表
  * @param groupon
  */
 @Override
 public int addCouponActivity(Map<String, Object> params){
	 return dao.insert(PREFIX + ".addCouponActivity", params);
 }
 /**
  * 添加优惠活动信息明细表
  * @param groupon
  */
 @Override
 public int addCouponDetail(Map<String, Object> params){
	 return dao.insert(PREFIX + ".addCouponDetail", params);
 }
 /**
  * 更新优惠活动信息表
  * @param groupon
  */
 @Override
 public int updateActivity(Map<String, Object> params){
	 return dao.update(PREFIX + ".updateActivity", params);
 }
 /**
  * 查看详细
  * @param groupon
  */
 @Override
 public <T, K, V> List<T> getCoupon(Map<K, V> params){
	 return dao.find(PREFIX + ".getCoupon", params);
 }
 /**
  * 根据优惠的id查询门店编号和门店名称
  * @param groupon
  */
 public <T, K, V> List<T> getBranchInfo(Map<K, V> params){
	 return dao.find(PREFIX + ".getBranchInfo", params);
 }
 /**
  * 优惠类型查询
  * @param groupon
  */
 public List<Map<String,Object>> getTypeDict(Map<String, Object> params){
	 return dao.find(PREFIX + ".getTypeDict", params);
 }
 /**
  * 在线支付优免支付类型
  * @param groupon
  */
 public List<Map<String,Object>> getPaywayType(Map<String, Object> params){
	 return dao.find(PREFIX + ".getPaywayType", params);
 }
 /**
  * 合作单位活动方式
  * @param groupon
  */
 public List<Map<String,Object>> getCooperationNnit(Map<String, Object> params){
	 return dao.find(PREFIX + ".getCooperationNnit", params);
 }
 /**
  * 手工优免活动方式
  * @param groupon
  */
 public List<Map<String,Object>> getActivityType(Map<String, Object> params){
	 return dao.find(PREFIX + ".getActivityType", params);
 }
 /**
  * 删除优惠活动门店信息
  * @param groupon
  */
 @Override
 public int deleteBranch(Map<String, Object> params){
	 return dao.delete(PREFIX + ".deleteBranch", params);
 }
 /**
  * 更新优惠活动信息明细表
  * @param groupon
  */
 @Override
 public int updateDetail(Map<String, Object> params){
	 return dao.update(PREFIX + ".updateDetail", params);
 }
 /**
  * 删除优惠活动信息表
  * @param groupon
  */
 @Override
 public int deleteActivity(String id){
	 Map<String, Object> params = new HashMap<String, Object>();
	 params.put("id", id);
	 return dao.delete(PREFIX + ".deleteActivity", params);
 }
 /**
  * 删除优惠活动信息明细表
  * @param groupon
  */
 @Override
 public int deleteDetail(String id){
	 Map<String, Object> params = new HashMap<String, Object>();
	 params.put("id", id);
	 return dao.delete(PREFIX + ".deleteDetail", params);
   }
}
