package com.candao.www.webroom.service;

import com.candao.www.webroom.model.PaywayRpet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 结算方式明细表
 * @author Administrator
 *
 */
public interface SettlementOptionService {

    List<Map<String, Object>> settlementOptionList(Map<String, Object> params);

    void exportXls(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
