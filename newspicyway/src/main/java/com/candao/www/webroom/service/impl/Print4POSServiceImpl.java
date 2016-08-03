package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.candao.common.utils.Constant;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.DishItem;
import com.candao.print.entity.OrderInfo4Pos;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.ResultInfo4Pos;
import com.candao.print.entity.ResultTip4Pos;
import com.candao.print.entity.SettlementInfo4Pos;
import com.candao.print.entity.TipItem;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.Print4POSService;

@Service
public class Print4POSServiceImpl implements Print4POSService {

	public static final String SETTLEMENT = "2";

	public static final String PRESETTLEMENT = "1";

	public static final String CUSTTEMPLATE = "3";

	public static final String MEMBERSALEINFO_CUST = "客户联";

	public static final String MEMBERSALEINFO_STORE = "商户联";

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(5000));
	@Autowired
	private Print4POSProcedure print4POSProcedure;
	@Autowired
	TbPrinterManagerDao tbPrinterManagerDao;
	@Autowired
	private TbBranchDao tbBranchDao;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private OrderDetailServiceImpl orderDetail;

	@Override
	public void print(List<SettlementInfo4Pos> settlementInfos, String printType) throws Exception {
		if (CollectionUtils.isEmpty(settlementInfos) || StringUtils.isEmpty(printType)) {
			return;
		}
		PrintObj obj = new PrintObj();
		// 三种类型类型单据
		switch (printType.trim()) {
		case SETTLEMENT:
			obj.setListenerType(Constant.ListenerType.SettlementDishListener);
			break;
		case PRESETTLEMENT:
			obj.setListenerType(Constant.ListenerType.PreSettlementTemplate);
			break;
		case CUSTTEMPLATE: {
			SettlementInfo4Pos settlement = settlementInfos.get(0);
			if (!CollectionUtils.isEmpty(settlement.getOrderJson())) {
				OrderInfo4Pos orderinfo = settlement.getOrderJson().get(0);
				orderDetail.printCust(orderinfo.getOrderid(), true);
			}
			return;
		}
		default:
			return;
		}
		Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
		Map<String, Object> param = new HashMap<>();
		param.clear();
		param.put("type", "ACCURACY");
		List<Map<String, Object>> resultmap = dataDictionaryService.findByParams(param);
		if (!CollectionUtils.isEmpty(resultmap)) {
			for (SettlementInfo4Pos it : settlementInfos) {
				it.init(Integer.parseInt(String.valueOf(resultmap.get(0).get("itemid"))));
			}
		}
		SettlementInfo4Pos settlementInfo4Pos = settlementInfos.get(0);
		settlementInfo4Pos.setBranchName(String.valueOf(branchInfo.get("branchname")));
		settlementInfo4Pos.setTel(String.valueOf(branchInfo.get("managertel")));
		settlementInfo4Pos.setAddress(String.valueOf(branchInfo.get("branchaddress")));
		obj.setSettlementInfo4Pos(settlementInfo4Pos);

		// TODO
		Map<String, Object> params = new HashMap<>();
		params.put("printertype", "10");
		sendToPrint(params, obj);
	}

	@Override
	public void printClearMachine(List<SettlementInfo4Pos> settlementInfos) {
		if (CollectionUtils.isEmpty(settlementInfos)) {
			return;
		}
		PrintObj obj = new PrintObj();
		obj.setSettlementInfo4Pos(settlementInfos.get(0));

		// TODO
		Map<String, Object> params = new HashMap<>();
		params.put("printertype", "10");
		sendToPrint(params, obj);
	}

	@Override
	public void printMemberSaleInfo(List<SettlementInfo4Pos> settlementInfos) {
		if (CollectionUtils.isEmpty(settlementInfos)) {
			return;
		}
		String[] types = { MEMBERSALEINFO_CUST, MEMBERSALEINFO_STORE };
		PrintObj obj = new PrintObj();
		SettlementInfo4Pos settlementInfo4Pos = settlementInfos.get(0);
		for (int i = 0; i < types.length; i++) {
			settlementInfo4Pos.getOrderJson().get(0).setType(types[i]);
			obj.setSettlementInfo4Pos(settlementInfo4Pos);
			Map<String, Object> params = new HashMap<>();
			params.put("printertype", "10");
			sendToPrint(params, obj);
		}
	}

	private void sendToPrint(Map<String, Object> param, PrintObj obj) {
		// TODO
		if (param == null) {
			return;
		}
		List<Map<String, Object>> res = tbPrinterManagerDao.find(param);
		if (CollectionUtils.isEmpty(res)) {
			return;
		}
		for (int i = 0; i < res.size(); i++) {
			Map<String, Object> map = res.get(i);
			obj.setPrinterid(String.valueOf(map.get("printerid")));
			obj.setCustomerPrinterIp(String.valueOf(map.get("ipaddress")));
			print4POSProcedure.setSource(obj);
			executor.execute(print4POSProcedure);
		}
	}

	@Override
	public void printItemSellDetail(ResultInfo4Pos resultInfo4Pos) {
		if (resultInfo4Pos == null) {
			return;
		}
		Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
		if (!MapUtils.isEmpty(branchInfo)) {
			resultInfo4Pos.setBranname(String.valueOf(branchInfo.get("branchname")));
		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		resultInfo4Pos.setDatetime(sdf.format(date));
		String total = "0";

		if (!CollectionUtils.isEmpty(resultInfo4Pos.getData())) {
			int index = 0;
			for (DishItem it : resultInfo4Pos.getData()) {
				it.setIndex(++index + "");
				total = stringAdd(total, it.getTotlePrice());
			}
		}
		resultInfo4Pos.setTotal(total);
		
		PrintObj obj = new PrintObj();
		obj.setItem(resultInfo4Pos);
		
		// TODO
		Map<String, Object> params = new HashMap<>();
		params.put("printertype", "10");
		sendToPrint(params, obj);
	}

	private String stringAdd(String i1, String i2) {
		i1 = StringUtils.isEmpty(i1) ? "0" : i1;
		i2 = StringUtils.isEmpty(i1) ? "0" : i2;
		return new BigDecimal(i1).add(new BigDecimal(i2)).toString();
	}

	@Override
	public void printTip(ResultTip4Pos resultInfo4Pos) {
		if (resultInfo4Pos == null) {
			return;
		}
		Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
		if (!MapUtils.isEmpty(branchInfo)) {
			resultInfo4Pos.setBranchname(String.valueOf(branchInfo.get("branchname")));
		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		resultInfo4Pos.setDatetime(sdf.format(date));
		String total = "0";

		if (!CollectionUtils.isEmpty(resultInfo4Pos.getData())) {
			int index = 0;
			for (TipItem it : resultInfo4Pos.getData()) {
				it.setIndex(++index + "");
				total = stringAdd(total, it.getTipMoney());
			}
		}
		resultInfo4Pos.setTotal(total);
		
		PrintObj obj = new PrintObj();
		obj.setTip(resultInfo4Pos);
		
		// TODO
		Map<String, Object> params = new HashMap<>();
		params.put("printertype", "10");
		sendToPrint(params, obj);
	}
}
