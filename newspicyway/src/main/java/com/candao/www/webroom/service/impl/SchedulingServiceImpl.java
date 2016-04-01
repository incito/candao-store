package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.SchedulingDao;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.webroom.service.SchedulingService;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 排班报表分析
 * @author zhouyao
 * @serialData 2015-12-29
 */
@Service
public class SchedulingServiceImpl implements SchedulingService {
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulingServiceImpl.class);
	@Autowired
	private SchedulingDao schedulingDao;
	
	public List<Map<String,Object>>  schedulingReport(Map<String, Object> params) {
		String branchid=PropertiesUtils.getValue("current_branch_id");
		params.put("pi_branchid", branchid);
		params.put("pi_sb",params.get("shiftid"));
		params.put("pi_ksrq",params.get("beginTime"));
		params.put("pi_jsrq", params.get("endTime"));
		params.put("pi_sjjg",params.get("dateinterval"));
		params.put("pi_week",params.get("week"));
		List<Map<String,Object>> listScheduling= schedulingDao.schedulingReport(params);
		return listScheduling;
	}
	
	/**
	 * 排班表导出
	 * @author weizhifang
	 * @since 2015-12-3
	 * @param request
	 * @param response
	 * @param schedulingReptList
	 */
	public void createExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> schedulingReptList,Map<String,Object> params){
		// 文件名称与路径  
        String fileName = "排班参考报表.xls";  
        String excelUrl = request.getSession().getServletContext().getRealPath("/");
        String realPath = excelUrl + fileName;
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
             WritableSheet sheet = wwb.createSheet("排班参考报表", 1);// 建立工作簿 
             //计算有多少日
             List<String> listDateinterval = new ArrayList<String>();
     		 for (int i = 0; i < schedulingReptList.size(); i++) {
     			 listDateinterval.add(schedulingReptList.get(i).get("datetimeStr").toString());
     		 }
     		 Iterator<String> it=listDateinterval.iterator();
     		 List<String> toHeavyResult = new ArrayList<String>();
     		 while(it.hasNext()){  
     			 String ateintervalStr=it.next();  
     			 if(toHeavyResult.contains(ateintervalStr)){  
     			     it.remove();  
     			 }else{  
     				 toHeavyResult.add(ateintervalStr);  
     			 }  
     	     }  
             //写表头  
     		 sheet.mergeCells(0, 0, toHeavyResult.size()*7, 0);
             sheet.setRowView(0, 1300);
             String title = setTabTitle("排班参考报表",params);
             jxl.write.Label labelTitle1 = new jxl.write.Label(0, 0, title);
             labelTitle1.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle1);// 放入工作簿  
             //写标题
             Map<String,List<String>> timeMap = new TreeMap<String,List<String>>();
             for(Map<String,Object> sch : schedulingReptList){
             	 String time = (String)sch.get("dateinterval");
             	 timeMap.put(time, toHeavyResult);
             }
             sheet.mergeCells(0, 1, 0, 2);
             sheet.setColumnView(0, 15);
             sheet.addCell(new Label(0,1,"时间段",wcfHead));
             //写入日期及标题
             int firstColumn = 1;
             int subCloumnNums = 1;
        	 for(int i=0;i<toHeavyResult.size();i++){
        		 String date = (String)toHeavyResult.get(i);
        		 if(date.equals("avg")){
        			 sheet.mergeCells(1, 1, 7, 1);
        			 sheet.addCell(new Label(1,1,"平均值",wcfHead));
        		 }else{
        			 firstColumn = firstColumn+7;
            		 sheet.mergeCells(firstColumn, 1, firstColumn+6, 1);
                	 sheet.addCell(new Label(firstColumn,1,date,wcfHead));
        		 }
        		 String subTitle [] = {"开台数","上客数","已点餐金额","已结账台数","结账金额","未结账台数","在台数"};
            	 for(int j=0;j<subTitle.length;j++){
            		 sheet.setColumnView(subCloumnNums, 15);
            		 sheet.addCell(new Label(subCloumnNums,2,subTitle[j],wcfHead));
            		 subCloumnNums++;
            	 }
        	 }
             //写入数据
             int cf = 3;
             for(Map.Entry<String, List<String>> entry : timeMap.entrySet()){
            	 int rf = 8;
            	 String time = entry.getKey();
            	 List<String> dm = (List<String>)timeMap.get(time);
            	 for(int i=0;i<dm.size();i++){
            		 String date = dm.get(i);
            		 for(int j=0;j<schedulingReptList.size();j++){
            			 String dateinterval = schedulingReptList.get(j).get("dateinterval") == null ? "" : schedulingReptList.get(j).get("dateinterval").toString();
            			 String datetimeStr = schedulingReptList.get(j).get("datetimeStr").toString(); //== null ? "" : schedulingReptList.get(j).get("datetimeStr").toString();
                		 if(datetimeStr.equals(date) && dateinterval.equals(time)){
                			 String notcheckNum = schedulingReptList.get(j).get("dateinterval") == null ? "" : schedulingReptList.get(j).get("notcheckNum").toString();
    	                     String openNum = schedulingReptList.get(j).get("openNum") == null ? "" : schedulingReptList.get(j).get("openNum").toString();
    	                     String intheNum = schedulingReptList.get(j).get("IntheNum") == null ? "" : schedulingReptList.get(j).get("IntheNum").toString();
    	                     String alreadycheckNum = schedulingReptList.get(i).get("alreadycheckNum") == null ? "" : schedulingReptList.get(j).get("alreadycheckNum").toString();
    	                     String checkamount = schedulingReptList.get(j).get("checkamount") == null ? "" : schedulingReptList.get(j).get("checkamount").toString();
    	                     String orderamount = schedulingReptList.get(j).get("orderamount") == null ? "" : schedulingReptList.get(j).get("orderamount").toString();
    	                     String guestNum = schedulingReptList.get(j).get("guestNum") == null ? "" : schedulingReptList.get(j).get("guestNum").toString();
    	                     sheet.addCell(new Label(0, cf, dateinterval, wcfTable));
    	                     if(date.equals("avg")){
                		    	 sheet.addCell(new Label(1, cf, openNum, wcfTable));
                		    	 sheet.addCell(new Label(2, cf, guestNum, wcfTable));
                		    	 sheet.addCell(new Label(3, cf, orderamount, wcfTable));
                		    	 sheet.addCell(new Label(4, cf, alreadycheckNum, wcfTable));
                		    	 sheet.addCell(new Label(5, cf, checkamount, wcfTable));
                		    	 sheet.addCell(new Label(6, cf, notcheckNum, wcfTable));
                		    	 sheet.addCell(new Label(7, cf, intheNum, wcfTable));
                		     }else{
	            		    	 sheet.addCell(new Label(rf++, cf, openNum, wcfTable));
	            		    	 sheet.addCell(new Label(rf++, cf, guestNum, wcfTable));
	            		    	 sheet.addCell(new Label(rf++, cf, orderamount, wcfTable));
	            		    	 sheet.addCell(new Label(rf++, cf, alreadycheckNum, wcfTable));
	            		    	 sheet.addCell(new Label(rf++, cf, checkamount, wcfTable));
	            		    	 sheet.addCell(new Label(rf++, cf, notcheckNum, wcfTable));
	            		    	 sheet.addCell(new Label(rf++, cf, intheNum, wcfTable));
                		     }
                		 }
                	 }
            	 }
            	 cf++;
             }
             wwb.write();
             wwb.close();
        }catch(Exception e){
        	logger.error(e.getMessage());
        	e.printStackTrace();
        }
        ExcelUtils.downloadExcel(request,response,fileName,realPath);
	}
        
    /**
	 * 构建excel表头
	 * @author weizhifang
	 * @since 2015-7-29
	 * @param sheetName
	 * @param params
	 * @return
	 */
	private String setTabTitle(String sheetName,Map<String,Object> params){
		String branchname = params.get("branchname") == null ? "" : params.get("branchname").toString();
		String shiftid = params.get("shiftid") == null ? "" : params.get("shiftid").toString();
		String beginTime = params.get("beginTime") == null ? "" : params.get("beginTime").toString();
		String endTime = params.get("endTime") == null ? "" : params.get("endTime").toString();
		String week = params.get("week") == null ? "" : params.get("week").toString();
		String dateinterval = params.get("dateinterval") == null ? "" : params.get("dateinterval").toString();
		String shiftname = "";
		if (shiftid.equals("0")) {
			shiftname = "午市";
		}else if (shiftid.equals("-1")) {
			shiftname = "全天";
		}else if (shiftid.equals("1")) {
			shiftname = "晚市";
		}
		String wn [] = week.split(",");
		StringBuffer weekName = new StringBuffer();
		for(int i=0;i<wn.length;i++){
			switch(wn[i]){
			    case "0":
			    	weekName.append(" 周一");
			    	break;
			    case "1":
			    	weekName.append(" 周二");
			    	break;
			    case "2":
			    	weekName.append(" 周三");
			    	break;
			    case "3":
			    	weekName.append(" 周四");
			    	break;
			    case "4":
			    	weekName.append(" 周五");
			    	break;
			    case "5":
			    	weekName.append(" 周六");
			    	break;
			    case "6":
			    	weekName.append(" 周日");
			    	break;
			    default:{
			    	weekName.append(" 全部");
			    }
			}
		}
		return sheetName+"\n门店名称:"+branchname+"   市别："+shiftname +"\n时间:"+beginTime+"——"+endTime+"\n周时间选择:"+weekName.toString()+"   时间间隔设置:"+dateinterval+"分钟";
	}
    	
}
	
	
