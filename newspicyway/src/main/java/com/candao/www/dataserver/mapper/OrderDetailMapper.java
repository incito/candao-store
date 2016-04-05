package com.candao.www.dataserver.mapper;


import org.apache.ibatis.annotations.Param;

/**
 * Created by lenovo on 2016/3/28.
 */
public interface OrderDetailMapper {

    int updateOrderPrice(@Param("price") String price, @Param("orderId") String orderId, @Param("orderDetailId") String orderDetailId);
}
