package com.candao.www.webroom.service.impl;

import com.candao.www.data.dao.TBusinessAnalysisChartsDao;
import com.candao.www.utils.DateTimeUtils;
import com.candao.www.webroom.model.BusinessReport;
import com.candao.www.webroom.service.BusinessAnalysisChartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 营业分析图表
 *
 * @author Administrator
 */
@Service
public class BusinessAnalysisChartsServiceImpl implements BusinessAnalysisChartsService {

    @Autowired
    private TBusinessAnalysisChartsDao tbusinessAnalysisChartsDao;

    @Override
    public List<BusinessReport> isfindBusinessReport(Map<String, Object> params) {
        List<Map<String, Object>> businessR = new ArrayList<Map<String, Object>>();
        if (params.get("Datetype").toString().equals("M")) {

            int beginmonth = Integer.parseInt(params.get("beginTime").toString().split("-")[1]);
            int endmonth = Integer.parseInt(params.get("endTime").toString().split("-")[1]);
            int beginyear = Integer.parseInt(params.get("beginTime").toString().split("-")[0]);
            int endyear = Integer.parseInt(params.get("endTime").toString().split("-")[0]);
            params.put("beginTime", DateTimeUtils.getMonthFirstTime(beginmonth, beginyear));
            params.put("endTime", DateTimeUtils.getMonthLastTime(endmonth,endyear));
        }

        businessR = tbusinessAnalysisChartsDao.isfindBusinessReport(params);
        List<BusinessReport> businessList = new ArrayList<BusinessReport>();

        if (businessR.size() > 0) {
            for (int i = 0; i < businessR.size(); i++) {
                BusinessReport businssRport = new BusinessReport();
                if (businessR.get(i) != null) {
                    if (businessR.get(i).get("shouldamount") != null && businessR.get(i).get("shouldamount").toString().equals("0.0")) {
                        continue;
                    }

                    // 应收总额
                    if (businessR.get(i).get("shouldamount") != null && businessR.get(i).get("shouldamount") != "") {
                        BigDecimal shouldamountResult = new BigDecimal(businessR.get(i).get("shouldamount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setShouldamount(shouldamountResult);
                    } else {
                        BigDecimal shouldamountResult = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setShouldamount(shouldamountResult);
                    }
                    // 实收总额
                    if (businessR.get(i).get("paidinamount") != null && businessR.get(i).get("paidinamount") != "") {
                        BigDecimal PaidinamountResult = new BigDecimal(businessR.get(i).get("paidinamount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setPaidinamount(PaidinamountResult);
                    } else {
                        BigDecimal PaidinamountResult = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setPaidinamount(PaidinamountResult);
                    }
                    // 折扣总额
                    if (businessR.get(i).get("discountamount") != null && businessR.get(i).get("discountamount") != "") {
                        BigDecimal DiscountamountResult = new BigDecimal(businessR.get(i).get("discountamount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setDiscountamount(DiscountamountResult);
                    } else {
                        BigDecimal DiscountamountResult = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setDiscountamount(DiscountamountResult);
                    }

                    // 桌数
                    if (businessR.get(i).get("tablenum") != null && businessR.get(i).get("tablenum") != "") {
                        BigDecimal tablecount = new BigDecimal(businessR.get(i).get("tablenum").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setTablenum(tablecount);
                    } else {
                        BigDecimal tablecount = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setTablenum(tablecount);
                    }

                    // 人均
                    if (businessR.get(i).get("personcon") != null && businessR.get(i).get("personcon") != "") {
                        BigDecimal personpercent = new BigDecimal(businessR.get(i).get("personcon").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setPersonPercent(personpercent);
                    } else {
                        BigDecimal personpercent = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setPersonPercent(personpercent);
                    }

                    businssRport.setStatistictime(businessR.get(i).get("statistictime").toString());

                    businessList.add(businssRport);
                }
            }
        }
        return businessList;
    }
}
