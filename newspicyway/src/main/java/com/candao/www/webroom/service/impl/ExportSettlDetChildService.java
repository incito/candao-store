package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Service;

import com.candao.www.utils.ExcelUtils;

@Service
public class ExportSettlDetChildService {

	/**
	 * 导出结算方式明细表详情
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param request
	 * @param response
	 * @param dataMap
	 * @param params
	 * @param parentItem
	 */
	public void createExcel(HttpServletRequest request,HttpServletResponse response,
			List<Map<String,Object>> dataMap,Map<String,Object> params,Map<String,Object> parentItem){
		// 文件名称与路径  
        String fileName = "结算方式明细表详情.xls";  
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
             WritableSheet sheet = wwb.createSheet("结算方式明细表详情", 1);// 建立工作簿 
             // 写表头  
             sheet.mergeCells(0, 0, 2, 0);
             sheet.setRowView(0, 1200);
             String title = ExcelUtils.setTabTitle("结算方式明细表详情",params);
             jxl.write.Label labelTitle = new jxl.write.Label(0, 0, title);
             labelTitle.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle);// 放入工作簿  
             String parentItemTitle [] = {"结算方式","笔数","金额"};
             for(int i=0;i<parentItemTitle.length;i++){
            	 sheet.setColumnView(i, 25);
            	 sheet.addCell(new Label(i,1,parentItemTitle[i],wcfHead)); 
             }
             String payWayName = (String)parentItem.get("payWayName");
             Long nums = (Long)parentItem.get("nums");
             BigDecimal  prices = (BigDecimal)parentItem.get("price");
             sheet.addCell(new Label(0, 2, payWayName, wcfTable));
             sheet.addCell(new Label(1, 2, nums.toString(), wcfTable));
             sheet.addCell(new Label(2, 2, prices.toString(), wcfTable));
             String text [] = {"时间","订单号","金额"};
             for(int i=0;i<text.length;i++){
            	 sheet.setColumnView(i,25);
            	 sheet.addCell(new Label(i,3,text[i],wcfHead));  
             }
             int rowNum = 3;
             for (int i=0;i<dataMap.size();i++) {
            	 rowNum++;
            	 sheet.setColumnView(i, 25);
 				 String inserttime = dataMap.get(i).get("insertTime")==null?"":dataMap.get(i).get("insertTime").toString();
 				 String orderid = dataMap.get(i).get("orderid")==null?"":dataMap.get(i).get("orderid").toString();
 				 String payamount = dataMap.get(i).get("payAmount")==null?"0":dataMap.get(i).get("payAmount").toString();
 				 sheet.addCell(new Label(0, rowNum, inserttime, wcfTable));
				 sheet.addCell(new Label(1, rowNum, orderid, wcfTable));
				 sheet.addCell(new Label(2, rowNum, payamount, wcfTable));
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
