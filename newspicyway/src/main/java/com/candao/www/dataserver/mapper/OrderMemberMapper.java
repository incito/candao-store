package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.OrderMember;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lenovo on 2016/3/24.
 */
public interface OrderMemberMapper {
    int deleteByOrderId(@Param("orderId") String orderId);

    int insert(OrderMember orderMember);

    OrderMember selectByOrderId(@Param("orderId") String orderId);

    int updateValid(OrderMember orderMember);

    String selectValid(@Param("orderId") String orderId);
}
