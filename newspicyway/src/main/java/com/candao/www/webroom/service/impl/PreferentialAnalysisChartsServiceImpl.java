package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TPreferentialAnalysisChartsDao;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.webroom.model.Base_CouponsRept;
import com.candao.www.webroom.service.PreferentialAnalysisChartsService;

/**
 * 优惠分析图表
 * @author Administrator
 *
 */
@Service
public class PreferentialAnalysisChartsServiceImpl implements PreferentialAnalysisChartsService {
	@Autowired
	private TPreferentialAnalysisChartsDao tpreferentialAnalysisChartsDao;
	/**
	 * 优惠报表统计
	 * @author zhouyao
	 * @serialData 2015-07-05
	 */
	public List<Map<String,Object>>  insertPreferential(Map<String, Object> params) {
		if(params.get("pi_branchid") != null && !"".equals(params.get("pi_branchid"))){
			params.put("pi_branchid", params.get("pi_branchid"));
		}else if(params.get("branchId") != null && !"".equals(params.get("branchId"))){
			params.put("pi_branchid", params.get("branchId"));
		}else{
			params.put("pi_branchid", PropertiesUtils.getValue("current_branch_id"));
		}
		params.put("pi_sb",params.get("shiftid"));
		params.put("pi_ksrq",params.get("beginTime"));
		params.put("pi_jsrq", params.get("endTime"));
		params.put("pi_hdmc",params.get("bankcardno"));
		params.put("pi_jsfs",params.get("settlementWay"));
		params.put("pi_hdlx",params.get("type"));
		List<Map<String,Object>> listCouponsRept = tpreferentialAnalysisChartsDao.findPreferential(params);
		return listCouponsRept;
	}
	/**
	 * 优惠报表统计明细
	 *  @author zhouyao
	 *  @serialData 2015-07-05
	 */
	public List<Map<String,Object>>  insertPreferentialDetail(Map<String,Object> params){
		String branchId = "";
    	if(params.get("branchId") != null && !"".equals(params.get("branchId"))){
    		branchId = params.get("branchId").toString();
    	}else{
    		branchId = PropertiesUtils.getValue("current_branch_id");
    	}
		params.put("pi_branchid",branchId);
		params.put("pi_sb",params.get("shiftid"));
		params.put("pi_ksrq",params.get("beginTime"));
		params.put("pi_jsrq",params.get("endTime"));
		params.put("pi_hdmc",params.get("couponsname"));
		params.put("pi_jsfs",params.get("payway"));
		params.put("pi_hdlx",params.get("type"));
		params.put("pi_dqy",params.get("currPage"));
		params.put("pi_myts",params.get("pageNums"));
		List<Map<String,Object>> CouponsReptDtailList = tpreferentialAnalysisChartsDao.findPreferentialDetail(params);
		return CouponsReptDtailList;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void exportxlsB(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String branchId = "";
    	if(params.get("branchId") != null && !"".equals(params.get("branchId"))){
    		branchId = params.get("branchId").toString();
    	}else{
    		branchId = PropertiesUtils.getValue("current_branch_id");
    	}
		params.put("pi_branchid", branchId);
		params.put("pi_sb",params.get("shiftid"));
		params.put("pi_ksrq",params.get("beginTime"));
		params.put("pi_jsrq", params.get("endTime"));
		params.put("pi_hdmc",params.get("bankcardno"));
		params.put("pi_jsfs",params.get("settlementWay"));
		params.put("pi_hdlx",params.get("type"));
		List<Map<String, Object>> list = insertPreferential(params);
		List<Base_CouponsRept> baseList =new ArrayList<Base_CouponsRept>();
		for (int i = 0;i<list.size();i++) {
			Base_CouponsRept baseC = new Base_CouponsRept();
			//活动名称
			if(list.get(i).get("pname")!=null && list.get(i).get("pname")!=""){
				baseC.setCouponsname(list.get(i).get("pname").toString());
		    }
			//活动类型
			if(list.get(i).get("ptypename")!=null && list.get(i).get("ptypename")!=""){
				baseC.setTypeName(list.get(i).get("ptypename").toString());
			}
			//结算方式
			if(list.get(i).get("paywaydesc")!=null && list.get(i).get("paywaydesc")!=""){
				baseC.setPaytype(list.get(i).get("paywaydesc").toString());
			}
			//结算方式code
			if(list.get(i).get("payway")!=null && list.get(i).get("payway")!=""){
				baseC.setPayway(list.get(i).get("payway").toString());
			}
			//笔数
			if(list.get(i).get("couponNum")!=null && list.get(i).get("couponNum")!=""){
				baseC.setNum(list.get(i).get("couponNum").toString());
		    }
			//发生金额
			if(list.get(i).get("payamount")!=null && list.get(i).get("payamount")!=""){
				baseC.setTotal(list.get(i).get("payamount").toString());
			}
			//拉动应收
			if(list.get(i).get("shouldamount")!=null && list.get(i).get("shouldamount")!=""){
				baseC.setYinshou(list.get(i).get("shouldamount").toString());
		    }
			//拉动实收
			if(list.get(i).get("paidinamount")!=null && list.get(i).get("paidinamount")!=""){
				baseC.setShishou(list.get(i).get("paidinamount").toString());
		    }
			//单数
			if(list.get(i).get("singular")!=null && list.get(i).get("singular")!=""){
				baseC.setSingular(list.get(i).get("singular").toString());
		    }
			//人均
			if(list.get(i).get("perCapita")!=null && list.get(i).get("perCapita")!=""){
				baseC.setPerCapita(list.get(i).get("perCapita").toString());
		    }
			baseList.add(baseC);
			
			String payway = params.get("payway").toString();
			String ptype = params.get("ptype").toString();
			String pname = params.get("pname").toString();
			if(!payway.equals("null")&& !payway.equals("")
			    &&!ptype.equals("null")&& !ptype.equals("")
			    &&!pname.equals("null")&& !pname.equals("")){
				if(list.get(i).get("pname").toString().equals(pname)
				  &&list.get(i).get("payway").toString().equals(payway)
				  &&list.get(i).get("ptype").toString().equals(ptype)){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("pi_branchid", branchId);
					map.put("pi_sb",params.get("shiftid"));
					map.put("pi_ksrq",params.get("beginTime"));
					map.put("pi_jsrq",params.get("endTime"));
					map.put("pi_hdmc",pname);
					map.put("pi_jsfs",payway);
					map.put("pi_hdlx",ptype);
					map.put("pi_dqy","-1");
					map.put("pi_myts","20");
				    List<Map<String, Object>> listDetail = tpreferentialAnalysisChartsDao.findPreferentialDetail(map);
				     if(listDetail.size()>0){
				    		Base_CouponsRept baseB= new Base_CouponsRept();
							baseB.setCouponsname("发生时间");
							baseB.setTypeName("订单号");
							baseB.setPaytype("结算金额");
							baseB.setNum("发生笔数");
							baseB.setTotal("发生金额");
							baseB.setYinshou("拉动应收");
							baseB.setShishou("拉动实收");
							baseB.setSingular("title");
							baseB.setPerCapita("title");
							baseList.add(baseB);
							for (int t = 0;t<listDetail.size();t++) {
								Base_CouponsRept base = new Base_CouponsRept();
								//发生时间==活动名称
								if(listDetail.get(t).get("begintime")!=null&&listDetail.get(t).get("begintime")!=""){
									base.setCouponsname(listDetail.get(t).get("begintime").toString());
								}
								//订单号==活动类型
								if(listDetail.get(t).get("orderid")!=null&&listDetail.get(t).get("orderid")!=""){
									base.setTypeName(listDetail.get(t).get("orderid").toString());
								}
								//结算方式==结算金额
								if(listDetail.get(t).get("price")!=null&&listDetail.get(t).get("price")!=""){
									base.setPaytype(listDetail.get(t).get("price").toString());
								}
								//发生笔数==发生笔数
								if(listDetail.get(t).get("couponNum")!=null&&listDetail.get(t).get("couponNum")!=""){
									base.setNum(listDetail.get(t).get("couponNum").toString());
								}
								//发生金额==发生金额
								if(listDetail.get(t).get("payamount")!=null&&listDetail.get(t).get("payamount")!=""){
									base.setTotal(listDetail.get(t).get("payamount").toString());
								}
								//拉动应收==拉动应收
								if(listDetail.get(t).get("shouldamount")!=null&&listDetail.get(t).get("shouldamount")!=""){
									base.setYinshou(listDetail.get(t).get("shouldamount").toString());
								}
								//拉动实收==拉动实收
								if(listDetail.get(t).get("paidinamount")!=null&&listDetail.get(t).get("paidinamount")!=""){
									base.setShishou(listDetail.get(t).get("paidinamount").toString());
								}
								base.setSingular("");
								base.setPerCapita("");
								baseList.add(base);	
						    
					       }
				     }
				 }
					
			 }
			
		  }
		String vasd ="优惠活动明细表";
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcleB(baseList,params, vasd, req,resp);
	}
	
	/**
	 * 营业分析_优惠活动统计
	 * @author zhouyao
	 * @serialData 2015-07-05
	 */
	public List<Map<String,Object>> insertPreferentialView(Map<String, Object> params){
		String branchId = "";
    	if(params.get("branchId") != null && !"".equals(params.get("branchId"))){
    		branchId = params.get("branchId").toString();
    	}else{
    		branchId = PropertiesUtils.getValue("current_branch_id");
    	}
		params.put("pi_branchid", branchId);// 分店id
		params.put("pi_xslx",params.get("dateType"));//显示类别 0:日  1:月  2:年
		params.put("pi_ksrq",params.get("beginTime"));//开始日期
		params.put("pi_jsrq",params.get("endTime"));//结束日期
		params.put("pi_tjx",params.get("dataType"));//统计项：0:活动名称  1:活动类别
		params.put("pi_hdlx",params.get("preftType"));//活动类别
		List<Map<String,Object>> listCou = tpreferentialAnalysisChartsDao.findPreferentialView(params);
		return listCou;
	}
	/**
	 * 根据门店编号查询活动名称
	 * @author zhouyao
	 * @serialData 2015-11-26
	 */
	public List<Map<String,Object>>  findBranchPreferential(Map<String, Object>params){
		String branchId = "";
    	if(params.get("branchId") != null && !"".equals(params.get("branchId"))){
    		branchId = params.get("branchId").toString();
    	}else{
    		branchId = PropertiesUtils.getValue("current_branch_id");
    	}
    	params.put("branchId", branchId);
    	List<Map<String,Object>> BranchPreferential = tpreferentialAnalysisChartsDao.findBranchPreferential(params);
		return BranchPreferential;
	}
	
	/**
	 * 导出优惠活动明细表主表
	 * @author weizhifang
	 * @author 2016-4-27
	 * @param request
	 * @param response
	 * @param CouponsReptList
	 * @param params
	 * @throws Exception
	 */
	public void exportPreferentialDetailtable(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> CouponsReptList,Map<String,Object> params)throws Exception{ 
		    // 文件名称与路径  
        String fileName = "优惠活动明细表.xls";  
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
             WritableSheet sheet = wwb.createSheet("优惠活动明细表", 1);// 建立工作簿 
             // 写表头  
             sheet.mergeCells(0, 0, 8, 0);
             sheet.setRowView(0, 1200);
             String title = ExcelUtils.setTabTitle("优惠活动明细表",params);
             jxl.write.Label labelTitle = new jxl.write.Label(0, 0, title);
             labelTitle.setCellFormat(wcfTitle); 
             sheet.addCell(labelTitle);// 放入工作簿  
             String text [] = {"活动名称","活动类型","结算方式","单数","笔数","发生金额","拉动应收","拉动实收","人均"};
             for(int i=0;i<text.length;i++){
            	 sheet.setColumnView(i,25);
            	 sheet.addCell(new Label(i,1,text[i],wcfHead));  
             }
             if(!CouponsReptList.toString().equals("[null]")){
            	 int rowNum = 1;
	             for(int i=0;i<CouponsReptList.size();i++){
	            	 rowNum++;
	            	 sheet.setColumnView(i,25);
	            	 String pname = CouponsReptList.get(i).get("pname") == null ? "" : CouponsReptList.get(i).get("pname").toString();
	            	 String singular = CouponsReptList.get(i).get("singular") == null ? "" : CouponsReptList.get(i).get("singular").toString();
	            	 String ptypename = CouponsReptList.get(i).get("ptypename") == null ? "" : CouponsReptList.get(i).get("ptypename").toString();
	            	 String perCapita = CouponsReptList.get(i).get("perCapita") == null ? "" : CouponsReptList.get(i).get("perCapita").toString();
	            	 String paywaydesc = CouponsReptList.get(i).get("paywaydesc") == null ? "" : CouponsReptList.get(i).get("paywaydesc").toString();
	            	 String couponNum = CouponsReptList.get(i).get("couponNum") == null ? "" : CouponsReptList.get(i).get("couponNum").toString();
	            	 String payamount = CouponsReptList.get(i).get("payamount") == null ? "" : CouponsReptList.get(i).get("payamount").toString();
	            	 String shouldamount = CouponsReptList.get(i).get("shouldamount") == null ? "" : CouponsReptList.get(i).get("shouldamount").toString();
	            	 String paidinamount = CouponsReptList.get(i).get("paidinamount") == null ? "" : CouponsReptList.get(i).get("paidinamount").toString();
	            	 sheet.addCell(new Label(0, rowNum, pname, wcfTable));
	            	 sheet.addCell(new Label(1, rowNum, ptypename, wcfTable));
	            	 sheet.addCell(new Label(2, rowNum, paywaydesc, wcfTable));
	            	 sheet.addCell(new Label(3, rowNum, singular, wcfTable));
	            	 sheet.addCell(new Label(4, rowNum, couponNum, wcfTable));
	            	 sheet.addCell(new Label(5, rowNum, payamount, wcfTable));
	            	 sheet.addCell(new Label(6, rowNum, shouldamount, wcfTable));
	            	 sheet.addCell(new Label(7, rowNum, paidinamount, wcfTable));
	            	 sheet.addCell(new Label(8, rowNum, perCapita, wcfTable));
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
