package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.SettlmentDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * Created by lenovo on 2016/3/23.
 */
public interface SettlementDetailMapper {
    int insert(SettlmentDetail detail);

    int update(@Param("dishId") String dishId, @Param("orderId") String orderId);

    int setClear(@Param("openDate") Date openDate, @Param("userId") String userId);

    int insertAfterInputCash(Map<String, Object> param);

    int insertWhenSdetailidExists(Map<String, Object> param);

    int isSdetailidExists(@Param("sdetailid") String sdetailid);
}
