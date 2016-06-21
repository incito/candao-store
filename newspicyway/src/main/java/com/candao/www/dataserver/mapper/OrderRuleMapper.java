package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.OrderRule;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lenovo on 2016/4/5.
 */
public interface OrderRuleMapper {
    int deleteByOrderId(@Param("orderId") String orderId);
    int insert(OrderRule orderRule);
}
