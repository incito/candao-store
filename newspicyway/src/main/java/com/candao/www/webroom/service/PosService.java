package com.candao.www.webroom.service;

import com.alibaba.fastjson.JSONObject;
import com.candao.common.utils.AjaxResponse;
import com.candao.www.data.model.TPrinterDevice;

import java.util.List;
import java.util.Map;

/**
 * POS相关业务
 * Created by liaoy on 2016/6/8.
 */
public interface PosService {
    /**
     * 获取打印机列表及状态
     *
     * @return
     */
    AjaxResponse getPrinterList();

    List<TPrinterDevice> getPOSList(Map param);

    void savePOS(TPrinterDevice tPrinterDevice);

    List<TPrinterDevice> getPOSByParam(Map param);

    /**
     * 删除POS
     *
     * @param param
     */
    void delPOS(Map param);

    /**
     * 删除POS、打印机中间表
     *
     * @param printerid
     */
    void delPOSPrinter(String printerid);

    void validateByCode(Map object, boolean isUpdate);
}
