package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

public interface TorderDetailPreferentialService {
	public Map<String, String>  deleteDetilPreFerInfo(Map<String, Object> params);
	public<T, K, V> List<T> queryGiveprefer(String orderid);
}
