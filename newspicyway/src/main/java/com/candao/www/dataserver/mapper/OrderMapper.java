package com.candao.www.dataserver.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/3/28.
 */
public interface OrderMapper {
    Integer selectOrderStatus(@Param("orderId") String orderId);

    int updateMemberNo(@Param("memberNo") String memberNo, @Param("orderId") String orderId);

    List<Map<String, Object>> selectByOrderId(@Param("orderId") String orderId);
    List<Map<String, Object>> selectPrice(@Param("orderId") String orderId);

    String selectVipPrice(@Param("dishId") String dishId, @Param("dishUnit") String dishUnit);
    String selectNormalPrice(@Param("dishId") String dishId, @Param("dishUnit") String dishUnit);
    String selectMemberNoByOrderId(@Param("orderId") String orderId);
}
