package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.dish.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ytq on 2016/3/18.
 */
@Controller
@RequestMapping("/datasnap/rest/TServerMethods1")
public class DishInterfaceController {
    @Autowired
    private DishService dishService;

    @RequestMapping(value = "/getFoodStatus/{dishId}/{dishUnit}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getFoodStatus(@PathVariable("dishId") String dishId, @PathVariable("dishUnit") String dishUnit) {
        return dishService.getFoodStatus(dishId, dishUnit);
    }

    @RequestMapping(value = "/getAllWmFood/{userId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllWmFood(@PathVariable("userId") String userId) {
        return dishService.getAllWmFood(userId);
    }

    @RequestMapping(value = "/getCJFood/{userId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getCJFood(@PathVariable("userId") String userId) {
        return dishService.getCJFood(userId);
    }

    @RequestMapping(value = "/getGroupDetail/{dishId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getGroupDetail(@PathVariable("dishId") String dishId) {
        return dishService.getGroupDetail(dishId);
    }

    @RequestMapping(value = "/getFavorale/{userId}/{orderId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getFavorable(@PathVariable("userId") String userId, @PathVariable("orderId") String orderId) {
        return dishService.getFavorable(userId, orderId);
    }
}
