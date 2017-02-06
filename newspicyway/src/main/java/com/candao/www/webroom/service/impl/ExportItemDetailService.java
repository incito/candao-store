package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.candao.www.utils.ExcelUtils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 导出品项报表
 *
 * @author weizhifang
 * @since 2015-6-1
 *
 */
@Service
public class ExportItemDetailService {
	
	private static final Logger logger = LoggerFactory.getLogger(ExportItemDetailService.class);
	
	/**
	 * 使用jxl导出
	 * @author weizhifang
	 * @since 2015-5-30
	 * @param request
	 * @param item
	 * @throws Exception
	 */
	public void createExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> item,Map<String,Object> params)throws Exception{  
        // 文件名称与路径  
        String fileName = "品项销售明细表.xls";  
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
             WritableSheet sheet1 = wwb.createSheet("品项销售表", 1);// 建立工作簿 
             // 写表头  
             sheet1.mergeCells(0, 0, 5, 0);
             sheet1.setRowView(0, 1200);
             String title1 = ExcelUtils.setTabTitle("品项销售表",params);
             jxl.write.Label labelTitle1 = new jxl.write.Label(0, 0, title1);
             labelTitle1.setCellFormat(wcfTitle); 
             sheet1.addCell(labelTitle1);// 放入工作簿  
             String text1 [] = {"品类","数量","千 次" ,"应收金额" ,"实收金额","营业额占比(%)"};
             for(int i=0;i<text1.length;i++){
            	 sheet1.setColumnView(i,25);
            	 sheet1.addCell(new Label(i,1,text1[i],wcfHead));  
             }
             if(!item.toString().equals("[null]")){
	             int rowNum1 = 1;
	             for(int i=0;i<item.size();i++){
	            	 rowNum1++;
	            	 sheet1.setColumnView(i,25);
	            	 String itemDesc = item.get(i).get("itemDesc") == null ? "" : item.get(i).get("itemDesc").toString();
	 				 String number = item.get(i).get("number") == null ? "" : item.get(i).get("number").toString();
	 				 String thousandstimes = item.get(i).get("thousandstimes") == null ? "" : item.get(i).get("thousandstimes").toString();
	 				 String orignalprice = item.get(i).get("orignalprice") == null ? "" : item.get(i).get("orignalprice").toString();
	 				 String debitamount = item.get(i).get("debitamount") == null?"":item.get(i).get("debitamount").toString();
	 				 String turnover = item.get(i).get("turnover") == null ? "" : item.get(i).get("turnover").toString();
	            	 sheet1.addCell(new Label(0, rowNum1, itemDesc, wcfTable));
	            	 sheet1.addCell(new Label(1, rowNum1, number, wcfTable));
	            	 sheet1.addCell(new Label(2, rowNum1, thousandstimes, wcfTable));
	            	 sheet1.addCell(new Label(3, rowNum1, orignalprice, wcfTable));
	            	 sheet1.addCell(new Label(4, rowNum1, debitamount, wcfTable));
	            	 sheet1.addCell(new Label(5, rowNum1, turnover, wcfTable));
	             }
             }
             // 写入数据     
             wwb.write();
             wwb.close();
        }catch(Exception e){
        	logger.error("-->",e);
        	e.printStackTrace();
        }
        ExcelUtils.downloadExcel(request,response,fileName,realPath);
	}
	/**
	 * 使用jxl导出
	 * @author weizhifang
	 * @since 2015-5-30
	 * @param request
	 * @param item
	 * @throws Exception
	 */
	public void createExcelSub(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> itemDetail,Map<String,Object> params)throws Exception{  
        // 文件名称与路径  
        String fileName = "品项销售明细表.xls";  
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
             WritableSheet sheet = wwb.createSheet("品项销售明细表", 1);// 建立工作簿 
             sheet.mergeCells(0, 0, 9, 0);
             sheet.setRowView(0, 1200);
             String title = ExcelUtils.setTabTitle("品项销售明细表",params);
             jxl.write.Label labelTitle = new jxl.write.Label(0, 0, title);
             labelTitle.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle);// 放入工作簿  
             String text[] = {"品项名称","品项编号","单价","单位","单品数量","套餐数量","千次" ,"应收金额","实收金额","营业额占比(%)"};
             for(int i=0;i<text.length;i++){
            	 sheet.setColumnView(i,25);
            	 sheet.addCell(new Label(i,1,text[i],wcfHead));
             }
             if(!itemDetail.toString().equals("[null]")){
	             int rowNum = 1;
	             for(int i=0;i<itemDetail.size();i++){
	            	 rowNum++;
	            	 sheet.setColumnView(i,25);
	            	 String itemTitle = itemDetail.get(i).get("title") == null ? "" : itemDetail.get(i).get("title").toString();
	 				 String dishNo = itemDetail.get(i).get("dishNo") == null ? "" : itemDetail.get(i).get("dishNo").toString();
	 				 String price = itemDetail.get(i).get("price") == null ? "" : itemDetail.get(i).get("price").toString();
	 				 String unit = itemDetail.get(i).get("unit") == null ? "" : itemDetail.get(i).get("unit").toString();
	 				 String danpinNumber = itemDetail.get(i).get("danpinnumber") == null ? "" : itemDetail.get(i).get("danpinnumber").toString();
	 				 String taocanNumber = itemDetail.get(i).get("taocannumber") == null ? "" : itemDetail.get(i).get("taocannumber").toString();
	 				 String thousandstimes = itemDetail.get(i).get("thousandstimes") == null ? "" : itemDetail.get(i).get("thousandstimes").toString();
	 				 String orignalprice = itemDetail.get(i).get("orignalprice") == null ? "" : itemDetail.get(i).get("orignalprice").toString();
	 				 String debitamount = itemDetail.get(i).get("debitamount") == null ? "" : itemDetail.get(i).get("debitamount").toString();
	 				 String turnover = itemDetail.get(i).get("turnover") == null ? "" : itemDetail.get(i).get("turnover").toString();
	            	 sheet.addCell(new Label(0, rowNum, itemTitle, wcfTable));
	            	 sheet.addCell(new Label(1, rowNum, dishNo, wcfTable));
	            	 sheet.addCell(new Label(2, rowNum, price, wcfTable));
	            	 sheet.addCell(new Label(3, rowNum, unit, wcfTable));
	            	 sheet.addCell(new Label(4, rowNum, danpinNumber, wcfTable));
	            	 sheet.addCell(new Label(5, rowNum, taocanNumber, wcfTable));
	            	 sheet.addCell(new Label(6, rowNum, thousandstimes, wcfTable));
	            	 sheet.addCell(new Label(7, rowNum, orignalprice, wcfTable));
	            	 sheet.addCell(new Label(8, rowNum, debitamount, wcfTable));
	            	 sheet.addCell(new Label(9, rowNum, turnover, wcfTable));
	             }
             }
             // 写入数据     
             wwb.write();
             wwb.close();
        }catch(Exception e){
        	logger.error("-->",e);
        	e.printStackTrace();
        }
        ExcelUtils.downloadExcel(request,response,fileName,realPath);
	}
}
