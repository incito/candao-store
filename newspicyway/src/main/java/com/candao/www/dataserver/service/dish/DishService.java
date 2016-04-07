package com.candao.www.dataserver.service.dish;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ytq on 2016/3/18.
 */
public interface DishService {
    //获取菜品状态
    String getFoodStatus(String dishId, String dishUnit);

    //获取全部菜品（点菜和外卖展示选择）
    String getAllWmFood(String userId);

    //获取餐具菜品信息
    String getCJFood(String userId);

    //获取套餐明细菜品
    String getGroupDetail(String dishId);

    //优惠自动使用接口(惠新，蓝港，辣倒兔临时方案)
    String getFavorable(String userId, String orderId);
}
