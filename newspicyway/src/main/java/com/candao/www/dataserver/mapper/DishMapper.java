package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/18.
 */
public interface DishMapper {
    Integer getFoodStatus(@Param("dishId") String dishId, @Param("dishUnit") String dishUnit);

    void updateDishPY();

    List<Map> getAllWmFood();

    List<Map> getCJFood();

    List<Map> getGroupDetail(String dishId);

    //鱼锅菜品
    Double getDjDishNum(String orderId);

    Double getScDishNum(String orderId);

    Map getYGListJson(String orderId);

    Map getYGDoubleJson(String orderId);

    List<Map<String, Object>> getBackDishInfo(@Param("orderId") String orderId, @Param("dishId") String dishId, @Param("dishUnit") String dishUnit);

    String getPriceByDishId(String dishId);

    String getUnitByDishId(String dishId);
}
