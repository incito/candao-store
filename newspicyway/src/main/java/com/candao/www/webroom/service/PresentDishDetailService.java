package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

/**
 * 退菜明细表
 *
 * @author Administrator
 */
public interface PresentDishDetailService {
    List<Map<String, Object>> presentDishList(Map<String, Object> params);
}
