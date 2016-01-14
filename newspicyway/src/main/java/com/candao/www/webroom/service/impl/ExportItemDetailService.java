package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
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

/**
 * 导出品项报表
 *
 * @author weizhifang
 * @since 2015-6-1
 *
 */
@Service
public class ExportItemDetailService {
	
	/**
	 * 使用jxl导出
	 * @author weizhifang
	 * @since 2015-5-30
	 * @param request
	 * @param item
	 * @throws Exception
	 */
	public void createExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> item,List<Map<String,Object>> itemDetail,Map<String,Object> params)throws Exception{  
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
             sheet1.mergeCells(0, 0, 3, 0);
             sheet1.setRowView(0, 1200);
             String title1 = ExcelUtils.setTabTitle("品项销售表",params);
             jxl.write.Label labelTitle1 = new jxl.write.Label(0, 0, title1);
             labelTitle1.setCellFormat(wcfTitle); 
             sheet1.addCell(labelTitle1);// 放入工作簿  
             String text1 [] = {"品类","品项类型","数量","份额（%）"};
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
	            	 String dishtypetitle = item.get(i).get("dishtypetitle") == null ? "" : item.get(i).get("dishtypetitle").toString();
	 				 String number = item.get(i).get("number") == null ? "" : item.get(i).get("number").toString();
	 				 String share = item.get(i).get("share") == null ? "" : item.get(i).get("share").toString();
	            	 sheet1.addCell(new Label(0, rowNum1, itemDesc, wcfTable));
	            	 sheet1.addCell(new Label(1, rowNum1, dishtypetitle, wcfTable));
	            	 sheet1.addCell(new Label(2, rowNum1, number.substring(0, number.length()-2), wcfTable));
	            	 sheet1.addCell(new Label(3, rowNum1, share, wcfTable));
	             }
             }
             WritableSheet sheet2 = wwb.createSheet("品项销售明细表", 2);// 建立工作簿 
             sheet2.mergeCells(0, 0, 5, 0);
             sheet2.setRowView(0, 1200);
             String title2 = ExcelUtils.setTabTitle("品项销售明细表",params);
             jxl.write.Label labelTitle2 = new jxl.write.Label(0, 0, title2);
             labelTitle2.setCellFormat(wcfTitle); 
             sheet2.addCell(labelTitle2);// 放入工作簿  
             String text2[] = {"品项名称","品项编号","单价","单位","数量","份额（%）"};
             for(int i=0;i<text2.length;i++){
            	 sheet2.setColumnView(i,25);
            	 sheet2.addCell(new Label(i,1,text2[i],wcfHead));
             }
             if(!itemDetail.toString().equals("[null]")){
	             int rowNum2 = 1;
	             for(int i=0;i<itemDetail.size();i++){
	            	 rowNum2++;
	            	 sheet2.setColumnView(i,25);
	            	 String title = itemDetail.get(i).get("title") == null ? "" : itemDetail.get(i).get("title").toString();
	 				 String dishNo = itemDetail.get(i).get("dishNo") == null ? "" : itemDetail.get(i).get("dishNo").toString();
	 				 String price = itemDetail.get(i).get("price") == null ? "" : itemDetail.get(i).get("price").toString();
	 				 String unit = itemDetail.get(i).get("unit") == null ? "" : itemDetail.get(i).get("unit").toString();
	 				 String dishNum = itemDetail.get(i).get("number") == null ? "" : itemDetail.get(i).get("number").toString();
	 				 String share = itemDetail.get(i).get("share") == null ? "" : itemDetail.get(i).get("share").toString();
	            	 sheet2.addCell(new Label(0, rowNum2, title, wcfTable));
	            	 sheet2.addCell(new Label(1, rowNum2, dishNo, wcfTable));
	            	 sheet2.addCell(new Label(2, rowNum2, price, wcfTable));
	            	 sheet2.addCell(new Label(3, rowNum2, unit, wcfTable));
	            	 sheet2.addCell(new Label(4, rowNum2, dishNum.substring(0, dishNum.length()-2), wcfTable));
	            	 sheet2.addCell(new Label(5, rowNum2, share, wcfTable));
	             }
             }
             // 写入数据     
             wwb.write();
             wwb.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
        ExcelUtils.downloadExcel(response,fileName,realPath);
	}
	
}
