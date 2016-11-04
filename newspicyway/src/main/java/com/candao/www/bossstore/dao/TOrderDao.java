package com.candao.www.bossstore.dao;

import java.util.List;
import java.util.Map;

public interface TOrderDao {
	public static final String PREFIX = TOrderDao.class.getName();
	/**
     * 获取营业数据
     *
     * @param branchId  门店id
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param xslx   类型 0 日 1 月
     * @return
     */
    public List<Map<String, Object>> getBusinessData(Map<String,Object> params);
    /**
     * 
     * 获取品项数据
     * @param branchId
     * @param beginTime
     * @param endTime
     * @param xslx
     * @return
     */
    public List<Map<String, Object>> getpxBusinessData(Map<String,Object> params);

    /**
     * 
     * 获取优惠数据
     * @param branchId
     * @param beginTime
     * @param endTime
     * @param xslx
     * @return
     */
    public List<Map<String, Object>> getyhBusinessData(Map<String,Object> params);

    /**
     * 
     * 获取退菜数据
     * @param branchId
     * @param beginTime
     * @param endTime
     * @param xslx
     * @return
     */
    public List<Map<String, Object>> gettcBusinessData(Map<String,Object> params);

    /**
     * 根据日期查询营业流水
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public List<Map<String, Object>> getFlowData(Map<String,Object> params);
    
    /**
     * 查询出当天所有的订单
     *
     * @return 订单list
     */
    public List<Object[]> getCurrDayOrders(Map<String,Object> params);
    /**
     * 查询出当天所有的订单
     *
     * @return 订单list
     */
    public List<Object[]> getCurrDayOrdersToday(Map<String,Object> params);
    /**
     * 根据订单id查询订单应收
     *
     * @param orderid 订单id
     * @return 应收
     */
    public Object getShouldAmountByOrderId(Map<String,Object> params);
    /**
     * 根据订单查询出订单相关的投诉
     *
     * @return 订单list
     */
    public List<Object[]> getOrderCom(Map<String,Object> params);
    /**
     * 根据开始查询优惠top1
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 优惠信息
     */
    public Object getCouponsTopOne(Map<String,Object> params);
}
