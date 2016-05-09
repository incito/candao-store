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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.dao.DaoSupport;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.model.TbUser;
import com.candao.www.data.model.User;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.utils.SessionUtils;

/**
 * 其他优惠方式的添加
 *  @author zhouyao
 *  @serialData 2016-1-21
 */
@Service
public class RegisterBillService {
  public final static String PREFIX = RegisterBillService.class.getName();
  @Autowired
  private DaoSupport dao;
 
	/**
	   * 查询挂账明细统计表
	   * @param groupon
	   */
	public List<Map<String, Object>> BillCount(Map<String, Object> params) {
//		List<Map<String, Object>> ax=new ArrayList<Map<String,Object>>();
		
		
			params.put("pi_branchid",params.get("branchid"));
			params.put("pi_ksrq",params.get("beginTime"));
			params.put("pi_jsrq", params.get("endTime"));
			params.put("pi_gzdw",params.get("billName"));
			params.put("pi_qsbz",params.get("clearStatus"));
			params.put("pi_dqym","-1");
			params.put("pi_myts","-1");
			
//			ax =  dao.find(PREFIX + ".getBillCount", params);
//			ax = (List<Map<String, Object>>) params.get("po_errmsg");
		
		return dao.find(PREFIX + ".getBillCount", params);
	}
	 
	
	/**
	   * 查询挂账明细表
	   * @param groupon
	   */
	public List<Map<String, Object>> BillDetail(Map<String, Object> params) {
//		List<Map<String, Object>> ax=new ArrayList<Map<String,Object>>();
//		
//		try {
			params.put("pi_branchid",params.get("branchid"));
			params.put("pi_ksrq",params.get("beginTime"));
			params.put("pi_jsrq", params.get("endTime"));
			params.put("pi_gzdw",params.get("billName"));
			params.put("pi_qsbz",params.get("clearStatus"));
			params.put("pi_dqym",params.get("pages"));
			params.put("pi_myts",params.get("pagesamount"));
			
			
		return dao.find(PREFIX + ".getBillDetail", params);
	}
	
	
	/**
	   * 查询挂单位
	   * @param groupon
	   */
	public List<Map<String, Object>> BillUnit(Map<String, Object> params) {
//		List<Map<String, Object>> ax=new ArrayList<Map<String,Object>>();
//		
//		try {
			params.put("pi_branchid",params.get("pi_branchid"));
			
//			ax =  dao.find(PREFIX + ".getBillUnit", params);
////			ax = (List<Map<String, Object>>) params.get("po_errmsg");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return dao.find(PREFIX + ".getBillUnit", params);
	}
	
	/**
	   * 查询挂结算历史
	   * @param groupon
	   */
	public List<Map<String, Object>> BillHistory(Map<String, Object> params) {
//		List<Map<String, Object>> ax=new ArrayList<Map<String,Object>>();
//		
//		try {
			params.put("ddbh",params.get("orderid"));
			params.put("mdid",params.get("branchid"));
//			ax =  dao.find(PREFIX + ".getBillHistory", params);
////			ax = (List<Map<String, Object>>) params.get("po_errmsg");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return  dao.find(PREFIX + ".getBillHistory", params);
	}
	
	/**
	   * 结算
	   * @param groupon
	   */
	public List<Map<String, Object>> Billing(Map<String, Object> params) {
//		List<Map<String, Object>> ax=new ArrayList<Map<String,Object>>();
//		try {
			User currentUser=SessionUtils.getCurrentUser();
			String czy = currentUser.getName();
			params.put("ddbh",params.get("orderid"));
			params.put("mdid",params.get("branchid"));
			params.put("gzdw",params.get("creaditname"));
			int zeor=0;
			
			if(StringUtils.isBlank(params.get("payamount").toString() )){
				params.put("jsje",zeor);
			}else{
				boolean  b = RegisterBillService.isNumeric(params.get("payamount").toString());
				if(b){
					params.put("jsje",params.get("payamount"));
				}else{
					
					
				}
				
			}
			/*if(StringUtils.isBlank(params.get("disamount").toString() )){
				params.put("ymje",zeor);
			}else{
				boolean  b = RegisterBillService.isNumeric(params.get("disamount").toString());
				if(b){
					params.put("ymje",params.get("disamount"));
				}else{
					
					
				}
				
			}*/
			params.put("czy",czy);
			params.put("itemtype",params.get("inserttime"));
			params.put("remark",params.get("remark"));
//			ax =  dao.find(PREFIX + ".Billing", params);
////			ax = (List<Map<String, Object>>) params.get("po_errmsg");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return dao.find(PREFIX + ".Billing", params);
	}
	/**
	   * 判断入参是否为数字
	   * @param groupon
	   */
	public static boolean isNumeric(String str){
		   for(int i=str.length();--i>=0;){
		      int chr=str.charAt(i);
		      if(chr<48 || chr>57)
		         return false;
		   }
		   return true;
		}
	/**
	   * 写入表头
	   * @param groupon
	   */
	public static String setTabTitle(String sheetName,Map<String,Object> params){
		String branchname = params.containsKey("branchname")?params.get("branchname").toString():"";
		String beginTime = params.get("beginTime").toString();
		String endTime = params.get("endTime").toString();
		String searchType = params.get("searchType").toString();
		String billName=null;
		String clearStatus=null;
		if(params.get("billName").toString().equals( "0")){
			billName = "全部";
		}else{
			billName = params.get("billName").toString();
			
		}
		if(params.get("clearStatus").toString().equals( "0")){
			clearStatus = "全部";
		}else if(params.get("clearStatus").toString().equals( "1")){
			clearStatus = "已清算";
			
		}else if(params.get("clearStatus").toString().equals( "2")){
			clearStatus = "未清算";
			
		}
		
		if(!searchType.equals("3")){
        	beginTime = DateUtils.stringDateFormat(beginTime);
        	endTime = DateUtils.stringDateFormat(endTime);
        }
		String shiftid = null;
		if(params.get("shiftid")!=null&&!params.get("shiftid").equals("")){
			shiftid = params.get("shiftid").toString();
			String shiftname = "";
			if (shiftid.equals("0")) {
				shiftname = "午市";
			}else if (shiftid.equals("-1")) {
				shiftname = "全天";
			}else if (shiftid.equals("1")) {
				shiftname = "晚市";
			}
			return sheetName+"\n门店名称:"+branchname+"   市别："+shiftname+"\n时间:"+beginTime+"——"+endTime;
		}
		return sheetName+"\n门店名称:"+branchname+"\n时间:"+beginTime+"——"+endTime+"\n挂账单位:"+billName+"\n清算标志:"+clearStatus;
	}
	/**
	   * 导出挂账统计表主表
	   * @param groupon
	   */
	
	public void createMainExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> list,Map<String,Object> params){
		// 文件名称与路径  
        String fileName = "挂账明细统计表.xls";  
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
             WritableSheet sheet = wwb.createSheet("挂账明细统计表", 1);// 建立工作簿 
             // 写表头  
             sheet.mergeCells(0, 0, 5, 0);
             sheet.setRowView(0, 1700);
             String title = setTabTitle("挂账明细统计表",params);
             jxl.write.Label labelTitle = new jxl.write.Label(0, 0, title);
             labelTitle.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle);// 放入工作簿  
             String text [] = {"挂账单位","挂账单数（单）","挂账总额（元）","已结金额（元）","未结金额（元）","最长挂账时间（天）"};
             for(int i=0;i<text.length;i++){
            	 sheet.setColumnView(i,25);
            	 sheet.addCell(new Label(i,1,text[i],wcfHead));  
             }
             if(!list.toString().equals("[null]")){
            	 int rowNum = 1;
	             for(int i=0;i<list.size();i++){
	            	 rowNum++;
	            	 sheet.setColumnView(i,25);
	            	 String gzdw = list.get(i).get("gzdw") == null ? "" : list.get(i).get("gzdw").toString();
	            	 String gzds = list.get(i).get("gzds") == null ? "" : list.get(i).get("gzds").toString();
	            	 String gzze = list.get(i).get("gzze") == null ? "" : list.get(i).get("gzze").toString();
	            	 String yjje = list.get(i).get("yjje") == null ? "" : list.get(i).get("yjje").toString();
	            	 String wjje = list.get(i).get("wjje") == null ? "" : list.get(i).get("wjje").toString();
	            	 String zcgzsj = list.get(i).get("zcgzsj") == null ? "" : list.get(i).get("zcgzsj").toString();
	            	 
	            	 sheet.addCell(new Label(0, rowNum, gzdw, wcfTable));
	            	 sheet.addCell(new Label(1, rowNum, gzds, wcfTable));
	            	 sheet.addCell(new Label(2, rowNum, gzze, wcfTable));
	            	 sheet.addCell(new Label(3, rowNum, yjje, wcfTable));
	            	 sheet.addCell(new Label(4, rowNum, wjje, wcfTable));
	            	 sheet.addCell(new Label(5, rowNum, zcgzsj, wcfTable));
	            	
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
