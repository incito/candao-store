package com.candao.www.dataserver.service.member.impl;

import com.candao.www.constant.Constant;
import com.candao.www.dataserver.mapper.OrderDetailMapper;
import com.candao.www.dataserver.mapper.OrderMapper;
import com.candao.www.dataserver.service.member.MemberService;
import com.candao.www.dataserver.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 雅座会员业务类
 * Created by lenovo on 2016/3/21.
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService {
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MemberServiceImpl.class);
    private Lock lock = new ReentrantLock();
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Value("${store.business_name}")
    private String businessName;
    @Value("${store.business}")
    private String business;
    @Value("${store.terminal}")
    private String terminal;

    @Override
    public String setMemberPrice(String userId, String orderId, String memberNo) {
        orderMapper.updateMemberNo(memberNo, orderId);
        List<Map<String, Object>> orderInfos = orderMapper.selectByOrderId(orderId);
        if (null == orderInfos) {
            return "{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}";
        }
        for (Map<String, Object> orderInfo : orderInfos) {
            String dishunitOld = getString(orderInfo.get("dishunit2")); //老版单位
            String vippriceOld = getString(orderInfo.get("vipprice")); //老版VIP价格
            float vippriceNew = 0;
            String dishid = getString(orderInfo.get("dishid"));
            String price = getString(orderInfo.get("price"));
            float priceFloat = objToFloat(price);
            String orderDetailId = getString(orderInfo.get("orderdetailid"));
            int orderDetailIdInt = 0;
            try {
                orderDetailIdInt = Integer.parseInt(orderDetailId);
            } catch (Exception e) {
                orderDetailIdInt = 0;
            }
            //新版本
            if (0 == orderDetailIdInt) {
                String dishunit = getString(orderInfo.get("dishunit"));
                // 新版从t_dish_unit取价格
                String vipprice2Str = orderMapper.selectVipPrice(dishid, dishunit);
                vippriceNew = 0;
                try {
                    vippriceNew = Float.parseFloat(vipprice2Str);
                } catch (Exception e) {
                    vippriceNew = 0;
                }
                // 修改成会员价
                if (vippriceNew > 0) {
                    vippriceNew = (int) (vippriceNew * 100) / 100.00f;
                    orderDetailMapper.updateOrderPrice(String.valueOf(vippriceNew), orderId, orderDetailId);
                }
            } else {
                vippriceOld = vippriceOld.trim();
                if ("/".equals(vippriceOld)) {
                    vippriceNew = 0;
                    //多计量单位
                } else if (vippriceOld.indexOf("/") > -1) {
                    String[] vippriceOlds = vippriceOld.split("/");
                    String[] dishunitOlds = dishunitOld.split("/");
                    if (vippriceOlds.length == 0 || dishunitOlds.length == 0) {
                        vippriceNew = 0;
                    } else {
                        String dishunit = getString(orderInfo.get("dishunit"));
                        for (int i = 0; i < dishunitOlds.length; i++) {
                            if (dishunitOlds[i].equals(dishunit)) {
                                vippriceNew = objToFloat(vippriceOlds[i]);
                                break;
                            }
                        }
                    }
                } else {
                    vippriceNew = objToFloat(vippriceOld);
                }
                if (vippriceNew > 0 || ((vippriceNew <= 0) && (priceFloat < 0.02) && (price.indexOf("/") == -1))) {
                    orderDetailMapper.updateOrderPrice(String.valueOf(vippriceNew), orderId, orderDetailId);
                }
            }
        }
        return "{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}";
    }

    @Transactional
    @Override
    public String revertMemberPrice(String userId, String orderId) {
        return revertMemberPrice(userId, orderId, false);
    }

    /**
     * 取消会员价
     *
     * @param userId
     * @param orderId
     * @param anyway  是否强制取消
     * @return
     */
    private String revertMemberPrice(String userId, String orderId, boolean anyway) {
        userId = StringUtil.clean(userId);
        orderId = StringUtil.clean(orderId);
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(orderId)) {
            return "{\"Data\":\"0\",\"workdate\":\"\",\"Info\":缺少参数\"\"}";
        }

        String memberNo = orderMapper.selectMemberNoByOrderId(orderId);
        // 如果是PAD登录过的就不取消会员价
        if (!StringUtil.isEmpty(memberNo)) {
            return "{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}";
        }
        if (!anyway) {
            // 如果帐单已经结算就不还原了
            Integer orderStatus = orderMapper.selectOrderStatus(orderId);
            if (Constant.ORDERSTATUS.ORDER_STATUS != orderStatus) {
                return "{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}";
            }
        }
        List<Map<String, Object>> orderDetails = orderMapper.selectPrice(orderId);
        if (null == orderDetails) {
            return "{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}";
        }
        for (Map<String, Object> orderDetail : orderDetails) {
            String dishUnit2 = getString(orderDetail.get("dishunit2"));
            String price = getString(orderDetail.get("price"));
            String dishId = getString(orderDetail.get("dishid"));
            String orderDetailId = getString(orderDetail.get("orderdetailid"));
            if (!StringUtil.isEmpty(orderDetailId)) {
                String dishUnit = getString(orderDetail.get("dishunit"));
                String normalPrice = orderMapper.selectNormalPrice(dishId, dishUnit);
                float normalPriceFloat = objToFloat(normalPrice);
                if (normalPriceFloat > 0) {
                    normalPriceFloat = (int) (normalPriceFloat * 100) / 100.00f;
                    orderDetailMapper.updateOrderPrice(String.valueOf(normalPriceFloat), orderId, orderDetailId);
                }
            } else {
                float normalPriceFloat = 0;
                if ("/".equals(price)) {
                    normalPriceFloat = 0;
                } else if (price.indexOf("/") > -1) {// 是多计量单位
                    String dishUnit = getString(orderDetail.get("dishunit"));
                    String[] prices = price.split("/");
                    String[] dishUnits = dishUnit2.split("/");
                    if (prices.length == 0 || dishUnits.length == 0) {
                        normalPriceFloat = 0;
                    } else {
                        for (int i = 0; i < dishUnits.length; i++) {
                            String unit = dishUnits[i];
                            if (unit.equals(dishUnit)) {
                                normalPriceFloat = objToFloat(prices[i]);
                            }
                        }
                    }
                } else {
                    normalPriceFloat = objToFloat(price);
                }
                if (normalPriceFloat > 0) {
                    orderDetailMapper.updateOrderPrice(String.valueOf(normalPriceFloat), orderId, orderDetailId);
                }
            }
        }
        orderMapper.updateMemberNo("", orderId);
        return "{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}";
    }

    @Override
    public String revertMemberPrice2(String userId, String orderId, String ip) {
        return revertMemberPrice(userId, orderId, true);
    }

    private String getString(Object obj) {
        return null == obj ? "" : obj.toString();
    }

    private float objToFloat(Object obj) {
        if (null == obj) {
            return 0;
        }
        try {
            Float.parseFloat(obj.toString());
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}
