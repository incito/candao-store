package com.candao.www.bossstore.quartzJob;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.candao.www.bossstore.protocol.ClientInfo;
import com.candao.www.bossstore.service.OrderService;
import com.candao.www.bossstore.util.HttpUtils;
import com.candao.www.bossstore.util.PropertiesUtil;

import net.sf.json.JSONObject;


/**
 * 当日流水
 * 
 * @author YANGZHONGLI
 *
 */
public class RunFlowDataTask {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	@Qualifier("bOrderService")
	private OrderService orderService;

	public void cuttingpayment() throws IOException {
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setBranchId(PropertiesUtil.getValue("branchId"));
		clientInfo.setCommand("order");
		clientInfo.setRequestType("syncCurrFlowData");
		clientInfo.setData(orderService.getCurrDayFlowData().toString());
		logger.info("当日流水数据查询："+orderService.getCurrDayFlowData().toString());
        JSONObject requestObject = JSONObject.fromObject(clientInfo);
        String link = PropertiesUtil.getValue("cloudLink");
	 	HttpUtils.getHttpPost(link, requestObject);
		logger.info(requestObject.toString());
	}

}