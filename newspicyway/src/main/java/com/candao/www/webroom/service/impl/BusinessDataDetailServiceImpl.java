package com.candao.www.webroom.service.impl;

import com.candao.www.data.dao.TBusinessDataDetailDao;
import com.candao.www.utils.ToolsUtil;
import com.candao.www.webroom.model.BusinessReport1;
import com.candao.www.webroom.service.BusinessDataDetailService;
import com.candao.www.webroom.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 营业数据明细表
 *
 * @author Administrator
 */
@Service
public class BusinessDataDetailServiceImpl implements BusinessDataDetailService {

	@Autowired
	private TBusinessDataDetailDao tbusinessDataDetailDao;


	@Autowired
	DataDictionaryService dataDictionaryService;

	@Override
	public List<BusinessReport1> isgetBusinessDetail(Map<String, Object> params) {
		List<Map<String, Object>> businessR = tbusinessDataDetailDao.isgetBusinessDetail(params);
		List<BusinessReport1> businessList = new ArrayList<BusinessReport1>();
		if (businessR.size() > 0) {
			BusinessReport1 businssRport = new BusinessReport1();
			for (int i = 0; i < businessR.size(); i++) {
				if (businessR.get(i) != null) {
//					//营业数据收入统计栏
//					//应收总额
					if (i == 0) {
						if (businessR.get(i).get("shouldamount") != null) {
							businssRport.setShouldamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldamount") + ""));
						}
					}
//					//实收总额
					if (businessR.get(i).get("paidinamount") != null) {
						businssRport.setPaidinamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("paidinamount") + ""));
					}
//					//折扣总额
					if (businessR.get(i).get("discountamount") != null) {
						businssRport.setDiscountamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("discountamount") + ""));
					}
//					//实收明细统计栏
//					//人民币

//					//挂账
					if (businessR.get(i).get("credit") != null) {
						businssRport.setCard(ToolsUtil.formatTwoDecimal(businessR.get(i).get("credit") + ""));
					}
//
//
					//刷卡（元）-工商银行
					if (businessR.get(i).get("card") != null) {
						businssRport.setICBC(businessR.get(i).get("card").toString());
					}

					//刷卡（元）-其他银行
					if (businessR.get(i).get("card") != null) {
						businssRport.setOtherbank(businessR.get(i).get("card").toString());
					}
//

//					//会员积分消费（元）Merbervaluenet
					if (businessR.get(i).get("integralconsum") != null) {
						businssRport.setIntegralconsum(ToolsUtil.formatTwoDecimal(businessR.get(i).get("integralconsum") + ""));
					}
//					//合计
					if (businessR.get(i).get("cash") != null) {
						businssRport.setTotal(ToolsUtil.formatTwoDecimal(businessR.get(i).get("cash") + ""));
					}

//						//会员券消费（元）
					if (businessR.get(i).get("meberTicket") != null) {
						businssRport.setMeberTicket(ToolsUtil.formatTwoDecimal(businessR.get(i).get("meberTicket") + ""));
					}
//					//会员储值消费净值（元）
					if (businessR.get(i).get("merbervaluenet") != null) {
						businssRport.setMerbervaluenet(ToolsUtil.formatTwoDecimal(businessR.get(i).get("merbervaluenet") + ""));
					}
//					//会员储值消费虚增（元）
					if (businessR.get(i).get("mebervalueadd") != null) {
						businssRport.setMebervalueadd(ToolsUtil.formatTwoDecimal(businessR.get(i).get("mebervalueadd") + ""));
					}
//
//					//桌数（桌）
					if (businessR.get(i).get("tablecount") != null) {
						businssRport.setTablenum(ToolsUtil.formatTwoDecimal(businessR.get(i).get("tablecount") + ""));
					}
//					//结算人数（个）
					if (businessR.get(i).get("settlementnum") != null) {
						businssRport.setSettlementnum(ToolsUtil.formatTwoDecimal(businessR.get(i).get("settlementnum") + ""));
					}
//					//平均消费时间（分）
					if (businessR.get(i).get("avgconsumtime") != null) {
						businssRport.setAvgconsumtime(ToolsUtil.formatTwoDecimal(businessR.get(i).get("avgconsumtime") + ""));
					}
//					//上座率（%）
					if (businessR.get(i).get("attendance") != null) {
						businssRport.setAttendance(ToolsUtil.formatTwoDecimal(businessR.get(i).get("attendance") + ""));
					}
//					//应收人均（元）
					if (businessR.get(i).get("shouldaverage") != null) {
						businssRport.setShouldaverage(ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldaverage") + ""));
					}
//					//实收人均（元）
					if (businessR.get(i).get("paidinaverage") != null) {
						businssRport.setPaidinaverage(ToolsUtil.formatTwoDecimal(businessR.get(i).get("paidinaverage") + ""));
					}
//					//桌均消费
					if (businessR.get(i).get("tableconsumption") != null) {
						businssRport.setTableconsumption(ToolsUtil.formatTwoDecimal(businessR.get(i).get("tableconsumption") + ""));
					}
//					//翻台率(%)
					if (businessR.get(i).get("overtaiwan") != null) {
						businssRport.setOvertaiwan(ToolsUtil.formatTwoDecimal(businessR.get(i).get("overtaiwan") + ""));
					}
//
//					//优免（元）
					if (businessR.get(i).get("free") != null) {
						businssRport.setBastfree(ToolsUtil.formatTwoDecimal(businessR.get(i).get("free") + ""));
					}
//
//					//折扣优惠
					if (businessR.get(i).get("discount") != null) {
						businssRport.setDiscountmoney(ToolsUtil.formatTwoDecimal(businessR.get(i).get("discount") + ""));
					}

					//现金
					if (businessR.get(i).get("cash") != null) {
						businssRport.setMoney(ToolsUtil.formatTwoDecimal(businessR.get(i).get("cash") + ""));
					}

//					#营业数据统计(堂吃应收）
					if (businessR.get(i).get("shouldamount_normal") != null) {
						businssRport.setShouldamountNormal(ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldamount_normal") + ""));
					}
//					外卖统计(应收）
					if (businessR.get(i).get("shouldamount_takeout") != null) {
						businssRport.setShouldamountTakeout(ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldamount_takeout") + ""));
					}
//					#外卖统计(实收）
					if (businessR.get(i).get("paidinamount_takeout") != null) {
						businssRport.setPaidinamountTakeout(ToolsUtil.formatTwoDecimal(businessR.get(i).get("paidinamount_takeout") + ""));
					}
// #外卖统计(订单数）
					if (businessR.get(i).get("ordercount_takeout") != null) {
						businssRport.setOrdercountTakeout(ToolsUtil.formatTwoDecimal(businessR.get(i).get("ordercount_takeout") + ""));
					}
//					#外卖统计(订单平均价格）
					if (businessR.get(i).get("avgprice_takeout") != null) {
						businssRport.setAvgpriceTakeout(ToolsUtil.formatTwoDecimal(businessR.get(i).get("avgprice_takeout") + ""));
					}

//#折扣(抹零）

					if (businessR.get(i).get("fraction") != null) {
						businssRport.setMalingincom(ToolsUtil.formatTwoDecimal(businessR.get(i).get("fraction").toString()));
					}
//					会员消费合计
					if (businessR.get(i).get("membertotal") != null) {
						businssRport.setTotal(ToolsUtil.formatTwoDecimal(businessR.get(i).get("membertotal").toString()));
					}
//会员消费笔数
					if (businessR.get(i).get("vipordercount") != null) {
						businssRport.setVipordercount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("vipordercount").toString()));
					}

//会员消费占比
					if (businessR.get(i).get("viporderpercent") != null) {
						businssRport.setViporderpercent(ToolsUtil.formatTwoDecimal(businessR.get(i).get("viporderpercent").toString()));
					}

                    if (businessR.get(i).get("give") != null) {// 赠送
                        businssRport.setGive(ToolsUtil.formatTwoDecimal(businessR.get(i).get("give").toString()));
                    }

					if (judgePayforType() == 0) {   //表示 不处理

					} else if (judgePayforType() == 1) {    //表示 四舍五入
						businssRport.setHanderWay("四舍五入调整");
						businssRport.setHandervalue(ToolsUtil.formatTwoDecimal(businessR.get(i).get("roundoff").toString()));
					} else if (judgePayforType() == 2) {    //表示 抹零
						businssRport.setHanderWay("抹零");
						businssRport.setHandervalue(ToolsUtil.formatTwoDecimal(businessR.get(i).get("fraction").toString()));
					}

					businessList.add(businssRport);
				}
			}
		}

		return businessList;
	}

	@Override
	public List<Map<String, Object>> isgetBusinessDetailexcel(Map<String, Object> params) {
		List<Map<String, Object>> businessR = tbusinessDataDetailDao.isgetBusinessDetail(params);
		List<BusinessReport1> businessList = new ArrayList<BusinessReport1>();
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (businessR.size() > 0) {
			BusinessReport1 businssRport = new BusinessReport1();
			for (int i = 0; i < businessR.size(); i++) {
				if (businessR.get(i) != null) {
//					//营业数据收入统计栏
//					//应收总额
					if (i == 0) {
						if (businessR.get(i).get("shouldamount") != null) {
							Map<String, Object> map = new HashMap<>();
							map.put("key", "应收总额");
							map.put("title", "营业收入统计");
							map.put("value", ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldamount") + ""));
							mapList.add(map);
						}
					}
//					//实收总额
					if (businessR.get(i).get("paidinamount") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "实收总额");
						map.put("title", "营业收入统计");
						map.put("value", ToolsUtil.formatTwoDecimal(businessR.get(i).get("paidinamount") + ""));
						mapList.add(map);
					}
//					//折扣总额
					if (businessR.get(i).get("discountamount") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "折扣总额");
						map.put("title", "营业收入统计");
						map.put("value", ToolsUtil.formatTwoDecimal(businessR.get(i).get("discountamount") + ""));
						mapList.add(map);
					}


//					//实收明细统计栏
//					//人民币
					//合计
					if (businessR.get(i).get("cash") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "现金");
						map.put("title", "实收总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("cash") + ""));
						mapList.add(map);
					}

//					//挂账
					if (businessR.get(i).get("credit") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "挂账");
						map.put("title", "实收总额统计");
						map.put("value", ToolsUtil.formatTwoDecimal(businessR.get(i).get("credit") + ""));
						mapList.add(map);
					}
//
//
					//刷卡（元）-工商银行
					if (businessR.get(i).get("card") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "刷卡");
						map.put("title", "实收总额统计");
						map.put("value", businessR.get(i).get("card").toString());
						mapList.add(map);
						}

					//会员储值消费净值（元）
					if (businessR.get(i).get("merbervaluenet") != null) {

						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员储值消费净值");
						map.put("title", "实收总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("merbervaluenet") + ""));
						mapList.add(map);
					}


//					//优免（元）
					if (businessR.get(i).get("free") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "优免");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("free") + ""));
						mapList.add(map);
					}
//


//					//会员积分消费（元）Merbervaluenet
					if (businessR.get(i).get("integralconsum") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员积分消费");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("integralconsum") + ""));
						mapList.add(map);
					}
//
//						//会员券消费（元）
					if (businessR.get(i).get("meberTicket") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员券消费");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("meberTicket") + ""));
						mapList.add(map);
					}

					if (judgePayforType() == 0) {   //表示 不处理

					} else if (judgePayforType() == 1) {    //表示 四舍五入
						Map<String, Object> map = new HashMap<>();
						map.put("key", "四舍五入调整");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("roundoff") + ""));
						mapList.add(map);
					} else if (judgePayforType() == 2) {    //表示 抹零
						Map<String, Object> map = new HashMap<>();
						map.put("key", "抹零");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("fraction") + ""));
						mapList.add(map);
					}
					//折扣优惠
					if (businessR.get(i).get("discount") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "折扣优惠");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("discount") + ""));
						mapList.add(map);
					}




//赠送总额

					if (businessR.get(i).get("give") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "赠送金额");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("give") + ""));
						mapList.add(map);
					}

					//折扣虚增（元）
					if (businessR.get(i).get("mebervalueadd") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员储值消费虚增");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("mebervalueadd") + ""));
						mapList.add(map);
					}

//
//					//桌数（桌）
					if (businessR.get(i).get("tablecount") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "桌数");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("tablecount") + ""));
						mapList.add(map);
					}

					//桌均消费
					if (businessR.get(i).get("tableconsumption") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "桌均消费");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("tableconsumption") + ""));
						mapList.add(map);
					}
//					//结算人数（个）
					if (businessR.get(i).get("settlementnum") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "结算人数");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("settlementnum") + ""));
						mapList.add(map);
					}

					//应收人均（元）
					if (businessR.get(i).get("shouldaverage") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "应收人均");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldaverage") + ""));
						mapList.add(map);
					}
//					//实收人均（元）
					if (businessR.get(i).get("paidinaverage") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "实收人均");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("paidinaverage") + ""));
						mapList.add(map);
					}
					//翻座率（%）
					if (businessR.get(i).get("attendance") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "翻座率（%）");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("attendance") + ""));
						mapList.add(map);
					}
//

//					//翻台率(%)
					if (businessR.get(i).get("overtaiwan") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "翻台率(%)");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("overtaiwan") + ""));
						mapList.add(map);
					}

//					//平均消费时间（分）
					if (businessR.get(i).get("avgconsumtime") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "平均消费时间");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("avgconsumtime") + ""));
						mapList.add(map);
					}
//
//
//                     	#营业数据统计(堂吃应收）
					if (businessR.get(i).get("shouldamount_normal") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "堂食应收");
						map.put("title", "营业数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldamount_normal") + ""));
						mapList.add(map);
					}

//					外卖统计(应收）
					if (businessR.get(i).get("shouldamount_takeout") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "外卖应收");
						map.put("title", "外卖");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldamount_takeout") + ""));
						mapList.add(map);
					}
				//#外卖统计(实收）
					if (businessR.get(i).get("paidinamount_takeout") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "外卖实收");
						map.put("title", "外卖");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("paidinamount_takeout") + ""));
						mapList.add(map);
					}


					// #外卖统计(订单数）
					if (businessR.get(i).get("ordercount_takeout") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "外卖单数");
						map.put("title", "外卖");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("ordercount_takeout") + ""));
						mapList.add(map);
					}

					//					#外卖统计(订单平均价格）
					if (businessR.get(i).get("avgprice_takeout") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "外卖单均");
						map.put("title", "外卖");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("avgprice_takeout") + ""));
						mapList.add(map);
					}

//                      //会员消费笔数
					if (businessR.get(i).get("vipordercount") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员消费笔数");
						map.put("title", "会员数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("vipordercount") + ""));
						mapList.add(map);
					}

					//会员消费占比
					if (businessR.get(i).get("viporderpercent") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员消费占比");
						map.put("title", "会员数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("viporderpercent") + ""));
						mapList.add(map);
					}
				//会员券消费
					if (businessR.get(i).get("meberTicket") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员券消费");
						map.put("title", "会员数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("meberTicket") + ""));
						mapList.add(map);
					}
						//会员积分消费
					if (businessR.get(i).get("integralconsum") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员积分消费");
						map.put("title", "会员数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("integralconsum") + ""));
						mapList.add(map);
					}


					//现金
					if (businessR.get(i).get("merbervaluenet") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员储值消费净值");
						map.put("title", "会员数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("merbervaluenet") + ""));
						mapList.add(map);
					}

 					if (businessR.get(i).get("mebervalueadd") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员储值消费虚增");
						map.put("title", "会员数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("mebervalueadd") + ""));
						mapList.add(map);
					}

					//现金
					if (businessR.get(i).get("membertotal") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "合计");
						map.put("title", "会员数据统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("membertotal") + ""));
						mapList.add(map);
					}
				}
			}
		}

		return mapList;
	}

	/**
	 * 0 表示 不处理，1 表示 四舍五入，2表示 抹零
	 *
	 * @return
	 */
	public int judgePayforType() {
		int i = 0;
		Map map = new HashMap();
		map.put("type", "ROUNDING");

		List<Map<String, Object>> listFind2 = dataDictionaryService.findByParams(map);
		if (listFind2 != null && listFind2.size() > 0) {
			String result = listFind2.get(0).get("itemid").toString();
			return Integer.parseInt(result);
		}
		return i;
	}

}
