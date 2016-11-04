package com.candao.www.bossstore.quartzJob;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.bossstore.protocol.ClientInfo;
import com.candao.www.bossstore.service.UserService;
import com.candao.www.bossstore.util.HttpUtils;

import net.sf.json.JSONObject;


/**
 * 
 * 获取值班经理
 * @author YANGZHONGLI
 *
 */
public class RunOnDutyManagerTask {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	@Qualifier("bUserService")
	private UserService userService;

	public void cuttingpayment() throws IOException {
		String temp = userService.getOnDutyManagerInfo().toString();
		System.out.println("******获取值班经理数据查询："+temp);
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setBranchId(PropertiesUtils.getValue("current_branch_id"));
		clientInfo.setCommand("login");
		clientInfo.setRequestType("syncOnDutyManager");
		clientInfo.setData(userService.getOnDutyManagerInfo().toString());
		logger.info("获取值班经理数据查询："+userService.getOnDutyManagerInfo().toString());
		JSONObject requestObject = JSONObject.fromObject(clientInfo);
        String link = PropertiesUtils.getValue("cloudLink");
        HttpUtils.getHttpPost(link, requestObject);
		logger.info(requestObject.toString());
	}

}