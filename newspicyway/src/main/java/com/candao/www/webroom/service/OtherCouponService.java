/**
 * 
 */
package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

/**
 * 更多优惠方式的添加
 *  @author zhouyao
 *  @serialData 2016-1-21
 */
public interface OtherCouponService {
	  /**
	   * 根据ID查询优惠活动信息表
	   * @param groupon
	   */
	  public List<Map<String,Object>> getActivity(String id);
	  /**
	   * 根据ID查询优惠活动信息明细表
	   * @param groupon
	   */
	  public List<Map<String,Object>> getDetail(String id);
	  /**
	   * 添加优惠活动信息表
	   * @param groupon
	   */
	  public int addCouponActivity(Map<String, Object> params);
	  /**
	   * 添加优惠活动信息明细表
	   * @param groupon
	   */
	  public int addCouponDetail(Map<String, Object> params);
	  /**
	   * 优惠类型查询
	   * @param groupon
	   */
	  public List<Map<String,Object>> getTypeDict(Map<String, Object> params);
	  /**
	   * 在线支付优免支付类型
	   * @param groupon
	   */
	  public List<Map<String,Object>> getPaywayType(Map<String, Object> params);
	  /**
	   * 手工优免活动方式
	   * @param groupon
	   */
	  public List<Map<String,Object>> getActivityType(Map<String, Object> params);
	  /**
	   * 合作单位活动方式
	   * @param groupon
	   */
	  public List<Map<String,Object>> getCooperationNnit(Map<String, Object> params);
	  /**
	   * 更新优惠活动信息表
	   * @param groupon
	   */
	  public int updateActivity(Map<String, Object> params);
	  /**
	   * 更新优惠活动信息明细表
	   * @param groupon
	   */
	  public int updateDetail(Map<String, Object> params);
	  /**
	   * 删除优惠活动信息表
	   * @param groupon
	   */
	  /**
	   * 删除优惠活动门店信息
	   * @param groupon
	   */
	  public int deleteBranch(Map<String, Object> params);
	  
	  public int deleteActivity(String id);
	  /**
	   * 删除优惠活动信息明细表
	   * @param groupon
	   */
	  public int deleteDetail(String id);
	  /**
	   * 查看详细
	   * @param groupon
	   */
	  public List<Map<String,Object>> getCoupon(String id);
	  /**
	   * 根据优惠的id查询门店id和名称
	   * @param groupon
	   */
	  public List<Map<String,Object>> getBranchInfo(String id);
}
