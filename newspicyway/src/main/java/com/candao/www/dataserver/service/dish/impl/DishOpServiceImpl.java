package com.candao.www.dataserver.service.dish.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.mapper.DishMapper;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.model.ResponseJsonData;
import com.candao.www.dataserver.service.dish.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/18.
 */
@Service
public class DishOpServiceImpl implements DishService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DishOpServiceImpl.class);
    @Autowired
    private DishMapper dishMapper;

    @Override
    public String getFoodStatus(String dishId, String dishUnit) {
        ResponseData responseData = new ResponseData();
        try {
            Integer status = dishMapper.getFoodStatus(dishId, dishUnit);
            if (status == null) {
                responseData.setInfo("0");
            } else {
                responseData.setInfo(status + "");
            }
        } catch (Exception e) {
            responseData.setData("0");
            responseData.setInfo("获取菜品状态异常");
            LOGGER.error("###getFoodStatus dishId={},dishUnit={},error={}###", dishId, dishUnit, e);
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    public String getAllWmFood(String userId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            dishMapper.updateDishPY();
            List<Map> mapList = dishMapper.getAllWmFood();
            responseJsonData.setOrderJson(mapList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("获取全部菜品异常");
            LOGGER.error("###getAllWmFood userId={},error={}###", userId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String getCJFood(String userId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            List<Map> mapList = dishMapper.getCJFood();
            responseJsonData.setOrderJson(mapList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("获取全部菜品异常");
            LOGGER.error("###getCJFood userId={},error={}###", userId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String getGroupDetail(String dishId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            List<Map> mapList = dishMapper.getGroupDetail(dishId);
            responseJsonData.setOrderJson(mapList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("获取套餐明细异常");
            LOGGER.error("###getGroupDetail dishId={},error={}###", dishId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String getFavorable(String userId, String orderId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        Long ymNum;
        Map<Object, Object> orderJson = new HashMap<>();
        Map listJson;
        Map doubleJson;
        try {
            LOGGER.info("###getFavorable userId={},orderId={}###", userId, orderId);
            Double djDishNum = dishMapper.getDjDishNum(orderId);
            if (djDishNum > 0) {
                Double scDishNum = dishMapper.getScDishNum(orderId);
                if (scDishNum > 0) {
                    if (djDishNum >= (scDishNum * 2)) {
                        ymNum = Math.round(djDishNum * 2);
                    } else {
                        ymNum = Math.round(djDishNum);
                    }
                    orderJson.put("couponname", "打底套餐自选优免");
                    orderJson.put("orderprice", 6);
                    orderJson.put("decdishnum", ymNum + "");
                    orderJson.put("decorderprice", 6);
                    orderJson.put("decamount", ymNum * 6 + "");
                }
            }
            listJson = dishMapper.getYGListJson(orderId);
            doubleJson = dishMapper.getYGDoubleJson(orderId);
            responseJsonData.setOrderJson(orderJson);
            responseJsonData.setListJson(listJson);
            responseJsonData.setDoubleJson(doubleJson);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("优惠自动使用接口(惠新，蓝港，辣倒兔临时方案)异常");
            LOGGER.error("###getFavorable  userId={},orderId={},error={}###", userId, orderId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }
}
