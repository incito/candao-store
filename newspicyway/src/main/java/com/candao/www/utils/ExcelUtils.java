package com.candao.www.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import com.candao.common.utils.DateUtils;

/**
 * Created by IntelliJ IDEA. User: johnson Date: 5/23/15 Time: 3:29 下午
 */
public class ExcelUtils {

	/**
	 * 复制excel行数
	 * 
	 * @param workbook
	 * @param worksheet
	 * @param sourceRowNum
	 * @param destinationRowNum
	 */
	public static void copyRow(HSSFWorkbook workbook, HSSFSheet worksheet,
			int sourceRowNum, int destinationRowNum) {
		// Get the source / new row
		HSSFRow newRow = worksheet.getRow(destinationRowNum);
		HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create
		// a new row
		if (newRow != null) {
			worksheet
					.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		} else {
			newRow = worksheet.createRow(destinationRowNum);
		}

		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
			HSSFCell oldCell = sourceRow.getCell(i);
			HSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			HSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new
		// row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(
						newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress
								.getFirstRow())), cellRangeAddress
								.getFirstColumn(), cellRangeAddress
								.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	/**
	 * 下载Excel
	 * 
	 * @author zhifang
	 * @since 2015-7-29
	 * @param response
	 * @param fileName
	 * @param realPath
	 */
	public static void downloadExcel(HttpServletRequest request,HttpServletResponse response, String fileName,
			String realPath) {
		try {
//			response.addHeader("Content-Disposition", new String(("attachment; filename=" + fileName).getBytes("GBK"), "ISO-8859-1"));
//			response.setContentType("application/vnd.ms-excel; charset=utf-8");
//			response.setCharacterEncoding("utf-8");
			
			response.setContentType("application/vnd.ms-excel");
			String filedi = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + filedi);
			
			OutputStream out = response.getOutputStream();
			InputStream in = new FileInputStream(realPath);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = in.read(buffer)) != -1) {
				out.write(buffer, 0, i);
			}
			in.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 定义jxl表头
	 * @author weizhifang
	 * @since 2015-8-3
	 * @return
	 */
	public static WritableCellFormat setWcfTitle() {
		// 表头定义格式:字体、下划线、斜体、 粗体、 颜色
		WritableFont wfTitle = new WritableFont(WritableFont.ARIAL, 12,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE); 
		// 单元格定义
		WritableCellFormat wcfTitle = new WritableCellFormat(wfTitle); 
		try {
			wcfTitle.setAlignment(jxl.format.Alignment.CENTRE);
			// 设置对齐方式
			wcfTitle.setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcfTitle;
	}
	
	/**
	 * 定义jxl标题
	 * @author weizhifang
	 * @since 2015-8-3
	 * @return
	 */
	public static WritableCellFormat setWcfHead() {
		// 表头定义格式:字体、下划线、斜体、 粗体、 颜色
		WritableFont wfHead = new WritableFont(WritableFont.ARIAL, 12,  
                WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE); 
		// 单元格定义
		WritableCellFormat wcfHead = new WritableCellFormat(wfHead);   
		try {
			wcfHead.setBackground(jxl.format.Colour.GRAY_25);  
	        wcfHead.setAlignment(jxl.format.Alignment.CENTRE); 
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcfHead;
	}
	
	/**
	 * 定义jxl内容
	 * @author weizhifang
	 * @since 2015-8-3
	 * @return
	 */
	public static WritableCellFormat setWcfTable() {
		// 表头定义格式:字体、下划线、斜体、 粗体、 颜色
		WritableFont wfTable = new WritableFont(WritableFont.ARIAL, 10,  
                WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
		// 单元格定义
		WritableCellFormat wcfTable = new WritableCellFormat(wfTable);   
		try {
			wcfTable.setAlignment(jxl.format.Alignment.CENTRE);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcfTable;
	}
	
	/**
	 * 构建excel表头
	 * @author weizhifang
	 * @since 2015-7-29
	 * @param sheetName
	 * @param params
	 * @return
	 */
	public static String setTabTitle(String sheetName,Map<String,Object> params){
		String branchname = params.containsKey("branchname")?params.get("branchname").toString():"";
		String beginTime = params.get("beginTime").toString();
		String endTime = params.get("endTime").toString();
		String searchType = params.get("searchType").toString();
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
		return sheetName+"\n门店名称:"+branchname+"\n时间:"+beginTime+"——"+endTime;
	}
	
	/**
	 * 构建excel表头
	 * @author zhouyao
	 * @since 2015-11-25
	 * @param sheetName
	 * @param params
	 * @return
	 */
	public static String setTabTitleToBusiness(String sheetName,Map<?, ?> params){
		String branchName = null;
		String beginTime = null;
		String endTime = null;
		String type = null;
		
		if(params.get("branchName")!=null&&!params.get("branchName").equals("")){
			branchName = params.get("branchName").toString();
		}else{
			branchName="全部门店";
		}
        if(params.get("type")!=null&&!params.get("type").equals("")){
        	type = params.get("type").toString();
		}
        if(params.get("beginTime")!=null&&!params.get("beginTime").equals("")){
        	beginTime = params.get("beginTime").toString();
		}
        if(params.get("endTime")!=null&&!params.get("endTime").equals("")){
        	endTime = params.get("endTime").toString();
		}

		String typename = "";
		if (type.equals("0")) {
			typename = "午市";
		}else if (type.equals("-1")) {
			typename = "全天";
		}else if (type.equals("1")) {
			typename = "晚市";
		}
		return sheetName+"\n门店名称:"+branchName+"   市别："+typename+"\n时间:"+beginTime+"——"+endTime;
	}
	
	
}
