package com.candao.www.dataserver.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/3/28.
 */
public interface OrderDetailMapper {

    int updateOrderPrice(@Param("price") String price, @Param("orderId") String orderId, @Param("orderDetailId") String orderDetailId);

    List<Map<String, Object>> selectStatByOrderId(@Param("orderId") String orderId);
}
