package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;

import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.TorderDetailPreferentialService;

/**
 * 
 * @author 计算金额
 *
 */
public interface CalMenuOrderAmountInterface {
	void  calPayAmount(DataDictionaryService dataDictionaryService, OperPreferentialResult preferentialResult,String itemid, BigDecimal statisticPrice);

}
