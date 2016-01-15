package com.candao.www.webroom.service.impl;

import com.candao.www.utils.DateTimeUtils;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.webroom.model.Code;
import com.candao.www.webroom.model.DataStatistics;
import com.candao.www.webroom.model.TableArea;
import com.candao.www.webroom.model.TjObj;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

//import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: johnson
 * Date: 5/20/15
 * Time: 1:19 下午
 */

@Service
public class ExportDateStatisticsExcelService {


	/**
	 * 备选方法
	 *
	 */
	public void formatExcel(List<Map<String,Object>> mapList , HttpServletRequest req, HttpServletResponse response, Map<String, Object> params) {

		try {
			HSSFWorkbook workbook = null;
			String courseFile = this.getClass().getClassLoader().getResource("").getPath().split("WEB-INF")[0] + "/template/templet.xls";
			System.out.println(courseFile);
			workbook = new HSSFWorkbook(new FileInputStream(courseFile));
			HSSFSheet sheet = workbook.getSheet("详细数据统计表");
			formatExcel(  mapList, workbook, sheet, params);

			if (params.get("shiftId").toString().equals("0")) {
				fillDate("午市",mapList, workbook, sheet, params);
			}
			if (params.get("shiftId").toString().equals("-1")) {
				fillDate("全天",mapList, workbook, sheet, params);
			}
			if (params.get("shiftId").toString().equals("1")) {
				fillDate("晚市",mapList, workbook, sheet, params);
			}


			String as = "详细数据统计表.xls";
			String fileName = new String(as.getBytes("GB2312"), "ISO_8859_1");
			as = fileName;
			//输出excl文件
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename="+as);
			OutputStream fout = response.getOutputStream();
			workbook.write(fout);
			fout.flush();
			fout.close();
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		}


	}


	/**
	 * 填充数据
	 *
	 * @param workbook
	 * @param worksheet
	 */
	private static void fillDate(String areaid,List<Map<String,Object>> mapList, HSSFWorkbook workbook, HSSFSheet worksheet, Map<String, Object> params) {
		int tablenum = mapList.size() - 1;//桌数
		String beginTime = params.get("beginTime") == null ? "" : params.get("beginTime").toString();
		String endTime = params.get("endTime") == null ? "" : params.get("endTime").toString();
		String dataType = params.get("dataType") == null ? "" : params.get("dataType").toString();
		List<String> daylist = getDayList(beginTime,endTime);

		int firstRow = 2;
		int firsttablevaluecell = 3;
		for (int n = 0; n < mapList.size(); n++) {
			spengvalue(areaid,mapList.get(n),daylist, worksheet, firstRow,params);
			firstRow = firstRow + 1;
		}

	}


	/**
	 * 添加该桌子在这日期下的营业额。
	 * 解决问题，有些桌子可能一天都不会有营业额但是这样的营业额不显示在
	 * 集合数据中
	 *
	 * @param dataStatisticsList
	 * @param worksheet
	 * @param firstRow
	 */
	private static void spengvalue(String areid,Map map,List<String>daylist, HSSFSheet worksheet, int firstRow,Map<String, Object> params) {
		int firstRow1 = firstRow;
		int firstcell = 3;
		int daynum = daylist.size();
		worksheet.getRow(firstRow1).getCell(0).setCellValue(areid);
		worksheet.getRow(firstRow1).getCell(1).setCellValue(map.get("areaname")==null?"":map.get("areaname").toString());
		worksheet.getRow(firstRow1).getCell(2).setCellValue(map.get("tableid")==null?"":map.get("tableid").toString());
		for (int j = 0; j < daynum; j++) {
			if (worksheet.getRow(1).getCell(firstcell) == null) {
				break;
			}
			if(map.get(daylist.get(j).toString())!=null) {  //确认数据是否存在
				if(params.get("dataType").toString().equals("3")||params.get("dataType").toString().equals("4")) {    //人数
					worksheet.getRow(firstRow1).getCell(firstcell).setCellValue(Double.valueOf(map.get(daylist.get(j).toString()).toString()).intValue());
				} else {  //金额
					worksheet.getRow(firstRow1).getCell(firstcell).setCellValue(map.get(daylist.get(j).toString()).toString());

				}
			}
			firstcell = firstcell + 1;
		}

	}

	private static Date getDate(String excelDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.parse(excelDate);
		} catch (ParseException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;
	}


	/**
	 * 格式化
	 * 将字符串中日期过滤掉年只有月日
	 *
	 * @param ds
	 * @return
	 */
	private static String getformatdatetoString(DataStatistics ds) {
		String datevalue = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(ds.getDateTime());
			datevalue = DateFormatUtils.format((date), "MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return datevalue;
	}


	/**
	 * 填充地域中的数据
	 * 表格要求一致性 全天
	 * 主要是合并单元格在区域等数据
	 */
	private static void formatExcel(List<Map<String,Object>> mapList  , HSSFWorkbook workbook, HSSFSheet worksheet, Map<String, Object> params) {
		int tablenum = mapList.size() - 1;//桌数
		String beginTime = params.get("beginTime") == null ? "" : params.get("beginTime").toString();
		String endTime = params.get("endTime") == null ? "" : params.get("endTime").toString();
		String dataType = params.get("dataType") == null ? "" : params.get("dataType").toString();

		if (dataType.equals("1")) {
			worksheet.getRow(0).getCell(0).setCellValue("应收金额");

		}
		if (dataType.equals("2")) {
			worksheet.getRow(0).getCell(0).setCellValue("实收金额");

		}
		if (dataType.equals("3")) {
			worksheet.getRow(0).getCell(0).setCellValue("结算人数");

		}
		if (dataType.equals("4")) {
			worksheet.getRow(0).getCell(0).setCellValue("开台数");
		}

		//赋值单元格
		for (int i = 0; i < tablenum; i++) {
			ExcelUtils.copyRow(workbook, worksheet, 2, 3);
		}
		//赋值时间列表
		creatDateList(worksheet, beginTime, endTime);
	}

	private static void creatDateList(HSSFSheet worksheet, String beginTime, String endTime) {
		//查询天数
		int daynum = DateTimeUtils.getsubtractDaynum("yyyy-MM-dd", beginTime, endTime) + 1;
		//过滤日期字符串列表
		List<String> stringList = DateTimeUtils.getdateList(beginTime, daynum, "yyyy-MM-dd", "yyyy/MM/dd");
		//时间赋值

		for (int i = 3; i < daynum + 3; i++) {  //日期从第三个格开始赋值
			worksheet.getRow(1).getCell(i).setCellValue(stringList.get(i - 3));
		}
	}

	public static List<String> getDayList(String beginTime, String endTime) {
		//查询天数
		int daynum = DateTimeUtils.getsubtractDaynum("yyyy-MM-dd", beginTime, endTime) + 1;
		//过滤日期字符串列表
		List<String> stringList = DateTimeUtils.getdateList(beginTime, daynum, "yyyy-MM-dd", "yyyy/MM/dd");
		//时间赋值
		return stringList;
	}

	private static HashMap getAreaHashMap(List<TableArea> areaList, List<DataStatistics> dataStatisticsList, Set<String> hs) {
		HashMap map = new HashMap<>();//统计合并区域格式
		for (TableArea ta : areaList) {
			Set<String> areaID = new HashSet<>();
			for (DataStatistics d : dataStatisticsList) {
				hs.add(d.getTableId());
				if (StringUtils.equals(ta.getAreaname(), d.getArea())) {
					areaID.add(d.getTableId());
				}
			}
			map.put(ta.getAreaNo(), areaID.size());
		}
		return map;
	}


}
