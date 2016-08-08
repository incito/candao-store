package com.candao.www.webroom.service.impl;

import com.candao.common.utils.AjaxResponse;
import com.candao.print.service.PrinterService;
import com.candao.www.constant.Constant;
import com.candao.www.printer.v2.PrinterStatusManager;
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
        List<Map<String, Object>> printers = printerService.queryPrinterWorkStatus();
        AjaxResponse response = new AjaxResponse();
        List<Map<String, Object>> printerList = new ArrayList<>();
        if (null != printers) {
            for (Map<String, Object> printer :
                    printers) {
                Map<String, Object> map = new HashMap<>(4);
                map.put("ip", printer.get("ip"));
                map.put("name", printer.get("printername"));
                Object workStatus = printer.get("workstatus");
                short status;
                try{
                    status=Short.parseShort(workStatus.toString());
                }catch (Exception e){
                    status=PrinterStatusManager.DISCONNECT;
                }
                map.put("status", workStatus);
                map.put("statusTitle", PrinterStatusManager.convertState(status));
                printerList.add(map);
            }
        }
        response.setIsSuccess(true);
        response.setData(printerList);
        return response;
    }
}
