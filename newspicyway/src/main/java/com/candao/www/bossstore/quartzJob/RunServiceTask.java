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
 * 服务情况统计
 * 
 * Created by IntelliJ IDEA.
 * User: 杨仲立
 * Date: 15/11/03
 * Desc:
 */
public class RunServiceTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	@Qualifier("bOrderService")
    private OrderService orderService;

    public void cuttingpayment() throws IOException {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setBranchId(PropertiesUtils.getValue("current_branch_id"));
        clientInfo.setCommand("order");
        clientInfo.setRequestType("syncService");
        clientInfo.setData(orderService.getCurrServiceData().toString());
        logger.info("服务情况统计数据查询："+orderService.getCurrServiceData().toString());
        JSONObject requestObject = JSONObject.fromObject(clientInfo);
        String link = PropertiesUtils.getValue("cloudLink");
        HttpUtils.getHttpPost(link, requestObject);
        logger.info(requestObject.toString());
    }
}
