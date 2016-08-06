package com.candao.www.utils.preferential;

import java.math.BigDecimal;

import com.candao.www.webroom.service.DataDictionaryService;

/**
 * 
 * @author 计算金额
 *
 */
public interface CalMenuOrderAmountInterface {
	BigDecimal  calPayAmount(DataDictionaryService dataDictionaryService,BigDecimal menuAmout,BigDecimal amount);

}
