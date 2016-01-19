package com.candao.www.webroom.service.impl;

import com.candao.www.utils.ExcelUtils;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by IntelliJ IDEA.
 * User: johnson
 * Date: 5/23/15
 * Time: 3:28 下午
 */
@Service
public class ExportReturnDishService {

	/**
	 * 退菜excel导出
	 * @author weizhifang
	 * @since 2015-7-30
	 * @param mapList
	 * @param req
	 * @param response
	 */
	public void exportReturnDishExcel(List<Map<String,Object>> mapList,HttpServletRequest request, HttpServletResponse response,Map<String,Object> params){
		// 文件名称与路径  
        String fileName = "退菜明细表.xls";  
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
             
             WritableSheet sheet1 = wwb.createSheet("退菜明细表", 1);// 建立工作簿 
             // 写表头  
             sheet1.mergeCells(0, 0, 7, 0);
             sheet1.setRowView(0, 1200);
             String title1 = ExcelUtils.setTabTitle("退菜明细表",params);
             jxl.write.Label labelTitle1 = new jxl.write.Label(0, 0, title1);
             labelTitle1.setCellFormat(wcfTitle); 
             sheet1.addCell(labelTitle1);// 放入工作簿 
             String tableTitle [] = {"发生时间","结账单号","菜品名称","份数","金额","退菜申请人","退菜授权人","退菜原因"};
             for(int i=0;i<tableTitle.length;i++){
            	 sheet1.setColumnView(i, 25);
            	 String text = tableTitle[i];
            	 sheet1.addCell(new Label(i,1,text,wcfHead));  
             }
             int rowNum = 1;
             for (int i=0;i<mapList.size();i++) {
            	 if(mapList.get(i)==null){
            		 continue;
            	 }
            	 rowNum++;
            	 sheet1.setColumnView(i, 25);
 				 String beginTime = mapList.get(i).get("beginTime")==null?"":mapList.get(i).get("beginTime").toString();
 				 String orderid = mapList.get(i).get("orderid")==null?"":mapList.get(i).get("orderid").toString();
 				 String title = mapList.get(i).get("title")==null?"":mapList.get(i).get("title").toString();
 				 String num = mapList.get(i).get("num")==null?"0":mapList.get(i).get("num").toString();
 				 String amount = mapList.get(i).get("amount")==null?"0":mapList.get(i).get("amount").toString();
 				 String waiter = mapList.get(i).get("waiter")==null?"0":mapList.get(i).get("waiter").toString();
 				 String discardusername = mapList.get(i).get("discardusername")==null?"":mapList.get(i).get("discardusername").toString();
 				 String discardreason = "";
 				 if (mapList.get(i).get("discardreason") != null) {
 					 discardreason = mapList.get(i).get("discardreason")==null?"":mapList.get(i).get("discardreason").toString();
 				 }
 				 sheet1.addCell(new Label(0, rowNum, beginTime, wcfTable));
 				 sheet1.addCell(new Label(1, rowNum, orderid, wcfTable));
 				 sheet1.addCell(new Label(2, rowNum, title, wcfTable));
 				 sheet1.addCell(new Label(3, rowNum, num, wcfTable));
 				 sheet1.addCell(new Label(4, rowNum, amount, wcfTable));
 				 sheet1.addCell(new Label(5, rowNum, waiter, wcfTable));
 				 sheet1.addCell(new Label(6, rowNum, discardusername, wcfTable));
 				 sheet1.addCell(new Label(7, rowNum, discardreason, wcfTable));
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

