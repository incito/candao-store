package com.candao.www.timedtask;

import java.util.HashMap;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.bossstore.protocol.ClientInfo;
import com.candao.www.bossstore.service.OrderService;
import com.candao.www.bossstore.util.HttpUtils;
import com.candao.www.data.dao.TdishDao;

/**
 * 启动version定时任务 
 * Created by QZM.
 * Date: 2016.11.3
 */

@Component
public class VersionTask{
	@Autowired
	private VersionTaskService versionTaskService;
	    
	@Scheduled(cron="0 0/5 * * * ?") 
	public void clientInfoTimer() {
	      System.out.println("---------启动VersionTask------------");
	  //传入参数
	  HashMap<String, String> map = new HashMap<>();
		  map.put("tenantid", PropertiesUtils.getValue("tenant_id"));//租户id
	      map.put("branchid", PropertiesUtils.getValue("current_branch_id"));//门店id
	      map.put("softtype", "05");//软件名称(01手环 02 标准pad,03 服务员pad 04pos 05 门店)
		  map.put("version", versionTaskService.getVersion());//当前软件版本号
	  //连接接口	 
	  String json = JacksonJsonMapper.objectToJson(map);
	  JSONObject jsonObject = JSONObject.fromObject(json);
	  String link = PropertiesUtils.getValue("oms.host")+"softversionrest";
	  HttpUtils.getHttpPost(link, jsonObject);
	}
	
}
