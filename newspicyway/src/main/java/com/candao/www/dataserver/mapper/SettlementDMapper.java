package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.SettlmentDetail;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lenovo on 2016/3/23.
 */
public interface SettlementDMapper {
    int insert(SettlmentDetail detail);

    int update(@Param("dishId") String dishId, @Param("orderId") String orderId);
}
