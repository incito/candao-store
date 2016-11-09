package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

/**
 * 自定义支付方式
 */
public interface PaywayService {
    List<Map> getPayways();

    int savePayways(List<Map<String, Object>> payways);
}
