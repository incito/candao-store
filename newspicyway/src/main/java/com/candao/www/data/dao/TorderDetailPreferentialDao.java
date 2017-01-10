package com.candao.www.data.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TbOrderDetailPreInfo;
import com.candao.www.data.model.TorderDetailPreferential;

/**
 * 
 * @author LiangDong
 *
 */
public interface TorderDetailPreferentialDao {
	public final static String PREFIX = TorderDetailPreferentialDao.class.getName();

	public int addBatchInfo(List<TorderDetailPreferential> detailPreferentials);

	public int addBatchorderPreInfo(List<TbOrderDetailPreInfo> detailPreInfos);

	public List<TorderDetailPreferential> queryDetailPreBy(String orderID);

	public List<TorderDetailPreferential> queryDetailPreByGift(String orderID);

	public int deleteBachInfo(List<TorderDetailPreferential> detailPreferentials);

	public int deleteDetileSubPreInfo(Map<String, Object> params);

	public int deleteDetilPreFerInfo(Map<String, Object> params);
	public int deleteSubPreInfo(Map<String,Object> params);

	public int deleteForXinladao(Map<String, Object> params);

	public List<TorderDetailPreferential> getTorderDetailSbyOrderid(Map<String, Object> params);

	/** 获取特价卷已经赠菜卷使用的优惠（按照使用时间排序） **/
	public List<TorderDetailPreferential> getPresentAndspecialPriclist(Map<String, Object> params);
	public List<TorderDetailPreferential> getAllPreInfolist(Map<String, Object> params);

	public BigDecimal statisticALLDiscount(String orderid);

	public <T, K, V> List<T> queryGiveprefer(String orderid);
}
