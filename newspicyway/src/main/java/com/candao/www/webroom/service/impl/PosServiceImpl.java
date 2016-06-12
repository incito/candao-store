package com.candao.www.webroom.service.impl;

import com.candao.common.utils.AjaxResponse;
import com.candao.print.service.PrinterService;
import com.candao.www.constant.Constant;
import com.candao.www.webroom.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liaoy on 2016/6/8.
 */
@Service
public class PosServiceImpl implements PosService {
    @Autowired
    private PrinterService printerService;

    @Override
    public AjaxResponse getPrinterList() {
        Map<String, Object> param = new HashMap<>();
        List<Map<String, Object>> printers = printerService.findAll(param);
        AjaxResponse response = new AjaxResponse();
        List<Map<String, Object>> printerList = new ArrayList<>();
        if (null != printers) {
            for (Map<String, Object> printer :
                    printers) {
                Map<String, Object> map = new HashMap<>(4);
                map.put("ip", printer.get("ipaddress"));
                map.put("name", printer.get("printername"));
                Object workStatus = printer.get("workStatus");
                map.put("status", workStatus);
                map.put("statusTitle", getStatusTitle(workStatus));
                printerList.add(map);
            }
        }
        response.setData(printerList);
        return response;
    }

    private String getStatusTitle(Object status) {
        short workStatus = 0;
        try {
            workStatus = Short.parseShort(status.toString());
        } catch (Exception e) {
        }
        switch (workStatus) {
            case Constant.PRINTER_STATUS.GOOD:
                return "良好";
            case Constant.PRINTER_STATUS.BAD:
                return "很差";
            case Constant.PRINTER_STATUS.NOT_REACHABLE:
                return "无连接";
            default:
                return "未知";
        }
    }

}
