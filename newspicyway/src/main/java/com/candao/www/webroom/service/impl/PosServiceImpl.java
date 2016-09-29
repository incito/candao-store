package com.candao.www.webroom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.candao.common.utils.AjaxResponse;
import com.candao.print.service.PrinterService;
import com.candao.www.data.model.TPrinterDevice;
import com.candao.www.data.model.TPrinterDeviceExample;
import com.candao.www.data.model.TPrinterDeviceprinter;
import com.candao.www.data.model.TPrinterDeviceprinterExample;
import com.candao.www.data.pos.TPrinterDeviceMapper;
import com.candao.www.data.pos.TPrinterDeviceprinterMapper;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.printer.v2.PrinterStatusManager;
import com.candao.www.webroom.service.PosService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by liaoy on 2016/6/8.
 */
@Service
public class PosServiceImpl implements PosService {
    @Autowired
    private PrinterService printerService;
    @Autowired
    private TPrinterDeviceMapper tPrinterDeviceMapper;
    @Autowired
    private TPrinterDeviceprinterMapper tPrinterDeviceprinterMapper;

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
                try {
                    status = Short.parseShort(workStatus.toString());
                } catch (Exception e) {
                    status = PrinterStatusManager.DISCONNECT;
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

    @Override
    public List<TPrinterDevice> getPOSList(Map param) {
        TPrinterDeviceExample example = new TPrinterDeviceExample();
        example.or().andDevicestatusEqualTo(0).andDevicetypeEqualTo(2);
        return tPrinterDeviceMapper.selectByExample(example);
    }

    @Override
    public void savePOS(TPrinterDevice tPrinterDevice) {
        if (tPrinterDevice == null)
            throw new RuntimeException("POS不能为空！");
        TPrinterDeviceExample example = new TPrinterDeviceExample();
        example.or().andDevicecodeEqualTo(tPrinterDevice.getDevicecode());
        List<TPrinterDevice> temp = tPrinterDeviceMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(temp)) {
            //新增
            tPrinterDevice.setDeviceid(UUID.randomUUID().toString());
            tPrinterDevice.setDevicestatus(0);
            tPrinterDevice.setDevicetype(2);
            tPrinterDeviceMapper.insertSelective(tPrinterDevice);
            //删除中间表
            deletePOSPrinterByCode(tPrinterDevice.getDevicecode());
            //新增中间表
            savePOSPrinter(tPrinterDevice.getPrinters());
        } else if (temp.size() == 1) {
            //修改
            tPrinterDevice.setDeviceid(temp.get(0).getDeviceid());
            tPrinterDeviceMapper.updateByPrimaryKey(tPrinterDevice);
            //删除中间表
            deletePOSPrinterByCode(tPrinterDevice.getDevicecode());
            //新增中间表
            savePOSPrinter(tPrinterDevice.getPrinters());
        } else {
            throw new RuntimeException("数据库中POS code有重复数据！");
        }
    }

    @Override
    public List<TPrinterDevice> getPOSByParam(Map param) {
        Assert.notEmpty(param,"参数不能为空！");
        //0 启用
        param.put("devicestatus",0);
        //2 POS
        param.put("devicetype",2);
        TPrinterDeviceExample example = new TPrinterDeviceExample();
        example.or().andDevice(param);
        List<TPrinterDevice> tPrinterDevices = tPrinterDeviceMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(tPrinterDevices)){
            for (TPrinterDevice it : tPrinterDevices) {
                TPrinterDeviceprinterExample example1 = new TPrinterDeviceprinterExample();
                example.or().andDevicecodeEqualTo(it.getDevicecode());
                it.setPrinters(tPrinterDeviceprinterMapper.selectByExample(example1));
            }
        }
        return tPrinterDevices;
    }

    @Override
    public void delPOS(Map param) {
        Assert.notEmpty(param,"参数不能为空！");
        TPrinterDeviceExample example = new TPrinterDeviceExample();
        example.or().andDevice(param);
        List<TPrinterDevice> tPrinterDevices = tPrinterDeviceMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(tPrinterDevices) || tPrinterDevices.size() != 1){
            throw new RuntimeException("打印机不存在或者重复");
        }
        example.clear();
        example.or().andDevice(param);
        TPrinterDevice temp = new TPrinterDevice();
        //1 删除
        temp.setDevicestatus(1);
        tPrinterDeviceMapper.updateByExampleSelective(temp,example);
        deletePOSPrinterByCode(tPrinterDevices.get(0).getDevicecode().toString());
    }

    @Override
    public void delPOSPrinter(String printerid) {
        if (StringUtil.isEmpty(printerid))
            return;
        TPrinterDeviceprinterExample example = new TPrinterDeviceprinterExample();
        example.or().andPrinteridEqualTo(printerid);
        tPrinterDeviceprinterMapper.deleteByExample(example);
    }

    @Override
    public void validateByCode(Map param) {
        Assert.notEmpty(param);

        TPrinterDeviceExample example = new TPrinterDeviceExample();
        example.or().andDevice(param);
        List<TPrinterDevice> list = tPrinterDeviceMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list))
            throw new RuntimeException("设备码重复");
    }


    private int deletePOSPrinterByCode(String code) {
        int count = 0;
        if (!StringUtils.isEmpty(code)) {
            TPrinterDeviceprinterExample examp = new TPrinterDeviceprinterExample();
            //删除中间表
            examp.or().andDevicecodeEqualTo(code);
            count += tPrinterDeviceprinterMapper.deleteByExample(examp);
        }
        return count;
    }

    private int savePOSPrinter(List<TPrinterDeviceprinter> param) {
        int count = 0;
        if (!CollectionUtils.isEmpty(param)) {
            for (TPrinterDeviceprinter it : param) {
                it.setId(UUID.randomUUID().toString());
                count += tPrinterDeviceprinterMapper.insert(it);
            }
        }
        return count;
    }
}
