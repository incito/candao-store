package com.candao.www.dataserver.service.dish.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.dataserver.mapper.DishMapper;
import com.candao.www.dataserver.mapper.OperationLogMapper;
import com.candao.www.dataserver.mapper.OrderOpMapper;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.model.ResponseJsonData;
import com.candao.www.dataserver.service.dish.DishService;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.dataserver.util.DataServerJsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/18.
 */
@Service
public class DishOpServiceImpl implements DishService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DishOpServiceImpl.class);
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private OperationLogMapper operationLogMapper;
    @Autowired
    private OrderOpMapper orderOpMapper;
    @Autowired
    private OrderOpService orderOpService;
	@Autowired
	private TdishDao tdishDao;

    @Override
    public String getFoodStatus(String dishId, String dishUnit) {
        ResponseData responseData = new ResponseData();
        System.out.println("###################" + dishId + "---" + dishUnit);
        LOGGER.info("###getFoodStatus dishId={},dishUnit={}###", dishId, dishUnit);
        try {
//            Integer status = dishMapper.getFoodStatus(dishId, dishUnit);
//            if (status == null) {
//                responseData.setInfo("0");
//            } else {
//                responseData.setInfo(status + "");
//            }
            //采用新的沽清机制，修改人：张继俊，修改时间：2016-09-29
            Map<String, Object> params = new HashMap<>();
            params.put("dishid", dishId);
			String dishStatus = tdishDao.getDishStatus(params);
			responseData.setInfo(dishStatus);
        } catch (Exception e) {
            responseData.setData("0");
            responseData.setInfo("获取菜品状态异常");
            LOGGER.error("###getFoodStatus dishId={},dishUnit={},error={}###", dishId, dishUnit, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    public String getAllWmFood(String userId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        LOGGER.info("###getAllWmFood userId={}###", userId);
        try {
            dishMapper.updateDishPY();
        } catch (Exception e) {
            LOGGER.error("###getAllWmFood userId={},error={}###", userId, "计算菜品拼音首字母出错！" + e.getCause().getStackTrace());
        }
        List<Map> mapList = dishMapper.getAllWmFood();
        if(null!=mapList&&!mapList.isEmpty()){
        	responseJsonData.setOrderJson(mapList);
        }else{
        	responseJsonData.setData("0");
        	responseJsonData.setInfo("没有菜品信息");
        }
        String s = JSON.toJSONString(responseJsonData);
        LOGGER.info("###getAllWmFood ###RESPONSE### result={}", s);
        return s;
    }

    @Override
    public String getCJFood(String userId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        LOGGER.info("###getCJFood userId={}###", userId);
        try {
            List<Map> mapList = dishMapper.getCJFood();
            responseJsonData.setOrderJson(mapList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("获取全部菜品异常");
            LOGGER.error("###getCJFood userId={},error={}###", userId, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String getGroupDetail(String dishId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        LOGGER.info("###getGroupDetail dishId={}###", dishId);
        try {
            List<Map> mapList = dishMapper.getGroupDetail(dishId);
            responseJsonData.setOrderJson(mapList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("获取套餐明细异常");
            LOGGER.error("###getGroupDetail dishId={},error={}###", dishId, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String getFavorable(String userId, String orderId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        Long ymNum;
        Map<Object, Object> orderJson = new HashMap<>();
        Map listJson = new HashMap<>();
        Map doubleJson = new HashMap<>();
        try {
            LOGGER.info("###getFavorable userId={},orderId={}###", userId, orderId);
            Double djDishNum = dishMapper.getDjDishNum(orderId);
            if (null != djDishNum && djDishNum > 0) {
                Double scDishNum = dishMapper.getScDishNum(orderId);
                if (null != scDishNum && scDishNum > 0) {
                    if (djDishNum >= (scDishNum * 2)) {
                        ymNum = Math.round(djDishNum * 2);
                    } else {
                        ymNum = Math.round(djDishNum);
                    }
                    orderJson.put("couponname", "打底套餐自选优免");
                    orderJson.put("orderprice", 6);
                    orderJson.put("decdishnum", ymNum + "");
                    orderJson.put("decorderprice", 6);
                    orderJson.put("decamount", ymNum * 6 + "");
                }
            }
            listJson = dishMapper.getYGListJson(orderId);
            doubleJson = dishMapper.getYGDoubleJson(orderId);
            responseJsonData.setOrderJson(orderJson);
            responseJsonData.setListJson(listJson);
            responseJsonData.setDoubleJson(doubleJson);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("优惠自动使用接口(惠新，蓝港，辣倒兔临时方案)异常");
            LOGGER.error("###getFavorable  userId={},orderId={},error={}###", userId, orderId, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    @Transactional
    public String getBackDishInfo(String orderId, String dishId, String dishUnit, String tableNo) {
        LOGGER.info("###getBackDishInfo orderId={} dishId={} dishUnit={} tableNo={}###", orderId, dishId, dishUnit, tableNo);
        ResponseData responseData = new ResponseData();
        try {
            operationLogMapper.deletePosOperation(tableNo);
            dishUnit = dishUnit.replace("&quot", "#");
            List<Map<String, Object>> mapList = dishMapper.getBackDishInfo(orderId, dishId, dishUnit);
            if (mapList.isEmpty()) {
                responseData.setData("0");
            } else {
                for (Map<String, Object> map : mapList) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String key = entry.getKey();
                        if (entry.getValue() != null) {
                            String value = entry.getValue() + "";
                            map.put(key, value.replace(".00", "."));
                        }
                    }
                    if (map.get("begintime") != null) {
                        String beginTime = map.get("begintime") + "";
                        map.put("begintime", beginTime.replace(".0", "").replace("-", ""));
//                        map.put("begintime", WorkDateUtil.parse(beginTime.replace(".0", "").replace("-", ""), "yyyyMMdd HH:mm:ss"));
                    }
                    if (map.get("endtime") != null) {
                        String endTime = map.get("endtime") + "";
                        map.put("endtime", endTime.replace(".0", "").replace("-", ""));
//                        map.put("endtime", WorkDdateUtil.parse(endTime.replace(".0", "").replace("-", ""), "yyyyMMdd HH:mm:ss"));
                    }
                }
                return JSON.toJSONString(DataServerJsonFormat.jsonFormat(mapList));
            }
        } catch (Exception e) {
            responseData.setData("0");
            responseData.setInfo("获取退菜dish列表,如果选择的是套餐明细，不能退，如果选择的是鱼锅，退整个锅，如果选择的是锅内明细，退鱼，不能只退锅,异常");
            LOGGER.error("###getBackDishInfo orderId={} dishId={} dishUnit={} tableNo={} error={}###", orderId, dishId, dishUnit, tableNo, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    @Transactional
    public String updateCj(String orderId, String userId) {
        LOGGER.info("###updateCj orderId={} userId={}###", orderId, userId);
        ResponseData responseData = new ResponseData();
        try {
            String dishId = "826ffa67-4a32-4ad0-b4ab-85694cab1db4";
            String orderPrice = dishMapper.getPriceByDishId(dishId);
            String dishUnit = dishMapper.getUnitByDishId(dishId);
            if (null == dishUnit || "".equals(dishUnit)) {
                return JSON.toJSONString(responseData);
            }
            String custnum = orderOpMapper.getCustnumByOrderId(orderId);
            String tmpDishId = orderOpMapper.getDishIdByOrDiId(orderId, dishId);
            float dishNum = orderOpMapper.getDishNumByOrDiId(orderId, dishId);
            int detailCount = orderOpMapper.countByOrderId(orderId);
            if (detailCount < 0) {
                return JSON.toJSONString(responseData);
            }
            String orderStatus = orderOpMapper.getStatusByOrderId(orderId);
            if (!"0".equals(orderStatus)) {
                return JSON.toJSONString(responseData);
            }
            if (null == tmpDishId || "".equals(tmpDishId.trim())) {
                if (custnum == (Math.round(dishNum) + "")) {
                    return JSON.toJSONString(responseData);
                }
            }
            if (null == tmpDishId || "".equals(tmpDishId.trim())) {
                orderOpMapper.saveOrderDetail(orderId, custnum, userId, orderPrice, dishUnit);
            }
            orderOpService.pCaleTableAmount(userId, orderId);
        } catch (Exception e) {
            LOGGER.error("###updateCj orderId={} userId={} error={}###", orderId, userId, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    public String deletePosOperation(String tableNo) {
        LOGGER.info("###deletePosOperation tableNo={}###", tableNo);
        operationLogMapper.deletePosOperation(tableNo);
        return "{\"Data\":\"1\"}";
    }
}
