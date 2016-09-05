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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TRethinkSettlementDao;
import com.candao.www.data.dao.TWaiterSaleDao;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.webroom.service.WaiterSaleService;

@Service
public class WaiterSaleServiceImpl implements WaiterSaleService {

	@Autowired
	private TWaiterSaleDao tWaiterSaleDao;
	
	@Autowired
	private TRethinkSettlementDao tRethinkSettlementDao;
	
	/**
	 * 查询服务员销售列表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> waiterSaleListProcedure(Map<String,Object> params){
		int page = Integer.parseInt((params.get("page").toString()));
		int rows = Integer.parseInt(params.get("rows").toString());
		params.put("page", page*rows);
		params.put("rows", rows);
		return tWaiterSaleDao.waiterSaleListProcedure(params);
	}
	
	/**
	 * 按订单查询服务员销售列表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getWaiterSaleDetail(Map<String,Object> params){
		return tWaiterSaleDao.getWaiterSaleDetail(params);
	}
	
	/**
	 * 导出Excel主表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param request
	 * @param response
	 * @param list
	 * @param params
	 */
	public void createMainExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> list,Map<String,Object> params){
		// 文件名称与路径  
        String fileName = "服务员销售统计表.xls";  
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
             WritableSheet sheet = wwb.createSheet("服务员销售统计表", 1);// 建立工作簿 
             // 写表头  
             sheet.mergeCells(0, 0, 7, 0);
             sheet.setRowView(0, 1200);
             String title = ExcelUtils.setTabTitle("服务员销售统计表",params);
             jxl.write.Label labelTitle = new jxl.write.Label(0, 0, title);
             labelTitle.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle);// 放入工作簿  
             String text [] = {"日期","服务员姓名","售卖菜品","单价","单位","赠送数量","打折数量","售卖数量"};
             for(int i=0;i<text.length;i++){
            	 sheet.setColumnView(i,25);
            	 sheet.addCell(new Label(i,1,text[i],wcfHead));  
             }
             if(!list.toString().equals("[null]")){
            	 int rowNum = 1;
	             for(int i=0;i<list.size();i++){
	            	 rowNum++;
	            	 sheet.setColumnView(i,25);
	            	 String currdate = list.get(i).get("currdate") == null ? "" : list.get(i).get("currdate").toString();
	            	 String waiterName = list.get(i).get("NAME") == null ? "" : list.get(i).get("NAME").toString();
	            	 String dishName = list.get(i).get("title") == null ? "" : list.get(i).get("title").toString();
	            	 String orderprice = list.get(i).get("orderprice") == null ? "" : list.get(i).get("orderprice").toString();
	            	 String dishunit = list.get(i).get("dishunit") == null ? "" : list.get(i).get("dishunit").toString();
	            	 String present = list.get(i).get("present") == null ? "" : list.get(i).get("present").toString();
	            	 String discount = list.get(i).get("discount") == null ? "" : list.get(i).get("discount").toString();
	            	 String num = list.get(i).get("num") == null ? "" : list.get(i).get("num").toString();
	            	 sheet.addCell(new Label(0, rowNum, currdate, wcfTable));
	            	 sheet.addCell(new Label(1, rowNum, waiterName, wcfTable));
	            	 sheet.addCell(new Label(2, rowNum, dishName, wcfTable));
	            	 sheet.addCell(new Label(3, rowNum, orderprice, wcfTable));
	            	 sheet.addCell(new Label(4, rowNum, dishunit, wcfTable));
	            	 sheet.addCell(new Label(5, rowNum, present.substring(0, num.length()-2), wcfTable)); 
	            	 sheet.addCell(new Label(6, rowNum, discount, wcfTable));
	            	 sheet.addCell(new Label(7, rowNum, num.substring(0, num.length()-2), wcfTable));
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
	
	/**
	 * 导出Excel子表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param request
	 * @param response
	 * @param list
	 * @param params
	 */
	public void createChildExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> childList,Map<String,Object> params,Map<String,Object> mainList){
		// 文件名称与路径  
        String fileName = "服务员销售统计表.xls";  
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
             WritableSheet sheet = wwb.createSheet("服务员销售统计子表", 1);// 建立工作簿 
             // 写表头  
             sheet.mergeCells(0, 0, 3, 0);
             sheet.setRowView(0, 1200);
             String title = ExcelUtils.setTabTitle("服务员销售统计子表",params);
             jxl.write.Label labelTitle = new jxl.write.Label(0, 0, title);
             labelTitle.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle);// 放入工作簿
             String main[] = {"服务员姓名","售卖菜品","单位","售卖数量"};
             for(int i=0;i<main.length;i++){
            	 sheet.setColumnView(i,25);
            	 sheet.addCell(new Label(i,1,main[i],wcfHead));  
             }
             String num = mainList.get("num").toString();
             sheet.addCell(new Label(0, 2, mainList.get("waiterName").toString(), wcfTable));
        	 sheet.addCell(new Label(1, 2, mainList.get("dishName").toString(), wcfTable));
        	 sheet.addCell(new Label(2, 2, mainList.get("dishunit").toString(), wcfTable));
        	 sheet.addCell(new Label(3, 2, num.substring(0, num.length()-2), wcfTable));
             String text [] = {"时间","订单号","单位","售卖数量"};
             for(int i=0;i<text.length;i++){
            	 sheet.setColumnView(i,25);
            	 sheet.addCell(new Label(i,3,text[i],wcfHead));  
             }
             if(!childList.toString().equals("[null]")){
            	 int rowNum = 3;
	             for(int i=0;i<childList.size();i++){
	            	 rowNum++;
	            	 sheet.setColumnView(i,25);
	            	 String begintime = childList.get(i).get("begintime") == null ? "" : childList.get(i).get("begintime").toString();
	            	 String orderid = childList.get(i).get("orderid") == null ? "" : childList.get(i).get("orderid").toString();
	            	 String dishnum = childList.get(i).get("dishnum") == null ? "" : childList.get(i).get("dishnum").toString();
	            	 sheet.addCell(new Label(0, rowNum, begintime, wcfTable));
	            	 sheet.addCell(new Label(1, rowNum, orderid, wcfTable));
	            	 sheet.addCell(new Label(2, rowNum, params.get("dishunit").toString(), wcfTable));
	            	 sheet.addCell(new Label(3, rowNum, dishnum.substring(0, dishnum.length()-2), wcfTable));
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
	
	/**
	 * 查询服务员菜品信息
	 * @author weizhifang
	 * @sice 2016-3-26
	 * @param params
	 * @return
	 */
	public Map<String,Object> getWaiterDishInfo(Map<String,Object> params){
		Map<String,Object> result = tWaiterSaleDao.getWaiterDishInfo(params);
		String waiterId = (String)params.get("userid");
		String waiter = tRethinkSettlementDao.queryUserNameByJobNumber(waiterId,params.get("branchId").toString());
		result.put("waiterName", waiter);
		return result;
	}
}
