package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.WaiterShiftDao;
import com.candao.www.webroom.service.WaiterShiftService;

/**
 * 服务员考核操作接口
 *
 * @author Administrator
 */
@Service
public class WaiterShiftImpl implements WaiterShiftService {

    private static DecimalFormat dataformat = new DecimalFormat("0.00");

    private static final Logger logger = LoggerFactory.getLogger(WaiterShiftImpl.class);

    @Autowired
    private WaiterShiftDao shiftDao;

    @Override
    public List<Map<String, String>> getWaiterShiftInfo(Map<String, Object> paramMap) {

        List<Map<String, String>> returnInfoList = new ArrayList<Map<String, String>>();
        //查询指定时间段所有的订单信息
        List<Map<String, Object>> orderInfoList = shiftDao.getWaiterShiftInfo(paramMap);
        if (orderInfoList == null || orderInfoList.size() <= 0) {
            return returnInfoList;
        }
        //查询订单实收
        List<Map<String, Object>> orderPayMountList = shiftDao.getOrderPayMount(paramMap);
        if (orderPayMountList == null || orderPayMountList.size() <= 0) {
            return returnInfoList;
        }
        //查询订单虚增
        List<Map<String, Object>> orderInflatedList = shiftDao.getOrderInflated(paramMap);

        Map<String, BigDecimal> orderPayMountMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> orderInflatedMap = new HashMap<String, BigDecimal>();

        //遍历所有实收
        for (Map<String, Object> payMount : orderPayMountList) {
            if (payMount == null || !payMount.containsKey("orderid") || !payMount.containsKey("paidinamount") || StringUtils.isBlank(String.valueOf(payMount.get("paidinamount"))) || StringUtils.isBlank(String.valueOf(payMount.get("orderid"))) || String.valueOf(payMount.get("paidinamount")).equals("null")) {
                continue;
            }
            String orderid = String.valueOf(payMount.get("orderid"));

            BigDecimal paymount = new BigDecimal(String.valueOf(payMount.get("paidinamount")));

            orderPayMountMap.put(orderid, paymount);
        }
        //遍历所有虚增
        if (orderInflatedList != null && orderInflatedList.size() > 0) {
            for (Map<String, Object> inflated : orderInflatedList) {
                if (inflated == null || !inflated.containsKey("orderid") || !inflated.containsKey("inflated") || StringUtils.isBlank(String.valueOf(inflated.get("inflated"))) || StringUtils.isBlank(String.valueOf(inflated.get("orderid"))) || String.valueOf(inflated.get("inflated")).equals("null")) {
                    continue;
                }
                String orderid = String.valueOf(inflated.get("orderid"));

                BigDecimal inflateddata = new BigDecimal(String.valueOf(inflated.get("inflated")));

                orderInflatedMap.put(orderid, inflateddata);
            }
        }
        Map<String, String> nameMap = new HashMap<String, String>();
        Map<String, Integer> orderNumMap = new HashMap<String, Integer>();
        Map<String, Integer> custnumMap = new HashMap<String, Integer>();
        Map<String, BigDecimal> shouldamountMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> paidinamountMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> xjMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> yhkMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> mlMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> hykxfMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> hyjfxfMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> gz2Map = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> wxzfMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> zfbzfMap = new HashMap<String, BigDecimal>();
        for (Map<String, Object> orderinfo : orderInfoList) {

            String orderid = orderinfo.containsKey("orderid") ? String.valueOf(orderinfo.get("orderid")) : "";
            if (StringUtils.isBlank(orderid)) {
                continue;
            }
            String userid = orderinfo.containsKey("userid") ? String.valueOf(orderinfo.get("userid")) : "";
            if (StringUtils.isBlank(userid)) {
                continue;
            }
            String username = orderinfo.containsKey("name") ? String.valueOf(orderinfo.get("name")) : "";
            int custnum = orderinfo.containsKey("custnum") ? parseInt(String.valueOf(orderinfo.get("custnum"))) : 0;
            if (custnum <= 0) {
                continue;
            }
            BigDecimal shouldamount = orderinfo.containsKey("shouldamount") && !StringUtils.isBlank(String.valueOf(orderinfo.get("shouldamount"))) && !String.valueOf(orderinfo.get("shouldamount")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("shouldamount"))) : new BigDecimal(0);

            BigDecimal paidinamount = orderPayMountMap.containsKey(orderid) ? orderPayMountMap.get(orderid) : new BigDecimal(0);

            BigDecimal inflated = orderInflatedMap.containsKey(orderid) ? orderInflatedMap.get(orderid) : new BigDecimal(0);
            BigDecimal xj = orderinfo.containsKey("xj") && !StringUtils.isBlank(String.valueOf(orderinfo.get("xj"))) && !String.valueOf(orderinfo.get("xf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("xj"))) : new BigDecimal(0);
            BigDecimal yhk = orderinfo.containsKey("yhk") && !StringUtils.isBlank(String.valueOf(orderinfo.get("yhk"))) && !String.valueOf(orderinfo.get("yhk")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("yhk"))) : new BigDecimal(0);
            BigDecimal ml = orderinfo.containsKey("ml") && !StringUtils.isBlank(String.valueOf(orderinfo.get("ml"))) && !String.valueOf(orderinfo.get("ml")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("ml"))) : new BigDecimal(0);
            BigDecimal hykxf = orderinfo.containsKey("hykxf") && !StringUtils.isBlank(String.valueOf(orderinfo.get("hykxf"))) && !String.valueOf(orderinfo.get("hykxf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("hykxf"))) : new BigDecimal(0);
            BigDecimal hyjfxf = orderinfo.containsKey("hyjfxf") && !StringUtils.isBlank(String.valueOf(orderinfo.get("hyjfxf"))) && !String.valueOf(orderinfo.get("hyjfxf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("hyjfxf"))) : new BigDecimal(0);
            BigDecimal gz2 = orderinfo.containsKey("gz2") && !StringUtils.isBlank(String.valueOf(orderinfo.get("gz2"))) && !String.valueOf(orderinfo.get("gz2")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("gz2"))) : new BigDecimal(0);
            BigDecimal wxzf = orderinfo.containsKey("wxzf") && !StringUtils.isBlank(String.valueOf(orderinfo.get("wxzf"))) && !String.valueOf(orderinfo.get("wxzf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("wxzf"))) : new BigDecimal(0);
            BigDecimal zfbzf = orderinfo.containsKey("zfbzf") && !StringUtils.isBlank(String.valueOf(orderinfo.get("zfbzf"))) && !String.valueOf(orderinfo.get("zfbzf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("zfbzf"))) : new BigDecimal(0);
            nameMap.put(userid, username);
            orderNumMap.put(userid, orderNumMap.containsKey(userid) ? orderNumMap.get(userid) + 1 : 1);
            custnumMap.put(userid, custnumMap.containsKey(userid) ? custnumMap.get(userid) + custnum : custnum);
            shouldamountMap.put(userid, shouldamountMap.containsKey(userid) ? shouldamountMap.get(userid).add(shouldamount) : shouldamount);
            paidinamountMap.put(userid, paidinamountMap.containsKey(userid) ? paidinamountMap.get(userid).add(paidinamount.subtract(inflated)) : paidinamount.subtract(inflated));
            xjMap.put(userid, xjMap.containsKey(userid) ? xjMap.get(userid).add(xj) : xj);
            yhkMap.put(userid, yhkMap.containsKey(userid) ? yhkMap.get(userid).add(yhk) : yhk);
            mlMap.put(userid, mlMap.containsKey(userid) ? mlMap.get(userid).add(ml) : ml);
            hykxfMap.put(userid, hykxfMap.containsKey(userid) ? hykxfMap.get(userid).add(hykxf) : hykxf);
            hyjfxfMap.put(userid, hyjfxfMap.containsKey(userid) ? hyjfxfMap.get(userid).add(hyjfxf) : hyjfxf);
            gz2Map.put(userid, gz2Map.containsKey(userid) ? gz2Map.get(userid).add(gz2) : gz2);
            wxzfMap.put(userid, wxzfMap.containsKey(userid) ? wxzfMap.get(userid).add(wxzf) : wxzf);
            zfbzfMap.put(userid, zfbzfMap.containsKey(userid) ? zfbzfMap.get(userid).add(zfbzf) : zfbzf);
        }

        for (Entry<String, String> entry : nameMap.entrySet()) {
            Map<String, String> waiterMap = new HashMap<String, String>();
            if (entry == null || StringUtils.isBlank(entry.getKey()) || StringUtils.isBlank(entry.getValue())) {
                continue;
            }
            String userid = entry.getKey();
            String username = entry.getValue();
            int ordernum = orderNumMap.containsKey(userid) ? orderNumMap.get(userid) : 0;
            int custnum = custnumMap.containsKey(userid) ? custnumMap.get(userid) : 0;
            BigDecimal shouldamount = shouldamountMap.containsKey(userid) ? shouldamountMap.get(userid) : new BigDecimal(0);
            BigDecimal paidinamount = paidinamountMap.containsKey(userid) ? paidinamountMap.get(userid) : new BigDecimal(0);
            BigDecimal xj = xjMap.containsKey(userid) ? xjMap.get(userid) : new BigDecimal(0);
            BigDecimal yhk = yhkMap.containsKey(userid) ? yhkMap.get(userid) : new BigDecimal(0);
            BigDecimal ml = mlMap.containsKey(userid) ? mlMap.get(userid) : new BigDecimal(0);
            BigDecimal hykxf = hykxfMap.containsKey(userid) ? hykxfMap.get(userid) : new BigDecimal(0);
            BigDecimal hyjfxf = hyjfxfMap.containsKey(userid) ? hyjfxfMap.get(userid) : new BigDecimal(0);
            BigDecimal gz2 = gz2Map.containsKey(userid) ? gz2Map.get(userid) : new BigDecimal(0);
            BigDecimal wxzf = wxzfMap.containsKey(userid) ? wxzfMap.get(userid) : new BigDecimal(0);
            BigDecimal zfbzf = zfbzfMap.containsKey(userid) ? zfbzfMap.get(userid) : new BigDecimal(0);
            waiterMap.put("userid", userid);
            waiterMap.put("username", username);
            waiterMap.put("ordernum", ordernum + "");
            waiterMap.put("custnum", custnum + "");
            waiterMap.put("shouldamount", dataformat.format(shouldamount.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("paidinamount", dataformat.format(paidinamount.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("xjamount", dataformat.format(xj.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("yhkamount", dataformat.format(yhk.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("mlamount", dataformat.format(ml.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("hykxfamount", dataformat.format(hykxf.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("hyjfxfamount", dataformat.format(hyjfxf.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("gz2amount", dataformat.format(gz2.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("wxzfamount", dataformat.format(wxzf.setScale(2, BigDecimal.ROUND_HALF_UP)));
            waiterMap.put("zfbzfamount", dataformat.format(zfbzf.setScale(2, BigDecimal.ROUND_HALF_UP)));
            if (custnum <= 0) {
                waiterMap.put("shouldpre", "0.00");
                waiterMap.put("paidinpre", "0.00");
            } else {
                waiterMap.put("shouldpre", dataformat.format(shouldamount.divide(new BigDecimal(custnum), 2, BigDecimal.ROUND_HALF_EVEN).setScale(2, BigDecimal.ROUND_HALF_UP)));
                waiterMap.put("paidinpre", dataformat.format(paidinamount.divide(new BigDecimal(custnum), 2, BigDecimal.ROUND_HALF_EVEN).setScale(2, BigDecimal.ROUND_HALF_UP)));
            }
            returnInfoList.add(waiterMap);
        }

        orderInfoList.clear();
        orderPayMountList.clear();
        orderInflatedList.clear();
        orderPayMountMap.clear();
        orderInflatedMap.clear();
        nameMap.clear();
        orderNumMap.clear();
        custnumMap.clear();
        shouldamountMap.clear();
        paidinamountMap.clear();

        return returnInfoList;
    }


    /**
     * 查询服务员订单信息
     */
    @Override
    public List<Map<String, String>> getWaiterShiftOrderInfo(Map<String, Object> paramMap) {

        List<Map<String, String>> returnInfoList = new ArrayList<Map<String, String>>();
        //查询指定时间段所有的订单信息
        List<Map<String, Object>> orderInfoList = shiftDao.getWaiterShiftInfo(paramMap);
        if (orderInfoList == null || orderInfoList.size() <= 0) {
            return returnInfoList;
        }
        //查询订单实收
        List<Map<String, Object>> orderPayMountList = shiftDao.getOrderPayMount(paramMap);
        if (orderPayMountList == null || orderPayMountList.size() <= 0) {
            return returnInfoList;
        }
        //查询订单虚增
        List<Map<String, Object>> orderInflatedList = shiftDao.getOrderInflated(paramMap);

        Map<String, BigDecimal> orderPayMountMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> orderInflatedMap = new HashMap<String, BigDecimal>();

        //遍历所有实收
        for (Map<String, Object> payMount : orderPayMountList) {
            if (payMount == null || !payMount.containsKey("orderid") || !payMount.containsKey("paidinamount") || StringUtils.isBlank(String.valueOf(payMount.get("paidinamount"))) || StringUtils.isBlank(String.valueOf(payMount.get("orderid"))) || String.valueOf(payMount.get("paidinamount")).equals("null")) {
                continue;
            }
            String orderid = String.valueOf(payMount.get("orderid"));

            BigDecimal paymount = new BigDecimal(String.valueOf(payMount.get("paidinamount")));

            orderPayMountMap.put(orderid, paymount);
        }
        //遍历所有虚增
        if (orderInflatedList != null && orderInflatedList.size() > 0) {
            for (Map<String, Object> inflated : orderInflatedList) {
                if (inflated == null || !inflated.containsKey("orderid") || !inflated.containsKey("inflated") || StringUtils.isBlank(String.valueOf(inflated.get("inflated"))) || StringUtils.isBlank(String.valueOf(inflated.get("orderid"))) || String.valueOf(inflated.get("inflated")).equals("null")) {
                    continue;
                }
                String orderid = String.valueOf(inflated.get("orderid"));

                BigDecimal inflateddata = new BigDecimal(String.valueOf(inflated.get("inflated")));

                orderInflatedMap.put(orderid, inflateddata);
            }
        }

        for (Map<String, Object> orderinfo : orderInfoList) {

            Map<String, String> orderInfoMap = new HashMap<String, String>();

            String orderid = orderinfo.containsKey("orderid") ? String.valueOf(orderinfo.get("orderid")) : "";
            if (StringUtils.isBlank(orderid)) {
                continue;
            }
            String userid = orderinfo.containsKey("userid") ? String.valueOf(orderinfo.get("userid")) : "";
            if (StringUtils.isBlank(userid)) {
                continue;
            }
            String tableids = orderinfo.containsKey("tableids") ? String.valueOf(orderinfo.get("tableids")) : "";
            int custnum = orderinfo.containsKey("custnum") ? parseInt(String.valueOf(orderinfo.get("custnum"))) : 0;
            if (custnum <= 0) {
                continue;
            }
            BigDecimal shouldamount = orderinfo.containsKey("shouldamount") && !StringUtils.isBlank(String.valueOf(orderinfo.get("shouldamount"))) && !String.valueOf(orderinfo.get("shouldamount")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("shouldamount"))) : new BigDecimal(0);

            BigDecimal paidinamount = orderPayMountMap.containsKey(orderid) ? orderPayMountMap.get(orderid) : new BigDecimal(0);

            BigDecimal inflated = orderInflatedMap.containsKey(orderid) ? orderInflatedMap.get(orderid) : new BigDecimal(0);
            BigDecimal xj = orderinfo.containsKey("xj") && !StringUtils.isBlank(String.valueOf(orderinfo.get("xj"))) && !String.valueOf(orderinfo.get("xf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("xj"))) : new BigDecimal(0);
            BigDecimal yhk = orderinfo.containsKey("yhk") && !StringUtils.isBlank(String.valueOf(orderinfo.get("yhk"))) && !String.valueOf(orderinfo.get("yhk")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("yhk"))) : new BigDecimal(0);
            BigDecimal ml = orderinfo.containsKey("ml") && !StringUtils.isBlank(String.valueOf(orderinfo.get("ml"))) && !String.valueOf(orderinfo.get("ml")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("ml"))) : new BigDecimal(0);
            BigDecimal hykxf = orderinfo.containsKey("hykxf") && !StringUtils.isBlank(String.valueOf(orderinfo.get("hykxf"))) && !String.valueOf(orderinfo.get("hykxf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("hykxf"))) : new BigDecimal(0);
            BigDecimal hyjfxf = orderinfo.containsKey("hyjfxf") && !StringUtils.isBlank(String.valueOf(orderinfo.get("hyjfxf"))) && !String.valueOf(orderinfo.get("hyjfxf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("hyjfxf"))) : new BigDecimal(0);
            BigDecimal gz2 = orderinfo.containsKey("gz2") && !StringUtils.isBlank(String.valueOf(orderinfo.get("gz2"))) && !String.valueOf(orderinfo.get("gz2")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("gz2"))) : new BigDecimal(0);
            BigDecimal wxzf = orderinfo.containsKey("wxzf") && !StringUtils.isBlank(String.valueOf(orderinfo.get("wxzf"))) && !String.valueOf(orderinfo.get("wxzf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("wxzf"))) : new BigDecimal(0);
            BigDecimal zfbzf = orderinfo.containsKey("zfbzf") && !StringUtils.isBlank(String.valueOf(orderinfo.get("zfbzf"))) && !String.valueOf(orderinfo.get("zfbzf")).equals("null") ? new BigDecimal(String.valueOf(orderinfo.get("zfbzf"))) : new BigDecimal(0);
            orderInfoMap.put("orderid", orderid);
            orderInfoMap.put("tableids", tableids);
            orderInfoMap.put("custnum", custnum + "");
            orderInfoMap.put("shouldamount", dataformat.format(shouldamount.setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("paidinamount", dataformat.format(paidinamount.subtract(inflated).setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("xjamount", dataformat.format(xj.setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("yhkamount", dataformat.format(yhk.setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("mlamount", dataformat.format(ml.setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("hykxfamount", dataformat.format(hykxf.setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("hyjfxfamount", dataformat.format(hyjfxf.setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("gz2amount", dataformat.format(gz2.setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("wxzfamount", dataformat.format(wxzf.setScale(2, BigDecimal.ROUND_HALF_UP)));
            orderInfoMap.put("zfbzfamount", dataformat.format(zfbzf.setScale(2, BigDecimal.ROUND_HALF_UP)));
            returnInfoList.add(orderInfoMap);

        }

        orderInfoList.clear();
        orderPayMountList.clear();
        orderInflatedList.clear();
        orderPayMountMap.clear();
        orderInflatedMap.clear();

        return returnInfoList;
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            logger.error("-->", ex);
        }
        return 0;
    }
}
