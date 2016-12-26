package com.candao.www.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.bossstore.util.HttpUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.www.printer.v2.PrinterManager;

import java.util.HashMap;

/**
 * 初始化Listener，需要放到Spring初始化listener之后
 * Created by liaoy on 2016/6/12.
 */
public class InitListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(InitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	try {
			// 初始化打印机
			PrinterManager.initialize(true);
			// 上传版本号
			uploadVersion();
		} catch (Exception e) {
			logger.error("InitListener error", e);
		}
    }
    private void uploadVersion(){
        logger.info("上传版本号");
        //传入参数
        HashMap<String, String> map = new HashMap<>();
        map.put("tenantid", PropertiesUtils.getValue("tenant_id"));//租户id
        map.put("branchid", PropertiesUtils.getValue("current_branch_id"));//门店id
        map.put("softtype", "05");//软件名称(01手环 02 标准pad,03 服务员pad 04pos 05 门店)
        map.put("version", PropertiesUtils.getValue("version"));//当前软件版本号
        //连接接口
        String json = JacksonJsonMapper.objectToJson(map);
        JSONObject jsonObject = JSONObject.fromObject(json);
        String link = PropertiesUtils.getValue("oms.host")+"softversionrest";
        HttpUtils.getHttpPost(link, jsonObject);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
