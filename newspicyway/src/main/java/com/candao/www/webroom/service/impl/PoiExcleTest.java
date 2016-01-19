package com.candao.www.webroom.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.candao.www.data.model.TBranchBusinessInfo;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.webroom.model.Base_CouponsRept;
import com.candao.www.webroom.model.TjObj;

public class PoiExcleTest {
	public static List<TjObj> getAllList(List<Map<String, Object>> list) {
		List<TjObj> lists = new ArrayList<TjObj>();
		for (int i = 0; i < list.size(); i++) {
			TjObj tj = new TjObj();
			TjObj tjs = new TjObj();
			if (i == 0) {
				tj.setBaseCom(list.get(i).get("title").toString());
				tjs.setSelfcom(list.get(i).get("key").toString());
				tjs.setValue(list.get(i).get("value").toString());
				lists.add(tj);
				lists.add(tjs);
			} else{
				if (list.get(i).get("title").toString().equals(list.get(i-1).get("title").toString())) {
					tjs.setSelfcom(list.get(i).get("key").toString());
					tjs.setValue(list.get(i).get("value").toString());
					lists.add(tjs);
				} else {
					tj.setBaseCom(list.get(i).get("title").toString());
					tjs.setSelfcom(list.get(i).get("key").toString());
					tjs.setValue(list.get(i).get("value").toString());
					lists.add(tj);
					lists.add(tjs);

				}
			}
		}
		return lists;
	}

	public static void exportExcle(List<Map<String, Object>> lists, Map<String, Object> params, String filedisplay, HttpServletRequest req, HttpServletResponse response) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		response.setContentType("application/vnd.ms-excel");
		String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");

		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
//         HSSFDataFormat format= wb.createDataFormat(); //--->单元格内容格式    
		 /*sheet.setColumnWidth((short)3, 20* 256);    //---》设置单元格宽度，因为一个单元格宽度定了那么下面多有的单元格高度都确定了所以这个方法是sheet的
		 sheet.setColumnWidth((short)4, 20* 256);    //--->第一个参数是指哪个单元格，第二个参数是单元格的宽度    */
//       sheet.setDefaultRowHeight((short)300);    // ---->有得时候你想设置统一单元格的高度，就用这个方法    
		sheet.setDefaultColumnWidth((short) 60);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		//-------------------------------------------------------------------------------------------------
		//设置导出的excel标题
		String title = ExcelUtils.setTabTitle("营业数据明细表", params);
		/*String shiftid = null;
		if(params.get("shiftid")!=null && params.get("shiftid")!=""){
			if(("1").equals(params.get("shiftid").toString())){
				shiftid = "晚市";
			}else if(("0").equals(params.get("shiftid").toString())){
				shiftid = "午市";
			}else{
				shiftid = "全天";
			}
		}
		String title = params.get("names").toString() + "\r\n"
				+ "门店名称：" + params.get("shopname").toString() + "\r\n"
				+ "市别："+shiftid +" 时间：" + params.get("dateShow").toString();*/
		HSSFRow firstRow = sheet.createRow(0);//下标为0开始
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (1)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		//------------------------------------------------------------------------------------------------
		HSSFRow rows = sheet.createRow(1);
	  
		//-------------------------------------------------------------------------------------------------
		
		List<TjObj> list = getAllList(lists);
		for (int i = 0; i < list.size(); i++) {
			   HSSFRow row = sheet.createRow(i + 2);
				if (list.get(i).getBaseCom() != null) {
					sheet.addMergedRegion(new Region(i+2, (short) (0), i+2, (short) (1)));
					HSSFCell cell = row.createCell((short) (0));
					cell.setCellValue(list.get(i).getBaseCom().toString());
					cell.setCellStyle(style);
				} else {
						if (list.get(i).getSelfcom()!= null && !list.get(i).getSelfcom().equals("")) {
							row.createCell((short) (0)).setCellValue(list.get(i).getSelfcom().toString());
						}
						if(list.get(i).getValue()!= null && !list.get(i).getValue().equals("")){
							row.createCell((short) (1)).setCellValue(list.get(i).getValue().toString());
						}
				}
			
		}
		//    将文件存到指定位置
 	         /*  FileOutputStream fout = new FileOutputStream("D:/"+filedisplay+".xls");  
 	            wb.write(fout);  
 	            fout.close();  	 */

		// 设置响应头和保存文件名
		OutputStream fout = response.getOutputStream();
		wb.write(fout);
		fout.flush();
		fout.close();
		wb.close();
	}
	public static void exportExcleA(List<Map<String, Object>> list, Map<String, Object> params, String filedisplay, HttpServletRequest req, HttpServletResponse response) throws Exception {


		String type = "0";//判断状态  0 是品项列表 1为品型列表

		if (StringUtils.isNotBlank(params.get("itemId").toString())) {  //判断查询品项列表还是品型列表
			type = "1";
		}


		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		response.setContentType("application/vnd.ms-excel");
		String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
		sheet.setDefaultColumnWidth((short) 30);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		//-------------------------------------------------------------------------------------------------
		//设置导出的excel标题
		String shiftid = null;
		if(params.get("shiftid")!=null&&params.get("shiftid")!=""){
			if(("1").equals(params.get("shiftid").toString())){
				shiftid = "晚市";
			}else if(("0").equals(params.get("shiftid").toString())){
				shiftid = "午市";
			}else{
				shiftid = "全天";
			}
		}
		String title = params.get("names").toString() + "\r\n"
				+ "门店名称：" + params.get("shopname").toString() + "\r\n"
				+ "市别:"+shiftid+" 时间：" + params.get("dateShow").toString();
		HSSFRow firstRow = sheet.createRow(0);
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (6)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		//------------------------------------------------------------------------------------------------
		HSSFRow rows = sheet.createRow(1);
		HSSFCell cell0 = rows.createCell((short) (0));
		HSSFCell cell1 = rows.createCell((short) (1));
		HSSFCell cell2 = rows.createCell((short) (2));
		HSSFCell cell3 = rows.createCell((short) (3));
		HSSFCell cell4 = rows.createCell((short) (4));
		HSSFCell cell5 = rows.createCell((short) (5));
		HSSFCell cell6 = rows.createCell((short) (6));
		cell0.setCellValue("品类");
		cell1.setCellValue("品项编号");
		cell2.setCellValue("品项名称");
		cell3.setCellValue("单价");
		cell4.setCellValue("单位");
		cell5.setCellValue("数量");
		cell6.setCellValue("份额");
		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		cell3.setCellStyle(style);
		cell4.setCellStyle(style);
		cell5.setCellStyle(style);
		cell6.setCellStyle(style);
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			if (type.equals("1")) {
				getpinxingROW(list, i, row);
			} else {
				getpinleiROW(list, i, row);
			}


		}
		// 设置响应头和保存文件名
		OutputStream fout = response.getOutputStream();
		wb.write(fout);
		fout.flush();
		fout.close();
		wb.close();
	}

	private static void getpinxingROW(List<Map<String, Object>> list, int i, HSSFRow row) {
		if (StringUtils.isNotBlank(list.get(i).get("itemDesc").toString())) {
			row.createCell((short) (0)).setCellValue(list.get(i).get("itemDesc").toString());
		} else {
			row.createCell((short) (0)).setCellValue("");
		}
		//row.createCell((short)(1)).setCellValue(list.get(i).get("childType").toString());
		if (StringUtils.isNotBlank(list.get(i).get("tbid").toString())) {
			row.createCell((short) (1)).setCellValue(list.get(i).get("tbid").toString());
		} else {
			row.createCell((short) (1)).setCellValue("");
		}
		if (StringUtils.isNotBlank(list.get(i).get("title").toString())) {
			row.createCell((short) (2)).setCellValue(list.get(i).get("title").toString());
		} else {
			row.createCell((short) (2)).setCellValue("");
		}
		//row.createCell((short)(4)).setCellValue(list.get(i).get("couponAfter").toString());
		//row.createCell((short)(5)).setCellValue(list.get(i).get("couponBefore").toString());
		if (StringUtils.isNotBlank(list.get(i).get("price").toString())) {
			row.createCell((short) (3)).setCellValue(list.get(i).get("price").toString());
		} else {
			row.createCell((short) (3)).setCellValue("");
		}
		if (StringUtils.isNotBlank(list.get(i).get("unit").toString())) {
			row.createCell((short) (4)).setCellValue(list.get(i).get("unit").toString());
		} else {
			row.createCell((short) (4)).setCellValue("");
		}
		if (StringUtils.isNotBlank(list.get(i).get("number").toString())) {
			String str = list.get(i).get("number").toString();
			String[] strs = str.split("[.]");
			row.createCell((short) (5)).setCellValue(strs[0]);
		} else {
			row.createCell((short) (5)).setCellValue("");
		}

		if (!list.get(i).get("share").toString().equals("*")) {
			row.createCell((short) (6)).setCellValue(list.get(i).get("share").toString());
		} else {
			row.createCell((short) (6)).setCellValue("");
		}
	}

	private static void getpinleiROW(List<Map<String, Object>> list, int i, HSSFRow row) {
		if (StringUtils.isNotBlank(list.get(i).get("itemDesc").toString())) {
			row.createCell((short) (0)).setCellValue(list.get(i).get("itemDesc").toString());
		} else {
			row.createCell((short) (0)).setCellValue("");
		}
		if (StringUtils.isNotBlank(list.get(i).get("dishid").toString())) {
			row.createCell((short) (1)).setCellValue(list.get(i).get("dishid").toString());
		} else {
			row.createCell((short) (1)).setCellValue("");
		}
		if (StringUtils.isNotBlank(list.get(i).get("title").toString())) {
			row.createCell((short) (2)).setCellValue(list.get(i).get("title").toString());
		} else {
			row.createCell((short) (2)).setCellValue("");
		}
		if (StringUtils.isNotBlank(list.get(i).get("price").toString())) {
			row.createCell((short) (3)).setCellValue(list.get(i).get("price").toString());
		} else {
			row.createCell((short) (3)).setCellValue("");
		}


		if (StringUtils.isNotBlank(list.get(i).get("unit").toString())) {
			row.createCell((short) (4)).setCellValue(list.get(i).get("unit").toString());
		} else {
			row.createCell((short) (4)).setCellValue("");
		}
		if (StringUtils.isNotBlank(list.get(i).get("number").toString())) {
			String str = list.get(i).get("number").toString();
			String[] strs = str.split("[.]");
			row.createCell((short) (5)).setCellValue(strs[0]);
		} else {
			row.createCell((short) (5)).setCellValue("");
		}


		row.createCell((short) (6)).setCellValue("");
	}

	public static void exportExcleB(List<Base_CouponsRept> baseList, Map<String, Object> params, String filedisplay, HttpServletRequest req, HttpServletResponse response) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		response.setContentType("application/vnd.ms-excel");
		String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
		sheet.setDefaultColumnWidth((short) 30);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		//-------------------------------------------------------------------------------------------------
		//样式2
		HSSFCellStyle stylerow = wb.createCellStyle(); // 样式对象
		stylerow.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		stylerow.setFillPattern(CellStyle.SOLID_FOREGROUND);
		stylerow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		stylerow.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font fontrow = wb.createFont();
		//设置字体样式
		fontrow.setFontHeightInPoints((short) 11);   //--->设置字体大小
		fontrow.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		fontrow.setBold(true);//---》是否加粗
		stylerow.setFont(fontrow);
		
		
		//样式3
		HSSFCellStyle stylerowFontCenter = wb.createCellStyle(); // 样式对象
		stylerowFontCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 字体居中
		//-------------------------------------------------------------------------------------------------
		//设置导出的excel标题
		String shiftid = null;
		if(params.get("shiftid")!=null&&params.get("shiftid")!=""){
			if(("1").equals(params.get("shiftid").toString())){
				shiftid = "晚市";
			}else if(("0").equals(params.get("shiftid").toString())){
				shiftid = "午市";
			}else{
				shiftid = "全天";
		    }
		}else{
			shiftid = "全天";
		}
		String title = params.get("names").toString() + "\r\n"+ "门店名称：" + (params.containsKey("branchname")?params.get("branchname").toString():"") + "\r\n"+ "市别："+shiftid+" 时间：" + params.get("dateShow").toString();
		HSSFRow firstRow = sheet.createRow(0);
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (8)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		//------------------------------------------------------------------------------------------------
		HSSFRow rows = sheet.createRow(1);
		HSSFCell cell0 = rows.createCell((short) (0));
		HSSFCell cell1 = rows.createCell((short) (1));
		HSSFCell cell2 = rows.createCell((short) (2));
		HSSFCell cell3 = rows.createCell((short) (3));
		HSSFCell cell4 = rows.createCell((short) (4));
		HSSFCell cell5 = rows.createCell((short) (5));
		HSSFCell cell6 = rows.createCell((short) (6));
		HSSFCell cell7 = rows.createCell((short) (7));
		HSSFCell cell8 = rows.createCell((short) (8));

		cell0.setCellValue("活动名称");
		cell1.setCellValue("活动类型 ");
		cell2.setCellValue("结算方式");
		cell3.setCellValue("笔数 ");
		cell4.setCellValue("发生金额 ");
		cell5.setCellValue("拉动应收");
		cell6.setCellValue("拉动实收 ");
		cell7.setCellValue("单数 ");
		cell8.setCellValue("人均 ");
		
		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		cell3.setCellStyle(style);
		cell4.setCellStyle(style);
		cell5.setCellStyle(style);
		cell6.setCellStyle(style);
		cell7.setCellStyle(style);
		cell8.setCellStyle(style);

		for (int i = 0; i < baseList.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			//活动名称==发生时间
			if (baseList.get(i).getCouponsname() == null && baseList.get(i).getCouponsname().equals("")) {
			        row.createCell((short) (0)).setCellValue("");
			} else {
				row.createCell((short) (0)).setCellValue(baseList.get(i).getCouponsname().toString());
				row.getCell(0).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getCouponsname().equals("发生时间")){
				     row.getCell(0).setCellStyle(stylerow);
				}
			}
			//活动类型==订单号
			if (baseList.get(i).getTypeName().equals("null") && baseList.get(i).getTypeName().equals("")) {
				row.createCell((short) (1)).setCellValue("");
			} else {
				row.createCell((short) (1)).setCellValue(baseList.get(i).getTypeName().toString());
				row.getCell(1).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getTypeName().equals("订单号")){
					row.getCell(1).setCellStyle(stylerow);
				}
			}
            //结算方式==结算金额
			if (baseList.get(i).getPaytype().equals("null") && baseList.get(i).getPaytype().equals("")) {
				row.createCell((short) (2)).setCellValue("");
			} else {
				row.createCell((short) (2)).setCellValue(baseList.get(i).getPaytype().toString());
				row.getCell(2).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getPaytype().equals("结算金额")){
					row.getCell(2).setCellStyle(stylerow);
				}
			}
            //笔数==发生笔数
			if (baseList.get(i).getNum().equals("null")&& baseList.get(i).getNum().equals("")) {
				row.createCell((short) (3)).setCellValue("");
			} else {
				row.createCell((short) (3)).setCellValue(baseList.get(i).getNum().toString());
				row.getCell(3).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getNum().equals("发生笔数")){
					row.getCell(3).setCellStyle(stylerow);
				}
			}
            //结算金额==发生金额
			if (baseList.get(i).getTotal().equals("null") && baseList.get(i).getTotal().equals("")) {
				row.createCell((short) (4)).setCellValue("");
			} else {
				row.createCell((short) (4)).setCellValue(baseList.get(i).getTotal().toString());
				row.getCell(4).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getTotal().equals("发生金额")){
					row.getCell(4).setCellStyle(stylerow);
				}
			}
            //拉松应收==拉动应收
			if (baseList.get(i).getYinshou().equals("null") &&baseList.get(i).getYinshou().equals("")) {
				row.createCell((short) (5)).setCellValue("");
			} else {
				row.createCell((short) (5)).setCellValue(baseList.get(i).getYinshou().toString());
				row.getCell(5).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getYinshou().equals("拉动应收")){
					row.getCell(5).setCellStyle(stylerow);
				}
			}
			//拉动实收==拉动实收
			if (baseList.get(i).getShishou().equals("null") && baseList.get(i).getShishou().equals("") ) {
				row.createCell((short) (6)).setCellValue("");
			} else {
				row.createCell((short) (6)).setCellValue(baseList.get(i).getShishou().toString());
				row.getCell(6).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getShishou().equals("拉动实收")){
					row.getCell(6).setCellStyle(stylerow);
				}
			}
			
			//单数
			if (baseList.get(i).getSingular().equals("null") && baseList.get(i).getSingular().equals("") ) {
				row.createCell((short) (7)).setCellValue("");
			} else {
				row.createCell((short) (7)).setCellValue(baseList.get(i).getSingular().toString());
				row.getCell(7).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getSingular().equals("title")){
					row.createCell((short) (7)).setCellValue("");
					row.getCell(7).setCellStyle(stylerow);
				}
			}
			
			//人均
			if (baseList.get(i).getPerCapita().equals("null") && baseList.get(i).getPerCapita().equals("") ) {
				row.createCell((short) (8)).setCellValue("");
			} else {
				row.createCell((short) (8)).setCellValue(baseList.get(i).getPerCapita().toString());
				row.getCell(8).setCellStyle(stylerowFontCenter);
				if(baseList.get(i).getPerCapita().equals("title")){
					row.createCell((short) (8)).setCellValue("");
					row.getCell(8).setCellStyle(stylerow);
				}
			}
		}
		// 设置响应头和保存文件名
		OutputStream fout = response.getOutputStream();
		wb.write(fout);
		fout.flush();
		fout.close();
		wb.close();
	}

	public static void exportExcleC(List<Map<String, Object>> list, Map<String, Object> params, String filedisplay, HttpServletRequest req, HttpServletResponse response) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		response.setContentType("application/vnd.ms-e"
				+ "xcel");
		String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
		sheet.setDefaultColumnWidth((short) 30);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		//-------------------------------------------------------------------------------------------------
		
		String title = ExcelUtils.setTabTitle("结算方式明细表", params);
		
		HSSFRow firstRow = sheet.createRow(0);
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (2)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		//------------------------------------------------------------------------------------------------
		HSSFRow rows = sheet.createRow(1);
	
		HSSFCell cell0 = rows.createCell((short) (0));
		
		HSSFCell cell1 = rows.createCell((short) (1));
		HSSFCell cell2 = rows.createCell((short) (2));
		
		cell0.setCellValue("结算方式");
		cell1.setCellValue("笔数");
		cell2.setCellValue("发生金额");
		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			row.createCell((short) (0)).setCellValue(list.get(i).get("payway").toString());
			row.createCell((short) (1)).setCellValue(list.get(i).get("nums").toString());
			row.createCell((short) (2)).setCellValue(list.get(i).get("prices").toString());
		}
		// 设置响应头和保存文件名
		OutputStream fout = response.getOutputStream();
		wb.write(fout);
		fout.flush();
		fout.close();
		wb.close();
	}

	public static void exportExcleD(List<Map<String, Object>> list, Map<String, Object> params, String filedisplay, HttpServletRequest req, HttpServletResponse response) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		response.setContentType("application/vnd.ms-excel");
		String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
		sheet.setDefaultColumnWidth((short) 30);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		//-------------------------------------------------------------------------------------------------
		//设置导出的excel标题
		String title = params.get("names").toString() + "\r\n"
				+ "门店名称：" + params.get("shopname").toString() + "\r\n"
				+ "时间：" + params.get("dateShow").toString();
		HSSFRow firstRow = sheet.createRow(0);
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (5)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		//------------------------------------------------------------------------------------------------

		HSSFRow rows = sheet.createRow(1);
		HSSFCell cell0 = rows.createCell((short) (0));
		HSSFCell cell1 = rows.createCell((short) (1));
		HSSFCell cell2 = rows.createCell((short) (2));
		HSSFCell cell3 = rows.createCell((short) (3));
		HSSFCell cell4 = rows.createCell((short) (4));
		HSSFCell cell5 = rows.createCell((short) (5));
		HSSFCell cell6 = rows.createCell((short) (6));
		HSSFCell cell7 = rows.createCell((short) (7));
		HSSFCell cell8 = rows.createCell((short) (8));
		HSSFCell cell9 = rows.createCell((short) (9));
		HSSFCell cell10 = rows.createCell((short) (10));
		HSSFCell cell11 = rows.createCell((short) (11));
		HSSFCell cell12 = rows.createCell((short) (12));
		HSSFCell cell13 = rows.createCell((short) (13));
		HSSFCell cell14 = rows.createCell((short) (14));
		HSSFCell cell15 = rows.createCell((short) (15));
		HSSFCell cell16 = rows.createCell((short) (16));
		HSSFCell cell17 = rows.createCell((short) (17));


		cell0.setCellValue("日期");
		cell1.setCellValue("主营业应收");
		cell2.setCellValue("主营业实收");
		cell3.setCellValue("刷卡");
		cell4.setCellValue("会员卡");
		cell5.setCellValue("挂账");
		cell6.setCellValue("现金");
		cell7.setCellValue("优免");
		cell8.setCellValue("抹零");
		cell9.setCellValue("会员积分消费");
		cell10.setCellValue("雅座优惠券");
		cell11.setCellValue("挂帐2");
		cell12.setCellValue("会员储值");
		cell13.setCellValue("优惠券");
		cell14.setCellValue("折扣");
		cell15.setCellValue("会员卡增送");
		cell16.setCellValue("挂账调整");
		cell17.setCellValue("会员卡销售");


		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		cell3.setCellStyle(style);
		cell4.setCellStyle(style);
		cell5.setCellStyle(style);
		cell6.setCellStyle(style);
		cell7.setCellStyle(style);
		cell8.setCellStyle(style);
		cell9.setCellStyle(style);
		cell10.setCellStyle(style);
		cell11.setCellStyle(style);
		cell12.setCellStyle(style);
		cell13.setCellStyle(style);
		cell14.setCellStyle(style);
		cell15.setCellStyle(style);
		cell16.setCellStyle(style);
		cell17.setCellStyle(style);


		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			row.createCell((short) (0)).setCellValue(list.get(i).get("opendate").toString());
			row.createCell((short) (1)).setCellValue(list.get(i).get("mainIncome").toString());
			row.createCell((short) (2)).setCellValue(list.get(i).get("mainIncome").toString());
			row.createCell((short) (3)).setCellValue(list.get(i).get("cardIncome").toString());
			row.createCell((short) (4)).setCellValue(list.get(i).get("vipIncome").toString());
			row.createCell((short) (5)).setCellValue(list.get(i).get("debtIncome").toString());
			row.createCell((short) (6)).setCellValue(list.get(i).get("cashIncome").toString());
			row.createCell((short) (7)).setCellValue(list.get(i).get("free").toString());
			row.createCell((short) (8)).setCellValue(list.get(i).get("maling").toString());
			row.createCell((short) (9)).setCellValue(list.get(i).get("vipIntegral").toString());
			row.createCell((short) (10)).setCellValue(list.get(i).get("yazuoCoupon").toString());
			row.createCell((short) (11)).setCellValue(list.get(i).get("debtIncome2").toString());
			row.createCell((short) (12)).setCellValue(list.get(i).get("vipValue").toString());
			row.createCell((short) (13)).setCellValue(list.get(i).get("coupon").toString());
			row.createCell((short) (14)).setCellValue(list.get(i).get("discount").toString());
			row.createCell((short) (15)).setCellValue(list.get(i).get("vipGive").toString());
			row.createCell((short) (16)).setCellValue(list.get(i).get("debtAdjustment").toString());
			row.createCell((short) (17)).setCellValue(list.get(i).get("vipSales").toString());

		}
		// 设置响应头和保存文件名
		OutputStream fout = response.getOutputStream();
		wb.write(fout);
		fout.flush();
		fout.close();
		wb.close();
	}
	/**
	 * 
	 * 生成excel的公共方法
	 * 
	 * 
	 */
	public static HSSFWorkbook createExcel(List<Map<String, String>> list, Map<String, String> params, String filedisplay,String title,String[] clounNames,String[] keys) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
		sheet.setDefaultColumnWidth((short) 30);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		
		//样式3
		HSSFCellStyle stylerowFontCenter = wb.createCellStyle(); // 样式对象
		stylerowFontCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 字体居中
		//-------------------------------------------------------------------------------------------------

		HSSFRow firstRow = sheet.createRow(0);
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (clounNames.length-1)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		
		//------------------------------------------------------------------------------------------------
		HSSFRow rows = sheet.createRow(1);

		for(int i =0;i<clounNames.length;i++){
			HSSFCell celli = rows.createCell((short) (i));
			celli.setCellValue(clounNames[i]);
			celli.setCellStyle(style);
		}

		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			for(int j =0;j<keys.length;j++){
				row.createCell((short) (j)).setCellValue(list.get(i).containsKey(keys[j])?list.get(i).get(keys[j]).toString():"");
				row.getCell(j).setCellStyle(stylerowFontCenter);
			}
		}
		return wb;
	}
	
	/**
	 * 导出门店营业报表(门店总)
	 * @param resultList
	 * @param params
	 * @param filedisplay
	 * @param req
	 * @param response
	 * @throws Exception
	 */
	public static void exportExcleInfos (List<TBranchBusinessInfo> resultList, Map<String, Object> params, String filedisplay, HttpServletRequest req, HttpServletResponse response) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		response.setContentType("application/vnd.ms-e"
				+ "xcel");
		String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
		sheet.setDefaultColumnWidth((short) 30);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		
		//样式3
		HSSFCellStyle stylerowFontCenter = wb.createCellStyle(); // 样式对象
		stylerowFontCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 字体居中
		//-------------------------------------------------------------------------------------------------

		String title = ExcelUtils.setTabTitleToBusiness("营业报表", params);

		HSSFRow firstRow = sheet.createRow(0);
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (15)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		
		//------------------------------------------------------------------------------------------------
		HSSFRow rows = sheet.createRow(1);

		HSSFCell cell0 = rows.createCell((short) (0));
		HSSFCell cell1 = rows.createCell((short) (1));
		HSSFCell cell2 = rows.createCell((short) (2));
		HSSFCell cell3 = rows.createCell((short) (3));
		HSSFCell cell4 = rows.createCell((short) (4));
		HSSFCell cell5 = rows.createCell((short) (5));
		HSSFCell cell6 = rows.createCell((short) (6));
		HSSFCell cell7 = rows.createCell((short) (7));
		HSSFCell cell8 = rows.createCell((short) (8));
		HSSFCell cell9 = rows.createCell((short) (9));
		HSSFCell cell10 = rows.createCell((short) (10));
		HSSFCell cell11 = rows.createCell((short) (11));
		HSSFCell cell12 = rows.createCell((short) (12));
		HSSFCell cell13 = rows.createCell((short) (13));
		HSSFCell cell14 = rows.createCell((short) (14));
		HSSFCell cell15 = rows.createCell((short) (15));



		cell0.setCellValue("门店名称");
		cell1.setCellValue("门店编号");
		cell2.setCellValue("JDE编码");
		cell3.setCellValue("应收");
		cell4.setCellValue("实收");
		cell5.setCellValue("折扣");
		cell6.setCellValue("现金");
		cell7.setCellValue("刷工行卡");
		cell8.setCellValue("刷他行卡");
		cell9.setCellValue("微信");
		cell10.setCellValue("支付宝");
		cell11.setCellValue("挂账");
		cell12.setCellValue("会员卡消费净值");
		cell13.setCellValue("会员卡消费虚增");
		cell14.setCellValue("会员积分消费");
		cell15.setCellValue("会员优惠券");

		
		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		cell3.setCellStyle(style);
		cell4.setCellStyle(style);
		cell5.setCellStyle(style);
		cell6.setCellStyle(style);
		cell7.setCellStyle(style);
		cell8.setCellStyle(style);
		cell9.setCellStyle(style);
		cell10.setCellStyle(style);
		cell11.setCellStyle(style);
		cell12.setCellStyle(style);
		cell13.setCellStyle(style);
		cell14.setCellStyle(style);
		cell15.setCellStyle(style);


		for (int i = 0; i < resultList.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			row.createCell((short) (0)).setCellValue(resultList.get(i).getBranchName());
			row.getCell(0).setCellStyle(stylerowFontCenter);
			row.createCell((short) (1)).setCellValue(resultList.get(i).getBranchId());
			row.getCell(1).setCellStyle(stylerowFontCenter);
			row.createCell((short) (2)).setCellValue(resultList.get(i).getJdeNo());
			row.getCell(2).setCellStyle(stylerowFontCenter);
			row.createCell((short) (3)).setCellValue(resultList.get(i).getShouldamount().toString());
			row.getCell(3).setCellStyle(stylerowFontCenter);
			row.createCell((short) (4)).setCellValue(resultList.get(i).getPaidinamount().toString());
			row.getCell(4).setCellStyle(stylerowFontCenter);
			row.createCell((short) (5)).setCellValue(resultList.get(i).getDiscountamount().toString());
			row.getCell(5).setCellStyle(stylerowFontCenter);
			row.createCell((short) (6)).setCellValue(resultList.get(i).getCash().toString());
			row.getCell(6).setCellStyle(stylerowFontCenter);
			row.createCell((short) (7)).setCellValue(resultList.get(i).getCard().toString());
			row.getCell(7).setCellStyle(stylerowFontCenter);
			row.createCell((short) (8)).setCellValue(resultList.get(i).getOthercard().toString());
			row.getCell(8).setCellStyle(stylerowFontCenter);
			row.createCell((short) (9)).setCellValue(resultList.get(i).getWeixin().toString());
			row.getCell(9).setCellStyle(stylerowFontCenter);
			row.createCell((short) (10)).setCellValue(resultList.get(i).getZhifubao().toString());
			row.getCell(10).setCellStyle(stylerowFontCenter);
			row.createCell((short) (11)).setCellValue(resultList.get(i).getCredit().toString());
			row.getCell(11).setCellStyle(stylerowFontCenter);
			row.createCell((short) (12)).setCellValue(resultList.get(i).getMerbervaluenet().toString());
			row.getCell(12).setCellStyle(stylerowFontCenter);
			row.createCell((short) (13)).setCellValue(resultList.get(i).getMebervalueadd().toString());
			row.getCell(13).setCellStyle(stylerowFontCenter);
			row.createCell((short) (14)).setCellValue(resultList.get(i).getIntegralconsum().toString());
			row.getCell(14).setCellStyle(stylerowFontCenter);
			row.createCell((short) (15)).setCellValue(resultList.get(i).getMeberTicket().toString());
			row.getCell(15).setCellStyle(stylerowFontCenter);
		}
		// 设置响应头和保存文件名
		OutputStream fout = response.getOutputStream();
		wb.write(fout);
		fout.flush();
		fout.close();
		wb.close();
	}
	/**
	 * 导出门店营业报表(门店明细)
	 * @param list
	 * @param params
	 * @param filedisplay
	 * @param req
	 * @param response
	 * @throws Exception
	 */
	public static void exportExcleInfosDetail (List<TBranchBusinessInfo> list, Map<String, Object> params, String filedisplay, HttpServletRequest req, HttpServletResponse response) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		response.setContentType("application/vnd.ms-e"
				+ "xcel");
		String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
		sheet.setDefaultColumnWidth((short) 30);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		
		//样式3
		HSSFCellStyle stylerowFontCenter = wb.createCellStyle(); // 样式对象
		stylerowFontCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 字体居中
		//-------------------------------------------------------------------------------------------------
		
		String title = ExcelUtils.setTabTitleToBusiness("营业报表", params);

		HSSFRow firstRow = sheet.createRow(0);
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (14)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		
		//------------------------------------------------------------------------------------------------
		HSSFRow rows = sheet.createRow(1);

		HSSFCell cell0 = rows.createCell((short) (0));
		HSSFCell cell1 = rows.createCell((short) (1));
		HSSFCell cell2 = rows.createCell((short) (2));
		HSSFCell cell3 = rows.createCell((short) (3));
		HSSFCell cell4 = rows.createCell((short) (4));
		HSSFCell cell5 = rows.createCell((short) (5));
		HSSFCell cell6 = rows.createCell((short) (6));
		HSSFCell cell7 = rows.createCell((short) (7));
		HSSFCell cell8 = rows.createCell((short) (8));
		HSSFCell cell9 = rows.createCell((short) (9));
		HSSFCell cell10 = rows.createCell((short) (10));
		HSSFCell cell11 = rows.createCell((short) (11));
		HSSFCell cell12 = rows.createCell((short) (12));
		HSSFCell cell13 = rows.createCell((short) (13));
		HSSFCell cell14 = rows.createCell((short) (14));


		cell0.setCellValue("时间");
		cell1.setCellValue("门店名称");
		cell2.setCellValue("应收");
		cell3.setCellValue("实收");
		cell4.setCellValue("折扣");
		cell5.setCellValue("现金");
		cell6.setCellValue("刷工行卡");
		cell7.setCellValue("刷他行卡");
		cell8.setCellValue("微信");
		cell9.setCellValue("支付宝");
		cell10.setCellValue("挂账");
		cell11.setCellValue("会员卡消费净值");
		cell12.setCellValue("会员卡消费虚增");
		cell13.setCellValue("会员积分消费");
		cell14.setCellValue("会员优惠券");

		
		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		cell3.setCellStyle(style);
		cell4.setCellStyle(style);
		cell5.setCellStyle(style);
		cell6.setCellStyle(style);
		cell7.setCellStyle(style);
		cell8.setCellStyle(style);
		cell9.setCellStyle(style);
		cell10.setCellStyle(style);
		cell11.setCellStyle(style);
		cell12.setCellStyle(style);
		cell13.setCellStyle(style);
		cell14.setCellStyle(style);


		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			row.createCell((short) (0)).setCellValue(list.get(i).getDate());
			row.getCell(0).setCellStyle(stylerowFontCenter);
			row.createCell((short) (1)).setCellValue(list.get(i).getBranchName());
			row.getCell(1).setCellStyle(stylerowFontCenter);
			row.createCell((short) (2)).setCellValue(list.get(i).getShouldamount().toString());
			row.getCell(2).setCellStyle(stylerowFontCenter);
			row.createCell((short) (3)).setCellValue(list.get(i).getPaidinamount().toString());
			row.getCell(3).setCellStyle(stylerowFontCenter);
			row.createCell((short) (4)).setCellValue(list.get(i).getDiscountamount().toString());
			row.getCell(4).setCellStyle(stylerowFontCenter);
			row.createCell((short) (5)).setCellValue(list.get(i).getCash().toString());
			row.getCell(5).setCellStyle(stylerowFontCenter);
			row.createCell((short) (6)).setCellValue(list.get(i).getCard().toString());
			row.getCell(6).setCellStyle(stylerowFontCenter);
			row.createCell((short) (7)).setCellValue(list.get(i).getOthercard().toString());
			row.getCell(7).setCellStyle(stylerowFontCenter);
			row.createCell((short) (8)).setCellValue(list.get(i).getWeixin().toString());
			row.getCell(8).setCellStyle(stylerowFontCenter);
			row.createCell((short) (9)).setCellValue(list.get(i).getZhifubao().toString());
			row.getCell(9).setCellStyle(stylerowFontCenter);
			row.createCell((short) (10)).setCellValue(list.get(i). getCredit().toString());
			row.getCell(10).setCellStyle(stylerowFontCenter);
			row.createCell((short) (11)).setCellValue(list.get(i).getMerbervaluenet().toString());
			row.getCell(11).setCellStyle(stylerowFontCenter);
			row.createCell((short) (12)).setCellValue(list.get(i).getMebervalueadd().toString());
			row.getCell(12).setCellStyle(stylerowFontCenter);
			row.createCell((short) (13)).setCellValue(list.get(i).getIntegralconsum().toString());
			row.getCell(13).setCellStyle(stylerowFontCenter);
			row.createCell((short) (14)).setCellValue(list.get(i).getMeberTicket().toString());
			row.getCell(14).setCellStyle(stylerowFontCenter);
		}
		// 设置响应头和保存文件名
		OutputStream fout = response.getOutputStream();
		wb.write(fout);
		fout.flush();
		fout.close();
		wb.close();
	}
	/**
	 * 导出门店营业报表(门店订单明细)
	 * @param list
	 * @param params
	 * @param filedisplay
	 * @param req
	 * @param response
	 * @throws Exception
	 */
	public static void exportExcleIDayOrders (List<Map<String, String>> list, Map<String, String> params, String filedisplay, HttpServletRequest req, HttpServletResponse response) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
		response.setContentType("application/vnd.ms-e"
				+ "xcel");
		String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
		HSSFSheet sheet = wb.createSheet(filedisplay);   //--->创建了一个工作簿
		sheet.setDefaultColumnWidth((short) 30);
		//样式1
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		//设置标题字体格式
		Font font = wb.createFont();
		//设置字体样式
		font.setFontHeightInPoints((short) 12);   //--->设置字体大小
		font.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		font.setBold(true);//---》是否加粗
		style.setFont(font);
		
		//样式3
		HSSFCellStyle stylerowFontCenter = wb.createCellStyle(); // 样式对象
		stylerowFontCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 字体居中
		//-------------------------------------------------------------------------------------------------

		String title = ExcelUtils.setTabTitleToBusiness("营业报表", params);

		HSSFRow firstRow = sheet.createRow(0);
		firstRow.setHeight((short) 1200);
		sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (15)));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCell firstcell = firstRow.createCell((short) (0));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Font cellfont = wb.createFont();
		//设置字体样式
		cellfont.setFontHeightInPoints((short) 16);   //--->设置字体大小
		cellfont.setFontName("宋体");   //---》设置字体，是什么类型例如：宋体
		cellfont.setBold(true);//---》是否加粗
		cellStyle.setFont(cellfont);
		cellStyle.setWrapText(true);
		firstcell.setCellStyle(cellStyle);
		firstcell.setCellValue(new HSSFRichTextString(title));
		
		//------------------------------------------------------------------------------------------------
		HSSFRow rows = sheet.createRow(1);

		HSSFCell cell0 = rows.createCell((short) (0));
		HSSFCell cell1 = rows.createCell((short) (1));
		HSSFCell cell2 = rows.createCell((short) (2));
		HSSFCell cell3 = rows.createCell((short) (3));
		HSSFCell cell4 = rows.createCell((short) (4));
		HSSFCell cell5 = rows.createCell((short) (5));
		HSSFCell cell6 = rows.createCell((short) (6));
		HSSFCell cell7 = rows.createCell((short) (7));
		HSSFCell cell8 = rows.createCell((short) (8));
		HSSFCell cell9 = rows.createCell((short) (9));
		HSSFCell cell10 = rows.createCell((short) (10));
		HSSFCell cell11 = rows.createCell((short) (11));
		HSSFCell cell12 = rows.createCell((short) (12));
		HSSFCell cell13 = rows.createCell((short) (13));
		HSSFCell cell14 = rows.createCell((short) (14));
		HSSFCell cell15 = rows.createCell((short) (15));


		/*cell0.setCellValue("订单号");
		cell1.setCellValue("开台时间");
		cell2.setCellValue("结账时间");
		cell3.setCellValue("应收");
		cell4.setCellValue("实收");
		cell5.setCellValue("折扣");
		cell6.setCellValue("现金");
		cell7.setCellValue("银行卡");
		cell8.setCellValue("挂账");
		cell9.setCellValue("会员卡消费净值");
		cell10.setCellValue("会员卡消费虚增");
		cell11.setCellValue("会员积分消费");
		cell12.setCellValue("会员优惠券");
	*/
		cell0.setCellValue("订单号");
		cell1.setCellValue("开台时间");
		cell2.setCellValue("结账时间");
		cell3.setCellValue("应收");
		cell4.setCellValue("实收");
		cell5.setCellValue("折扣");
		cell6.setCellValue("现金");
		cell7.setCellValue("刷卡-工行");
		cell8.setCellValue("刷卡-他行");
		cell9.setCellValue("挂账");
		cell10.setCellValue("微信支付");
		cell11.setCellValue("支付宝支付");
		cell12.setCellValue("会员卡消费净值");
		cell13.setCellValue("会员卡消费虚增");
		cell14.setCellValue("会员积分消费");
		cell15.setCellValue("会员优惠券");

		
		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		cell3.setCellStyle(style);
		cell4.setCellStyle(style);
		cell5.setCellStyle(style);
		cell6.setCellStyle(style);
		cell7.setCellStyle(style);
		cell8.setCellStyle(style);
		cell9.setCellStyle(style);
		cell10.setCellStyle(style);
		cell11.setCellStyle(style);
		cell12.setCellStyle(style);
		cell13.setCellStyle(style);
		cell14.setCellStyle(style);
		cell15.setCellStyle(style);


		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			row.createCell((short) (0)).setCellValue(list.get(i).get("orderId").toString());
			row.getCell(0).setCellStyle(stylerowFontCenter);
			row.createCell((short) (1)).setCellValue(list.get(i).get("beginTime").toString());
			row.getCell(1).setCellStyle(stylerowFontCenter);
			row.createCell((short) (2)).setCellValue(list.get(i).get("endTime").toString());
			row.getCell(2).setCellStyle(stylerowFontCenter);
			row.createCell((short) (3)).setCellValue(String.valueOf(list.get(i).get("shouldamount")));
			row.getCell(3).setCellStyle(stylerowFontCenter);
			row.createCell((short) (4)).setCellValue(String.valueOf(list.get(i).get("paidinamount")));
			row.getCell(4).setCellStyle(stylerowFontCenter);
			row.createCell((short) (5)).setCellValue(String.valueOf(list.get(i).get("discountamount")));
			row.getCell(5).setCellStyle(stylerowFontCenter);
			row.createCell((short) (6)).setCellValue(String.valueOf(list.get(i).get("cash")));
			row.getCell(6).setCellStyle(stylerowFontCenter);
			row.createCell((short) (7)).setCellValue(String.valueOf(list.get(i).get("card")));
			row.getCell(7).setCellStyle(stylerowFontCenter);
			row.createCell((short) (8)).setCellValue(String.valueOf(list.get(i).get("othercard")));
			row.getCell(8).setCellStyle(stylerowFontCenter);
			row.createCell((short) (9)).setCellValue(String.valueOf(list.get(i).get("credit")));
			row.getCell(9).setCellStyle(stylerowFontCenter);
			row.createCell((short) (10)).setCellValue(String.valueOf(list.get(i).get("weixin")));
			row.getCell(10).setCellStyle(stylerowFontCenter);
			row.createCell((short) (11)).setCellValue(String.valueOf(list.get(i).get("zhifubao")));
			row.getCell(11).setCellStyle(stylerowFontCenter);
			row.createCell((short) (12)).setCellValue(String.valueOf(list.get(i).get("merbervaluenet")));
			row.getCell(12).setCellStyle(stylerowFontCenter);
			row.createCell((short) (13)).setCellValue(String.valueOf(list.get(i).get("mebervalueadd")));
			row.getCell(13).setCellStyle(stylerowFontCenter);
			row.createCell((short) (14)).setCellValue(String.valueOf(list.get(i).get("integralconsum")));
			row.getCell(14).setCellStyle(stylerowFontCenter);
			row.createCell((short) (15)).setCellValue(String.valueOf(list.get(i).get("meberTicket")));
			row.getCell(15).setCellStyle(stylerowFontCenter);
		}
		// 设置响应头和保存文件名
		OutputStream fout = response.getOutputStream();
		wb.write(fout);
		fout.flush();
		fout.close();
		wb.close();
	}
}
