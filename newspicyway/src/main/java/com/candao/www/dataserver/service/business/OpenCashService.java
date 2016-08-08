package com.candao.www.dataserver.service.business;

import com.candao.www.dataserver.service.log.BaseLogService;

/**
 * Created by ytq on 2016/3/15.
 */
public interface OpenCashService extends BaseLogService {
    //开打印机钱箱
    String openCash(String ip);

    //获取清机单内容
    String getClearMachineData(String aUserId, String jsOrder, String posId);
}
