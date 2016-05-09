package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.candao.www.utils.DateTimeUtils;
import com.candao.www.utils.ExcelUtils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 导出详细数据统计表excel
 * @author weizhifang
 * @since 2015-8-2
 *
 */
@Service
public class ExportDataDetailExcelService {
	
	private static final Logger logger = LoggerFactory.getLogger(ExportDataDetailExcelService.class);

	/**
	 * 导出excel
	 * @author weizhifang
	 * @since 2015-8-2
	 * @param mapList
	 * @param req
	 * @param response
	 * @param params
	 */
	public void formatExcel(List<Map<String,Object>> mapList , HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) {
		// 文件名称与路径  
        String fileName = "详细数据统计表.xls";  
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
             WritableSheet sheet = wwb.createSheet("详细数据统计表", 1);// 建立工作簿 
             //查询天数
             String beginTime = params.get("beginTime").toString();
     		 String endTime = params.get("endTime").toString();
     		 int daynum = DateTimeUtils.getsubtractDaynum("yyyy-MM-dd", beginTime, endTime);
     		 //过滤日期字符串列表
     		 List<String> stringList = DateTimeUtils.getdateList(beginTime, daynum, "yyyy-MM-dd", "yyyy/MM/dd");
             sheet.mergeCells(0, 0, daynum+2, 0);
             sheet.setRowView(0, 1200);
             // 写表头  
             String title = setTabTitle("详细数据统计表",params);
             jxl.write.Label labelTitle1 = new jxl.write.Label(0, 0, title);
             labelTitle1.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle1);// 放入工作簿  
             sheet.addCell(new Label(0,1,"区域",wcfHead));
             sheet.addCell(new Label(1,1,"桌号/时间",wcfHead));
             sheet.setColumnView(0,18);
             sheet.setColumnView(1,18);
             for (int i = 2; i < daynum + 3; i++) {  //日期从第三个格开始赋值
            	 sheet.setColumnView(i,18);
            	 sheet.addCell(new Label(i,1,stringList.get(i-2),wcfHead));
             }
             //单元格赋值
             int rowNum1 = 1;
             for(int i=0;i<mapList.size();i++){
            	 rowNum1++;
            	 sheet.setColumnView(i,16);
            	 Map<String,Object> dataMap = mapList.get(i);
	             String areaname = dataMap.get("areaname") == null ? "" : dataMap.get("areaname").toString();
	             String tableid = dataMap.get("tableid") == null ? "" : dataMap.get("tableid").toString();
	             sheet.addCell(new Label(0, rowNum1, areaname, wcfTable));
	        	 sheet.addCell(new Label(1, rowNum1, tableid, wcfTable));
	        	 List<String> daylist = getDayList(beginTime,endTime);
	        	 int columnId = 2;
	             for (int j = 0; j < daylist.size(); j++) {
	            	 if (daylist.size() == 0) {
	     				break;
	     			 }
	            	 String day = daylist.get(j).toString();
	                 for(Entry<String, Object> entry : dataMap.entrySet()){
	                     String strkey = entry.getKey();
	                     String strval = entry.getValue().toString();
	                     if(strkey.equals(day)) {  //确认数据是否存在
	                    	 if(params.get("dataType").toString().equals("3")||params.get("dataType").toString().equals("4")) {    //人数
	                    		 sheet.addCell(new Label(columnId, rowNum1, strval.substring(0, strval.length()-2), wcfTable));
	                    	 }else{
	                    		 sheet.addCell(new Label(columnId, rowNum1, strval, wcfTable));
	                    	 }
	 	     			 }
	                 }
	            	 columnId++;
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
	 * 构建excel表头
	 * @author weizhifang
	 * @since 2015-7-29
	 * @param sheetName
	 * @param params
	 * @return
	 */
	private String setTabTitle(String sheetName,Map<String,Object> params){
		String branchname = params.get("branchname") == null ? "" : params.get("branchname").toString();
		String shiftid = params.get("shiftId") == null ? "" : params.get("shiftId").toString();
		String beginTime = params.get("beginTime") == null ? "" : params.get("beginTime").toString();
		String endTime = params.get("endTime") == null ? "" : params.get("endTime").toString();
		String dataType = params.get("dataType") == null ? "" : params.get("dataType").toString();
		String shiftname = "";
		if (shiftid.equals("0")) {
			shiftname = "午市";
		}else if (shiftid.equals("-1")) {
			shiftname = "全天";
		}else if (shiftid.equals("1")) {
			shiftname = "晚市";
		}
        String dataTypeName = "";
		if (dataType.equals("1")) {
			dataTypeName = "应收金额";
		}else if (dataType.equals("2")) {
			dataTypeName = "实收金额";
		}else if (dataType.equals("3")) {
			dataTypeName = "结算人数";
		}else if (dataType.equals("4")) {
			dataTypeName = "开台数";
		}
		return sheetName+"\n门店名称:"+branchname+"    查询类型："+dataTypeName+"\n市别："+shiftname +"   时间:"+beginTime+"——"+endTime;
	}
	
	/**
	 * 得到间隔天数
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDayList(String beginTime, String endTime) {
		//查询天数
		int daynum = DateTimeUtils.getsubtractDaynum("yyyy-MM-dd", beginTime, endTime) + 1;
		//过滤日期字符串列表
		List<String> stringList = DateTimeUtils.getdateList(beginTime, daynum, "yyyy-MM-dd", "yyyy/MM/dd");
		//时间赋值
		return stringList;
	}
}
