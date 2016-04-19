package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by lenovo on 2016/4/8.
 */
public interface TableMapper {
    int updaStatus0(@Param("tableNo") String tableNo);

    int updaStatus1(@Param("orderId") String orderId, @Param("tableId") String tableId);

    String getOrderIdByTableNo(String tableNo);

    String selectOrderIdOfStatusN5(@Param("tableNo") String tableNo);

    String selectOrderId(@Param("tableNo") String tableNo);

    String selectTableNo(@Param("tableId") String tableId);

    Integer selectStatus(@Param("tableId") String tableId);

    void updateTableByTableId(@Param("orderId") String orderId, @Param("tableId") String tableId);
}
