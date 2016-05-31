package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TBusinessAnalysisChartsDao;
import com.candao.www.utils.DateTimeUtils;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.webroom.model.BusinessReport;
import com.candao.www.webroom.service.BusinessAnalysisChartsService;

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
                    if (businessR.get(i).get("tablecount") != null && businessR.get(i).get("tablecount") != "") {
                        BigDecimal tablecount = new BigDecimal(businessR.get(i).get("tablecount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setTablenum(tablecount);
                    } else {
                        BigDecimal tablecount = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        businssRport.setTablenum(tablecount);
                    }

                    // 人均
                    if (businessR.get(i).get("personpercent") != null && businessR.get(i).get("personpercent") != "") {
                        BigDecimal personpercent = new BigDecimal(businessR.get(i).get("personpercent").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
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
    
    /**
	 * 导出营业分析报表
	 * @author weizhifang
	 * @since 2016-5-18
	 * @param request
	 * @param response
	 * @param BusinessList
	 * @param params
	 */
	public void createBusinessReportExcel(HttpServletRequest request, HttpServletResponse response,List<BusinessReport> list,Map<String, Object> params){
		// 文件名称与路径  
        String fileName = "营业数据分析表.xls";  
        String excelUrl = request.getSession().getServletContext().getRealPath("/");
        String realPath =  excelUrl + fileName;
        // 创建Excel工作薄     
        WritableWorkbook wwb = null;
        try {
        	 //表头
        	 WritableCellFormat wcfTitle = ExcelUtils.setWcfTitle();// 单元格定义  
             //标题
             WritableCellFormat wcfHead = ExcelUtils.setWcfHead();  
             //内容
             WritableCellFormat wcfTable = ExcelUtils.setWcfTable();   
        	 OutputStream os = new FileOutputStream(realPath);
             wwb = Workbook.createWorkbook(os);  
             WritableSheet sheet = wwb.createSheet("营业数据分析表", 1);// 建立工作簿 
             // 写表头  
             sheet.mergeCells(0, 0, 5, 0);
             sheet.setRowView(0, 1200);
             String title = "营业数据分析表"+"\n门店名称:"+params.get("branchName")+"\n时间:"+params.get("begintime")+"——"+params.get("endtime");
             jxl.write.Label labelTitle = new jxl.write.Label(0, 0, title);
             labelTitle.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle);// 放入工作簿  
             String text [] = {"时间","应收总额","实收总额","折扣总额","人均","桌数"};
             for(int i=0;i<text.length;i++){
            	 sheet.setColumnView(i,25);
            	 sheet.addCell(new Label(i,1,text[i],wcfHead));  
             }
             if(!list.toString().equals("[null]")){
            	 int rowNum = 1;
	             for(int i=0;i<list.size();i++){
	            	 rowNum++;
	            	 sheet.setColumnView(i,25);
	            	 BusinessReport bus = list.get(i);
	            	 String statistictime = bus.getStatistictime();
	            	 BigDecimal shouldamount = bus.getShouldamount();
	            	 BigDecimal paidinamount = bus.getPaidinamount();
	            	 BigDecimal discountamount = bus.getDiscountamount();
	            	 BigDecimal personPercent = bus.getPersonPercent();
	            	 BigDecimal tablenum = bus.getTablenum();
	            	 sheet.addCell(new Label(0, rowNum, statistictime, wcfTable));
	            	 sheet.addCell(new Label(1, rowNum, shouldamount.toString(), wcfTable));
	            	 sheet.addCell(new Label(2, rowNum, paidinamount.toString(), wcfTable));
	            	 sheet.addCell(new Label(3, rowNum, discountamount.toString(), wcfTable));
	            	 sheet.addCell(new Label(4, rowNum, personPercent.toString(), wcfTable));
	            	 sheet.addCell(new Label(5, rowNum, tablenum.toString(), wcfTable));
	             }
             }
             // 写入数据     
             wwb.write();
             wwb.close();
         }catch(Exception e){
        	 e.printStackTrace();
         }
         ExcelUtils.downloadExcel(request,response,fileName,realPath);
	}
}
