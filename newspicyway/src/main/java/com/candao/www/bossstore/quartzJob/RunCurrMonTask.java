package com.candao.www.bossstore.quartzJob;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.bossstore.protocol.ClientInfo;
import com.candao.www.bossstore.service.OrderService;
import com.candao.www.bossstore.util.HttpUtils;

import net.sf.json.JSONObject;


/**
 * 
 * 本月统计
 * @author YANGZHONGLI
 *
 */
public class RunCurrMonTask {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	@Qualifier("bOrderService")
	private OrderService orderService;

	public void cuttingpayment() {
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setBranchId(PropertiesUtils.getValue("current_branch_id"));
		clientInfo.setCommand("order");
		clientInfo.setRequestType("syncCurrMon");
		String temp = orderService.getBusinessDataByMon().toString();
		clientInfo.setData(temp);
		logger.info("本月数据查询："+temp);
		JSONObject requestObject = JSONObject.fromObject(clientInfo);
        String link = PropertiesUtils.getValue("cloudLink");
	 	HttpUtils.getHttpPost(link, requestObject);
		logger.info(requestObject.toString());
	}

}