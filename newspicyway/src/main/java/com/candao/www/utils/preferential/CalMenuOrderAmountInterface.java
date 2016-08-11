package com.candao.www.utils.preferential;

import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.service.DataDictionaryService;

/**
 * 
 * @author 计算金额
 *
 */
public interface CalMenuOrderAmountInterface {
	void  calPayAmount(DataDictionaryService dataDictionaryService, OperPreferentialResult preferentialResult);

}
