package com.candao.www.webroom.controller;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.AjaxResponse;
import com.candao.www.webroom.service.PosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * POS业务接口
 */
@Controller
@RequestMapping("/pos")
public class PosController {
    private static Logger logger = LoggerFactory.getLogger(PosController.class);
    @Autowired
    private PosService posService;

    /**
     * 重启dataserver
     *
     * @return
     */
    @RequestMapping(value = "/printerlist.json", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String printerlist() {
        AjaxResponse printerList;
        try {
            printerList = posService.getPrinterList();
        } catch (Exception e) {
            logger.error("printerlist.json出现异常", e);
            printerList = new AjaxResponse();
            printerList.setErrorMsg("后台服务出现异常");
        }
        return JSON.toJSONString(printerList);
    }
}
