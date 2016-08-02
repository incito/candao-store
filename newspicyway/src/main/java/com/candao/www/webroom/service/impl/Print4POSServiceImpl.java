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
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.SettlementInfo4Pos;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.webroom.service.Print4POSService;

@Service
public class Print4POSServiceImpl implements Print4POSService {

	public static final String SETTLEMENT = "2";

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(5000));
	@Autowired
	private Print4POSProcedure print4POSProcedure;

	@Autowired
	TbPrinterManagerDao tbPrinterManagerDao;

	@Autowired
	private TbBranchDao tbBranchDao;

	@Override
	public void print(List<SettlementInfo4Pos> settlementInfos, String printType) throws Exception {
		if (CollectionUtils.isEmpty(settlementInfos) || StringUtils.isEmpty(printType)) {
			return;
		}
		PrintObj obj = new PrintObj();
		Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
		SettlementInfo4Pos settlementInfo4Pos = settlementInfos.get(0);
		settlementInfo4Pos.setBranchName(String.valueOf(branchInfo.get("branchname")));
		settlementInfo4Pos.setTel(String.valueOf(branchInfo.get("managertel")));
		settlementInfo4Pos.setAddress(String.valueOf(branchInfo.get("branchaddress")));
		obj.setSettlementInfo4Pos(settlementInfo4Pos);
		switch (printType) {
		case SETTLEMENT:
			obj.setListenerType(Constant.ListenerType.SettlementDishListener);
			break;

		default:
			break;
		}
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
