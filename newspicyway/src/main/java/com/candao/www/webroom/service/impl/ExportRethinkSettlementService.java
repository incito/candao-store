package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Service;

import com.candao.www.utils.ExcelUtils;

/**
 * 反结算明细表导出
 * @author weizhifang
 * @since 2015-11-26
 *
 */
@Service
public class ExportRethinkSettlementService {

	/**
	 * 导出反结算明细表
	 * @author weizhifang
	 * @since 2015-11-24
	 * @param request
	 * @param response
	 * @param dataMap
	 * @param params
	 */
	public void createExcel(HttpServletRequest request,HttpServletResponse response,
	    List<Map<String,Object>> dataMap,Map<String,Object> params){
		// 文件名称与路径  
        String fileName = "反结算明细表.xls";  
        String excelUrl = request.getSession().getServletContext().getRealPath("/");
        String realPath =  excelUrl + fileName;
        // 创建Excel工作薄     
        WritableWorkbook wwb = null;
        try {
    	    //表头
    	    WritableCellFormat wcfTitle = ExcelUtils.setWcfTitle();// 单元格定义  
            //标题
            WritableCellFormat wcfHead = ExcelUtils.setWcfHead();
            wcfHead.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK); 
            //内容
            WritableCellFormat wcfTable = ExcelUtils.setWcfTable();   
    	    OutputStream os = new FileOutputStream(realPath);
            wwb = Workbook.createWorkbook(os);  
            WritableSheet sheet = wwb.createSheet("反结算明细表", 1);// 建立工作簿 
            // 写表头  
            sheet.mergeCells(0, 0, 11, 0);
            sheet.setRowView(0, 1200);
            String title = ExcelUtils.setTabTitle("反结算明细表",params);
            jxl.write.Label labelTitle = new jxl.write.Label(0, 0, title);
            labelTitle.setCellFormat(wcfTitle);
            sheet.addCell(labelTitle);// 放入工作簿  
            sheet.mergeCells(0, 1, 0, 2);
            sheet.mergeCells(1, 1, 3, 1);
            sheet.mergeCells(4, 1, 6, 1);
            sheet.mergeCells(7, 1, 7, 2);
            sheet.mergeCells(8, 1, 8, 2);
            sheet.mergeCells(9, 1, 9, 2);
            sheet.mergeCells(10, 1, 10, 2);
            sheet.mergeCells(11, 1, 11, 2);
            sheet.addCell(new Label(0,1,"订单号",wcfHead));
            sheet.addCell(new Label(1,1,"反结算前",wcfHead));
            sheet.addCell(new Label(4,1,"反结算后",wcfHead));
            sheet.addCell(new Label(1,2,"结算时间",wcfHead));
            sheet.addCell(new Label(2,2,"应收",wcfHead));
            sheet.addCell(new Label(3,2,"实收",wcfHead));
            sheet.addCell(new Label(4,2,"结算时间",wcfHead));
            sheet.addCell(new Label(5,2,"应收",wcfHead));
            sheet.addCell(new Label(6,2,"实收",wcfHead));
            sheet.addCell(new Label(7,1,"时间差异",wcfHead));
            sheet.addCell(new Label(8,1,"实收差异",wcfHead));
            sheet.addCell(new Label(9,1,"服务员",wcfHead));
            sheet.addCell(new Label(10,1,"收银员",wcfHead));
            sheet.addCell(new Label(11,1,"授权人",wcfHead));
            int rowNum = 2;
            for (int i=0;i<dataMap.size();i++) {
           	    rowNum++;
           	    sheet.setColumnView(i, 25);
           	    String before_paidamount = dataMap.get(i).get("before_paidamount")==null?"":dataMap.get(i).get("before_paidamount").toString();
	           	String authorized = dataMap.get(i).get("authorized")==null?"":dataMap.get(i).get("authorized").toString();
	           	String after_paidamount = dataMap.get(i).get("after_paidamount")==null?"":dataMap.get(i).get("after_paidamount").toString();
	           	String before_shouldamount = dataMap.get(i).get("before_shouldamount")==null?"":dataMap.get(i).get("before_shouldamount").toString();
	           	String paidindifference = dataMap.get(i).get("paidindifference")==null?"":dataMap.get(i).get("paidindifference").toString();
	           	String after_shouldamount = dataMap.get(i).get("after_shouldamount")==null?"":dataMap.get(i).get("after_shouldamount").toString();
	           	String after_cleartime = dataMap.get(i).get("after_cleartime")==null?"":dataMap.get(i).get("after_cleartime").toString();
	           	String before_cleartime = dataMap.get(i).get("before_cleartime")==null?"":dataMap.get(i).get("before_cleartime").toString();
	           	String orderid = dataMap.get(i).get("orderid")==null?"":dataMap.get(i).get("orderid").toString();
	           	String waiter = dataMap.get(i).get("waiter")==null?"":dataMap.get(i).get("waiter").toString();
	           	String timedifference = dataMap.get(i).get("timedifference")==null?"":dataMap.get(i).get("timedifference").toString();
	           	String cashier = dataMap.get(i).get("cashier")==null?"":dataMap.get(i).get("cashier").toString();
	           	sheet.addCell(new Label(0, rowNum, orderid, wcfTable));
	           	sheet.addCell(new Label(1, rowNum, before_cleartime, wcfTable));
	           	sheet.addCell(new Label(2, rowNum, before_shouldamount, wcfTable));
	            sheet.addCell(new Label(3, rowNum, before_paidamount, wcfTable));
	           	sheet.addCell(new Label(4, rowNum, after_cleartime, wcfTable));
	           	sheet.addCell(new Label(5, rowNum, after_shouldamount, wcfTable));
	           	sheet.addCell(new Label(6, rowNum, after_paidamount, wcfTable));
	           	sheet.addCell(new Label(7, rowNum, timedifference, wcfTable));
	           	sheet.addCell(new Label(8, rowNum, paidindifference, wcfTable));
	           	sheet.addCell(new Label(9, rowNum, waiter, wcfTable));
	           	sheet.addCell(new Label(10, rowNum, cashier, wcfTable));
	           	sheet.addCell(new Label(11, rowNum, authorized, wcfTable));
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
