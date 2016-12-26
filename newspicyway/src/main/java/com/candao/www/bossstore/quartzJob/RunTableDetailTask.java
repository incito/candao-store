package com.candao.www.bossstore.quartzJob;

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
 * 正在使用的桌子的情况
 * Created by IntelliJ IDEA.
 * User: cheung
 * Date: 15/7/23
 * Desc:
 */
public class RunTableDetailTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	@Qualifier("bOrderService")
    private OrderService orderService;

    public void cuttingpayment() {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setBranchId(PropertiesUtils.getValue("current_branch_id"));
        clientInfo.setCommand("order");
        clientInfo.setRequestType("syncTableDetail");
        String temp = orderService.getCurrDayTableDetail().toString();
        clientInfo.setData(temp);
        logger.info("正在使用的桌子的情况数据查询："+temp);
        JSONObject requestObject = JSONObject.fromObject(clientInfo);
        String link = PropertiesUtils.getValue("cloudLink");
        HttpUtils.getHttpPost(link, requestObject);
        logger.info(requestObject.toString());
    }
}
