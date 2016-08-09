package com.candao.www.webroom.controller;

import com.candao.www.webroom.service.impl.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    return syncservice.syncDataFromCloud(type);
  }

}
