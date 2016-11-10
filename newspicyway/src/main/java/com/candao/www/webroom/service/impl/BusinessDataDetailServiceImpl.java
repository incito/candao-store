package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TBusinessDataDetailDao;
import com.candao.www.utils.ToolsUtil;
import com.candao.www.webroom.model.BusinessReport1;
import com.candao.www.webroom.service.BusinessDataDetailService;
import com.candao.www.webroom.service.DataDictionaryService;

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
	//修改后
	public List<BusinessReport1> isgetBusinessDetail1(Map<String, Object> params) {
        List<BusinessReport1> businessList = new ArrayList<BusinessReport1>();
        List<Map<String, Object>> businessR = tbusinessDataDetailDao.isgetBusinessDetail(params);
        //实收方式，动态变化，按照itemsort排序
        List<Map<String,Object>> settlementList = tbusinessDataDetailDao.getSettlementOptions(params);
        List<String> settlementKey = new ArrayList<String>();
        //实收表头tr
		List<String> settlementDescList = new ArrayList<String>();
		Map<String,Integer> settlementSort = new HashMap<String,Integer>();
		Integer sort = 0;
		for(Map<String,Object> map:settlementList){
			if(Integer.valueOf(map.get("itemId")+"")!=1){
				if(Integer.valueOf(map.get("itemId")+"")==8){
					settlementSort.put(map.get("itemId")+"", sort++);
					settlementDescList.add("会员储值消费净值");
					settlementKey.add(map.get("itemId")+"");
				}else{
					settlementSort.put(map.get("itemId")+"", sort++);
					settlementDescList.add(map.get("itemDesc")+"");
					settlementKey.add(map.get("itemId")+"");
				}
			}else{
				settlementSort.put(map.get("itemId")+"_1", sort++);
				settlementDescList.add("刷卡-工行");
				settlementKey.add(map.get("itemId")+"_1");
				settlementSort.put(map.get("itemId")+"_2", sort++);
				settlementDescList.add("刷卡-他行");
				settlementKey.add(map.get("itemId")+"_2");
			}
		}
        //实收数据，按照实收方式分组，按照itemsort排序
        List<Map<String,Object>> orderActualSettlements = tbusinessDataDetailDao.getOrderActualSettlements(params);
        params.put("orderType", "0");
        List<Map<String,Object>> waimaiActualSettlements = tbusinessDataDetailDao.getOrderActualSettlements(params);
        Map<String,Object> orderInflate = tbusinessDataDetailDao.getOrderInflate(params);
        Map<String,Object> thActualAmount = tbusinessDataDetailDao.getthActualAmount(params);
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
//					//实收明细统计栏

//					//会员积分消费（元）Merbervaluenet
					if (businessR.get(i).get("integralconsum") != null) {
						businssRport.setIntegralconsum(ToolsUtil.formatTwoDecimal(businessR.get(i).get("integralconsum") + ""));
					}
//						//会员券消费（元）
					if (businessR.get(i).get("meberTicket") != null) {
						businssRport.setMeberTicket(ToolsUtil.formatTwoDecimal(businessR.get(i).get("meberTicket") + ""));
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
                    // 会员优惠（元）
                    if (businessR.get(i).get("memberDishPriceFree") != null) {
                        businssRport.setMemberDishPriceFree(ToolsUtil.formatTwoDecimal(businessR.get(i).get("memberDishPriceFree") + ""));
                    }
//
//					//折扣优惠
					if (businessR.get(i).get("discount") != null) {
						businssRport.setDiscountmoney(ToolsUtil.formatTwoDecimal(businessR.get(i).get("discount") + ""));
					}

//					#营业数据统计(堂吃应收）
					if (businessR.get(i).get("shouldamount_normal") != null) {
						businssRport.setShouldamountNormal(ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldamount_normal") + ""));
					}
//					外卖统计(应收）
					if (businessR.get(i).get("shouldamount_takeout") != null) {
						businssRport.setShouldamountTakeout(ToolsUtil.formatTwoDecimal(businessR.get(i).get("shouldamount_takeout") + ""));
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
						businssRport.setFraction(ToolsUtil.formatTwoDecimal(businessR.get(i).get("fraction").toString()));
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
                    
                    if (businessR.get(i).get("taocanyouhui") != null) {// 赠送
                        businssRport.setTaocanyouhui(ToolsUtil.formatTwoDecimal(businessR.get(i).get("taocanyouhui").toString()));
                    }

					/*if (judgePayforType() == 0) {   //表示 不处理

					} else if (judgePayforType() == 1) {    //表示 四舍五入
						businssRport.setHanderWay("四舍五入调整");
						businssRport.setHandervalue(ToolsUtil.formatTwoDecimal(businessR.get(i).get("roundoff").toString()));
					} else if (judgePayforType() == 2) {    //表示 抹零
						businssRport.setHanderWay("抹零");
						businssRport.setHandervalue(ToolsUtil.formatTwoDecimal(businessR.get(i).get("fraction").toString()));
					}*/
                    if(businessR.get(i).get("roundoff") != null){ //四舍五入
                        businssRport.setRoundoff(ToolsUtil.formatTwoDecimal(businessR.get(i).get("roundoff").toString()));
                    }
					if(businessR.get(i).get("closedbillnums") != null){ //已结账单数
                    	businssRport.setClosedordermums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedbillnums").toString()));
                    }
                    if(businessR.get(i).get("closedbillshouldamount") != null){ //已结账单应收
                    	businssRport.setClosedordershouldamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedbillshouldamount").toString()));
                    }
                    if(businessR.get(i).get("closedpersonnums") != null){ //已结人数
                    	businssRport.setClosedorderpersonnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedpersonnums").toString()));
                    }
                    if(businessR.get(i).get("nobillnums") != null){ //未结账单数
                    	businssRport.setNobillnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("nobillnums").toString()));
                    }
                    if(businessR.get(i).get("nobillshouldamount") != null){  //未结账单应收
                    	businssRport.setNobillshouldamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("nobillshouldamount").toString()));
                    }
                    if(businessR.get(i).get("nopersonnums") != null){  //未结人数
                    	businssRport.setNopersonnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("nopersonnums").toString()));
                    }
                    if(businessR.get(i).get("billnums") != null){ //全部账单数
                    	businssRport.setBillnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("billnums").toString()));
                    }
                    if(businessR.get(i).get("billshouldamount") != null){  //全部账单应收
                    	businssRport.setBillshouldamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("billshouldamount").toString()));
                    }
                    if(businessR.get(i).get("personnums") != null){  //全部人数
                    	businssRport.setPersonnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("personnums").toString()));
                    }
                    if(businessR.get(i).get("zaitaishu") != null){  //在台数
                    	businssRport.setZaitaishu(ToolsUtil.formatTwoDecimal(businessR.get(i).get("zaitaishu").toString()));
                    }
                    if(businessR.get(i).get("kaitaishu") != null){  //开台数
                    	businssRport.setKaitaishu(ToolsUtil.formatTwoDecimal(businessR.get(i).get("kaitaishu").toString()));
                    }
                    //应收总额
                    String shouldAmount = businessR.get(i).get("shouldamount")+"";
                    BigDecimal shouldAmountDecimal = new BigDecimal(shouldAmount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    //实收(总实收，具体实收)
                    Map<String,Object> actualAmountMap = sortSettlements(orderActualSettlements,settlementSort,thActualAmount);
                    BigDecimal actualTotalAmount = new BigDecimal(actualAmountMap.get("actualTotalAmount")+"");
                    //外卖实收
                    Map<String,Object> waimaiActualAmountMap = sortSettlements(waimaiActualSettlements,settlementSort,thActualAmount);
                    BigDecimal waimaiTotalAmount = new BigDecimal(waimaiActualAmountMap.get("actualTotalAmount")+"");
                    //平均实收
                    BigDecimal cusNumDecimal = new BigDecimal(businssRport.getSettlementnum()).setScale(2);
                    BigDecimal actualPre = actualTotalAmount.divide(cusNumDecimal.intValue()==0?new BigDecimal(1):cusNumDecimal,2);
					businssRport.setPaidinaverage(actualPre+"");
                    //虚增
                    String inflate = orderInflate.get("inflated")+"";
                    BigDecimal inflateDecimal = new BigDecimal(inflate).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    businssRport.setMebervalueadd(inflateDecimal+"");
                    //外卖虚增
                    String waimaiInflate = orderInflate.get("waimaiInflated")+"";
                    BigDecimal waimaiInflateDecimal = new BigDecimal(waimaiInflate).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    //外卖实收（外卖实收减外卖虚增）
                    businssRport.setPaidinamountTakeout(waimaiTotalAmount.subtract(waimaiInflateDecimal)+"");
                    //会员卡消费净值
                    String hykAmount = ((List<String>)actualAmountMap.get("settlements")).get(settlementSort.get("8"));
                    BigDecimal hykAmountDecimal = new BigDecimal(hykAmount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    businssRport.setMerbervaluenet(hykAmountDecimal.subtract(inflateDecimal)+"");
                    //实收总额（实收减虚增）
                    BigDecimal actualTotalAmountPure = actualTotalAmount.subtract(inflateDecimal);
					businssRport.setPaidinamount(actualTotalAmountPure+"");
					//折扣总额
					businssRport.setDiscountamount(shouldAmountDecimal.subtract(actualTotalAmountPure)+"");
					//实收列表值td
					List<String> settlements = (List<String>) actualAmountMap.get("settlements");
					settlements.set(settlementSort.get("8"), hykAmountDecimal+"");
					//封装
					businssRport.setSettlementDescList(settlementDescList);
					businssRport.setSettlements((List<String>)actualAmountMap.get("settlements"));
					businessList.add(businssRport);
				}
			}
		}
        return businessList;
    }
	public Map<String,Object> sortSettlements(List<Map<String,Object>> orderActualSettlements,Map<String,Integer> settlementSort,Map<String,Object> thActualAmount){
		BigDecimal thActualAmountDecimal = new BigDecimal(thActualAmount.get("actualAmount")+"").setScale(2, BigDecimal.ROUND_HALF_DOWN);
        Map<String,Object> result = new HashMap<String,Object>();
		BigDecimal actualTotalAmount = new BigDecimal("0.00");
        List<String> settlements = new ArrayList<String>(settlementSort.keySet().size());
        for(int i=0;i<settlementSort.keySet().size();i++){
        	settlements.add("0.00");
        }
		for(Map<String,Object> map : orderActualSettlements){
        	String itemId = map.get("itemId")+"";
        	String itemDesc = map.get("itemDesc")+"";
        	String actualAmount = map.get("actualAmount")+"";
        	BigDecimal actualAmountDecimal = new BigDecimal(actualAmount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        	actualTotalAmount = actualTotalAmount.add(actualAmountDecimal);
        	//挂账和挂账1合并
        	if("5".equals(itemId) || "13".equals(itemId)){
        		BigDecimal temp = new BigDecimal(settlements.get(settlementSort.get("5")));
        		actualAmountDecimal = actualAmountDecimal.add(temp);
        		settlements.set(settlementSort.get("5"), actualAmountDecimal+"");
        	}else if("17".equals(itemId) || "30".equals(itemId)){ //微信和微信扫码合并
        		BigDecimal temp = new BigDecimal(settlements.get(settlementSort.get("17")));
        		actualAmountDecimal = actualAmountDecimal.add(temp);
        		settlements.set(settlementSort.get("17"), actualAmountDecimal+"");
        	}else if("1".equals(itemId)){//银行分为工行和他行
        		actualAmountDecimal = actualAmountDecimal.subtract(thActualAmountDecimal);
        		settlements.set(settlementSort.get("1_1"), actualAmountDecimal+"");
        		settlements.set(settlementSort.get("1_2"), thActualAmountDecimal+"");
        	}else{
        		settlements.set(settlementSort.get(itemId), actualAmountDecimal+"");
        	}
        }
		result.put("settlements", settlements);
		result.put("actualTotalAmount", actualTotalAmount);
		return result;
	}
	@Override
	public List<BusinessReport1> isgetBusinessDetail(Map<String, Object> params) {
		List<Map<String, Object>> businessR = tbusinessDataDetailDao.isgetBusinessDetail(params);
		List<BusinessReport1> businessList = new ArrayList<BusinessReport1>();
		System.out.println("明细表："+businessR.size());
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
					if (businessR.get(i).get("icbccard") != null) {
						businssRport.setICBC(businessR.get(i).get("icbccard").toString());
					}

					//刷卡（元）-其他银行
					if (businessR.get(i).get("card") != null) {
						businssRport.setOtherbank(businessR.get(i).get("card").toString());
					}
//
					//微信
                    if (businessR.get(i).get("weixin") != null) {
                        businssRport.setWeixin(businessR.get(i).get("weixin").toString());
                    }
                    //支付宝
                    if (businessR.get(i).get("zhifubao") != null) {
                        businssRport.setZhifubao(businessR.get(i).get("zhifubao").toString());
                    }
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
					if(businessR.get(i).get("memberDishPriceFree") != null){//会员菜价优惠
						businssRport.setMemberDishPriceFree(ToolsUtil.formatTwoDecimal(businessR.get(i).get("memberDishPriceFree").toString()));
					}

//					if (judgePayforType() == 0) {   //表示 不处理
//
//					} else if (judgePayforType() == 1) {    //表示 四舍五入
//						businssRport.setHanderWay("四舍五入调整");
						businssRport.setHandervalue(ToolsUtil.formatTwoDecimal(businessR.get(i).get("roundoff").toString()));
//					} else if (judgePayforType() == 2) {    //表示 抹零
//						businssRport.setHanderWay("抹零");
//						businssRport.setHandervalue(ToolsUtil.formatTwoDecimal(businessR.get(i).get("fraction").toString()));
//					}
					if(businessR.get(i).get("closedbillnums") != null){ //已结账单数
                    	businssRport.setClosedordermums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedbillnums").toString()));
                    }
                    if(businessR.get(i).get("closedbillshouldamount") != null){ //已结账单应收
                    	businssRport.setClosedordershouldamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedbillshouldamount").toString()));
                    }
                    if(businessR.get(i).get("closedpersonnums") != null){ //已结人数
                    	businssRport.setClosedorderpersonnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedpersonnums").toString()));
                    }
                    if(businessR.get(i).get("nobillnums") != null){ //未结账单数
                    	businssRport.setNobillnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("nobillnums").toString()));
                    }
                    if(businessR.get(i).get("nobillshouldamount") != null){  //未结账单应收
                    	businssRport.setNobillshouldamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("nobillshouldamount").toString()));
                    }
                    if(businessR.get(i).get("nopersonnums") != null){  //未结人数
                    	businssRport.setNopersonnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("nopersonnums").toString()));
                    }
                    if(businessR.get(i).get("billnums") != null){ //全部账单数
                    	businssRport.setBillnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("billnums").toString()));
                    }
                    if(businessR.get(i).get("billshouldamount") != null){  //全部账单应收
                    	businssRport.setBillshouldamount(ToolsUtil.formatTwoDecimal(businessR.get(i).get("billshouldamount").toString()));
                    }
                    if(businessR.get(i).get("personnums") != null){  //全部人数
                    	businssRport.setPersonnums(ToolsUtil.formatTwoDecimal(businessR.get(i).get("personnums").toString()));
                    }
                    if(businessR.get(i).get("zaitaishu") != null){  //在台数
                    	businssRport.setZaitaishu(ToolsUtil.formatTwoDecimal(businessR.get(i).get("zaitaishu").toString()));
                    }
                    if(businessR.get(i).get("kaitaishu") != null){  //开台数
                    	businssRport.setKaitaishu(ToolsUtil.formatTwoDecimal(businessR.get(i).get("kaitaishu").toString()));
                    }
                    businessList.add(businssRport);
					businessList.add(businssRport);
				}
			}
		}

		return businessList;
	}
	@Override
	public List<Map<String, Object>> isgetBusinessDetailexcel1(Map<String, Object> params) {
		List<BusinessReport1> businessR = this.isgetBusinessDetail1(params);
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (businessR.size() > 0) {
			for (int i = 0; i < businessR.size(); i++) {
				if (businessR.get(i) != null) {
//					//营业数据收入统计栏
//					//应收总额
					Map<String, Object> map = new HashMap<>();
					map.put("key", "应收总额");
					map.put("title", "营业收入统计");
					map.put("value", businessR.get(i).getShouldamount());
					mapList.add(map);
//					//实收总额
					map = new HashMap<>();
					map.put("key", "实收总额");
					map.put("title", "营业收入统计");
					map.put("value", businessR.get(i).getPaidinamount());
					mapList.add(map);
//					//折扣总额
					map = new HashMap<>();
					map.put("key", "折扣总额");
					map.put("title", "营业收入统计");
					map.put("value", businessR.get(i).getDiscountamount());
					mapList.add(map);


//					//实收明细统计栏
//					//人民币
					//合计
					List<String> settlementDescList = businessR.get(i).getSettlementDescList();
					List<String> settlements = businessR.get(i).getSettlements();
					for(int j=0;j<settlementDescList.size();j++){
						map = new HashMap();
						map.put("key", settlementDescList.get(j));
						map.put("title", "实收总额统计");
						map.put("value", settlements.get(j));
						mapList.add(map);
					}

//					//优免（元）
					map = new HashMap<>();
					map.put("key", "优免");
					map.put("title", "折扣总额统计");
					map.put("value",businessR.get(i).getBastfree());
					mapList.add(map);

//					//会员积分消费（元）
					map = new HashMap<>();
					map.put("key", "会员积分消费");
					map.put("title", "折扣总额统计");
					map.put("value", businessR.get(i).getIntegralconsum());
					mapList.add(map);
//
//						//会员券消费（元）
					map = new HashMap<>();
					map.put("key", "会员券消费");
					map.put("title", "折扣总额统计");
					map.put("value",businessR.get(i).getMeberTicket());
					mapList.add(map);

					//if (judgePayforType() == 0) {   //表示 不处理

					//} else if (judgePayforType() == 1) {    //表示 四舍五入
						map = new HashMap<>();
						map.put("key", "四舍五入调整");
						map.put("title", "折扣总额统计");
						map.put("value",businessR.get(i).getRoundoff());
						mapList.add(map);
					//} else if (judgePayforType() == 2) {    //表示 抹零
						map = new HashMap<>();
						map.put("key", "抹零");
						map.put("title", "折扣总额统计");
						map.put("value",businessR.get(i).getFraction());
						mapList.add(map);
					//}
					//折扣优惠
					/*map = new HashMap<>();
					map.put("key", "折扣优惠");
					map.put("title", "折扣总额统计");
					map.put("value",businessR.get(i).getDiscount());
					mapList.add(map);*/
					//会员优惠
					map = new HashMap<>();
					map.put("key", "会员优惠");
					map.put("title", "折扣总额统计");
					map.put("value",businessR.get(i).getMemberDishPriceFree());
					mapList.add(map);
                    //赠送总额
					map = new HashMap<>();
					map.put("key", "赠送金额");
					map.put("title", "折扣总额统计");
					map.put("value",businessR.get(i).getGive());
					mapList.add(map);

					//折扣虚增（元）
					map = new HashMap<>();
					map.put("key", "会员储值消费虚增");
					map.put("title", "折扣总额统计");
					map.put("value",businessR.get(i).getMebervalueadd());
					mapList.add(map);
					map = new HashMap<>();
					map.put("key", "套餐优惠");
					map.put("title", "折扣总额统计");
					map.put("value",businessR.get(i).getTaocanyouhui());
					mapList.add(map);

//					//桌数（桌）
					map = new HashMap<>();
					map.put("key", "桌数");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getTablenum());
					mapList.add(map);
					
					//桌均消费
					map = new HashMap<>();
					map.put("key", "桌均消费");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getTableconsumption());
					mapList.add(map);

//					//结算人数（个）
					map = new HashMap<>();
					map.put("key", "结算人数");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getSettlementnum());
					mapList.add(map);

					//应收人均（元）
					map = new HashMap<>();
					map.put("key", "应收人均");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getShouldaverage());
					mapList.add(map);
//					//实收人均（元）
					map = new HashMap<>();
					map.put("key", "实收人均");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getPaidinaverage());
					mapList.add(map);
					//翻座率（%）
					map = new HashMap<>();
					map.put("key", "翻座率（%）");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getAttendance());
					mapList.add(map);

//					//翻台率(%)
					map = new HashMap<>();
					map.put("key", "翻台率(%)");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getOvertaiwan());
					mapList.add(map);

//					//平均消费时间（分）
					map = new HashMap<>();
					map.put("key", "平均消费时间");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getAvgconsumtime());
					mapList.add(map);
//
//                  #营业数据统计(堂吃应收）
					map = new HashMap<>();
					map.put("key", "堂食应收");
					map.put("title", "营业数据统计");
					map.put("value",businessR.get(i).getShouldamountNormal());
					mapList.add(map);

//					外卖统计(应收）
					map = new HashMap<>();
					map.put("key", "外卖应收");
					map.put("title", "外卖");
					map.put("value",businessR.get(i).getShouldamountTakeout());
					mapList.add(map);
					
					//#外卖统计(实收）
					map = new HashMap<>();
					map.put("key", "外卖实收");
					map.put("title", "外卖");
					map.put("value",businessR.get(i).getPaidinamountTakeout());
					mapList.add(map);

					// #外卖统计(订单数）
					map = new HashMap<>();
					map.put("key", "外卖单数");
					map.put("title", "外卖");
					map.put("value",businessR.get(i).getOrdercountTakeout());
					mapList.add(map);

					//#外卖统计(订单平均价格）
					map = new HashMap<>();
					map.put("key", "外卖单均");
					map.put("title", "外卖");
					map.put("value",businessR.get(i).getAvgpriceTakeout());
					mapList.add(map);

//                  会员消费笔数
					map = new HashMap<>();
					map.put("key", "会员消费笔数");
					map.put("title", "会员数据统计");
					map.put("value",businessR.get(i).getVipordercount());
					mapList.add(map);

					//会员消费占比
					map = new HashMap<>();
					map.put("key", "会员消费占比");
					map.put("title", "会员数据统计");
					map.put("value",businessR.get(i).getViporderpercent());
					mapList.add(map);

				//会员券消费
					map = new HashMap<>();
					map.put("key", "会员券消费");
					map.put("title", "会员数据统计");
					map.put("value",businessR.get(i).getMeberTicket());
					mapList.add(map);
				//会员积分消费
					map = new HashMap<>();
					map.put("key", "会员积分消费");
					map.put("title", "会员数据统计");
					map.put("value",businessR.get(i).getIntegralconsum());
					mapList.add(map);

				//现金
					map = new HashMap<>();
					map.put("key", "会员储值消费净值");
					map.put("title", "会员数据统计");
					map.put("value",businessR.get(i).getMerbervaluenet());
					mapList.add(map);

					map = new HashMap<>();
					map.put("key", "会员储值消费虚增");
					map.put("title", "会员数据统计");
					map.put("value",businessR.get(i).getMebervalueadd());
					mapList.add(map);
					//现金
					map = new HashMap<>();
					map.put("key", "合计");
					map.put("title", "会员数据统计");
					map.put("value",businessR.get(i).getMembertotal());
					mapList.add(map);
					//账单信息统计
					map = new HashMap<>();
					map.put("key", "已结账单数");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getClosedordermums());
					mapList.add(map);
					
					map = new HashMap<>();
					map.put("key", "已结账单应收");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getClosedordershouldamount());
					mapList.add(map);
					
					map = new HashMap<>();
					map.put("key", "已结人数");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getClosedorderpersonnums());
					mapList.add(map);
					
					map = new HashMap<>();
					map.put("key", "未结账单数");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getNobillnums());
					mapList.add(map);

					map = new HashMap<>();
					map.put("key", "未结账单应收");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getNobillshouldamount());
					mapList.add(map);

					map = new HashMap<>();
					map.put("key", "未结人数");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getNopersonnums());
					mapList.add(map);

					map = new HashMap<>();
					map.put("key", "全部账单数");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getBillnums());
					mapList.add(map);

					map = new HashMap<>();
					map.put("key", "全部账单应收");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getBillshouldamount());
					mapList.add(map);
					
					map = new HashMap<>();
					map.put("key", "全部人数");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getPersonnums());
					mapList.add(map);

					map = new HashMap<>();
					map.put("key", "在台数");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getZaitaishu());
					mapList.add(map);

					map = new HashMap<>();
					map.put("key", "开台数");
					map.put("title", "账单信息统计");
					map.put("value",businessR.get(i).getKaitaishu());
					mapList.add(map);
				}
			}
		}
		return mapList;
	}
	@Override
	public List<Map<String, Object>> isgetBusinessDetailexcel(Map<String, Object> params) {
		List<Map<String, Object>> businessR = tbusinessDataDetailDao.isgetBusinessDetail(params);
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (businessR.size() > 0) {
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
					//微信
					if (businessR.get(i).get("weixin") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "微信");
						map.put("title", "实收总额统计");
						map.put("value", businessR.get(i).get("weixin").toString());
						mapList.add(map);
						}
					
					//支付宝
					if (businessR.get(i).get("zhifubao") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "支付宝");
						map.put("title", "实收总额统计");
						map.put("value", businessR.get(i).get("zhifubao").toString());
						mapList.add(map);
						}

					//刷工行卡
					if (businessR.get(i).get("icbccard") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "刷卡-工行");
						map.put("title", "实收总额统计");
						map.put("value", businessR.get(i).get("icbccard").toString());
						mapList.add(map);
						}
					
					//刷卡他行
					if (businessR.get(i).get("card") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "刷卡-他行");
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
//					//折扣优惠
//					if (businessR.get(i).get("discount") != null) {
//						Map<String, Object> map = new HashMap<>();
//						map.put("key", "折扣优惠");
//						map.put("title", "折扣总额统计");
//						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("discount") + ""));
//						mapList.add(map);
//					}
					//会员优惠
					if(businessR.get(i).get("memberDishPriceFree") != null){//会员菜价优惠
						Map<String, Object> map = new HashMap<>();
						map.put("key", "会员优惠");
						map.put("title", "折扣总额统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("memberDishPriceFree") + ""));
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
					
					//账单信息统计
					if (businessR.get(i).get("closedbillnums") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "已结账单数");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedbillnums") + ""));
						mapList.add(map);
					}
					
					if (businessR.get(i).get("closedbillshouldamount") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "已结账单应收");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedbillshouldamount") + ""));
						mapList.add(map);
					}
					
					if (businessR.get(i).get("closedpersonnums") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "已结人数");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("closedpersonnums") + ""));
						mapList.add(map);
					}
					
					if (businessR.get(i).get("nobillnums") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "未结账单数");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("nobillnums") + ""));
						mapList.add(map);
					}
					if (businessR.get(i).get("nobillshouldamount") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "未结账单应收");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("nobillshouldamount") + ""));
						mapList.add(map);
					}
					
					if (businessR.get(i).get("nopersonnums") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "未结人数");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("nopersonnums") + ""));
						mapList.add(map);
					}
					if (businessR.get(i).get("billnums") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "全部账单数");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("billnums") + ""));
						mapList.add(map);
					}
					if (businessR.get(i).get("billshouldamount") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "全部账单应收");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("billshouldamount") + ""));
						mapList.add(map);
					}
					if (businessR.get(i).get("personnums") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "全部人数");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("personnums") + ""));
						mapList.add(map);
					}
					if (businessR.get(i).get("zaitaishu") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "在台数");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("zaitaishu") + ""));
						mapList.add(map);
					}
					if (businessR.get(i).get("kaitaishu") != null) {
						Map<String, Object> map = new HashMap<>();
						map.put("key", "开台数");
						map.put("title", "账单信息统计");
						map.put("value",ToolsUtil.formatTwoDecimal(businessR.get(i).get("kaitaishu") + ""));
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