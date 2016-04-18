package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.business.OpenCashService;
import com.candao.www.dataserver.util.StringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ytq on 2016/3/14.
 */
@Controller
@RequestMapping("/datasnap/rest/TServerMethods1")
public class StoreInterfaceController {
    @Autowired
    private OpenCashService openCashService;

    @RequestMapping(value = "/OpenCash/{ip}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String openCash(@PathVariable("ip") String ipAddress) {
        String result = openCashService.openCash(ipAddress);
        result = "{\"result\":[\"" + result + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = {"/getClearMachineData/{aUserid}/{jsorder}/{posid}/", "/getClearMachineData/{aUserid}/{posid}/"}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getClearMachineData(@PathVariable("aUserid") String aUserId, @PathVariable("jsorder") String jsOrder, @PathVariable("posid") String posId) {
        String result = openCashService.getClearMachineData(aUserId, jsOrder, posId);
        result = "{\"result\":[\"" + result + "\"]}";
        return StringUtil.string2Unicode(result);
    }
}
