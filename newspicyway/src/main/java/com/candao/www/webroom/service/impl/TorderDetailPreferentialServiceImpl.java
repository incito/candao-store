package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.webroom.service.TorderDetailPreferentialService;

/**
 * 
 * @author Candao 这个是订单使用优惠
 */

@Service
public class TorderDetailPreferentialServiceImpl implements TorderDetailPreferentialService {

	@Autowired
	private TorderDetailPreferentialDao detailPreferentialDao;

	@Override
	public Map<String, String> deleteDetilPreFerInfo(Map<String, Object> params) {
	
		Map<String, String> result = new HashMap<String, String>();
		int delNum = detailPreferentialDao.deleteDetilPreFerInfo(params);
		if (delNum > 0) {
			result.put("mes", "当前订单删除优惠成功");
			result.put("code", "1");
		}
		return result;
	}

	@Override
	public List<TorderDetailPreferential> getTorderDetailSbyOrderid(Map<String, Object> params) {
		return detailPreferentialDao.getTorderDetailSbyOrderid(params);
	}

	@Override
	public BigDecimal statisticALLDiscount(String orderid) {
		return detailPreferentialDao.statisticALLDiscount(orderid);
	}

	@Override
	public <T, K, V> List<T> queryGiveprefer(String orderid) {
		return detailPreferentialDao.queryGiveprefer(orderid);
	}

}
