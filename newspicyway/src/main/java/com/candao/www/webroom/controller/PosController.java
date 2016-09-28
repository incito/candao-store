package com.candao.www.webroom.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.candao.common.utils.AjaxResponse;
import com.candao.www.data.model.TPrinterDevice;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.webroom.service.PosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @RequestMapping("/getlist")
    @ResponseBody
    public String getList(@RequestBody String json) {
        List<TPrinterDevice> devices = null;
        try {
            devices = posService.getPOSList(JSON.parseObject(json));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败！", e);
            return getResponseMsg(null, e.getMessage(), false);
        }
        Map res = new HashMap();
        res.put("list", devices);
        return getResponseMsg(res, "查询成功！", true);
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(@RequestBody String json) {
        TPrinterDevice tPrinterDevice = JSON.parseObject(json, TPrinterDevice.class);
        try {
            posService.savePOS(tPrinterDevice);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存POS机失败", e);
            return getResponseMsg(null, e.getMessage(), false);
        }
        return getResponseMsg(null, "保存成功", true);
    }

    @RequestMapping("/getposbyid")
    @ResponseBody
    public String getPosByID(@RequestBody String json) {
        try {
            if (StringUtil.isEmpty(json))
                return getResponseMsg(null, "参数不能为空！", false);
            JSONObject object = JSON.parseObject(json);
            if (!object.containsKey("posid"))
                return getResponseMsg(null, "参数格式错误！", false);
            object.put("devicecode",object.get("posid"));
            List<TPrinterDevice> res = posService.getPOSByParam(object);
            Map temp = new HashMap();
            temp.put("list", res);
            return getResponseMsg(temp, "查询成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败", e);
            return getResponseMsg(null, e.getMessage(), false);
        }
    }

    @RequestMapping("/deleteposbyid")
    @ResponseBody
    public String deletePOSByID(@RequestBody String json) {
        try {
            if (StringUtil.isEmpty(json))
                return getResponseMsg(null, "参数不能为空！", false);
            JSONObject object = JSON.parseObject(json);
            if (!object.containsKey("posid"))
                return getResponseMsg(null, "参数格式错误！", false);
            object.put("devicecode",object.get("posid"));
            posService.delPOS(object);
            return getResponseMsg(null, "删除成功！", true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除失败", e);
            return getResponseMsg(null, e.getMessage(), false);
        }
    }

    private String getResponseMsg(Object data, String msg, boolean sucess) {
        // Assert.notEmpty(new Object[] { data, msg, sucess });
        Map<String, Object> res = new HashMap<>();
        res.put("result", sucess ? 0 : 1);
        res.put("data", data);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }
}
