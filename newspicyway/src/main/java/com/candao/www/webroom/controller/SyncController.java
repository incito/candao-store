package com.candao.www.webroom.controller;

import com.candao.common.utils.HttpUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.service.impl.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店与总店 数据同步功能
 *
 * @author zgt
 */
@Controller
@RequestMapping("/sync")
public class SyncController {
  private Logger logger = LoggerFactory.getLogger(SyncController.class);
  @Autowired
  private SyncService syncservice;

  /**
   * @return String 同步结果
   * @Description:分店拉取总店数据
   * @Modification:
   */
  @RequestMapping(value = "/synDataFromCloud")
  @ResponseBody
  public Map<String, Object> synDataFromCloud(String type) {
    Map<String, Object> resp = new HashMap<>();
    String url = "http://" + PropertiesUtils.getValue("cloud.host") + "/" + PropertiesUtils.getValue("cloud.webroot") + "/sync/dish";
    String queryString = "tenantId=" + PropertiesUtils.getValue("tenant_id") + "&branchId=" + PropertiesUtils.getValue("current_branch_id") + "&type=" + type;
    String result = HttpUtils.doGet(url, queryString);
    Map<String, Object> map = null;

    if ("Read timed out".equalsIgnoreCase(result)) {
      logger.error("从总店同步数据超时：" + url + "?" + queryString);
      resp.put("statusCode", "500");
      resp.put("data", "从总店获取数据超时");
      return resp;
    }

    try {
      map = JacksonJsonMapper.jsonToObject(result, Map.class);
    } catch (Exception e) {
      logger.error("从总店同步数据失败\n" + result);
      resp.put("statusCode", "500");
      resp.put("data", "从总店获取数据失败");
      return resp;
    }

    try {
      syncservice.saveDish((String) map.get("sql"), (List) map.get("tableNames"));
      resp.put("statusCode", "200");
    } catch (Exception e) {
      logger.error("从总店同步数据异常", e);
      resp.put("statusCode", "500");
      resp.put("data", "保存数据失败");
    }
    return resp;
  }

}
