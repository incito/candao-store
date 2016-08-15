package com.candao.www.dataserver.controller;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.service.dish.DishService;
import com.candao.www.dataserver.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ytq on 2016/3/18.
 */
@Controller
@RequestMapping("/datasnap/rest/TServerMethods1")
public class DishInterfaceController {
    @Autowired
    private DishService dishService;

    @RequestMapping(value = "/getFoodStatus/{dishId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getFoodStatus(@PathVariable("dishId") String dishId, @RequestBody String dishUnit) {
        Map<String, String> jsonObject = JSON.parseObject(dishUnit, HashMap.class);
        dishUnit = jsonObject.get("dishUnit");
        String result = dishService.getFoodStatus(dishId, dishUnit);
        result = "{\"result\":[\"" + result + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/getAllWmFood/{userId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllWmFood(@PathVariable("userId") String userId) {
        String result = dishService.getAllWmFood(userId);
        result = "{\"result\":[\"" + result + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/getCJFood/{userId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getCJFood(@PathVariable("userId") String userId) {
        String result = dishService.getCJFood(userId);
        result = "{\"result\":[\"" + result + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/getGroupDetail/{dishId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getGroupDetail(@PathVariable("dishId") String dishId) {
        String result = dishService.getGroupDetail(dishId);
        result = "{\"result\":[\"" + result + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/getFavorale/{userId}/{orderId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getFavorable(@PathVariable("userId") String userId, @PathVariable("orderId") String orderId) {
        String result = dishService.getFavorable(userId, orderId);
        result = "{\"result\":[\"" + result + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/getBackDishInfo/{orderId}/{dishId}/{dishUnit}/{tableNo}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getBackDishInfo(@PathVariable("orderId") String orderId, @PathVariable("dishId") String dishId,
                                  @PathVariable("dishUnit") String dishUnit, @PathVariable("tableNo") String tableNo) {
        String result = dishService.getBackDishInfo(orderId, dishId, dishUnit, tableNo);
        result = "{\"result\":[" + result + "]}";
        //todo 转unicode会对"进行转移成\"
        //return StringUtil.string2Unicode(result);
        return result;
    }

    @RequestMapping(value = "/deletePosOperation/{tableNo}", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String deletePosOperation(@PathVariable("tableNo") String tableNo) {
        String result = dishService.deletePosOperation(tableNo);
        result = "{\"result\":[\"" + result + "\"]}";
        return StringUtil.string2Unicode(result);
    }
}
