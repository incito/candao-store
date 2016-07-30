package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TorderDetailPreferential;

public interface TorderDetailPreferentialService {
	public Map<String, String>  deleteDetilPreFerInfo(Map<String, Object> params);
	public  List<TorderDetailPreferential> getTorderDetailSbyOrderid(	Map<String, Object> params );
}
