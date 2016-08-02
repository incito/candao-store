package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TorderDetailPreferential;

/**
 * 
 * @author LiangDong
 *
 */
public interface TorderDetailPreferentialDao {
	public final static String PREFIX = TorderDetailPreferentialDao.class.getName();

	public int addBatchInfo(List<TorderDetailPreferential> detailPreferentials);

	public int addDetailPreFerInfo(TorderDetailPreferential detailPreferential);

	public List<TorderDetailPreferential> queryDetailPreBy(String orderID);

	public int deleteBachInfo(List<TorderDetailPreferential> detailPreferentials);
	public int deleteDetilPreFerInfo(Map<String, Object> params);

	public List<TorderDetailPreferential> getTorderDetailSbyOrderid(Map<String, Object> params);
}