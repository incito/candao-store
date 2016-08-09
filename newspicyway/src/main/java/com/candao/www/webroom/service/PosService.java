package com.candao.www.webroom.service;

import com.candao.common.utils.AjaxResponse;

/**
 * POS相关业务
 * Created by liaoy on 2016/6/8.
 */
public interface PosService {
    /**
     * 获取打印机列表及状态
     * @return
     */
    AjaxResponse getPrinterList();
}
