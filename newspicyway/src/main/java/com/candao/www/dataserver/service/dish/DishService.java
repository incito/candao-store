package com.candao.www.dataserver.service.dish;

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

    //获取退菜dish列表,如果选择的是套餐明细，不能退，如果选择的是鱼锅，退整个锅，如果选择的是锅内明细，退鱼，不能只退锅
    String getBackDishInfo(String orderId, String dishId, String dishUnit, String tableNo);

    //更新餐具信息
    String updateCj(String orderId, String userId);

    //删除pos的操作
    String deletePosOperation(String tableNo);
}
