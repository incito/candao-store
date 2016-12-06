package com.candao.www.preferential.calcpre;

import java.util.Map;

import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;

/**
 * 
 * @author 梁东 优惠计算策略模式 利用策略模式-对优惠进行处理
 * 
 */
public interface CalPreferentialStrategyInterface {
	Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao);
	}
