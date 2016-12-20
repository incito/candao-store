package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;

import com.candao.www.preferential.model.PreDealInfoBean;
import com.candao.www.webroom.service.DataDictionaryService;

/**
 * 
 * @author 计算金额
 *
 */
public interface CalMenuOrderAmountInterface {
	/**
	 * 
	 * @param dataDictionaryService
	 * 获取计算方式
	 * @param itemid
	 * 是否是手动计算方式
	 * @param statisticPrice
	 * @param menuAmount
	 * @param amount
	 * @return
	 */
	PreDealInfoBean  calPayAmount(DataDictionaryService dataDictionaryService, String itemid, BigDecimal menuAmount,BigDecimal amount);

}
