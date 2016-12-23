package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailSimple;

public interface TorderDetailMapper {
	
	
    public final static String PREFIX = TorderDetailMapper.class.getName();
    
	public TorderDetail get(java.lang.String id);
	
	public <T, K, V>T findOne(Map<K, V> params);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	public   List<ComplexTorderDetail> findorderByDish(String orderId);
	
	public <T, K, V> List<T> findTemp(Map<K, V> params);
	/**
	 * 更换pad使用
	 * @author shen
	 * @date:2015年5月14日下午9:10:22
	 * @Description: TODO
	 */
	public <T, K, V> List<T> findOrderDetailPad(Map<K, V> params);
	
	/**
	 * 根据订单查询发票，订单信息  餐台信息
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> findOrderByInfo(String param);
	public <T, K, V> List<T> findOrderByTip(String param);
	
	public int insert(TorderDetail torderDetail);
	
	public int insertOnce(List<TorderDetail> torderDetails);
	
	public int update(TorderDetail torderDetail);
	public int updateOrderDetail(TorderDetail torderDetail);
	
	public int delete(java.lang.String id );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

//	public int  updateDiscardDish(List<TorderDetail> list);

	public int updateDiscardDish(Map<String, Object> map);

	public TorderDetail findByOrderNoAndDishNo(Map<String, String> mapDetail);

	public int getMaxByDishId(String dishid);

	public int updateOrderid(Map<String, Object> detailMap);

	public int updateOrderDetailDishNum(Map<String, Object> map);

	public int insertTempOnce(List<TorderDetail> list);

	public int deleteDish(Map<String, Object> map);

	public int deleteDishById(Map<String, Object> deleteMap);

	public int insertDiscardDish(TorderDetail deleteDetail);
	
	/**
	 * 对账单使用优惠券，更新账单中菜品的信息。
	 * @param list 要更的菜品的数据
	 * 		map<String,Object> key存放字段名，value 存放要更新的值。
	 * @return
	 */
	public int updateOrderDetailWithPreferential(List< Map<String,Object> > list);
	
	/**
	 * 取消账单的优惠
	 * @param params
	 * 			参数  orderid     账单id
	 * 				preferetialid 优惠id
	 * @return
	 */
	public int cancelPreferentialItemInOrder(Map params);
	
	public TorderDetail getOrderDetailByPrimaryKey(java.lang.String primarykey);
	
	public int countByPrimarykey(List<TorderDetail> orderDetails);

	public List<TorderDetail> getByPrimarykey(List<TorderDetail> orderDetails);
	/**
	 * 一次插入到退单表
	 * @author tom_zhao
	 * @param orderId
	 */
	public void insertDiscardDishOnce(String orderId,String userId,String userName,String reason);

	/**
	 * 更新退菜授权人
	 * @author tom_zhao
	 * @param orderDetail
	 */
	public void updateDiscardUserId(TorderDetail orderDetail);
	/**
	 * 更新菜品重量
	 * @author shen
	 * @date:2015年5月29日下午2:15:19
	 * @Description: TODO
	 */
	public int updateDishWeight(Map<String,Object> params);

	/**
	 * 
	 * @author tom_zhao
	 * @param orderDetail
	 */
	public void updateDiscardDishUserIdOnce(TorderDetail orderDetail);
	
	public void deletefishpot(Map<String,Object> params);
	public void insertDiscardfishpot(Map<String,Object> params);
	public int updateDishCall(Map<String, Object> params);

	public void updateDetailByPrimaryKey(Map<String, Object> map0);
	
	
	public void insertDiscardDishSetOnce(Map<String, Object> params);

	public void updateDiscardDishSetUserId(TorderDetail orderDetailDel);
	/**
	 * 更新单退鱼锅的退菜原因和退菜人
	 * @author shen
	 * @date:2015年6月19日下午3:36:58
	 * @Description: TODO
	 */
	public void updateFishpotReason(TorderDetail orderDetailDel);

	/**
	 * 根据订单获取到订单的菜品
	 * @param orderid
	 * @return
	 */
	public List<TorderDetailSimple> getOrderDetailByOrderId(String orderid);
	
	
	/**
	 * 根据主键获取下菜单称重数量
	 * @author tom_zhao
	 * @param pk
	 * @return
	 */
//	public TorderDetail getDishDetailByPK(String pk);

	public int updateOrderinfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getDishesInfoByOrderId(String orderid);
	public Map<String, Object> selectorderinfos(String orderid);
	
	public List<Map<String, Object>> getItemSellDetail(Map<String, Object> timeMap);
	public List<Map<String, Object>> getItemSellDetailForPos(Map<String, Object> timeMap);
	public int updateOrderDetailWithPreferentialNew(String dishids, String orderid, String preferentialid);

	public void deleteTemp(String orderid);
	
	public int updateOrderDetailDiscard(String sourceOrderid, String targetOrderid);
	
	public int updateOrderDetailForMergeTable(String sourceOrderid, String targetOrderid);
	
}