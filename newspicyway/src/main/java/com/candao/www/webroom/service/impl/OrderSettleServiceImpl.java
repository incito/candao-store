package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
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
import org.springframework.util.StringUtils;

import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.constant.Constant.TABLETYPE;
import com.candao.www.data.dao.EmployeeUserDao;
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
import com.candao.www.webroom.service.OrderDetailService;
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
	 TsettlementDetailMapper  tsettlementDetailMapper;
	
	@Autowired
	TorderDetailMapper  torderDetailMapper;
	@Autowired
	TipService tipService;
	
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
	
	@Autowired
	private OrderDetailService orderdetailservice;
	@Autowired
	private EmployeeUserDao employeeUserDao;
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
			String errorMes="结算失败！查找餐桌失败 ,订单id:"+orderId;
			logger.error(errorMes);
			mapRet.put("result", "2");
			mapRet.put("mes", errorMes);
			return JacksonJsonMapper.objectToJson(mapRet); 
		}
		
		boolean tableBusy = checkTableStatus(resultMap);
		if(!tableBusy){
			String errorMes="结算失败！餐台状态不正常,订单号：" + orderId;
			logger.error(errorMes);
			mapRet.put("result", "1");
			mapRet.put("mes", errorMes);
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
			String errorMes="结算失败！未找到开业记录";
			logger.error(errorMes);
			mapRet.put("result", "1");
			mapRet.put("mes", errorMes);
			return JacksonJsonMapper.objectToJson(mapRet);
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
            if (detail.getPayWay() != null || !"".equals(detail.getPayWay())) {
                int payway = Integer.parseInt(detail.getPayWay());
                recordDetail.setPayway(payway);
            }
            switch (Integer.parseInt(detail.getPayWay())) {
                case Constant.PAYWAY.PAYWAY_CASH:
                    cashAmount.add(detail.getPayAmount());
                    break;
                case Constant.PAYWAY.PAYWAY_BANK_CARD:
                    bankAmount.add(detail.getPayAmount());
                    break;
                case Constant.PAYWAY.PAYWAY_MEMBER_CARD:
                    memberAmount.add(detail.getPayAmount());
                    break;
                case Constant.PAYWAY.PAYWAY_COUPON_CARD:
                    memberAmount.add(detail.getPayAmount());
                    break;
                case Constant.PAYWAY.PAYWAY_DISCOUNT:
                    discountAmount.add(detail.getPayAmount());
                    break;
                case Constant.PAYWAY.DEBITE_ACCOUNT:
                    recordDetail.setDebitParterner(detail.getDebitParterner());
                    debitAmount.add(detail.getPayAmount());
                    break;
                case Constant.PAYWAY.PAYWAY_FREE:
                    recordDetail.setPayamount(detail.getPayAmount());
                    recordDetail.setPayway(Constant.PAYWAY.PAYWAY_FREE);
                    freeAmount.add(detail.getPayAmount());
                    break;
            }

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
        Torder updateOrder = new Torder();
        updateOrder.setOrderid(orderId);
        updateOrder.setOrderstatus(3);
        updateOrder.setEndtime(new Date());
        orderService.update(updateOrder);


        //更新菜品的数量
        dishService.updateDishNum(orderId);
        //结账之后把操作的数据删掉
        Map<String, Object> delmap = new HashMap<String, Object>();
        delmap.put("orderid", orderId);
        toperationLogService.deleteToperationLog(delmap);
        //桌子空閒
        TbTable tbTable = new TbTable();
        tbTable.setStatus(0);
        tbTable.setOrderid(orderId);
        tableService.updateSettleStatus(tbTable);

        logger.info("结算成功！");
        return "0";
    }

	private boolean checkTableStatus(List<Map<String, Object>> resultMap) {
		for (Map<String, Object> map2 : resultMap) {
			if("1".equals(String.valueOf(map2.get("status")))){
				return true;
			}
		}
		return false;
	}
 	
 	/**
 	 * 咖啡模式结账
 	 * 去除清台，增加打印
 	 */
 	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String saveOrder(SettlementInfo settlementInfo) {
		// TODO Auto-generated method stub
		// 1.根據訂單 查詢金額
		// 2.減去會員優惠，折扣 等信息
		// 3.計算總額 減去所對應的優惠
 		
 		//判断是否需要打印
 		boolean isPrint = true;
		String orderId = settlementInfo.getOrderNo();

		Map<String, String> mapRet = new HashMap<String, String>();
		Map<String,Object> order = orderService.findOrderById(orderId);
		if (order == null || order.isEmpty()) {
			String mes="结算失败！查找订单失败 ,订单id:" + orderId;
			logger.error(mes);
			mapRet.put("result", "2");
			mapRet.put("mes", mes);
			return JacksonJsonMapper.objectToJson(mapRet);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableid", order.get("currenttableid"));
		List<Map<String, Object>> resultMap = tableService.find(map);

		if (resultMap == null || resultMap.size() == 0) {
			String mes="结算失败！查找餐桌失败 ,订单id:" + orderId;
			logger.error("结算失败！查找餐桌失败 ,订单id:" + orderId);
			mapRet.put("result", "2");
			mapRet.put("mes", mes);
			return JacksonJsonMapper.objectToJson(mapRet);
		}

		ArrayList<TsettlementDetail> listInsert = new ArrayList<TsettlementDetail>();

		Tsettlement record = new Tsettlement();

		record.setOrderid(settlementInfo.getOrderNo());
		record.setUserid(settlementInfo.getUserName());
		String settleId = IdentifierUtils.getId().generate().toString();
		record.setStatus(0);
		record.setSettledid(settleId);

		TbOpenBizLog bizLog = tbOpenBizLogDao.findOpenBizDate();
		if (bizLog == null) {
			String mes="结算失败！未找到开业记录";
			logger.error(mes);
			mapRet.put("result", "1");
			mapRet.put("mes", mes);
			return JacksonJsonMapper.objectToJson(mapRet);
		}

		BigDecimal cashAmount = new BigDecimal(0.00);
		BigDecimal bankAmount = new BigDecimal(0.00);
		BigDecimal memberAmount = new BigDecimal(0.00);
		BigDecimal discountAmount = new BigDecimal(0.00);
		BigDecimal debitAmount = new BigDecimal(0.00);
		BigDecimal freeAmount = new BigDecimal(0.00);

		for (SettlementDetail detail : settlementInfo.getPayDetail()) {

			TsettlementDetail recordDetail = new TsettlementDetail();
			recordDetail.setSdetailid(IdentifierUtils.getId().generate().toString());

			recordDetail.setSettleid(settleId);
			recordDetail.setOrderid(orderId);
			recordDetail.setIncometype(Constant.INCOMETYPE.NORMALINCOME);
			recordDetail.setMembercardno(detail.getMemerberCardNo());
			recordDetail.setBankcardno(detail.getBankCardNo());
			recordDetail.setPayamount(detail.getPayAmount());

			recordDetail.setCouponnum(detail.getCouponnum());
			recordDetail.setCouponid(detail.getCouponid());
			recordDetail.setCoupondetailid(detail.getCoupondetailid());

			if (detail.getPayWay() != null || !"".equals(detail.getPayWay())) {
				int payway = Integer.parseInt(detail.getPayWay());
				recordDetail.setPayway(payway);
			}

			if (String.valueOf(Constant.PAYWAY.PAYWAY_CASH).equals(detail.getPayWay())) {
				cashAmount.add(detail.getPayAmount());
			}
			if (String.valueOf(Constant.PAYWAY.PAYWAY_BANK_CARD).equals(detail.getPayWay())) {
				bankAmount.add(detail.getPayAmount());
			}
			if (String.valueOf(Constant.PAYWAY.PAYWAY_MEMBER_CARD).equals(detail.getPayWay())) {
				memberAmount.add(detail.getPayAmount());
			}
			if (String.valueOf(Constant.PAYWAY.PAYWAY_COUPON_CARD).equals(detail.getPayWay())) {
				memberAmount.add(detail.getPayAmount());
			}
			if (String.valueOf(Constant.PAYWAY.PAYWAY_DISCOUNT).equals(detail.getPayWay())) {
				discountAmount.add(detail.getPayAmount());
			}
			if (String.valueOf(Constant.PAYWAY.DEBITE_ACCOUNT).equals(detail.getPayWay())) {
				recordDetail.setDebitParterner(detail.getDebitParterner());
				debitAmount.add(detail.getPayAmount());
			}

			if (String.valueOf(Constant.PAYWAY.PAYWAY_FREE).equals(detail.getPayWay())) {
				recordDetail.setPayamount(detail.getPayAmount());
				recordDetail.setPayway(Constant.PAYWAY.PAYWAY_FREE);
				freeAmount.add(detail.getPayAmount());
			}
			listInsert.add(recordDetail);
		}

		record.setBankamount(bankAmount);
		record.setCashamount(cashAmount);
		record.setMemeberamount(memberAmount);
		record.setCreditamount(bankAmount.add(cashAmount).add(memberAmount));
		record.setIncomeType(Constant.INCOMETYPE.NORMALINCOME);
		record.setOpendate(bizLog.getOpendate());
		// 挂账
		record.setDebitamount(debitAmount);

		settlementMapper.insert(record);
		tsettlementDetailMapper.insertOnce(listInsert);
		
		//挂单结账时候不需要打印
		String payway = order.get("payway")==null?"":order.get("payway").toString();
		String ordertype = order.get("ordertype")==null?"":order.get("ordertype").toString();
		if ("3".equals(payway) && "2".equals(ordertype)) {
			isPrint = false;
		}

		Torder updateOrder = new Torder();
		updateOrder.setOrderid(orderId);
		updateOrder.setOrderstatus(3);
		updateOrder.setEndtime(new Date());
		orderService.update(updateOrder);

		// 更新菜品的数量
		dishService.updateDishNum(orderId);
		// 结账之后把操作的数据删掉
		Map<String, Object> delmap = new HashMap<String, Object>();
		delmap.put("orderid", orderId);
		toperationLogService.deleteToperationLog(delmap);
		
		//最后一步打印
		if (isPrint) {
			orderdetailservice.afterprint(orderId);
		}
		
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
		TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
		// TODO Auto-generated method stub
		// 1.根據訂單 查詢金額
		// 2.減去會員優惠，折扣 等信息
		try {
			// 3.計算總額 減去所對應的優惠
			String orderId = settlementInfo.getOrderNo();

			// start先查询是不是微信扫码支付
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("orderno", orderId);
			dataMap.put("payway", Constant.PAYWAY.PAYWAY_WEIXIN);
			int isweixin = settlementMapper.selectIsPayWeixin(dataMap);
			// end
			// 查询反结算次数
			String againSettleNums = settlementMapper
					.queryAgainSettleNums(orderId);
			Map<String, Object> userInfo = employeeUserDao.getUserByJobNumber(settlementInfo.getUserName());
			if (againSettleNums == null || againSettleNums.equals("0")) {
				// 插入反结算 主记录，先判断是否有会员消费虚增
				Double inflated = settlementMapper.getMemberInflated(orderId);
				settlementMapper.insertSettlementHistory(orderId,
						settlementInfo.getReason(), 1,
						userInfo.get("name")+"("+settlementInfo.getUserName()+")", inflated);
			} else {
				// 如果不是第一次反结算，修改反结算表反结算次数字段，每反结算一次加1
				int nums = Integer.parseInt(againSettleNums) + 1;
				settlementMapper.updateSettlementHistory(orderId, nums,
						userInfo.get("name")+"("+settlementInfo.getUserName()+")",
						settlementInfo.getReason());
			}
			// 删除原结算信息
			settlementMapper.delete(orderId);
			tsettlementDetailMapper.deleteBySettleId(orderId);
			// 查询会员消费虚增
			BigDecimal inflated = tRethinkSettlementDao
					.queryMemberInflate(orderId);
			if (inflated != null) {
				settlementMapper.updateTorderMember(orderId);
			}

			// //已經結清 0 已下单 1 单桌结清 2 相关联桌号已经结清 3 内部结算 4 正在下单
			// Map<String, Object> mapOrder =
			// orderService.findOrderById(orderId);
			// String tableid = String.valueOf(mapOrder.get("currenttableid"));
			Torder updateOrder = new Torder();
			updateOrder.setOrderid(orderId);
			updateOrder.setOrderstatus(0);
			orderService.update(updateOrder);
			
			//查询账单类型
			Map<String, Object> order = orderService.findOrderById(orderId);
			if (MapUtils.isEmpty(order)) {
				logger.error("反结算出现异常,查询不到账单，" + orderId);
				this.transactionManager.rollback(status);
				return "{\"result\":\"1\"}";
			}
			String currenttableid = String.valueOf(order.get("currenttableid"));
			TbTable table =  tableService.findById(currenttableid);
			if (table == null ) {
				logger.error("反结算出现异常,查询不到账单相关餐台，" + orderId);
				this.transactionManager.rollback(status);
				return "{\"result\":\"1\"}";
			}
			String tabletype = table.getTabletype();
			
			// //桌子空閒 0 空闲 1 就餐 3 预定 4 已结账
			//外卖和咖啡外卖自动清台
			TbTable tbTable = new TbTable();
			if (StringUtils.isEmpty(tbTable) || (!TABLETYPE.TAKEOUT.equals(tabletype) && !TABLETYPE.TAKEOUT_COFFEE.equals(tabletype))) {
				tbTable.setStatus(1);
			} else {
				tbTable.setStatus(0);
			}
			tbTable.setOrderid(orderId);

			tableService.updateSettleStatus(tbTable);
			//反结算小费信息
			tipService.rebacktip(orderId);
			// AUTO事物处理
			// 微信扫码支付反结算调用
			if (isweixin > 0) {// 是微信扫码结算的
				try {
					String weixinturnback = "http://"
							+ PropertiesUtils.getValue("PSI_URL")
							+ "/newspicyway/weixin/turnback";
					logger.info("微信扫码反结算"+weixinturnback);
					String retPSI = new HttpRequestor().doPost(weixinturnback,
							dataMap);
					Map<String, String> retMap = JacksonJsonMapper.jsonToObject(retPSI, Map.class);
					if (retMap == null || "1".equals(retMap.get("code"))) {
						transactionManager.rollback(status); // 强制回滚
						logger.error("反结算失败！微信反结算失败");
						return Constant.FAILUREMSG;
					}
					transactionManager.commit(status);
					return "2";// 微信扫码反结算成功
				} catch (Exception e) {
					logger.error("-->", e);
					e.printStackTrace();
					transactionManager.rollback(status);
					return "1";
				}
			}
			transactionManager.commit(status);
			logger.info("反结算成功 ");
			return "0";
		} catch (Exception e) {
			logger.error("反结算出现异常", e);
			this.transactionManager.rollback(status);
			return "{\"result\":\"1\"}";
		}
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
			
			//added by caicai
			//是否打印厨打单
			String tableNO = String.valueOf(orderinfo.get("currenttableid"));
			TbTable table = tableService.findById(tableNO);
			if (table != null) {
				String type = table.getTabletype();
				if (!StringUtils.isEmpty(type) && TABLETYPE.COFFEETABLE.equals(type)) {
					settlementStrInfoDto.setFlag(true);
				}
			}
			
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
		//attacht数据格式[0]orderId,[1]支付金额,[2]优免金额[3]会员号[4]默认值
		//<attach><![CDATA[H20160926561706011680;0.24;0.06;0;0]]></attach>
		if(attach!=null){
			String[] args=attach.split(";");
			if(args.length>2){
				Map<String, Object> map=new HashMap<>();
				//castmoney应收=实际支付方式+优惠
				BigDecimal  b1=new BigDecimal(args[1]);
				BigDecimal  b2=new BigDecimal(args[2]);
				map.put("orderno", args[0]);
				map.put("castmoney",b1.add(b2));
				map.put("paymoney", args[1]);
				map.put("youmian", b2);
				torderDetailMapper.updateOrderinfo(map);
			}
		}
	}
	
	
	@Override
	public Map<String, Object> selectorderinfos(String orderid) {
		return torderDetailMapper.selectorderinfos(orderid);
	}
}
