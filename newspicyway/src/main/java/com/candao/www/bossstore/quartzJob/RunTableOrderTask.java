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
 * 统计当天所有的桌子的订单信息
 * 
 * Created by IntelliJ IDEA.
 * User: 杨仲立
 * Date: 15/11/03
 * Desc:
 */
public class RunTableOrderTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	@Qualifier("bOrderService")
    private OrderService orderService;

    public void cuttingpayment() throws IOException {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setBranchId(PropertiesUtil.getValue("branchId"));
        clientInfo.setCommand("order");
        clientInfo.setRequestType("syncTableOrder");
        clientInfo.setData(orderService.getTableOrderData().toString());
        logger.info("统计当天所有的桌子的订单信息数据查询："+orderService.getTableOrderData().toString());
        JSONObject requestObject = JSONObject.fromObject(clientInfo);
        String link = PropertiesUtil.getValue("cloudLink");
        HttpUtils.getHttpPost(link, requestObject);
        logger.info(requestObject.toString());
        System.out.println(">>>>>>>>>"+requestObject.toString());
    }
}
