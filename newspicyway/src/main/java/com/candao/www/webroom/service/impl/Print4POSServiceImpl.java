package com.candao.www.webroom.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.candao.common.utils.Constant;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.OrderInfo4Pos;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.SettlementInfo4Pos;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.Print4POSService;

@Service
public class Print4POSServiceImpl implements Print4POSService {

	public static final String SETTLEMENT = "2";

	public static final String PRESETTLEMENT = "1";

	public static final String CUSTTEMPLATE = "3";

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
		List<Map<String, Object>> res = tbPrinterManagerDao.find(params);
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

}
