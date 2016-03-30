package com.candao.www.webroom.service.impl;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbDayIncomeBillDao;
import com.candao.www.utils.DateTimeUtils;
import com.candao.www.webroom.model.Base_CouponsRept;
import com.candao.www.webroom.model.BusinessReport;
import com.candao.www.webroom.model.Code;
import com.candao.www.webroom.model.CouponsRept;
import com.candao.www.webroom.model.CouponsReptDtail;
import com.candao.www.webroom.model.DataStatistics;
import com.candao.www.webroom.model.Dishsalerept;
import com.candao.www.webroom.model.ItemReport;
import com.candao.www.webroom.model.PaywayRpet;
import com.candao.www.webroom.model.PreferentialReport;
import com.candao.www.webroom.model.SettlementReport;
import com.candao.www.webroom.model.TableArea;
import com.candao.www.webroom.model.TjObj;
import com.candao.www.webroom.service.BusinessDataDetailService;
import com.candao.www.webroom.service.DayIncomeBillService;
@Service
public class DayIncomeBillServiceImpl implements DayIncomeBillService{
	private static final Logger logger = LoggerFactory.getLogger(DayIncomeBillServiceImpl.class);
	
	@Autowired
	private TbDayIncomeBillDao tbDayIncomeBillDao;
	@Autowired
	private BusinessDataDetailService businessDataDetailService;

	@Override
	public  List<TjObj> getDaliyReport(Map<String, Object> params) {
		List<TjObj> list = new ArrayList<TjObj>();
//-----------营业收入统计栏-----------------------------------------------------		
		List<Map<String,Object>> list1 = tbDayIncomeBillDao.find(params);
		for(int i=0;i<list1.size()+1;i++){
			TjObj tj = new TjObj();
			tj.setBaseCom("营业收入统计栏");
			if(i<list1.size()){
				tj.setSelfcom(list1.get(i).get("columnDetial").toString());
				tj.setObjvalue(new BigDecimal(list1.get(i).get("Money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
			}else{
				tj.setSelfcom("折扣率（%）");
				if(!list1.get(0).get("Money").toString().equals("0.0")){
					BigDecimal b1 = new BigDecimal(list1.get(0).get("Money").toString());
					BigDecimal b2 = new BigDecimal(list1.get(2).get("Money").toString());
					BigDecimal b3 = (b2.divide(b1,2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal("100")); 
					tj.setObjvalue(b3);
				}else{
					tj.setObjvalue(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
				}			
			}						
			list.add(tj);			
		}
//-----------实收明细统计栏-----------------------------------------------------		
		List<Map<String,Object>> list2 = tbDayIncomeBillDao.findO(params);
		for(int i=0;i<list2.size();i++){		
			TjObj tj = new TjObj();
			tj.setBaseCom("实收明细统计栏");
			tj.setSelfcom(list2.get(i).get("columnDetial").toString());
			tj.setObjvalue(new BigDecimal(list2.get(i).get("Money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
			list.add(tj);			
	}
//-----------会员数据统计栏-----------------------------------------------------	
		List<Map<String,Object>> list3 = tbDayIncomeBillDao.findTh(params);
		for(int i=0;i<list3.size();i++){		
			TjObj tj = new TjObj();
			tj.setBaseCom("会员数据统计栏");
			tj.setSelfcom(list3.get(i).get("columnDetial").toString());
			tj.setObjvalue(new BigDecimal(list3.get(i).get("Money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
			list.add(tj);			
	}
//-----------营业数据统计栏-----------------------------------------------------
		List<Map<String,Object>> list4 = tbDayIncomeBillDao.findT(params);
		for(int i=0;i<list4.size()+4;i++){
			TjObj tj4 = new TjObj();
			tj4.setBaseCom("营业数据统计栏");
			if(i<list4.size()-1){
			    tj4.setSelfcom(list4.get(i).get("columnDetial").toString());
			    if(list4.get(i).get("columnDetial").toString().equals("翻座率（%）")){
			    	tj4.setObjvalue(new BigDecimal(list4.get(i).get("num").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
			    }else{
			    	tj4.setObjvalue(new BigDecimal(list4.get(i).get("num").toString()).setScale(0, BigDecimal.ROUND_HALF_UP));
			    }
			    list.add(tj4);
			}else if(i==list4.size()-1){
				 tj4.setSelfcom(list4.get(i).get("columnDetial").toString());
				    tj4.setObjvalue(new BigDecimal(Double.parseDouble(list4.get(i).get("num").toString())*100).setScale(2, BigDecimal.ROUND_HALF_UP));
				    list.add(tj4);
			}else if(i==list4.size()){
			    tj4.setSelfcom("应收人均（元）");
			    if(!list4.get(1).get("num").toString().equals("0.0000")){
			    	 BigDecimal b1 = new BigDecimal(list1.get(0).get("Money").toString());
					    BigDecimal b2 = new BigDecimal(list4.get(1).get("num").toString());
					    BigDecimal b3 = b1.divide(b2,2, BigDecimal.ROUND_HALF_UP); 
					    tj4.setObjvalue(b3);
			    }else{
			    	 tj4.setObjvalue(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
			    }			   
			    list.add(tj4);
			}else if(i==list4.size()+1){
				tj4.setSelfcom("实收人均（元）");				
				 if(!list4.get(1).get("num").toString().equals("0.0000")){
					 BigDecimal b1 = new BigDecimal(list1.get(1).get("Money").toString());
					 BigDecimal b2 = new BigDecimal(list4.get(1).get("num").toString());
					 BigDecimal b3 = b1.divide(b2,2, BigDecimal.ROUND_HALF_UP); 
					 tj4.setObjvalue(b3);
				 }else{
					 tj4.setObjvalue(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
				 }				
				list.add(tj4);	
			}else if(i==list4.size()+2){
				tj4.setSelfcom("桌均消费");				
				 if(!list4.get(1).get("num").toString().equals("0.0000")){
					 //获取实收总金额
					 BigDecimal b1 = new BigDecimal(list1.get(1).get("Money").toString());
					 //获取实际就餐桌数
					 BigDecimal b2 = new BigDecimal(list4.get(0).get("num").toString());
					 
					 BigDecimal b3 = b1.divide(b2,2, BigDecimal.ROUND_HALF_UP); 
					 tj4.setObjvalue(b3);
				 }else{
					 tj4.setObjvalue(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
				 }				
				list.add(tj4);	
			}else if(i==list4.size()+3){
				tj4.setSelfcom("翻台率(%)");				
				 if(!list4.get(1).get("num").toString().equals("0.0000")){
					 String beginTimeStr=null;
					 String endTimeStr = null;
					 Date beginTime;
					 Date endTime;
					 if(params.get("beginTime")!=null && params.get("beginTime")!=""){
						 beginTimeStr =  params.get("beginTime").toString();
					 }
					 if(params.get("endTime")!=null && params.get("endTime")!=""){
						 endTimeStr =  params.get("endTime").toString();
					 }
					 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
					 int dayNum=0;
					 try {
							beginTime = sdf.parse(beginTimeStr);
							endTime = sdf.parse(endTimeStr);
							dayNum = daysBetween(beginTime,endTime)+1;
							if(dayNum==0){
								dayNum = 1;
							}
						} catch (ParseException e) {
							logger.error(e.getMessage());
							e.printStackTrace();
					 }
					 //餐厅餐桌总台数
					 List<Map<String,Object>> tableNums = tbDayIncomeBillDao.getTableNum(params);
					 BigDecimal z_num = new BigDecimal(tableNums.get(0).get("nums").toString());
					 //获取实际就餐桌数
					 BigDecimal s_num = new BigDecimal(list4.get(0).get("num").toString());
					 //两个时间间隔的天数
					 BigDecimal d_num = new BigDecimal(dayNum);
					 //总用餐桌数(获取实际就餐桌数)/总天数=平均每天总桌数
					 BigDecimal avgNum = s_num.divide(d_num,4,BigDecimal.ROUND_HALF_EVEN);
					 //平均每天总桌数/标准桌数=翻台率
					 BigDecimal f_num= avgNum.divide(z_num,4,BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100)); 
					 BigDecimal b4 = f_num.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					 tj4.setObjvalue(b4);
				 }else{
					 tj4.setObjvalue(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				 }			
				list.add(tj4);	
			}
		}
//-----------折扣明细统计栏-----------------------------------------------------	
		List<Map<String,Object>> list5 = tbDayIncomeBillDao.findF(params);
		for(int i=0;i<list5.size();i++){		
			TjObj tj = new TjObj();
			tj.setBaseCom("折扣明细统计栏");
			tj.setSelfcom(list5.get(i).get("columnDetial").toString());
			tj.setObjvalue(new BigDecimal(list5.get(i).get("Money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
			list.add(tj);			
	}
//-----------其他财务统计栏-----------------------------------------------------	
//-----------银行应收账款统计栏---------------------------------------------------
		List<Map<String,Object>> list7 = tbDayIncomeBillDao.findFv(params);
		for(int i=0;i<list7.size();i++){		
			TjObj tj = new TjObj();
			tj.setBaseCom("银行应收账款统计栏");
			tj.setSelfcom(list7.get(i).get("columnDetial").toString());
			tj.setObjvalue(new BigDecimal(list7.get(i).get("Money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
			list.add(tj);			
	}
		return list;
}
	
	@SuppressWarnings("static-access")
	@Override
	public void exportDaliyRport(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//List<TjObj> list = getDaliyReport(params);
		List<Map<String, Object>> list  = businessDataDetailService.isgetBusinessDetailexcel(params);
		String vasd =params.get("names").toString() ;
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcle(list,params, vasd, req,resp);
	}

	@Override
	public Page<Map<String, Object>> getDaliyReport1(Map<String, Object> params,int current, int pagesize) {
//		List<CouponsRept> list = new ArrayList<CouponsRept>(); 
		Page<Map<String, Object>> page  = tbDayIncomeBillDao.pageCoupons(params, current,  pagesize);
		return page;
//		Page<Map<String, Object>>
//		for(int i=0;i<list5.size();i++){		
 //		CouponsRept tj = new CouponsRept();
//			tj.setBaseCom("优惠分析表");
//			tj.setPaytype(list5.get(i).get("paytype").toString());
//			tj.setTotal(new BigDecimal(list5.get(i).get("total").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
//			tj.setNum(Integer.parseInt(list5.get(i).toString()));
//			tj.setShishou(new BigDecimal(list5.get(i).get("shishou").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
//			tj.setYinshou(new BigDecimal(list5.get(i).get("yinshou").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
//			list.add(tj);			
//	}
		 
//		return list;
	}
	
	/**
	 * 优惠报表明细统计
	 */
	@Override
	public List<CouponsReptDtail>  findCouponsDtail(Map<String, Object> params) {
		List<CouponsReptDtail> list = new ArrayList<CouponsReptDtail>();
		List<Map<String,Object>> listCou = tbDayIncomeBillDao.findCouponsDtail(params);
		for(int i=0;i<listCou.size();i++){
			CouponsReptDtail cou = new CouponsReptDtail();
			if(listCou.get(i).get("code")!=null && listCou.get(i).get("code")!=""){
				if(listCou.get(i).get("code").toString().equals(params.get("couponsname"))
						||listCou.get(i).get("code").toString().equals(params.get("codeid"))){
					if(listCou.get(i).get("inserttime")!=null&&listCou.get(i).get("inserttime")!=""){
						 SimpleDateFormat formatter  = new SimpleDateFormat( "yyyy-MM-dd ");
						 String dateStr = listCou.get(i).get("inserttime").toString();
						 Date date = null;
						try {
							date = formatter.parse(dateStr);
						} catch (ParseException e) {
							logger.error(e.getMessage());
							e.printStackTrace();
						}
						 String dateResult = formatter.format(date);
						 cou.setInsertTime(dateResult);
					}
					if(listCou.get(i).get("orderid")!=null&&listCou.get(i).get("orderid")!=""){
						cou.setOrderid(listCou.get(i).get("orderid").toString());
					}
					if(listCou.get(i).get("payway")!=null&&listCou.get(i).get("payway")!=""){
						cou.setBaseCom(listCou.get(i).get("payway").toString());
					}
					if(listCou.get(i).get("num")!=null&&listCou.get(i).get("num")!=""){
						cou.setNum(Integer.parseInt(listCou.get(i).get("num").toString()));
					}
					if(listCou.get(i).get("couponsname")!=null&&listCou.get(i).get("couponsname")!=""){
						cou.setCouponsname(listCou.get(i).get("couponsname").toString());
					}
					
					if(listCou.get(i).get("payamount")!=null&&listCou.get(i).get("payamount")!=""){
						BigDecimal payamountRuslt = new BigDecimal(listCou.get(i).get("payamount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					    cou.setPayamount(payamountRuslt);
					}
					if(listCou.get(i).get("z_payamount")!=null && listCou.get(i).get("z_payamount")!=""){
						BigDecimal z_payamountReslut = new BigDecimal(listCou.get(i).get("z_payamount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						cou.setTotal(z_payamountReslut);
					}
					if(listCou.get(i).get("code")!=null && listCou.get(i).get("code")!=""){
						cou.setCode(listCou.get(i).get("code").toString());
					}
					if(listCou.get(i).get("yinshou")!=""&&listCou.get(i).get("yinshou")!=null){
						BigDecimal yinshouResult = new BigDecimal(listCou.get(i).get("yinshou").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						cou.setYinshou(yinshouResult);
					}else{
						BigDecimal yinshouResult = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						cou.setYinshou(yinshouResult);
					}
					if(listCou.get(i).get("shishou")!=null && listCou.get(i).get("shishou")!=""){
						BigDecimal shishouResult;
						Double a = Double.parseDouble(listCou.get(i).get("shishou").toString());
						int  datum = (int) Math.signum(a);
						if( datum == -1 && datum == 0){
							shishouResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						}else{
							shishouResult = new BigDecimal(listCou.get(i).get("shishou").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						}
						cou.setShishou(shishouResult);
				    }else{
						BigDecimal shishouResult = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						cou.setShishou(shishouResult);
					}
					list.add(cou);	
				}
			}
			
	   } 
		return list;
	}
	/**
	 * 优惠报表统计
	 */
	public List<CouponsRept>  findCoupons(Map<String, Object> params) {
		List<CouponsRept> list = new ArrayList<CouponsRept>();
		List<Map<String,Object>> listCou = tbDayIncomeBillDao.findCoupons(params);
		for(int i=0;i<listCou.size();i++){
			CouponsRept cou = new CouponsRept();
			Integer num = null ;
			if(listCou.get(i).get("couponsname")!=null && listCou.get(i).get("couponsname")!=""){
				cou.setCouponsname(listCou.get(i).get("couponsname").toString());
		    }
			if(listCou.get(i).get("paytype")!=null && listCou.get(i).get("paytype")!=""){
				cou.setPaytype(listCou.get(i).get("paytype").toString());
			}
			if(listCou.get(i).get("payway")!=null && listCou.get(i).get("payway")!=""){
				cou.setPayway(listCou.get(i).get("payway").toString());
			}
			if(listCou.get(i).get("payamount")!=null && listCou.get(i).get("payamount")!=""){
				cou.setBaseCom(listCou.get(i).get("payamount").toString());
		    }
			if(listCou.get(i).get("num")!=null && listCou.get(i).get("num")!=""){
				num = Integer.parseInt(listCou.get(i).get("num").toString());
				cou.setNum(num);
		    }
			if(listCou.get(i).get("code")!=null && listCou.get(i).get("code")!=""){
				cou.setCode(listCou.get(i).get("code").toString());
			}
			if(listCou.get(i).get("total")!=null && listCou.get(i).get("total")!=""){
				BigDecimal TotalRsult = new BigDecimal(listCou.get(i).get("total").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				cou.setTotal(TotalRsult);
			}
			if(listCou.get(i).get("yinshou")!=null && listCou.get(i).get("yinshou")!=""){
				BigDecimal yinshouRsult = new BigDecimal(listCou.get(i).get("yinshou").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				cou.setYinshou(yinshouRsult);
		    }else{
		    	BigDecimal yinshouRsult = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				cou.setYinshou(yinshouRsult);
		    }
			if(listCou.get(i).get("shishou")!=null && listCou.get(i).get("shishou")!=""){
				BigDecimal shishouResult;
				Double a = Double.parseDouble(listCou.get(i).get("shishou").toString());
				int  datum = (int) Math.signum(a);
				if( datum == -1 && datum == 0){
					shishouResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				}else{
					shishouResult = new BigDecimal(listCou.get(i).get("shishou").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				cou.setShishou(shishouResult);
		    }else{
				BigDecimal shishouResult = new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				cou.setShishou(shishouResult);
			}
			if(listCou.get(i).get("type")!=null && listCou.get(i).get("type")!=""){
				cou.setType(listCou.get(i).get("type").toString());
		    }else{
		    	cou.setType("");
		    }
			if(listCou.get(i).get("typeName")!=null && listCou.get(i).get("typeName")!=""){
				cou.setTypeName(listCou.get(i).get("typeName").toString());
		    }else{
		    	cou.setTypeName("");
		    }
			list.add(cou);					
	   } 
		return list;
	}
	
	@Override
	public List<TjObj> getDaliyReport2(Map<String, Object> params) {
		List<TjObj> list = new ArrayList<TjObj>();
		List<Map<String,Object>> list2 = tbDayIncomeBillDao.findO(params);
		for(int i=0;i<list2.size();i++){		
			TjObj tj = new TjObj();
			tj.setBaseCom("营业额汇总表");
			tj.setSelfcom(list2.get(i).get("columnDetial").toString());
			tj.setObjvalue(new BigDecimal(list2.get(i).get("Money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
			list.add(tj);			
	}
		return list;
	}
	
	/**
	 * 优惠分析表
	 */
	@SuppressWarnings("static-access")
	@Override
	public void exportDaliyRport1(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		/*List<TjObj> list = tbDayIncomeBillDao.findCoupons(params);
		String vasd =  params.get("names").toString();
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcle(list,params, vasd, req,resp);*/
		
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void exportDaliyRport2(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		/*List<TjObj> list = getDaliyReport2(params);
		String vasd = params.get("names").toString();
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcle(list,params, vasd, req,resp);*/
	}
	public List<Dishsalerept> findItemDetail(Map<String, Object> params) {
		List<Dishsalerept> list = new ArrayList<Dishsalerept>();
		List<Map<String,Object>> listCou = tbDayIncomeBillDao.page(params);
		for(int i=0;i<listCou.size();i++){		
			Dishsalerept dis = new Dishsalerept();
			if(listCou.get(i).get("itemDesc")!=null && listCou.get(i).get("itemDesc")!=""){
				dis.setItemDesc(listCou.get(i).get("itemDesc").toString());
			}
			if(listCou.get(i).get("itemID")!=null && listCou.get(i).get("itemID")!=""){
				dis.setItemID(listCou.get(i).get("itemID").toString());
			}
			if(listCou.get(i).get("dishtype")!=null && listCou.get(i).get("dishtype")!=""){
				dis.setDishtype(listCou.get(i).get("dishtype").toString());
			}
			if(listCou.get(i).get("pinNum")!=null && listCou.get(i).get("pinNum")!=""){
				dis.setPinNum(listCou.get(i).get("pinNum").toString());
			}
			if(listCou.get(i).get("pinName")!=null && listCou.get(i).get("pinName")!=""){
				dis.setPinName(listCou.get(i).get("pinName").toString());
			}
			if(listCou.get(i).get("nums")!=null && listCou.get(i).get("nums")!=""){
				dis.setNums(listCou.get(i).get("nums").toString());
			}
			if(listCou.get(i).get("unit")!=null && listCou.get(i).get("unit")!=""){
				 if(listCou.get(i).get("unit").equals("*")){
						
				 }else{
				    dis.setUnit(listCou.get(i).get("unit").toString());
				  }
		    }
			if(listCou.get(i).get("price")!=null && listCou.get(i).get("price")!=""){
				 if(listCou.get(i).get("price").equals("*")){
						
				 }else{
					 if(listCou.get(i).get("price")!=null && listCou.get(i).get("price")!=""){
						 String Str = listCou.get(i).get("price").toString();
					     String [] strs = Str.split("[/]");
					     BigDecimal price;
					     if(strs.length>0){
					    	  price = new BigDecimal(strs[0]).setScale(2, BigDecimal.ROUND_HALF_UP);
					     }else{
					    	 price = new BigDecimal(listCou.get(i).get("price").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					     }
					     dis.setPrice(price);
					 }
					
				 }
		    }
			if(listCou.get(i).get("share")!=null && listCou.get(i).get("share")!=""){
				BigDecimal shareResult = new BigDecimal(listCou.get(i).get("share").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				dis.setShare(shareResult);
			}
			list.add(dis);			
	   } 
		return list;
	}
	public List<Dishsalerept> grid(Map<String, Object> params) {
		List<Dishsalerept> list = new ArrayList<Dishsalerept>();
		List<Map<String,Object>> listCou = tbDayIncomeBillDao.page(params);
		for(int i=0;i<listCou.size();i++){		
			Dishsalerept dis = new Dishsalerept();
			if(listCou.get(i).get("itemDesc")!=null && listCou.get(i).get("itemDesc")!=""){
				dis.setItemDesc(listCou.get(i).get("itemDesc").toString());
			}
			if(listCou.get(i).get("itemID")!=null && listCou.get(i).get("itemID")!=""){
				dis.setItemID(listCou.get(i).get("itemID").toString());
			}
			if(listCou.get(i).get("dishtype")!=null && listCou.get(i).get("dishtype")!=""){
				dis.setDishtype(listCou.get(i).get("dishtype").toString());
			}
			if(listCou.get(i).get("pinNum")!=null && listCou.get(i).get("pinNum")!=""){
				dis.setPinNum(listCou.get(i).get("pinNum").toString());
			}
			if(listCou.get(i).get("pinName")!=null && listCou.get(i).get("pinName")!=""){
				dis.setPinName(listCou.get(i).get("pinName").toString());
			}
			if(listCou.get(i).get("nums")!=null && listCou.get(i).get("nums")!=""){
				dis.setNums(listCou.get(i).get("nums").toString());
			}
			if(listCou.get(i).get("share")!=null && listCou.get(i).get("share")!=""){
				BigDecimal shareResult = new BigDecimal(listCou.get(i).get("share").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				dis.setShare(shareResult);
			}
			list.add(dis);			
	   } 
		return list;
	}

	public Page<Map<String, Object>> gridB(Map<String, Object> params,
			int current, int pagesize) {
		// TODO Auto-generated method stub
		return  tbDayIncomeBillDao.pageB(params, current, pagesize);
	}
	
	/**
	 * 查询需所有的桌号
	 */
	public  List<Code>  getTableNo(Map<String, Object>  params){
		// TODO Auto-generated method stub
		List<Map<String,Object>> TableNo =  tbDayIncomeBillDao.getTableNo(params);
		List<Code> tableNoList = new ArrayList<Code>();
		if(TableNo.size()>0){
			for (int i = 0; i < TableNo.size(); i++) {
			     Code tableCode = new Code();
			     if(TableNo.get(i).get("tableNo")!=null && TableNo.get(i).get("tableNo")!=""){
			    	 tableCode.setCodeId(TableNo.get(i).get("tableNo").toString());
			     }
			     if(TableNo.get(i).get("areaname")!=null && TableNo.get(i).get("areaname")!=""){
			    	 tableCode.setCodeDesc(TableNo.get(i).get("areaname").toString());
			     }
			     if(TableNo.get(i).get("areaid")!=null && TableNo.get(i).get("areaid")!=""){
			    	 tableCode.setAredId(TableNo.get(i).get("areaid").toString());
			     }
			     tableNoList.add(tableCode);
			}
		}
		return tableNoList;
	}
	
	public List<PaywayRpet> gridC(Map<String, Object> params) {
		List<PaywayRpet> list = new ArrayList<PaywayRpet>();
		List<Map<String,Object>> listCou = tbDayIncomeBillDao.pageC(params);
		for(int i=0;i<listCou.size();i++){		
			PaywayRpet payway = new PaywayRpet();
			if(listCou.get(i).get("pinpai")!=null && listCou.get(i).get("pinpai")!=""){
				payway.setPinpai(listCou.get(i).get("pinpai").toString());
			}
			if(listCou.get(i).get("shichang")!=null && listCou.get(i).get("shichang")!=""){
				payway.setShichang(listCou.get(i).get("shichang").toString());
			}
			if(listCou.get(i).get("department")!=null && listCou.get(i).get("department")!=""){
				payway.setDepartment(listCou.get(i).get("department").toString());
			}
			if(listCou.get(i).get("shopName")!=null && listCou.get(i).get("shopName")!=""){
				payway.setShopName(listCou.get(i).get("shopName").toString());
			}
			if(listCou.get(i).get("paytype")!=null && listCou.get(i).get("paytype")!=""){
				payway.setPaytype(listCou.get(i).get("paytype").toString());
			}
			if(listCou.get(i).get("payway")!=null && listCou.get(i).get("payway")!=""){
				payway.setPayway(listCou.get(i).get("payway").toString());
			}
			if(listCou.get(i).get("nums")!=null && listCou.get(i).get("nums")!=""){
				payway.setNums(listCou.get(i).get("nums").toString());
			}
			if(listCou.get(i).get("prices")!=null && listCou.get(i).get("prices")!=""){
				BigDecimal pricesRsult= new BigDecimal(listCou.get(i).get("prices").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				payway.setPrices(pricesRsult);
			}
			if(listCou.get(i).get("yinshou")!=null && listCou.get(i).get("yinshou")!=""){
				BigDecimal yinshouRsult = new BigDecimal(listCou.get(i).get("yinshou").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				payway.setYinshou(yinshouRsult);
		    }
			if(listCou.get(i).get("shishou")!=null && listCou.get(i).get("shishou")!=""){
				BigDecimal shishouRsult = new BigDecimal(listCou.get(i).get("shishou").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				payway.setShishou(shishouRsult);
		    }
			list.add(payway);
		}
		return list;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void exportxlsA(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Map<String,Object>> list = tbDayIncomeBillDao.getItemDetailList(params);
		String vasd ="品项销售明细表";
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcleA(list,params, vasd, req,resp);
	}
	
	@SuppressWarnings("static-access")
	@Override
	/*public void exportxlsB(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<CouponsRept> list = findCoupons(params);
		String vasd ="优惠方式明细表";
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcleB(list,params, vasd, req,resp);
	}*/
	public void exportxlsB(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<CouponsRept> list = findCoupons(params);
		List<Base_CouponsRept> baseList =new ArrayList<Base_CouponsRept>();
		for (CouponsRept couponsRept : list) {
			Base_CouponsRept baseC = new Base_CouponsRept();
			//活动名称
			if(couponsRept.getCouponsname()!=null && couponsRept.getCouponsname()!=""){
				baseC.setCouponsname(couponsRept.getCouponsname());
		    }
			//活动类型
			if(StringUtils.isNotBlank((String)couponsRept.getTypeName())){
				baseC.setTypeName(couponsRept.getTypeName());
			}
			//结算方式
			if(couponsRept.getPaytype()!=null && couponsRept.getPaytype()!=""){
				baseC.setPaytype(couponsRept.getPaytype());
			}
			//结算方式code
			if(couponsRept.getPayway()!=null && couponsRept.getPayway()!=""){
				baseC.setPayway(couponsRept.getPayway());
			}
			//笔数
			if(couponsRept.getNum()!=null){
				baseC.setNum(couponsRept.getNum());
		    }
			//发生金额
			if(couponsRept.getTotal()!=null){
				baseC.setTotal(couponsRept.getTotal());
			}
			//拉动应收
			if(couponsRept.getYinshou()!=null){
				baseC.setYinshou(couponsRept.getYinshou());
		    }
			//拉动实收
			if(couponsRept.getShishou()!=null){
				baseC.setShishou(couponsRept.getShishou());
		    }
			baseList.add(baseC);
			if(params.get("codes")!=null && !params.get("codes").equals("")){
				String codes = params.get("codes").toString();
				String[] codeStr = codes.split("\\|");
				for (int i = 0; i < codeStr.length; i++) {
					if(codeStr[i]!=null&&codeStr[i]!=""){
						String[] StrCodes = codeStr[i].split(",");
						if(StrCodes[0].equals(couponsRept.getCode())&&StrCodes[1].equals(couponsRept.getPayway())){
							params.put("codeid", StrCodes[0]);
							params.put("payway", StrCodes[1]);
							List<CouponsReptDtail> listDtail = findCouponsDtail(params);
							Base_CouponsRept baseB= new Base_CouponsRept();
							baseB.setCouponsname("发生时间");
							baseB.setTypeName("订单号");
							baseB.setPaytype("结算方式");
							baseB.setNum("发生笔数");
							baseB.setTotal("发生金额");
							baseB.setYinshou("拉动应收");
							baseB.setShishou("拉动实收");
							baseList.add(baseB);
							for (CouponsReptDtail couponsReptDtail : listDtail) {
								Base_CouponsRept base = new Base_CouponsRept();
								if(couponsRept.getCode()!=null&&couponsRept.getCode()!=""){
									if(couponsReptDtail.getCode()!=null&&couponsReptDtail.getCode()!=""){
										if(couponsRept.getCode().equals(couponsReptDtail.getCode())){
											//发生时间==活动名称
											if(couponsReptDtail.getInsertTime()!=null&&couponsReptDtail.getInsertTime()!=""){
												base.setCouponsname(couponsReptDtail.getInsertTime());
											}
											//订单号==活动类型
											if(couponsReptDtail.getOrderid()!=null&&couponsReptDtail.getOrderid()!=""){
												base.setTypeName(couponsReptDtail.getOrderid());
											}
											//结算方式==结算金额
											if(couponsReptDtail.getPayamount()!=null){
												base.setPaytype(couponsReptDtail.getPayamount().toString());
											}
											//发生笔数==发生笔数
											if(couponsReptDtail.getNum()!=null){
												base.setNum(couponsReptDtail.getNum());
											}
											//发生金额==发生金额
											if(couponsReptDtail.getTotal()!=null){
												base.setTotal(couponsReptDtail.getTotal());
											}
											//拉动应收==拉动应收
											if(couponsReptDtail.getYinshou()!=null){
												base.setYinshou(couponsReptDtail.getYinshou());
											}
											//拉动实收==拉动实收
											if(couponsReptDtail.getShishou()!=null){
												base.setShishou(couponsReptDtail.getShishou());
											}
											baseList.add(base);
										}
									}
								}
							}
						}
					}
				}
			
			}
		}
		String vasd ="优惠方式明细表";
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcleB(baseList,params, vasd, req,resp);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void exportxlsC(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Map<String,Object>> list = tbDayIncomeBillDao.findFC(params);
		String vasd ="结算方式明细表";
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcleC(list,params, vasd, req,resp);
	}
	/**
	 * 营业图表明细数据查询
	 */
	@Override
	public  List<BusinessReport> getBusinessDetail(Map<String, Object> params) {
		List<TjObj> businessR  = getDaliyReport(params);
		List<BusinessReport> businessList = new ArrayList<BusinessReport>();
		if(businessR.size()>0){
			BusinessReport businssRport = new BusinessReport();
	        	for(int i=0;i<businessR.size();i++){
	         		if(businessR.get(i)!=null){
	         			//营业数据收入统计栏
	         			//应收总额
	         			if(i==0){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setShouldamount(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//实收总额
	         			if(i==1){
	         				if(businessR.get(i).getObjvalue()!=null){
	         					
		             			businssRport.setPaidinamount(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//折扣总额
	         			if(i==2){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setDiscountamount(businessR.get(i).getObjvalue());
		        			}
	         			}
	         			
	         			//实收明细统计栏
	             		//人民币
	         			if(i==4){
		             		if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setMoney(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//挂账
	         			if(i==5){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setCard(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//刷卡（元）-工商银行
	         			if(i==6){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setICBC(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		
	             		//刷卡（元）-其他银行
	         			if(i==7){
	         				if(businessR.get(i).getObjvalue()!=null ){
		             			businssRport.setOtherbank(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		
	             		//会员积分消费（元）Merbervaluenet
	         			if(i==11){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setIntegralconsum(businessR.get(i).getObjvalue());
		        			}
	         			}
	         			//合计
	         			if(i==14){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setTotal(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		
	         			if(i==10){
	         				//会员券消费（元）
		             		if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setMeberTicket(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//会员储值消费净值（元）
	         			if(i==12){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setMerbervaluenet(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//会员储值消费虚增（元）
	         			if(i==13){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setMebervalueadd(businessR.get(i).getObjvalue());
		        			}
	         			}
	         			
	             		//桌数（桌）
	         			if(i==18){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setTablenum(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//结算人数（个）
	         			if(i==19){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setSettlementnum(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//平均消费时间（分）
	         			if(i==20){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setAvgconsumtime(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//上座率（%）
	         			if(i==21){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setAttendance(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//应收人均（元）
	         			if(i==22){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setShouldaverage(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//实收人均（元）
	         			if(i==23){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setPaidinaverage(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//桌均消费
	         			if(i==24){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setTableconsumption(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//翻台率(%)
	         			if(i==25){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setOvertaiwan(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		
	             		//优免（元）
	         			if(i==26){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setBastfree(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		
	             		//折扣优惠
	         			if(i==30){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setDiscountmoney(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		//抹零收入
	         			if(i==31){
	         				if(businessR.get(i).getObjvalue()!=null){
		             			businssRport.setMalingincom(businessR.get(i).getObjvalue());
		        			}
	         			}
	             		businessList.add(businssRport);
	         		}
	         	}
	        }
	         
	        return businessList;
	}


	/**
	 * //格式化日期格式
	 * @param params
	 * @param beginTime
	 * @param datetype
	 * johnson
	 */
	private void getformatDayParam(Map<String, Object> params, String beginTime, String datetype) {


		if (StringUtils.equals(datetype, "M")) {//月查询
			int month = Integer.parseInt(beginTime.split("-")[1]);
			int year = Integer.parseInt(beginTime.split("-")[0]);
			params.put("beginTime", DateTimeUtils.getMonthFirstDay(month, year));
			params.put("endTime", DateTimeUtils.getMonthLastDay(month,year));
		}
		if (StringUtils.equals(datetype, "Y")) {//年查询
			int year = Integer.parseInt(beginTime);
			params.put("beginTime", DateTimeUtils.getMonthFirstDay(1,year));
			params.put("endTime", DateTimeUtils.getMonthLastDay(12,year));
		}
	}


	private  Double sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return new Double(b1.subtract(b2).doubleValue());
	}


	private   Double div(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.divide(b2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	public List<BusinessReport> findBusinessReport(Map<String, Object> params){


	  List<String> dateArrary = DateTimeUtils.getDateArrayByParams(params);
	  List<Map<String, Object>> businessR = new ArrayList<>();

	  for (String s : dateArrary) {
		  String beginTime = s;
		  String endTime = s;
		  String datetype = params.get("Datetype").toString();

		  HashMap map = new HashMap();
		  map.put("beginTime", beginTime);
		  map.put("endTime", endTime);
		  map.put("datetype", datetype);
		  //格式化日期格式
		  getformatDayParam(map, s, datetype);
		  List<Map<String, Object>> tempbusinessR = tbDayIncomeBillDao.findBusinessReport(map);
		  List<Map<String, Object>> getorderMembervalue = tbDayIncomeBillDao.getorderMembervalue(map);
		  List<Map<String, Object>> getxuzheng = tbDayIncomeBillDao.getxuzheng(map);
		  if(tempbusinessR.size()>0) {

			  Map tempbusiness = tempbusinessR.get(0);
			  Map orderMember = getorderMembervalue.get(0);
			  if (tempbusiness.get("Shouldamount") == null) {
				  continue;
			  }
			  String shouldamount =  tempbusiness.get("Shouldamount")==null?"":tempbusiness.get("Shouldamount").toString();   // 应收
			  //实收 当会员卡消费需要减去净值消费
			  String paidinamount =orderMember.get("Paidinamount")==null?"":orderMember.get("Paidinamount").toString();
			  String xuzheng = getxuzheng.get(0).get("Inflated")==null?"":getxuzheng.get(0).get("Inflated").toString();

			  double paidinamountresult =sub(orderMember.get("Paidinamount")==null?"":orderMember.get("Paidinamount").toString(), xuzheng);
			  double discountamount =sub(shouldamount, (paidinamountresult+""));   //折扣金额
			  double  discount = 0.0;
			  if (shouldamount.equals("0.0")) {
				  discount = 0.0;
			  }else {
				  discount = div(discountamount+"",shouldamount);      //折扣率
			  }

			  tempbusiness.put("Shouldamount",shouldamount);
			  tempbusiness.put("Paidinamount",paidinamountresult);
			  tempbusiness.put("Discountamount",discountamount);
			  tempbusiness.put("Discount",discount);
			  businessR.add(tempbusiness);
		  }
	  }




	  List<BusinessReport> businessList = new ArrayList<BusinessReport>();
        if(businessR.size()>0){
        	for(int i=0;i<businessR.size();i++){		
         		BusinessReport businssRport = new BusinessReport();
         		if(businessR.get(i)!=null){
         			//应收总额
         			if(businessR.get(i).get("Shouldamount")!=null && businessR.get(i).get("Shouldamount")!=""){
             			BigDecimal shouldamountResult= new BigDecimal(businessR.get(i).get("Shouldamount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setShouldamount(shouldamountResult);
        			}else{
        				BigDecimal shouldamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setShouldamount(shouldamountResult);
        			}
             		//实收总额
             		if(businessR.get(i).get("Paidinamount")!=null && businessR.get(i).get("Paidinamount") !=""){
             			BigDecimal PaidinamountResult= new BigDecimal(businessR.get(i).get("Paidinamount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setPaidinamount(PaidinamountResult);
        			}else{
        				BigDecimal PaidinamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setPaidinamount(PaidinamountResult);
        			}
             		//折扣总额
             		if(businessR.get(i).get("Discountamount")!=null && businessR.get(i).get("Discountamount")!=""){
             			BigDecimal DiscountamountResult= new BigDecimal(businessR.get(i).get("Discountamount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setDiscountamount(DiscountamountResult);
        			}else{
        				BigDecimal DiscountamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setDiscountamount(DiscountamountResult);
        			}
             		//折扣率（%）
             		if(businessR.get(i).get("Discount")!=null && businessR.get(i).get("Discount")!=""){
             			BigDecimal DiscountResult= new BigDecimal(businessR.get(i).get("Discount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setDiscount(DiscountResult);
        			}else{
        				BigDecimal DiscountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setDiscount(DiscountResult);
        			}
             		//人民币
             		if(businessR.get(i).get("Money")!=null && businessR.get(i).get("Money")!=""){
             			BigDecimal MoneyResult= new BigDecimal(businessR.get(i).get("Money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMoney(MoneyResult);
        			}else{
        				BigDecimal MoneyResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMoney(MoneyResult);
        			}
             		//挂账
             		if(businessR.get(i).get("Card")!=null && businessR.get(i).get("Card") !=""){
             			BigDecimal CardeRsult= new BigDecimal(businessR.get(i).get("Card").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setCard(CardeRsult);
        			}else{
        				BigDecimal CardeRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setCard(CardeRsult);
        			}
             		//刷卡（元）-工商银行
             		if(businessR.get(i).get("ICBC")!=null && businessR.get(i).get("ICBC")!=""){
             			BigDecimal ICBCRsult= new BigDecimal(businessR.get(i).get("ICBC").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setICBC(ICBCRsult);
        			}else{
        				BigDecimal ICBCRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setICBC(ICBCRsult);
        			}
             		//刷卡（元）-其他银行
             		if(businessR.get(i).get("Otherbank")!=null && businessR.get(i).get("Otherbank")!=""){
             			BigDecimal OtherbankRsult= new BigDecimal(businessR.get(i).get("Otherbank").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setOtherbank(OtherbankRsult);
        			}else{
        				BigDecimal OtherbankRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setOtherbank(OtherbankRsult);
        			}
             		//会员积分消费（元）
             		if(businessR.get(i).get("Merbervaluenet")!=null && businessR.get(i).get("Merbervaluenet") !=""){
             			BigDecimal MerbervaluenetRsult= new BigDecimal(businessR.get(i).get("Merbervaluenet").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMerbervaluenet(MerbervaluenetRsult);
        			}else{
        				BigDecimal MerbervaluenetRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMerbervaluenet(MerbervaluenetRsult);
        			}
             		//合计（元）
             		if(businessR.get(i).get("Total")!=null && businessR.get(i).get("Total")!=""){
             			BigDecimal TotalRsult= new BigDecimal(businessR.get(i).get("Total").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setTotal(TotalRsult);
        			}else{
        				BigDecimal TotalRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setTotal(TotalRsult);
        			}
             		//会员券消费（元）
             		if(businessR.get(i).get("MeberTicket")!=null && businessR.get(i).get("MeberTicket")!=""){
             			BigDecimal MeberTicketRsult= new BigDecimal(businessR.get(i).get("MeberTicket").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMeberTicket(MeberTicketRsult);
        			}else{
        				BigDecimal MeberTicketRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMeberTicket(MeberTicketRsult);
        			}
             		//会员储值消费净值（元）
             		if(businessR.get(i).get("Merbervaluenet")!=null && businessR.get(i).get("Merbervaluenet") !=""){
             			BigDecimal MerbervaluenetRsult= new BigDecimal(businessR.get(i).get("Merbervaluenet").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMerbervaluenet(MerbervaluenetRsult);
        			}else{
        				BigDecimal MerbervaluenetRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMerbervaluenet(MerbervaluenetRsult);
        			}
             		//会员储值消费虚增（元）
             		if(businessR.get(i).get("Mebervalueadd")!=null && businessR.get(i).get("Mebervalueadd")!=""){
             			BigDecimal MebervalueaddRsult= new BigDecimal(businessR.get(i).get("Mebervalueadd").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMebervalueadd(MebervalueaddRsult);
        			}else{
        				BigDecimal MebervalueaddRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMebervalueadd(MebervalueaddRsult);
        			}
             		//会员消费汇总（元）
             		if(businessR.get(i).get("Mebercousumcollet")!=null && businessR.get(i).get("Mebercousumcollet")!=""){
             			BigDecimal MebercousumcolletRsult= new BigDecimal(businessR.get(i).get("Mebercousumcollet").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMebercousumcollet(MebercousumcolletRsult);
        			}else{
        				BigDecimal MebercousumcolletRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMebercousumcollet(MebercousumcolletRsult);
        			}
             		//桌数（桌）
             		if(businessR.get(i).get("Tablenum")!=null && businessR.get(i).get("Tablenum")!=""){
             			BigDecimal TablenumResult= new BigDecimal(businessR.get(i).get("Tablenum").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setTablenum(TablenumResult);
        			}else{
        				BigDecimal TablenumResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setTablenum(TablenumResult);
        			}
             		//结算人数（个）
             		if(businessR.get(i).get("Settlementnum")!=null && businessR.get(i).get("Settlementnum") !=""){
             			BigDecimal SettlementnumResult= new BigDecimal(businessR.get(i).get("Settlementnum").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setSettlementnum(SettlementnumResult);
        			}else{
        				BigDecimal SettlementnumResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setSettlementnum(SettlementnumResult);
        			}
             		//平均消费时间（分）
             		if(businessR.get(i).get("Avgconsumtime")!=null && businessR.get(i).get("Avgconsumtime")!=""){
             			BigDecimal AvgconsumtimeRsult= new BigDecimal(businessR.get(i).get("Avgconsumtime").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setAvgconsumtime(AvgconsumtimeRsult);
        			}else{
        				BigDecimal AvgconsumtimeRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setAvgconsumtime(AvgconsumtimeRsult);
        			}
             		//上座率（%）
             		if(businessR.get(i).get("Attendance")!=null && businessR.get(i).get("Attendance")!=""){
             			BigDecimal AttendanceRsult= new BigDecimal(businessR.get(i).get("Attendance").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setAttendance(AttendanceRsult);
        			}else{
        				BigDecimal AttendanceRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setAttendance(AttendanceRsult);
        			}
             		//应收人均（元）
             		if(businessR.get(i).get("Shouldaverage")!=null && businessR.get(i).get("Shouldaverage")!=""){
             			BigDecimal ShouldaverageRsult= new BigDecimal(businessR.get(i).get("Shouldaverage").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setShouldaverage(ShouldaverageRsult);
        			}else{
        				BigDecimal ShouldaverageRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setShouldaverage(ShouldaverageRsult);
        			}
             		//实收人均（元）
             		if(businessR.get(i).get("Paidinaverage")!=null && businessR.get(i).get("Paidinaverage")!=""){
             			BigDecimal PaidinaverageRsult= new BigDecimal(businessR.get(i).get("Paidinaverage").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setPaidinaverage(PaidinaverageRsult);
        			}else{
        				BigDecimal PaidinaverageRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setPaidinaverage(PaidinaverageRsult);
        			}
             		//桌均消费
             		if(businessR.get(i).get("Tableconsumption")!=null && businessR.get(i).get("Tableconsumption")!=""){
             			BigDecimal TableconsumptionRsult= new BigDecimal(businessR.get(i).get("Tableconsumption").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setTableconsumption(TableconsumptionRsult);
        			}else{
        				BigDecimal TableconsumptionRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setTableconsumption(TableconsumptionRsult);
        			}
             		//翻台率(%)
             		if(businessR.get(i).get("Overtaiwan")!=null && businessR.get(i).get("Overtaiwan") !=""){
             			BigDecimal OvertaiwanRsult= new BigDecimal(businessR.get(i).get("Overtaiwan").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setOvertaiwan(OvertaiwanRsult);
        			}else{
        				BigDecimal OvertaiwanRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setOvertaiwan(OvertaiwanRsult);
        			}
             		//优免（元）
             		if(businessR.get(i).get("Bastfree")!=null && businessR.get(i).get("Bastfree") !=""){
             			BigDecimal BastfreeRsult= new BigDecimal(businessR.get(i).get("Bastfree").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setBastfree(BastfreeRsult);
        			}else{
        				BigDecimal BastfreeRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setBastfree(BastfreeRsult);
        			}
             		//折扣优惠
             		if(businessR.get(i).get("Discountmoney")!=null && (businessR.get(i).get("Discountmoney"))!=""){
             			BigDecimal DiscountmoneyRsult= new BigDecimal((businessR.get(i).get("Discountmoney")).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setDiscountmoney(DiscountmoneyRsult);
        			}else{
        				BigDecimal DiscountmoneyRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setDiscountmoney(DiscountmoneyRsult);
        			}
             		//抹零收入
             		if(businessR.get(i).get("Malingincom")!=null && businessR.get(i).get("Malingincom") !=""){
             			BigDecimal MalingincomRsult= new BigDecimal(businessR.get(i).get("Malingincom").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMalingincom(MalingincomRsult);
        			}else{
        				BigDecimal MalingincomRsult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
             			businssRport.setMalingincom(MalingincomRsult);
        			}
             		//时间
             		if(businessR.get(i).get("Statistictime")!=null && businessR.get(i).get("Statistictime") !=""){
             			String datatype = params.get("Datetype").toString();
            			 SimpleDateFormat formatter = null ;
            			 String dateStr = businessR.get(i).get("Statistictime").toString();
    	     			 Date Statistictime = null;
            			if(datatype.equals("D")){
							businssRport.setStatistictime(dateStr);
            			}
            			if(datatype.equals("M")){

							businssRport.setStatistictime(dateStr.split("-")[0]+"-"+dateStr.split("-")[1]);
            			}
            			if(datatype.equals("Y")){
							businssRport.setStatistictime(dateStr.split("-")[0]);

						}
       			     }else{
       			    	businssRport.setStatistictime("");
       			     }
             		businessList.add(businssRport);
         		}
         	}
        }
         
        return businessList;
     }
	 
    /**
     * <!-- 优惠分析饼图数根据活动名称分组据查询 -->
     *findGroupByNmaeReport
     */
     public   List<PreferentialReport> findGroupByNmaeReport(Map<String, Object>  params){
    	 List<Map<String,Object>> Preferential = tbDayIncomeBillDao.findGroupByNmaeReport(params);
		 List<PreferentialReport> PreferentialList = new ArrayList<PreferentialReport>();
		 if(Preferential!=null){
			 if(Preferential.size()>0){
				 for (int i = 0; i < Preferential.size(); i++) {
					 PreferentialReport pr = new PreferentialReport();
					 //活动类型
					 if(Preferential.get(i).get("activity_type")!=null && Preferential.get(i).get("activity_type")!=""){
						 pr.setActivitytype(Preferential.get(i).get("activity_type").toString());
					 }else{
						 pr.setActivitytype("");
					 }
					 //活动名称
					 if(Preferential.get(i).get("activity_name")!=null && Preferential.get(i).get("activity_name")!=""){
						 pr.setActivitytame(Preferential.get(i).get("activity_name").toString());
					 }else{
						 pr.setActivitytame("");
					 }
					 //结算方式
					 if(Preferential.get(i).get("pay_way")!=null && Preferential.get(i).get("pay_way")!=""){
						 pr.setPayway(Integer.parseInt(Preferential.get(i).get("pay_way").toString()));
					 }else{
						 pr.setPayway(Integer.parseInt("0"));
					 }
					 //结算方式名字
					 if(Preferential.get(i).get("pay_type")!=null && Preferential.get(i).get("pay_type")!=""){
						 pr.setPaywayDesc(Preferential.get(i).get("pay_type").toString());
					 }else{
						 pr.setPaywayDesc("");
					 }
					 //活动笔数
					 if(Preferential.get(i).get("num")!=null && Preferential.get(i).get("num")!=""){
						 pr.setNum(Integer.parseInt(Preferential.get(i).get("num").toString()));
					 }else{
						 pr.setNum(Integer.parseInt("0"));
					 }
					//发生金额
					 if(Preferential.get(i).get("amount")!=null){
						 BigDecimal AmountResult= new BigDecimal(Preferential.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setAmount(AmountResult);
					 }else{
						 BigDecimal AmountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setAmount(AmountResult);
					 }
					//拉动应收
					 if(Preferential.get(i).get("should_amount")!=null){
						 BigDecimal ShouldamountResult= new BigDecimal(Preferential.get(i).get("should_amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setShouldamount(ShouldamountResult);
					 }else{
						 BigDecimal ShouldamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setShouldamount(ShouldamountResult);
					 }
					//拉动实收
					 if(Preferential.get(i).get("paidin_amount")!=null){
						 BigDecimal PaidinamountResult= new BigDecimal(Preferential.get(i).get("paidin_amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setPaidinamount(PaidinamountResult);
					 }else{
						 BigDecimal PaidinamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setPaidinamount(PaidinamountResult);
					 }
					 PreferentialList.add(pr);
				 }
			 }
		 }
		 return PreferentialList;
     }
     /**
	  *  优惠分析趋势图数根据活动名称，日期分组据查询 
      *  findGroupByNmaeAndTimeReport
	  */
	 public   List<PreferentialReport> findGroupByNmaeAndTimeReport(Map<String, Object>  params){
		 List<Map<String,Object>> Preferential = tbDayIncomeBillDao.findGroupByNmaeAndTimeReport(params);
		 List<PreferentialReport> PreferentialList = new ArrayList<PreferentialReport>();
			 if(Preferential.size()>0){
				 for (int i = 0; i < Preferential.size(); i++) {
					 if(Preferential.get(i)!=null){
						 PreferentialReport pr = new PreferentialReport();
						 //活动类型
						 if(Preferential.get(i).get("activity_type")!=null && Preferential.get(i).get("activity_type")!=""){
							 pr.setActivitytype(Preferential.get(i).get("activity_type").toString());
						 }else{
							 pr.setActivitytype("");
						 }
						 //活动名称
						 if(Preferential.get(i).get("activity_name")!=null && Preferential.get(i).get("activity_name")!=""){
							 pr.setActivitytame(Preferential.get(i).get("activity_name").toString());
						 }else{
							 pr.setActivitytame("");
						 }
						 //结算方式
						 if(Preferential.get(i).get("pay_way")!=null && Preferential.get(i).get("pay_way")!=""){
							 pr.setPayway(Integer.parseInt(Preferential.get(i).get("pay_way").toString()));
						 }else{
							 pr.setPayway(Integer.parseInt("0"));
						 }
						 //结算方式名字
						 if(Preferential.get(i).get("pay_type")!=null && Preferential.get(i).get("pay_type")!=""){
							 pr.setPaywayDesc(Preferential.get(i).get("pay_type").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						 //活动笔数
						 if(Preferential.get(i).get("num")!=null && Preferential.get(i).get("num")!=""){
							 pr.setNum(Integer.parseInt(Preferential.get(i).get("num").toString()));
						 }else{
							 pr.setNum(Integer.parseInt("0"));
						 }
						//发生金额
						 if(Preferential.get(i).get("amount")!=null){
							 BigDecimal AmountResult= new BigDecimal(Preferential.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }else{
							 BigDecimal AmountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }
						//拉动应收
						 if(Preferential.get(i).get("should_amount")!=null){
							 BigDecimal ShouldamountResult= new BigDecimal(Preferential.get(i).get("should_amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setShouldamount(ShouldamountResult);
						 }else{
							 BigDecimal ShouldamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setShouldamount(ShouldamountResult);
						 }
						//拉动实收
						 if(Preferential.get(i).get("paidin_amount")!=null){
							 BigDecimal PaidinamountResult= new BigDecimal(Preferential.get(i).get("paidin_amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setPaidinamount(PaidinamountResult);
						 }else{
							 BigDecimal PaidinamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setPaidinamount(PaidinamountResult);
						 }
						//时间
		             	if(Preferential.get(i).get("statistic_time")!=null && Preferential.get(i).get("statistic_time") !=""){
		             			 String datatype = params.get("Datetype").toString();
		            			 SimpleDateFormat formatter = null ;
		            			 String dateStr = null;
		    	     			 Date Statistictime = null;
		            			if(datatype.equals("D")){
		            				formatter = new SimpleDateFormat("YYYY-MM-dd");
		            			}
		            			if(datatype.equals("M")){
		            				formatter = new SimpleDateFormat("YYYY-MM");
		            			}
		            			if(datatype.equals("Y")){
		            				formatter = new SimpleDateFormat("YYYY");
		            			}
		            			Statistictime = (Date) Preferential.get(i).get("statistic_time");
		            			dateStr = formatter.format(Statistictime);
		            			pr.setStatistictime(dateStr);
		       			     }else{
		       			    	pr.setStatistictime("");
		       			     }
						 PreferentialList.add(pr);
					 }
				 }
			 }
		 return PreferentialList;
	 }
	 /**
	  * 优惠分析饼图数根据活动类别分组据查询 
	  * findGroupBytypeReport
	  */
	 public   List<PreferentialReport> findGroupBytypeReport(Map<String, Object>   params){
		 List<Map<String,Object>> Preferential = tbDayIncomeBillDao.findGroupBytypeReport(params);
		 List<PreferentialReport> PreferentialList = new ArrayList<PreferentialReport>();
		 if(Preferential!=null){
			 if(Preferential.size()>0){
				 for (int i = 0; i < Preferential.size(); i++) {
					 PreferentialReport pr = new PreferentialReport();
					 //活动类型
					 if(Preferential.get(i).get("activity_type")!=null && Preferential.get(i).get("activity_type")!=""){
						 pr.setActivitytype(Preferential.get(i).get("activity_type").toString());
					 }else{
						 pr.setActivitytype("");
					 }
					 //活动名称
					 if(Preferential.get(i).get("activity_name")!=null && Preferential.get(i).get("activity_name")!=""){
						 pr.setActivitytame(Preferential.get(i).get("activity_name").toString());
					 }else{
						 pr.setActivitytame("");
					 }
					 //结算方式
					 if(Preferential.get(i).get("pay_way")!=null && Preferential.get(i).get("pay_way")!=""){
						 pr.setPayway(Integer.parseInt(Preferential.get(i).get("pay_way").toString()));
					 }else{
						 pr.setPayway(Integer.parseInt("0"));
					 }
					 //结算方式名字
					 if(Preferential.get(i).get("pay_type")!=null && Preferential.get(i).get("pay_type")!=""){
						 pr.setPaywayDesc(Preferential.get(i).get("pay_type").toString());
					 }else{
						 pr.setPaywayDesc("");
					 }
					 //活动笔数
					 if(Preferential.get(i).get("num")!=null && Preferential.get(i).get("num")!=""){
						 pr.setNum(Integer.parseInt(Preferential.get(i).get("num").toString()));
					 }else{
						 pr.setNum(Integer.parseInt("0"));
					 }
					//发生金额
					 if(Preferential.get(i).get("amount")!=null){
						 BigDecimal AmountResult= new BigDecimal(Preferential.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setAmount(AmountResult);
					 }else{
						 BigDecimal AmountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setAmount(AmountResult);
					 }
					//拉动应收
					 if(Preferential.get(i).get("should_amount")!=null){
						 BigDecimal ShouldamountResult= new BigDecimal(Preferential.get(i).get("should_amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setShouldamount(ShouldamountResult);
					 }else{
						 BigDecimal ShouldamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setShouldamount(ShouldamountResult);
					 }
					//拉动实收
					 if(Preferential.get(i).get("paidin_amount")!=null){
						 BigDecimal PaidinamountResult= new BigDecimal(Preferential.get(i).get("paidin_amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setPaidinamount(PaidinamountResult);
					 }else{
						 BigDecimal PaidinamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						 pr.setPaidinamount(PaidinamountResult);
					 }
					 PreferentialList.add(pr);
				 }
			 }
		 }
		 return PreferentialList;
	 }
	  
	 /**
	  * 优惠分析趋势图数根据活动类别，日期分组据查询 
	  * findGroupBytypeAndTimeReport
	  */
	 public   List<PreferentialReport> findGroupBytypeAndTimeReport(Map<String, Object>  params){
		 List<Map<String,Object>> Preferential = tbDayIncomeBillDao.findGroupBytypeAndTimeReport(params);
		 List<PreferentialReport> PreferentialList = new ArrayList<PreferentialReport>();
			 if(Preferential.size()>0){
				 for (int i = 0; i < Preferential.size(); i++) {
					 if(Preferential.get(i)!=null){
						 PreferentialReport pr = new PreferentialReport();
						 //活动类型
						 if(Preferential.get(i).get("activity_type")!=null && Preferential.get(i).get("activity_type")!=""){
							 pr.setActivitytype(Preferential.get(i).get("activity_type").toString());
						 }else{
							 pr.setActivitytype("");
						 }
						 //活动名称
						 if(Preferential.get(i).get("activity_name")!=null && Preferential.get(i).get("activity_name")!=""){
							 pr.setActivitytame(Preferential.get(i).get("activity_name").toString());
						 }else{
							 pr.setActivitytame("");
						 }
						 //结算方式
						 if(Preferential.get(i).get("pay_way")!=null && Preferential.get(i).get("pay_way")!=""){
							 pr.setPayway(Integer.parseInt(Preferential.get(i).get("pay_way").toString()));
						 }else{
							 pr.setPayway(Integer.parseInt("0"));
						 }
						 //结算方式名字
						 if(Preferential.get(i).get("pay_type")!=null && Preferential.get(i).get("pay_type")!=""){
							 pr.setPaywayDesc(Preferential.get(i).get("pay_type").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						 //活动笔数
						 if(Preferential.get(i).get("num")!=null && Preferential.get(i).get("num")!=""){
							 pr.setNum(Integer.parseInt(Preferential.get(i).get("num").toString()));
						 }else{
							 pr.setNum(Integer.parseInt("0"));
						 }
						//发生金额
						 if(Preferential.get(i).get("amount")!=null){
							 BigDecimal AmountResult= new BigDecimal(Preferential.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }else{
							 BigDecimal AmountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }
						//拉动应收
						 if(Preferential.get(i).get("should_amount")!=null){
							 BigDecimal ShouldamountResult= new BigDecimal(Preferential.get(i).get("should_amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setShouldamount(ShouldamountResult);
						 }else{
							 BigDecimal ShouldamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setShouldamount(ShouldamountResult);
						 }
						//拉动实收
						 if(Preferential.get(i).get("paidin_amount")!=null){
							 BigDecimal PaidinamountResult= new BigDecimal(Preferential.get(i).get("paidin_amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setPaidinamount(PaidinamountResult);
						 }else{
							 BigDecimal PaidinamountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setPaidinamount(PaidinamountResult);
						 }
						//时间
			             if(Preferential.get(i).get("statistic_time")!=null && Preferential.get(i).get("statistic_time") !=""){
			             		String datatype = params.get("Datetype").toString();
			            		SimpleDateFormat formatter = null ;
			            		String dateStr = null;
			    	     		Date Statistictime = null;
			            		if(datatype.equals("D")){
			            				formatter = new SimpleDateFormat("YYYY-MM-dd");
			            			}
			            		if(datatype.equals("M")){
			            				formatter = new SimpleDateFormat("YYYY-MM");
			            			}
			            		if(datatype.equals("Y")){
			            				formatter = new SimpleDateFormat("YYYY");
			            			}
			            			Statistictime = (Date) Preferential.get(i).get("statistic_time");
			            			dateStr = formatter.format(Statistictime);
			            			pr.setStatistictime(dateStr);
			       			     }else{
			       			    	pr.setStatistictime("");
			       			     }
						 PreferentialList.add(pr);
					 }
				 }
			 }
	
		 return PreferentialList;
	 }
	/**
	 * 优惠分析饼图数根据支付类型、活动名称分组据查询(发生金额) 
	 */
	public List<PreferentialReport> findGroupByPaywayandName(Map<String, Object> params){
		 List<Map<String,Object>> Preferential = tbDayIncomeBillDao.findGroupByPaywayandName(params);
		 List<PreferentialReport> PreferentialList = new ArrayList<PreferentialReport>();
			 if(Preferential.size()>0){
				 for (int i = 0; i < Preferential.size(); i++) {
					 if(Preferential.get(i)!=null){
						 PreferentialReport pr = new PreferentialReport();
						 String paytype="";
						 //结算方式名字
						 if(Preferential.get(i).get("paytype")!=null && Preferential.get(i).get("paytype")!=""){
							 paytype = Preferential.get(i).get("paytype").toString();
							 pr.setPaywayDesc(Preferential.get(i).get("paytype").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						 //活动名称
						 if(Preferential.get(i).get("activity_name")!=null && Preferential.get(i).get("activity_name")!=""){
							 pr.setName(Preferential.get(i).get("activity_name").toString());
							 pr.setActivitytame(Preferential.get(i).get("activity_name").toString()+"("+paytype+")");
						 }else{
							 pr.setName("");
							 pr.setActivitytame("");
						 }
						 //结算方式
						 if(Preferential.get(i).get("payway")!=null && Preferential.get(i).get("payway")!=""){
							 pr.setPayway(Integer.parseInt(Preferential.get(i).get("payway").toString()));
						 }else{
							 pr.setPayway(Integer.parseInt("0"));
						 }
						//结算方式名称
						 if(Preferential.get(i).get("paytype")!=null && Preferential.get(i).get("paytype")!=""){
							 pr.setPaywayDesc(Preferential.get(i).get("paytype").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						//发生金额
						 if(Preferential.get(i).get("amount")!=null){
							 BigDecimal AmountResult= new BigDecimal(Preferential.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }else{
							 BigDecimal AmountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }
						//时间
			             if(Preferential.get(i).get("statistic_time")!=null && Preferential.get(i).get("statistic_time") !=""){
			             		String datatype = params.get("Datetype").toString();
			            		SimpleDateFormat formatter = null ;
			            		String dateStr = null;
			    	     		Date Statistictime = null;
			            		if(datatype.equals("D")){
			            				formatter = new SimpleDateFormat("YYYY-MM-dd");
			            			}
			            		if(datatype.equals("M")){
			            				formatter = new SimpleDateFormat("YYYY-MM");
			            			}
			            		if(datatype.equals("Y")){
			            				formatter = new SimpleDateFormat("YYYY");
			            			}
			            			Statistictime = (Date) Preferential.get(i).get("statistic_time");
			            			dateStr = formatter.format(Statistictime);
			            			pr.setStatistictime(dateStr);
			       			     }else{
			       			    	pr.setStatistictime("");
			       			     }
						 PreferentialList.add(pr);
					 }
				 }
			 }
	
		 return PreferentialList;
	}
	/**
	 * 优惠分析饼图数根据支付类型、活动名称、日期分组据查询(发生金额)	
	 */
	public List<PreferentialReport> findGroupByPaywayandNameandtiem(Map<String, Object> params){
		 List<Map<String,Object>> Preferential = tbDayIncomeBillDao.findGroupByPaywayandNameandtiem(params);
		 List<PreferentialReport> PreferentialList = new ArrayList<PreferentialReport>();
			 if(Preferential.size()>0){
				 for (int i = 0; i < Preferential.size(); i++) {
					 if(Preferential.get(i)!=null){
						 PreferentialReport pr = new PreferentialReport();
						 //结算方式名字
						 if(Preferential.get(i).get("paytype")!=null && Preferential.get(i).get("paytype")!=""){
							 pr.setPaywayDesc(Preferential.get(i).get("paytype").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						 //活动名称
						 if(Preferential.get(i).get("activity_name")!=null && Preferential.get(i).get("activity_name")!=""){
							 pr.setActivitytame(Preferential.get(i).get("activity_name").toString());
						 }else{
							 pr.setActivitytame("");
						 }
						 //结算方式
						 if(Preferential.get(i).get("payway")!=null && Preferential.get(i).get("payway")!=""){
							 pr.setPayway(Integer.parseInt(Preferential.get(i).get("payway").toString()));
						 }else{
							 pr.setPayway(Integer.parseInt("0"));
						 }
						//结算方式名称
						 if(Preferential.get(i).get("paytype")!=null && Preferential.get(i).get("paytype")!=""){
							 pr.setPaywayDesc(Preferential.get(i).get("paytype").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						//发生金额
						 if(Preferential.get(i).get("amount")!=null){
							 BigDecimal AmountResult= new BigDecimal(Preferential.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }else{
							 BigDecimal AmountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }
						//时间
			             if(Preferential.get(i).get("statistic_time")!=null && Preferential.get(i).get("statistic_time") !=""){
			             		String datatype = params.get("Datetype").toString();
			            		SimpleDateFormat formatter = null ;
			            		String dateStr = null;
			    	     		Date Statistictime = null;
			            		if(datatype.equals("D")){
			            				formatter = new SimpleDateFormat("YYYY-MM-dd");
			            			}
			            		if(datatype.equals("M")){
			            				formatter = new SimpleDateFormat("YYYY-MM");
			            			}
			            		if(datatype.equals("Y")){
			            				formatter = new SimpleDateFormat("YYYY");
			            			}
			            			Statistictime = (Date) Preferential.get(i).get("statistic_time");
			            			dateStr = formatter.format(Statistictime);
			            			pr.setStatistictime(dateStr);
			       			     }else{
			       			    	pr.setStatistictime("");
			       			     }
						 PreferentialList.add(pr);
					 }
				 }
			 }
	
		 return PreferentialList;
	}
	/**
	 * 优惠分析饼图数根据支付类型、活动类型分组据查询(发生金额)	
	 */	
	public List<PreferentialReport> findGroupByPaywayandType(Map<String, Object> params){
		 List<Map<String,Object>> Preferential = tbDayIncomeBillDao.findGroupByPaywayandType(params);
		 List<PreferentialReport> PreferentialList = new ArrayList<PreferentialReport>();
			 if(Preferential.size()>0){
				 for (int i = 0; i < Preferential.size(); i++) {
					 if(Preferential.get(i)!=null){
						 PreferentialReport pr = new PreferentialReport();
						 String paytype="";
						 //结算方式名字
						 if(Preferential.get(i).get("paytype")!=null && Preferential.get(i).get("paytype")!=""){
							 paytype = Preferential.get(i).get("paytype").toString();
							 pr.setPaywayDesc(Preferential.get(i).get("paytype").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						 //活动类型
						 if(Preferential.get(i).get("activity_type")!=null && Preferential.get(i).get("activity_type")!=""){
							 pr.setName(Preferential.get(i).get("activity_type").toString());
							 pr.setActivitytype(Preferential.get(i).get("activity_type").toString()+"("+paytype+")");
						 }else{
							 pr.setName("");
							 pr.setActivitytype("");
						 }
						 //活动名称
						 if(Preferential.get(i).get("activity_name")!=null && Preferential.get(i).get("activity_name")!=""){
							 pr.setActivitytame(Preferential.get(i).get("activity_name").toString());
						 }else{
							 pr.setActivitytame("");
						 }
						 //结算方式
						 if(Preferential.get(i).get("payway")!=null && Preferential.get(i).get("payway")!=""){
							 pr.setPayway(Integer.parseInt(Preferential.get(i).get("payway").toString()));
						 }else{
							 pr.setPayway(Integer.parseInt("0"));
						 }
						 //结算方式名称
						 if(Preferential.get(i).get("paytype")!=null && Preferential.get(i).get("paytype")!=""){
							 pr.setPaywayDesc(Preferential.get(i).get("paytype").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						//发生金额
						 if(Preferential.get(i).get("amount")!=null){
							 BigDecimal AmountResult= new BigDecimal(Preferential.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }else{
							 BigDecimal AmountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }
						//时间
			             if(Preferential.get(i).get("statistic_time")!=null && Preferential.get(i).get("statistic_time") !=""){
			             		String datatype = params.get("Datetype").toString();
			            		SimpleDateFormat formatter = null ;
			            		String dateStr = null;
			    	     		Date Statistictime = null;
			            		if(datatype.equals("D")){
			            				formatter = new SimpleDateFormat("YYYY-MM-dd");
			            			}
			            		if(datatype.equals("M")){
			            				formatter = new SimpleDateFormat("YYYY-MM");
			            			}
			            		if(datatype.equals("Y")){
			            				formatter = new SimpleDateFormat("YYYY");
			            			}
			            			Statistictime = (Date) Preferential.get(i).get("statistic_time");
			            			dateStr = formatter.format(Statistictime);
			            			pr.setStatistictime(dateStr);
			       			     }else{
			       			    	pr.setStatistictime("");
			       			     }
						 PreferentialList.add(pr);
					 }
				 }
			 }
	
		 return PreferentialList;
	}
	/**
	 * 优惠分析饼图数根据支付类型、活动类型、日期分组据查询(发生金额)
	 */	
	public List<PreferentialReport> findGroupByPaywayandTypeandTime(Map<String, Object> params){
		 List<Map<String,Object>> Preferential = tbDayIncomeBillDao.findGroupByPaywayandTypeandTime(params);
		 List<PreferentialReport> PreferentialList = new ArrayList<PreferentialReport>();
			 if(Preferential.size()>0){
				 for (int i = 0; i < Preferential.size(); i++) {
					 if(Preferential.get(i)!=null){
						 PreferentialReport pr = new PreferentialReport();
						 //结算方式名字
						 if(Preferential.get(i).get("paytype")!=null && Preferential.get(i).get("paytype")!=""){
							 pr.setPaywayDesc(Preferential.get(i).get("paytype").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						 //活动类型
						 if(Preferential.get(i).get("activity_type")!=null && Preferential.get(i).get("activity_type")!=""){
							 pr.setActivitytype(Preferential.get(i).get("activity_type").toString());
						 }else{
							 pr.setActivitytype("");
						 }
						 //活动名称
						 if(Preferential.get(i).get("activity_name")!=null && Preferential.get(i).get("activity_name")!=""){
							 pr.setActivitytame(Preferential.get(i).get("activity_name").toString());
						 }else{
							 pr.setActivitytame("");
						 }
						 //结算方式
						 if(Preferential.get(i).get("payway")!=null && Preferential.get(i).get("payway")!=""){
							 pr.setPayway(Integer.parseInt(Preferential.get(i).get("payway").toString()));
						 }else{
							 pr.setPayway(Integer.parseInt("0"));
						 }
						 //结算方式名称
						 if(Preferential.get(i).get("paytype")!=null && Preferential.get(i).get("paytype")!=""){
							 pr.setPaywayDesc(Preferential.get(i).get("paytype").toString());
						 }else{
							 pr.setPaywayDesc("");
						 }
						//发生金额
						 if(Preferential.get(i).get("amount")!=null){
							 BigDecimal AmountResult= new BigDecimal(Preferential.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }else{
							 BigDecimal AmountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 pr.setAmount(AmountResult);
						 }
						//时间
			             if(Preferential.get(i).get("statistic_time")!=null && Preferential.get(i).get("statistic_time") !=""){
			             		String datatype = params.get("Datetype").toString();
			            		SimpleDateFormat formatter = null ;
			            		String dateStr = null;
			    	     		Date Statistictime = null;
			            		if(datatype.equals("D")){
			            				formatter = new SimpleDateFormat("YYYY-MM-dd");
			            			}
			            		if(datatype.equals("M")){
			            				formatter = new SimpleDateFormat("YYYY-MM");
			            			}
			            		if(datatype.equals("Y")){
			            				formatter = new SimpleDateFormat("YYYY");
			            			}
			            			Statistictime = (Date) Preferential.get(i).get("statistic_time");
			            			dateStr = formatter.format(Statistictime);
			            			pr.setStatistictime(dateStr);
			       			     }else{
			       			    	pr.setStatistictime("");
			       			     }
						 PreferentialList.add(pr);
					 }
				 }
			 }
			return PreferentialList;
	}
	 
	 
	 
	 
	 /**
	  * 结算方式
	  */
	 public List<SettlementReport> findSettlementReport(Map<String, Object> params){
		 List<Map<String,Object>> SettlementR = tbDayIncomeBillDao.findSettlementReport(params);
		 List<SettlementReport> SettlementRList = new ArrayList<SettlementReport>();
		 if(SettlementR.size()>0){
			 for (int i = 0; i < SettlementR.size(); i++) {
				 SettlementReport sr = new  SettlementReport();
				 //结算方式编号
				 if(SettlementR.get(i).get("pay_way")!=null && SettlementR.get(i).get("pay_way")!=""){
					 sr.setPayway(Integer.parseInt(SettlementR.get(i).get("pay_way").toString()));
				 }else{
					 sr.setPayway(Integer.parseInt("0"));
				 }
				 //结算方式
				 if(SettlementR.get(i).get("pay_type")!=null && SettlementR.get(i).get("pay_type")!=""){
					 sr.setPaywayDesc(SettlementR.get(i).get("pay_type").toString());
				 }else{
					 sr.setPaywayDesc("");
				 }
				//笔数 
				 if(SettlementR.get(i).get("num")!=null && SettlementR.get(i).get("num")!=""){
					 sr.setNum(Integer.parseInt(SettlementR.get(i).get("num").toString()));
				 }else{
					 sr.setNum(Integer.parseInt("0"));
				 }
				//发生金额
				 if(SettlementR.get(i).get("amount")!=null && SettlementR.get(i).get("amount")!=""){
					 BigDecimal amountResult= new BigDecimal(SettlementR.get(i).get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				     sr.setAmount(amountResult);
				 }else{
					 BigDecimal amountResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				     sr.setAmount(amountResult);
				 }
			 }
		 }
		 return  SettlementRList;
	 }
	 
	 /**
	  * 品项分析销售数量top10柱状图数据查询
	  */
	 public List<ItemReport> findItemReport(Map<String, Object> params){
		 List<Map<String,Object>> ItemR = tbDayIncomeBillDao.findItemReport(params);
		 List<ItemReport> ItemReportList = new ArrayList<ItemReport>();
		 if(ItemR.size()>0){
			 for (int i = 0; i < ItemR.size(); i++) {
				  ItemReport item = new  ItemReport();
				  //品项类型
				  if(ItemR.get(i).get("Category")!=null && ItemR.get(i).get("Category")!=""){
					  item.setCategory(ItemR.get(i).get("Category").toString());
				  }else{
					  item.setCategory("");
				  }
				  //品类编号
				  if(ItemR.get(i).get("item_id")!=null && ItemR.get(i).get("item_id")!=""){
					  item.setItemid(ItemR.get(i).get("item_id").toString());
				  }else{
					  item.setItemid("");
				  }
				  //品类名称
				  if(ItemR.get(i).get("item_desc")!=null && ItemR.get(i).get("item_desc")!=""){
					  item.setItemdesc(ItemR.get(i).get("item_desc").toString());
				  }else{
					  item.setItemdesc("");
				  }
				  //数量
				  if(ItemR.get(i).get("Num")!=null && ItemR.get(i).get("Num")!=""){
					  item.setNum(Integer.parseInt(ItemR.get(i).get("Num").toString()));
				  }else{
					  item.setNum(Integer.parseInt("0"));
				  }
				 //销售金额
				  if(ItemR.get(i).get("Share")!=null && ItemR.get(i).get("Share")!=""){
					  BigDecimal shareResult= new BigDecimal(ItemR.get(i).get("Share").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  item.setShare(shareResult);
				  }else{
					  BigDecimal shareResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  item.setShare(shareResult);
				  }
				  ItemReportList.add(item);
			}
		 }
		 return ItemReportList;
	 }

	/**
	 * 品项分析销售数量top10趋势图数据查询
	 */
	@Override
	public List<ItemReport> findItemNumQushiReport(Map<String, Object> params) {
		 List<Map<String,Object>> ItemR = tbDayIncomeBillDao.findItemNumQushiReport(params);
		 List<ItemReport> ItemReportList = new ArrayList<ItemReport>();
		 if(ItemR.size()>0){
			 for (int i = 0; i < ItemR.size(); i++) {
				  ItemReport item = new  ItemReport();
				  //品项类型
				  if(ItemR.get(i).get("Category")!=null && ItemR.get(i).get("Category")!=""){
					  item.setCategory(ItemR.get(i).get("Category").toString());
				  }else{
					  item.setCategory("");
				  }
				  //品类编号
				  if(ItemR.get(i).get("item_id")!=null && ItemR.get(i).get("item_id")!=""){
					  item.setItemid(ItemR.get(i).get("item_id").toString());
				  }else{
					  item.setItemid("");
				  }
				  //品类名称
				  if(ItemR.get(i).get("item_desc")!=null && ItemR.get(i).get("item_desc")!=""){
					  item.setItemdesc(ItemR.get(i).get("item_desc").toString());
				  }else{
					  item.setItemdesc("");
				  }
				  //数量
				  if(ItemR.get(i).get("Num")!=null && ItemR.get(i).get("Num")!=""){
					  item.setNum(Integer.parseInt(ItemR.get(i).get("Num").toString()));
				  }else{
					  item.setNum(Integer.parseInt("0"));
				  }
				 //销售金额
				  if(ItemR.get(i).get("Share")!=null && ItemR.get(i).get("Share")!=""){
					  BigDecimal shareResult= new BigDecimal(ItemR.get(i).get("Share").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  item.setShare(shareResult);
				  }else{
					  BigDecimal shareResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  item.setShare(shareResult);
				  }
				  if(ItemR.get(i).get("statistic_date")!=null && ItemR.get(i).get("statistic_date") !=""){
	      			 String datatype = params.get("Datetype").toString();
	     			 SimpleDateFormat formatter = new SimpleDateFormat();
	     			 String dateStr = null;
	     			 Date Statistictime = null;
	     			if(datatype.equals("D")){
	     				formatter = new SimpleDateFormat("YYYY-MM-dd");
	     			}
	     			if(datatype.equals("M")){
	     				formatter = new SimpleDateFormat("YYYY-MM");
	     			}
	     			if(datatype.equals("Y")){
	     				formatter = new SimpleDateFormat("YYYY");
	     			}
	     			Statistictime = (Date) ItemR.get(i).get("statistic_date");
        			dateStr = formatter.format(Statistictime);
	     			item.setStatistictime(dateStr);
				}else{
					item.setStatistictime("");
				}
				  ItemReportList.add(item);
			}
		 }
		 return ItemReportList;
	}
    
	/**
	 * 品项分析销售金额top10柱状图数据查询
	 */
	@Override
	public List<ItemReport> findItemSharezhuzhuangReport(Map<String, Object> params) {
		 List<Map<String,Object>> ItemR = tbDayIncomeBillDao.findItemSharezhuzhuangReport(params);
		 List<ItemReport> ItemReportList = new ArrayList<ItemReport>();
	     if(ItemR.size()>0){
	    	 for (int i = 0; i < ItemR.size(); i++) {
				  ItemReport item = new  ItemReport();
				  //品项类型
				  if(ItemR.get(i).get("Category")!=null && ItemR.get(i).get("Category")!=""){
					  item.setCategory(ItemR.get(i).get("Category").toString());
				  }else{
					  item.setCategory("");
				  }
				  //品类编号
				  if(ItemR.get(i).get("item_id")!=null && ItemR.get(i).get("item_id")!=""){
					  item.setItemid(ItemR.get(i).get("item_id").toString());
				  }else{
					  item.setItemid("");
				  }
				  //品类名称
				  if(ItemR.get(i).get("item_desc")!=null && ItemR.get(i).get("item_desc")!=""){
					  item.setItemdesc(ItemR.get(i).get("item_desc").toString());
				  }else{
					  item.setItemdesc("");
				  }
				  //数量
				  if(ItemR.get(i).get("Num")!=null && ItemR.get(i).get("Num")!=""){
					  item.setNum(Integer.parseInt(ItemR.get(i).get("Num").toString()));
				  }else{
					  item.setNum(Integer.parseInt("0"));
				  }
				 //销售金额
				  if(ItemR.get(i).get("Share")!=null && ItemR.get(i).get("Share")!=""){
					  BigDecimal shareResult= new BigDecimal(ItemR.get(i).get("Share").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  item.setShare(shareResult);
				  }else{
					  BigDecimal shareResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  item.setShare(shareResult);
				  }
				  ItemReportList.add(item);
			}
	     }
		return ItemReportList;
	} 
    
	/**
	 * 品项分析销售金额top10趋势图数据查询
	 */
	@Override
	public List<ItemReport> findItemShareQushiReport(Map<String, Object> params) {
		 List<Map<String,Object>> ItemR = tbDayIncomeBillDao.findItemShareQushiReport(params);
		 List<ItemReport> ItemReportList = new ArrayList<ItemReport>();
		 if(ItemR.size()>0){
			 for (int i = 0; i < ItemR.size(); i++) {
				  ItemReport item = new  ItemReport();
				  //品项类型
				  if(ItemR.get(i).get("Category")!=null && ItemR.get(i).get("Category")!=""){
					  item.setCategory(ItemR.get(i).get("Category").toString());
				  }else{
					  item.setCategory("");
				  }
				  //品类编号
				  if(ItemR.get(i).get("item_id")!=null && ItemR.get(i).get("item_id")!=""){
					  item.setItemid(ItemR.get(i).get("item_id").toString());
				  }else{
					  item.setItemid("");
				  }
				  //品类名称
				  if(ItemR.get(i).get("item_desc")!=null && ItemR.get(i).get("item_desc")!=""){
					  item.setItemdesc(ItemR.get(i).get("item_desc").toString());
				  }else{
					  item.setItemdesc("");
				  }
				  //数量
				  if(ItemR.get(i).get("Num")!=null && ItemR.get(i).get("Num")!=""){
					  item.setNum(Integer.parseInt(ItemR.get(i).get("Num").toString()));
				  }else{
					  item.setNum(Integer.parseInt("0"));
				  }
				 //销售金额
				  if(ItemR.get(i).get("Share")!=null && ItemR.get(i).get("Share")!=""){
					  BigDecimal shareResult= new BigDecimal(ItemR.get(i).get("Share").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  item.setShare(shareResult);
				  }else{
					  BigDecimal shareResult= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  item.setShare(shareResult);
				  }
				  if(ItemR.get(i).get("statistic_date")!=null && ItemR.get(i).get("statistic_date") !=""){
	       			String datatype = params.get("Datetype").toString();
	      			 SimpleDateFormat formatter = null ;
	      			 String dateStr = null;
	     			 Date Statistictime = null;
	      			if(datatype.equals("D")){
	      				formatter = new SimpleDateFormat("YYYY-MM-dd");
	      			}
	      			if(datatype.equals("M")){
	      				formatter = new SimpleDateFormat("YYYY-MM");
	      			}
	      			if(datatype.equals("Y")){
	      				formatter = new SimpleDateFormat("YYYY");
	      			}
	      			Statistictime = (Date) ItemR.get(i).get("statistic_date");
	      			dateStr = formatter.format(Statistictime);
	      			item.setStatistictime(dateStr);
	 			}else{
	 				item.setStatistictime("");
	 			}
				  ItemReportList.add(item);
			}
		 }
		return ItemReportList;
	}

	/**
	 * 查询品项销售明细列表
	 * 
	 * @author weizhifang
	 * @since 2015-5-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDetailList(Map<String, Object> params) {
		List<Map<String,Object>> itemDetailList = tbDayIncomeBillDao.getItemDetailList(params);
		Double sumDishNum = getAllItemReportCout(params);
		List<Map<String,Object>> itemDetail = getShareFormItemDetail(sumDishNum,itemDetailList,params);
		if(params.get("id") != null){
			String id = params.get("id").toString();
			if(id.equals("DISHES_98")){
				List<Map<String,Object>> dishes = tbDayIncomeBillDao.getDishesList(params);
				for(Map<String,Object> m : dishes){
					itemDetail.add(m);
				}
			}
		}
		return itemDetail;
	}
	
	/**
	 * 获取品项销售明细表的份额
	 * @author weizhifang
	 * @since 2015-5-27
	 * @param itemDetailList
	 * @return
	 */
	public List<Map<String,Object>> getShareFormItemDetail(Double sumDishNum,List<Map<String,Object>> itemDetailList,Map<String, Object> params){
		System.out.println("sumDishNum="+sumDishNum);
		List<Map<String,Object>> dishRsult = new ArrayList<Map<String,Object>>();
		for (Map<String,Object> m : itemDetailList) {
			String dishNumStr = m.get("dishnum").toString();
			double dishNum = Double.parseDouble(dishNumStr) ;
			double share = 0;
			if(sumDishNum > 0){
			    share = dishNum/sumDishNum;
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id",m.get("id"));
			map.put("dishid",m.get("dishid"));
			map.put("itemDesc",m.get("itemDesc"));
			map.put("title",m.get("title"));
			map.put("dishType",m.get("dishType"));
			map.put("price",m.get("price"));
			map.put("unit",m.get("unit"));
			map.put("number",m.get("dishnum"));
			map.put("shiftid",m.get("shiftid"));
			map.put("inserttime",m.get("inserttime"));
			map.put("dishNo",m.get("dishNo"));
			map.put("share",new java.text.DecimalFormat("#.00").format(share*100));
			dishRsult.add(map);
		}
		return dishRsult;
	}
	
	/**
	 * 得到品项数量总和
	 * @author weizhifang
	 * @since 2015-6-2
	 * @param params
	 * @return
	 */
	public Double getAllItemReportCout(Map<String,Object> params){
		return tbDayIncomeBillDao.getAllItemReportCout(params);
	}
	
	/**
	 * 查询品项列表
	 * @author weizhifang
	 * @since 2015-5-24
	 */
	public List<Map<String,Object>> getItemReportForList(Map<String, Object>  params){
		List<Map<String,Object>> item = tbDayIncomeBillDao.getItemReportForList(params);
		Double sumDishNum = getAllItemReportCout(params);
		List<Map<String,Object>> itemList = getShareForItem(sumDishNum,item);
		//查全部时拼餐具
		if(params.get("id") != null){
			String id = params.get("id").toString();
			if(id.equals("DISHES_98")){
				List<Map<String,Object>> dishes = tbDayIncomeBillDao.getDishesList(params);
				for(Map<String,Object> m : dishes){
					itemList.add(m);
				}
			}
		}
		return itemList;
	}
	
	/**
	 * 获取品项的份额
	 * @author weizhifang
	 * @since 2015-05-26
	 * @param item
	 * @return
	 */
	private List<Map<String,Object>> getShareForItem(Double sumDishNum,List<Map<String,Object>> item){
		List<Map<String,Object>> dishRsult = new ArrayList<>();
		for (Map<String,Object> m : item) {
			String dishNumStr = m.get("dishnum").toString();
			double dishNum = Double.parseDouble(dishNumStr) ;
			double share = 0;
			if(sumDishNum > 0){
			    share = dishNum/sumDishNum;
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("begintime", m.get("begintime"));
			map.put("endtime", m.get("endtime"));
			map.put("itemDesc",m.get("itemDesc"));
			map.put("id",m.get("id"));
			map.put("dishType",m.get("dishType"));
			map.put("number",m.get("dishnum"));
			map.put("shiftid",m.get("shiftid"));
			map.put("share",new java.text.DecimalFormat("#.00").format(share*100));
			dishRsult.add(map);
		}
		return dishRsult;
	}
	
	/**
	 * 查询品类列表
	 * 
	 * @author weizhifang
	 * @since 2015-5-16
	 * @param itemid
	 * @return
	 */
	public List<Code> getItemDescList() {
		List<Map<String,Object>> codeList = tbDayIncomeBillDao.getItemDescList();
		List<Code> codeResultList = new ArrayList<Code>();
		if(codeList.size()>0){
			for (int i = 0; i < codeList.size(); i++) {
				Code code =  new Code();
				if(codeList.get(i).get("id")!=null && codeList.get(i).get("id")!=""){
					code.setCodeId(codeList.get(i).get("id").toString());
				}else{
					code.setCodeId("");
				}
				if(StringUtils.isNotBlank((String) codeList.get(i).get("itemDesc"))){
					code.setCodeDesc((codeList.get(i).get("itemDesc").toString()));
				}else{
					code.setCodeDesc("");
				}
				codeResultList.add(code);
			}
		}
		return codeResultList;
	}

	/**
	 * 退菜明细列表
	 * @author weizhifang
	 * @since 2015-05-22
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getReturnDishList(Map<String, Object> params){
		List<Map<String,Object>> returnDishList = tbDayIncomeBillDao.getReturnDishList(params);
		List<Map<String,Object>> list = returnDishList;
		for(Map<String,Object> map : returnDishList){
			if(map.get("discarduserid") != null){ 
				String discarduserid = map.get("discarduserid").toString();
				Map<String,Object> pMap = new HashMap<String,Object>();
				pMap.put("discarduserid", discarduserid);
				//查询授权人
				String userName = tbDayIncomeBillDao.findUserNameBydiscarduserid(pMap);
				map.put("discardusername", userName);
				list.add(map);
			}
		}
		return list;
	}
    
	/**
	 * 营业数据分析统计(午市、晚市 ) DataStatisticsHalf
	 */
	@Override
	public List<DataStatistics> findDataStatisticsHalf(Map<String, Object> params) {
		 List<Map<String,Object>> DatehalfList = tbDayIncomeBillDao.findDataStatisticsHalf(params);
		 List<DataStatistics> DataStatisticsHalfList = new ArrayList<DataStatistics>();
		 if(DatehalfList!=null){
			 if(DatehalfList.size()>0){
				 for (int i = 0; i < DatehalfList.size(); i++) {
					  DataStatistics dateS = new DataStatistics();
					  if(DatehalfList.get(i).get("shiftid")!=null && DatehalfList.get(i).get("shiftid")!=""){
						  dateS.setShiftId(DatehalfList.get(i).get("shiftid").toString());
					  }else{
						  dateS.setShiftId("");
					  }
					  if(DatehalfList.get(i).get("areaid")!=null && DatehalfList.get(i).get("areaid")!=""){
						  dateS.setAreaId(DatehalfList.get(i).get("areaid").toString());
					  }else{
						  dateS.setAreaId("");
					  }
					  if(StringUtils.isNotBlank((String) DatehalfList.get(i).get("areaname"))){
						  dateS.setArea(DatehalfList.get(i).get("areaname").toString());
					  }else{
						  dateS.setArea("");
					  }
					  if(StringUtils.isNotBlank((String) DatehalfList.get(i).get("tableNo"))){
						  dateS.setTableId(DatehalfList.get(i).get("tableNo").toString());
					  }else{
						  dateS.setTableId("");
					  }
					  if(DatehalfList.get(i).get("begintime")!=null && DatehalfList.get(i).get("begintime")!=""){
						  dateS.setDateTime(DatehalfList.get(i).get("begintime").toString());
					  }else{
						  dateS.setDateTime("");
					  }
					  if(DatehalfList.get(i).get("datavalues")!=null && DatehalfList.get(i).get("datavalues")!=""){
						  if(params.get("dataType")!=null && params.get("dataType")!=""){
							  if("1".equals(params.get("dataType").toString())||"2".equals(params.get("dataType").toString())){
								  BigDecimal values= new BigDecimal(DatehalfList.get(i).get("datavalues").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
								  dateS.setValues(values);
							  }else{
								  BigDecimal values= new BigDecimal(DatehalfList.get(i).get("datavalues").toString()).setScale(0, BigDecimal.ROUND_HALF_UP);
								  dateS.setValues(values);
							  }
						  }
					  }else{
						  if(params.get("dataType")!=null && params.get("dataType")!=""){
							  if("1".equals(params.get("dataType"))||"2".equals(params.get("dataType"))){
								  BigDecimal values= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
								  dateS.setValues(values);
							  }else{
								  BigDecimal values= new BigDecimal(("0.00").toString()).setScale(0, BigDecimal.ROUND_HALF_UP);
								  dateS.setValues(values);
							  }
						  }
					  }
					  DataStatisticsHalfList.add(dateS);
				  }
			 }
			 
		 }
		return DataStatisticsHalfList;
	}
    
	/**
	 * <!--营业数据分析统计(全天)DataStatisticsDay-->
	 */
	@Override
	public List<DataStatistics> findDataStatisticsDay(Map<String, Object> params) {
		 List<Map<String,Object>> DatehalfList = tbDayIncomeBillDao.findDataStatisticsDay(params);
		 List<DataStatistics> DataStatisticsHalfList = new ArrayList<DataStatistics>();
		 if(DatehalfList!=null){
			 if(DatehalfList.size()>0){
				 for (int i = 0; i < DatehalfList.size(); i++) {
					  DataStatistics dateS = new DataStatistics();
					 /* if(StringUtils.isNotBlank((String) DatehalfList.get(i).get("shiftid"))){
						  dateS.setShiftId(DatehalfList.get(i).get("shiftid").toString());
					  }else{
						  dateS.setShiftId("");
					  }*/
					  if(StringUtils.isNotBlank((String) DatehalfList.get(i).get("areaname"))){
						  dateS.setArea(DatehalfList.get(i).get("areaname").toString());
					  }else{
						  dateS.setArea("");
					  }
					  if(DatehalfList.get(i).get("areaid")!=null && DatehalfList.get(i).get("areaid")!=""){
						  dateS.setAreaId(DatehalfList.get(i).get("areaid").toString());
					  }else{
						  dateS.setAreaId("");
					  }
					  if(StringUtils.isNotBlank((String) DatehalfList.get(i).get("tableNo"))){
						  dateS.setTableId(DatehalfList.get(i).get("tableNo").toString());
					  }else{
						  dateS.setTableId("");
					  }
					  if(DatehalfList.get(i).get("begintime")!=null && DatehalfList.get(i).get("begintime")!=""){
						  dateS.setDateTime(DatehalfList.get(i).get("begintime").toString());
					  }else{
						  dateS.setDateTime("");
					  }
					  if(DatehalfList.get(i).get("datavalues")!=null && DatehalfList.get(i).get("datavalues")!=""){
						  if(params.get("dataType")!=null && params.get("dataType")!=""){
							  if("1".equals(params.get("dataType").toString())||"2".equals(params.get("dataType").toString())){
								  BigDecimal values= new BigDecimal(DatehalfList.get(i).get("datavalues").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
								  dateS.setValues(values);
							  }else{
								  BigDecimal values= new BigDecimal(DatehalfList.get(i).get("datavalues").toString()).setScale(0, BigDecimal.ROUND_HALF_UP);
								  dateS.setValues(values);
							  }
						  }
					  }else{
						  if(params.get("dataType")!=null && params.get("dataType")!=""){
							  if("1".equals(params.get("dataType"))||"2".equals(params.get("dataType"))){
								  BigDecimal values= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
								  dateS.setValues(values);
							  }else{
								  BigDecimal values= new BigDecimal(("0.00").toString()).setScale(0, BigDecimal.ROUND_HALF_UP);
								  dateS.setValues(values);
							  }
						  }
					  }
					  DataStatisticsHalfList.add(dateS);
				  }
			 }
			 
		 }
		return DataStatisticsHalfList;
	}
	
	/**
	 * <!--查询营业数据查询的类型-->
	 */
	public List<Code> findCode(Map<String, Object> params){
		List<Map<String,Object>> codeList = tbDayIncomeBillDao.findCode(params);
		List<Code> codeResultList = new ArrayList<Code>();
		if(codeList.size()>0){
			for (int i = 0; i < codeList.size(); i++) {
				Code code =  new Code();
				if(codeList.get(i).get("itemid")!=null && codeList.get(i).get("itemid")!=""){
					code.setCodeId(codeList.get(i).get("itemid").toString());
				}else{
					code.setCodeId("");
				}
				if(StringUtils.isNotBlank((String) codeList.get(i).get("itemDesc"))){
					code.setCodeDesc((codeList.get(i).get("itemDesc").toString()));
				}else{
					code.setCodeDesc("");
				}
				codeResultList.add(code);
			}
		}
		return codeResultList;
	}
	
	/**
	 * <!--查询所有的区域tablearea-->
	 */
	public List<TableArea> findTableArea(Map<String, Object> params){
		List<Map<String,Object>> areaList = tbDayIncomeBillDao.findTableArea(params);
		List<TableArea> areaResultList = new ArrayList<TableArea>();
		if(areaList.size()>0){
			for (int i = 0; i < areaList.size(); i++) {
				TableArea area =  new TableArea();
				if(areaList.get(i).get("areaNo")!=null && areaList.get(i).get("areaNo")!=""){
					area.setAreaNo(areaList.get(i).get("areaNo").toString());
				}else{
					area.setAreaNo("");
				}
				if(StringUtils.isNotBlank((String) areaList.get(i).get("areaname"))){
					area.setAreaname((areaList.get(i).get("areaname").toString()));
				}else{
					area.setAreaname("");
				}
				areaResultList.add(area);
			}
		}
		return areaResultList;
	}
	
    public List<Code> findPreferentialActivity (Map<String, Object> params){
    	List<Map<String,Object>> ActivityaList = tbDayIncomeBillDao.findPreferentialActivity(params);
		List<Code> ActivityResultList = new ArrayList<Code>();
		if(ActivityaList.size()>0){
			for (int i = 0; i < ActivityaList.size(); i++) {
				Code code =  new Code();
				if(ActivityaList.get(i).get("code")!=null && ActivityaList.get(i).get("code")!=""){
				     code.setCodeId(ActivityaList.get(i).get("code").toString());
				}else{
					 code.setCodeId("");
				}
				if(StringUtils.isNotBlank((String) ActivityaList.get(i).get("name"))){
					 code.setCodeDesc(ActivityaList.get(i).get("name").toString());
				}else{
				}
				ActivityResultList.add(code);
			}
		}
		return ActivityResultList;
    }
	
	public List<Code> findPreferentialTypeDict(Map<String, Object> params){
		List<Map<String,Object>> TypeDictList = tbDayIncomeBillDao.findPreferentialTypeDict(params);
		List<Code> TypeDictResultList = new ArrayList<Code>();
		if(TypeDictList.size()>0){
			for (int i = 0; i < TypeDictList.size(); i++) {
				Code code =  new Code();
				if(TypeDictList.get(i).get("code")!=null && TypeDictList.get(i).get("code")!=""){
				     code.setCodeId(TypeDictList.get(i).get("code").toString());
				}else{
					 code.setCodeId("");
				}
				if(StringUtils.isNotBlank((String) TypeDictList.get(i).get("name"))){
					 code.setCodeDesc(TypeDictList.get(i).get("name").toString());
				}else{
				}
				TypeDictResultList.add(code);
			}
		}
		return TypeDictResultList;
	}
	
	 /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }

}
  
  