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
 * 店面桌子使用情况
 * 
 * Created by IntelliJ IDEA.
 * User: 张文栋
 * Date: 15/7/23
 * Desc:
 */
public class RunTableTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	@Qualifier("bOrderService")
    private OrderService orderService;

    public void cuttingpayment() throws IOException {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setBranchId(PropertiesUtil.getValue("branchId"));
        clientInfo.setCommand("order");
        clientInfo.setRequestType("syncTable");
        clientInfo.setData(orderService.getCurrDayTableData().toString());
        logger.info("店面桌子使用情况数据查询："+orderService.getCurrDayTableData().toString());
        JSONObject requestObject = JSONObject.fromObject(clientInfo);
        String link = PropertiesUtil.getValue("cloudLink");
        HttpUtils.getHttpPost(link, requestObject);
        logger.info(requestObject.toString());
    }
}
