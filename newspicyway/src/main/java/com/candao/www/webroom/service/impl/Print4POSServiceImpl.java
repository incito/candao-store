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
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
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
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Tinvoice;
import com.candao.www.spring.SpringContext;
import com.candao.www.utils.CloneUtil;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.InvoiceService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.Print4POSService;
import com.candao.www.webroom.service.TableService;

@Service
public class Print4POSServiceImpl implements Print4POSService {

	public static final String SETTLEMENT = "2";

	public static final String PRESETTLEMENT = "1";

	public static final String CUSTTEMPLATE = "3";

	public static final String MEMBERSALEINFO_CUST = "客户联";

	public static final String MEMBERSALEINFO_STORE = "商户联";

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(5000));

	// private Print4POSProcedure print4POSProcedure;
	@Autowired
	TbPrinterManagerDao tbPrinterManagerDao;
	@Autowired
	private TbBranchDao tbBranchDao;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private OrderDetailServiceImpl orderDetail;
	@Autowired
	private InvoiceService invoiceinfo;
	@Autowired
	private OrderService orderService;
	@Autowired
	private TableService tableService;

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
		obj.setListenerType(Constant.ListenerType.ClearMachineDataTemplate);

		// TODO
		Map<String, Object> params = new HashMap<>();
		params.put("printertype", "10");
		sendToPrint(params, obj);
	}

	@Override
	public void printMemberSaleInfo(List<SettlementInfo4Pos> settlementInfos) throws Exception {
		if (CollectionUtils.isEmpty(settlementInfos)) {
			return;
		}
		String[] types = { MEMBERSALEINFO_CUST, MEMBERSALEINFO_STORE };
		for (int i = 0; i < types.length; i++) {
			PrintObj obj = new PrintObj();
			obj.setListenerType(Constant.ListenerType.MemberSaleInfoTemplate);
			SettlementInfo4Pos settlementInfo4Pos = settlementInfos.get(0);
			OrderInfo4Pos temp = (OrderInfo4Pos) settlementInfo4Pos.getOrderJson().get(0).clone();
			temp.setType(types[i]);
			settlementInfo4Pos.getOrderJson().set(0, temp);
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
			Print4POSProcedure print4POSProcedure = (Print4POSProcedure) SpringContext
					.getBean(Print4POSProcedure.class);
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
		obj.setListenerType(Constant.ListenerType.ItemSellDetailTemplate);

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
		obj.setListenerType(Constant.ListenerType.TipListTemplate);

		// TODO
		Map<String, Object> params = new HashMap<>();
		params.put("printertype", "10");
		sendToPrint(params, obj);
	}

	@Override
	public void printInvoice(Map<String, Object> map) throws Exception {
		Assert.notEmpty(map);
		if (!map.containsKey("orderid")) {
			throw new Exception("参数错误");
		}
		List<Tinvoice> inList = invoiceinfo.findInvoiceByOrderid(map);
		Assert.notEmpty(inList);
		Map<String, Object> order = orderService.findOrderById(String.valueOf(map.get("orderid")));
		Assert.notEmpty(order, "找不到该订单");
		TbTable table = tableService.findById(String.valueOf(order.get("currenttableid")));

		Map<String, Object> posData = new HashMap<>();
		posData.put("tableno", table.getTableNo());
		posData.put("orderid", order.get("orderid"));
		posData.put("title", inList.get(0).getInvoice_title());
		posData.put("amount", map.get("amount"));

		PrintObj obj = new PrintObj();
		obj.setPosData(posData);
		obj.setListenerType(Constant.ListenerType.InvoiceTemplate);

		// TODO
		Map<String, Object> params = new HashMap<>();
		params.put("printertype", "10");
		sendToPrint(params, obj);
	}

	@Override
	public void printStoredCard(Map<String, Object> map) throws Exception {
		Assert.notEmpty(map);

		String[] types = { MEMBERSALEINFO_CUST, MEMBERSALEINFO_STORE };
		for (int i = 0; i < types.length; i++) {
			PrintObj obj = new PrintObj();
			obj.setListenerType(Constant.ListenerType.StoreCardToNewPosTemplate);
			@SuppressWarnings("unchecked")
			Map<String, Object> temp = (Map<String, Object>) CloneUtil.clone(map, -1);
			temp.put("type", types[i]);
			obj.setPosData(temp);
			Map<String, Object> params = new HashMap<>();
			params.put("printertype", "10");
			sendToPrint(params, obj);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void printBusinessDetail(String... params) throws Exception {
		Assert.noNullElements(params, "参数错误！");
		Assert.state(params.length == 5, "前面接口错误");

		Map<String, Object> posData = new HashMap<>();
		
		List<Map<String, Object>> itemList = JSON.parseObject(params[0], List.class);
		
		List<Map<String, Object>> preferList = JSON.parseObject(params[1], List.class);
		
		
		
		
//		for (int i = 0; i < params.length; i++) {
//			Map<String, Object> temp = JSON.parseObject(params[i], Map.class);
//			posData.putAll(temp);
//		}
		
		Assert.notEmpty(posData);
		
		PrintObj obj = new PrintObj();
		obj.setPosData(posData);
		obj.setListenerType(Constant.ListenerType.BillDetailTemplate);

		// TODO
		Map<String, Object> param = new HashMap<>();
		param.put("printertype", "10");
		sendToPrint(param, obj);
		
	}
}
