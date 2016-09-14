package com.candao.www.webroom.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TorderDetailPreferential;

public interface TorderDetailPreferentialService {
	public Map<String, String>  deleteDetilPreFerInfo(Map<String, Object> params);
	public  List<TorderDetailPreferential> getTorderDetailSbyOrderid(	Map<String, Object> params );
	public BigDecimal  statisticALLDiscount(String orderid);
	public<T, K, V> List<T> queryGiveprefer(String orderid);
}
