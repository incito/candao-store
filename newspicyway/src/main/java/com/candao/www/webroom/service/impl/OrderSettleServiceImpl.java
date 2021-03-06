package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TRethinkSettlementDao;
import com.candao.www.data.dao.TbOpenBizLogDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TsettlementDetailMapper;
import com.candao.www.data.dao.TsettlementMapper;
import com.candao.www.data.model.TbOpenBizLog;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Torder;
import com.candao.www.data.model.Tsettlement;
import com.candao.www.data.model.TsettlementDetail;
import com.candao.www.utils.HttpRequestor;
import com.candao.www.webroom.model.SettlementDetail;
import com.candao.www.webroom.model.SettlementInfo;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.OrderDetailSettleService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.OrderSettleService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.ToperationLogService;
import com.candao.www.weixin.dao.WeixinDao;
import com.candao.www.weixin.dto.PayDetail;
import com.candao.www.weixin.dto.SettlementStrInfoDto;
import com.candao.www.weixin.dto.WxPayResult;


@Service
public class OrderSettleServiceImpl implements OrderSettleService{
	
	private static final Logger logger = LoggerFactory.getLogger(OrderSettleServiceImpl.class);
	
	@Autowired
	TsettlementMapper settlementMapper;
	
	@Autowired
	OrderDetailSettleService orderDetailSettleService;
	
	@Autowired
	 TsettlementDetailMapper  tsettlementDetailMapper;
	
	@Autowired
	TorderDetailMapper  torderDetailMapper;
	
	@Autowired
	private TdishDao tdishDao;
	
	@Autowired
	private WeixinDao   weixinDao;
	
	@Autowired
	OrderService  orderService;
	
	@Autowired
	TableService  tableService;
	
	@Autowired
	DishService  dishService;
	
	@Autowired
	TbOpenBizLogDao tbOpenBizLogDao;
	@Autowired
	private ToperationLogService  toperationLogService;
	
	@Autowired
	private TRethinkSettlementDao tRethinkSettlementDao;
	@Autowired
	 DataSourceTransactionManager transactionManager ;

 	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveposcash(SettlementInfo settlementInfo) {

	 String orderId = settlementInfo.getOrderNo();
	 TbOpenBizLog tbOpenBizLog = tbOpenBizLogDao.findOpenBizDate();
	 BigDecimal totalAmount = new BigDecimal(0);
    
	 String settleId = IdentifierUtils.getId().generate().toString();
	 Tsettlement settle = new Tsettlement();
	 settle.setCashamount(settlementInfo.getPayamount());
	 settle.setOrderid(settlementInfo.getOrderNo());
	 settle.setSettledid(settleId);
	 settle.setStatus(Constant.SETTLE.NORMALSETTLE);
	 settle.setIncomeType(Constant.INCOMETYPE.ODDCHANGE);
	 settle.setOpendate(tbOpenBizLog.getOpendate());
	 
	 settlementMapper.insert(settle);
	 
	 TsettlementDetail record = new TsettlementDetail();
	 record.setOrderid(settlementInfo.getOrderNo());
	 record.setUserName(settlementInfo.getUserName());
	 record.setSdetailid(IdentifierUtils.getId().generate().toString());
	 record.setOrderid(orderId);
	 record.setPayamount(settlementInfo.getPayamount());
	 totalAmount.add(settlementInfo.getPayamount());
	 record.setIncometype(Constant.INCOMETYPE.ODDCHANGE);
	 record.setPayway(Constant.PAYWAY.PAYWAY_CASH);
	 
	 tsettlementDetailMapper.insert(record);
	 
 
	 return Constant.SUCCESSMSG;
	}
 	
 	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class) 
	@Override
	public String settleOrder(SettlementInfo settlementInfo) {
		// TODO Auto-generated method stub
		//1.根據訂單 查詢金額  
		//2.減去會員優惠，折扣 等信息
		//3.計算總額  減去所對應的優惠

 		 String orderId = settlementInfo.getOrderNo();
 		 
		Map<String, String> mapRet = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", orderId);
//		map.put("status", "0");
		List<Map<String, Object>> resultMap = tableService.find(map);
		
		if(resultMap == null || resultMap.size() == 0  ){
			logger.error("结算失败！查找餐桌失败 ,订单id:"+orderId);
			mapRet.put("result", "2");
			return JacksonJsonMapper.objectToJson(mapRet); 
		}
		
		if(! "1".equals(String.valueOf(resultMap.get(0).get("status")))){
			logger.error("结算失败！ 餐桌状态为：status为 1");
			mapRet.put("result", "1");
			return JacksonJsonMapper.objectToJson(mapRet); 
		}
	 
//	 BigDecimal totalAmount = new BigDecimal(0);
	 
	 ArrayList<TsettlementDetail> listInsert = new ArrayList<TsettlementDetail>();
	 
	 Tsettlement record = new Tsettlement();
		
	 record.setOrderid(settlementInfo.getOrderNo());
	 record.setUserid(settlementInfo.getUserName());
	 String settleId = IdentifierUtils.getId().generate().toString();
	 record.setStatus(0);
	 record.setSettledid(settleId);
	 
	 TbOpenBizLog bizLog = tbOpenBizLogDao.findOpenBizDate();
	 if(bizLog == null)	 {
		 logger.error("结算失败！未找到开业记录");
		 return "1";
	 }
	 
	 BigDecimal cashAmount = new BigDecimal(0.00);
	 BigDecimal bankAmount = new BigDecimal(0.00);
	 BigDecimal memberAmount = new BigDecimal(0.00);
//	 BigDecimal couponAmount = new BigDecimal(0.00);
	 BigDecimal discountAmount = new BigDecimal(0.00);
	 BigDecimal debitAmount = new BigDecimal(0.00);
	 BigDecimal freeAmount = new BigDecimal(0.00);
	 
	 for(SettlementDetail detail: settlementInfo.getPayDetail() ){
		 
		 TsettlementDetail recordDetail = new TsettlementDetail();
		 recordDetail.setSdetailid(IdentifierUtils.getId().generate().toString());
		 
		 recordDetail.setSettleid(settleId);
		 recordDetail.setOrderid(orderId);
		 recordDetail.setIncometype(Constant.INCOMETYPE.NORMALINCOME);
//		 recordDetail.setSdetailid(settleId);
		 recordDetail.setMembercardno(detail.getMemerberCardNo());
		 recordDetail.setBankcardno(detail.getBankCardNo());
		 recordDetail.setPayamount(detail.getPayAmount());
		 
		 recordDetail.setCouponnum(detail.getCouponnum());
		 recordDetail.setCouponid(detail.getCouponid());
		 recordDetail.setCoupondetailid(detail.getCoupondetailid());
		 
//		 recordDetail.setPayway(detail.getPayWay());
		if( detail.getPayWay() != null || !"".equals(detail.getPayWay())){
			int payway = Integer.parseInt(detail.getPayWay());
			 recordDetail.setPayway(payway);
		}
		 
		 if(String.valueOf(Constant.PAYWAY.PAYWAY_CASH).equals(detail.getPayWay())){
//			 recordDetail.setPayamount(detail.getPayAmount());
			
			 cashAmount.add(detail.getPayAmount());
		 }
		 if(String.valueOf(Constant.PAYWAY.PAYWAY_BANK_CARD).equals(detail.getPayWay())){
//			 recordDetail.setPayamount(detail.getPayAmount());
//			 recordDetail.setPayway(Constant.PAYWAY.PAYWAY_BANK_CARD);
			 bankAmount.add(detail.getPayAmount());
		 }
		 if(String.valueOf(Constant.PAYWAY.PAYWAY_MEMBER_CARD).equals(detail.getPayWay())){
//			 recordDetail.setPayamount(detail.getPayAmount());
//			 recordDetail.setPayway(Constant.PAYWAY.PAYWAY_MEMBER_CARD);
			 memberAmount.add(detail.getPayAmount());
		 }
		 if(String.valueOf(Constant.PAYWAY.PAYWAY_COUPON_CARD).equals(detail.getPayWay())){
//			 recordDetail.setPayamount(detail.getPayAmount());
//			 recordDetail.setPayway(Constant.PAYWAY.PAYWAY_COUPON_CARD);
			 memberAmount.add(detail.getPayAmount());
		 }
		 if(String.valueOf(Constant.PAYWAY.PAYWAY_DISCOUNT).equals(detail.getPayWay())){
//			 recordDetail.setPayamount(detail.getPayAmount());
//			 recordDetail.setPayway(Constant.PAYWAY.PAYWAY_DISCOUNT);
			 discountAmount.add(detail.getPayAmount());
		 }
		 if(String.valueOf(Constant.PAYWAY.DEBITE_ACCOUNT).equals(detail.getPayWay())){
//			 recordDetail.setPayamount(detail.getPayAmount());
//			 recordDetail.setPayway(Constant.PAYWAY.DEBITE_ACCOUNT);
			 recordDetail.setDebitParterner(detail.getDebitParterner());
			 debitAmount.add(detail.getPayAmount());
		 }
		 
		 if(String.valueOf(Constant.PAYWAY.PAYWAY_FREE).equals(detail.getPayWay())){
			 recordDetail.setPayamount(detail.getPayAmount());
			 recordDetail.setPayway(Constant.PAYWAY.PAYWAY_FREE);
			 freeAmount.add(detail.getPayAmount());
		 }
	  //   tsettlementMapper.insert(record);
		 listInsert.add(recordDetail);
	  }
	 
	 
	 record.setBankamount(bankAmount);
	 record.setCashamount(cashAmount);
	 record.setMemeberamount(memberAmount);
	 record.setCreditamount(bankAmount.add(cashAmount).add(memberAmount));
	 record.setIncomeType(Constant.INCOMETYPE.NORMALINCOME);
	 record.setOpendate(bizLog.getOpendate());
	 //挂账
	 record.setDebitamount(debitAmount);
	 
	 
	 settlementMapper.insert(record);
	 tsettlementDetailMapper.insertOnce(listInsert);
	 
	 //已經結清
//	 Map<String, Object> mapOrder = orderService.findOrderById(orderId);
//	 String tableid = String.valueOf(mapOrder.get("currenttableid"));
	 Torder updateOrder = new Torder();
	 updateOrder.setOrderid(orderId);
	 updateOrder.setOrderstatus(3);
	 updateOrder.setEndtime(new Date());
	 orderService.update(updateOrder);
	 
	 
	 //更新菜品的数量
	 dishService.updateDishNum(orderId);
	 //结账之后把操作的数据删掉
	  Map<String,Object> delmap=new HashMap<String,Object>();
	  delmap.put("orderid", orderId);
	  toperationLogService.deleteToperationLog(delmap);
	 //桌子空閒
	 TbTable tbTable = new TbTable();
	 tbTable.setStatus(0);
	 tbTable.setOrderid(orderId);
	 tableService.updateSettleStatus(tbTable);
	 
//	 TbTable tbTableOrderid = new TbTable();
//	 tbTableOrderid.setTableid(String.valueOf(resultMap.get(0).get("tableid")));
//	 tbTableOrderid.setOrderid(orderId);
//	 //不知道干嘛的，遇到问题再说
//	 tableService.updateSettleOrderNull(tbTableOrderid);
	 
 //修改到了controller 单独调用
//	  Map<String, Object> orderDetailMap = new HashMap<String, Object>();
//	  orderDetailMap.put("orderid", orderId);
//	  tsettlementDetailMapper.calDebitAmount(orderDetailMap);
	  
//	  List<TorderDetail> details = torderDetailMapper.find(orderDetailMap);
//	  for(TorderDetail detail : details){
//		 Tdish  dish = new Tdish();
//		 dish.setDishid(detail.getDishid());
//		 dish.setOrderNum(String.valueOf(new BigDecimal(dish.getOrderNum() == null?"0":dish.getOrderNum() ).subtract(new BigDecimal(detail.getDishnum() == null?"0":detail.getDishnum()))));
//		 tdishDao.updateOrderNum(dish);
//	  }
	 logger.info("结算成功！");
	  return "0";
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
 	public String calDebitAmount(String orderId){
		  Map<String, Object> orderDetailMap = new HashMap<String, Object>();
		  orderDetailMap.put("orderid", orderId);
		  tsettlementDetailMapper.calDebitAmount(orderDetailMap);
		  
		  return "0";
 	}
 	
	@Override
	public String rebackSettleOrder(SettlementInfo settlementInfo) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		  def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); 
		  TransactionStatus status = transactionManager.getTransaction(def); //获得事务状态
		// TODO Auto-generated method stub
		//1.根據訂單 查詢金額  
		//2.減去會員優惠，折扣 等信息

		//3.計算總額  減去所對應的優惠
		String orderId = settlementInfo.getOrderNo();
		
		 //start先查询是不是微信扫码支付
		 Map<String, String> dataMap = new HashMap<String, String>();
		 dataMap.put("orderno", orderId);
		 dataMap.put("payway", Constant.PAYWAY.PAYWAY_WEIXIN);
		 int isweixin=settlementMapper.selectIsPayWeixin(dataMap);
		 //end
		//查询反结算次数
		String againSettleNums = settlementMapper.queryAgainSettleNums(orderId);
		if(againSettleNums == null || againSettleNums.equals("0")){
			//插入反结算 主记录，先判断是否有会员消费虚增
			Double inflated = settlementMapper.getMemberInflated(orderId);  
			settlementMapper.insertSettlementHistory(orderId,settlementInfo.getReason(),1,settlementInfo.getUserName(),inflated);
		}else{
			//如果不是第一次反结算，修改反结算表反结算次数字段，每反结算一次加1
			int nums = Integer.parseInt(againSettleNums)+1;
			settlementMapper.updateSettlementHistory(orderId, nums, settlementInfo.getUserName(),settlementInfo.getReason());
	    }
		//删除原结算信息
		settlementMapper.delete(orderId);
		tsettlementDetailMapper.deleteBySettleId(orderId);
		//查询会员消费虚增
	    BigDecimal inflated = tRethinkSettlementDao.queryMemberInflate(orderId);
	    if(inflated != null){
	    	settlementMapper.updateTorderMember(orderId);
	    }
	 
//	 //已經結清 0 已下单 1 单桌结清 2 相关联桌号已经结清  3 内部结算 4 正在下单
//	 Map<String, Object> mapOrder = orderService.findOrderById(orderId);
//	 String tableid = String.valueOf(mapOrder.get("currenttableid"));
	 Torder updateOrder = new Torder();
	 updateOrder.setOrderid(orderId);
	 updateOrder.setOrderstatus(0);
	 orderService.update(updateOrder);
//	 
//	 //桌子空閒 0 空闲	 1 就餐	 3 预定	 4 已结账
	 TbTable tbTable = new TbTable();
	 tbTable.setStatus(1);
	 tbTable.setOrderid(orderId);
	 
	 tableService.updateSettleStatus(tbTable);
	 //AUTO事物处理
	 //微信扫码支付反结算调用
	 if(isweixin>0){//是微信扫码结算的
				 try {
					 String weixinturnback="http://"+PropertiesUtils.getValue("PSI_URL")+"/newspicyway/weixin/turnback";
					String retPSI=new HttpRequestor().doPost(weixinturnback, dataMap);
					Map<String,String> retMap = JacksonJsonMapper.jsonToObject(retPSI, Map.class);
					System.out.println("微信扫码反结算");
					 if(retMap == null || "1".equals(retMap.get("code"))){	
						    transactionManager.rollback(status);  //强制回滚
						    logger.error("反结算失败！微信反结算失败");
							return Constant.FAILUREMSG;
					 }
					 transactionManager.commit(status);
					 return "2";//微信扫码反结算成功
				} catch (Exception e) {
					logger.error("-->",e);
					e.printStackTrace();
					transactionManager.rollback(status);
					return "1";
				}
	 }
	 //
	 transactionManager.commit(status);
	 logger.info("反结算成功 ");
     return "0";
	}

	@Override
	public int rebaceSettleOrder(SettlementInfo settlementInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public SettlementStrInfoDto setInitData(SettlementStrInfoDto settlementStrInfoDto,WxPayResult payResult) {
		String[]  infos=payResult.getAttach().split(";");
		payResult.setAttach(payResult.getAttach().replaceAll(";", "|"));
		settlementStrInfoDto.setOrderNo(infos[0]);
		String orderno=settlementStrInfoDto.getOrderNo();
		//根据订单id查询出订单信息
		Map<String, Object>  orderinfo= orderService.findOrderById(orderno);
		if(orderinfo!=null){
			String  userName=orderinfo.get("userid").toString();
			settlementStrInfoDto.setUserName(userName);
		}
		//订单详情
		List<PayDetail>  payDetails=   new ArrayList<>();
		
		for(int i=1;i<infos.length-2;i++){
				PayDetail  detail=new PayDetail();
				detail.setBankCardNo("");
				detail.setCoupondetailid("");
				detail.setCouponid("");
				detail.setCouponnum("");
				detail.setMemerberCardNo("");
				detail.setPayAmount(infos[i]);
				if(i==1){
					detail.setPayWay(Constant.PAYWAY.PAYWAY_WEIXIN);
				}
				if(i==2){
					detail.setPayWay(String.valueOf(Constant.PAYWAY.PAYWAY_FREE));
				}
				payDetails.add(detail);
		}
		
		settlementStrInfoDto.setPayDetail(payDetails);
		//
		weixinDao.saveTempoldOrderid(payResult.getOutTradeNo(),infos[0]);
		return settlementStrInfoDto;
	}
	
	
	@Override
	public void updatePadData(String attach) {
		if(attach!=null){
			String[] args=attach.split(";");
			if(args.length>2){
				Map<String, Object> map=new HashMap<>();
				map.put("orderno", args[0]);
				map.put("castmoney", args[1]);
				map.put("paymoney", args[2]);
				BigDecimal  b1=new BigDecimal(args[1]);
				BigDecimal  b2=new BigDecimal(args[2]);
				BigDecimal  b3= b1.subtract(b2);
				map.put("youmian", b3);
				torderDetailMapper.updateOrderinfo(map);
			}
		}
	}
	
	
	@Override
	public Map<String, Object> selectorderinfos(String orderid) {
		return torderDetailMapper.selectorderinfos(orderid);
	}
}
