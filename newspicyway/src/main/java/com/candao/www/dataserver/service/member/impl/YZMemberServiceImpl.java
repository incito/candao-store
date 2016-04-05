package com.candao.www.dataserver.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.DateUtils;
import com.candao.www.constant.Constant;
import com.candao.www.dataserver.entity.OrderMember;
import com.candao.www.dataserver.entity.SettlmentDetail;
import com.candao.www.dataserver.mapper.OrderDetailMapper;
import com.candao.www.dataserver.mapper.OrderMapper;
import com.candao.www.dataserver.mapper.OrderMemberMapper;
import com.candao.www.dataserver.mapper.SettlementDMapper;
import com.candao.www.dataserver.model.ConsumerData;
import com.candao.www.dataserver.model.MemberAPIData;
import com.candao.www.dataserver.model.RechargeData;
import com.candao.www.dataserver.service.member.MemberCard;
import com.candao.www.dataserver.service.member.MemberService;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.dataserver.util.WorkDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 雅座会员业务类
 * Created by lenovo on 2016/3/21.
 */
@Service
public class YZMemberServiceImpl implements MemberService {
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(YZMemberServiceImpl.class);
    private Lock lock = new ReentrantLock();
    /**
     * 线程等待时间，单位秒
     */
    private final int waitTime = 6;

    @Autowired
    private MemberCard yzAPI;
    @Autowired
    private SettlementDMapper settlementDetailMapper;
    @Autowired
    private OrderMemberMapper orderMemberMapper;
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
    private String valid = "1";//撤销成功标记

    @Override
    public String queryBalance(String memberNo) {
        MemberAPIData result = null;
        try {
            if (lock.tryLock(waitTime, TimeUnit.SECONDS)) {
                result = yzAPI.queryBanlance(memberNo);
            } else {
                return "{\"Data\":\"0\",\"Info\":\"系统繁忙，请稍后再试\"}";
            }
        } catch (Exception e) {
            logger.error("###Member queryBalance occur error! error={}", e);
            return "{\"Data\":\"0\",\"Info\":\"系统错误\"}";
        } finally {
            lock.unlock();
        }
        if (!StringUtils.isEmpty(result.getErrMsg())) {
            return "{\"Data\":\"0\",\"Info\":\"" + result.getErrMsg() + "\"}";
        }
        Map<String, String> data = (Map<String, String>) result.getData();
        data.put("Data", "1");
        return JSON.toJSONString(data);

    }

    @Override
    public String VoidSale(String orderId, String userPwd, String adminPwd) {
        OrderMember orderMember = orderMemberMapper.selectByOrderId(orderId);
        //已撤销则直接返回
        if (StringUtils.isEmpty(orderMember.getValid()) || valid.equals(orderMember.getValid())) {
            return "{\"Data\":\"1\",\"Info\":\"\"}";
        }

        MemberAPIData result = null;
        try {
            if (lock.tryLock(waitTime, TimeUnit.SECONDS)) {
                result = yzAPI.voidSale(orderMember.getCardno(), orderMember.getSerial(), orderId, userPwd, adminPwd);
            } else {
                return "{\"Data\":\"0\",\"Info\":\"系统繁忙，请稍后再试\"}";
            }
        } catch (Exception e) {
            logger.error("###Member VoidSale occur error! error={}", e);
            return "{\"Data\":\"0\",\"Info\":\"系统错误\"}";
        } finally {
            lock.unlock();
        }
        if (!StringUtils.isEmpty(result.getErrMsg())) {
            return "{\"Data\":\"0\",\"Info\":\"" + result.getErrMsg() + "\"}";
        }
        Map<String, String> data = (Map<String, String>) result.getData();
        String pszRestInfo = data.get("pszRestInfo");
        //标记订单撤销
        orderMember = new OrderMember();
        orderMember.setValid(Integer.parseInt(valid));
        orderMember.setOrderid(orderId);
        orderMemberMapper.updateValid(orderMember);
        return "{\"Data\":\"1\",\"Info\":\"" + pszRestInfo + "\"}";
    }

    @Override
    public String storeCardDeposit(RechargeData recharge) {
        Date workDate = WorkDateUtil.getWorkDate1();
        SettlmentDetail detail = new SettlmentDetail();
        detail.setSdetailid(IDUtil.getID());
        String orderId = DateUtils.toString(workDate, "yyyyMMdd") + recharge.getMemberNo();
        detail.setOrderid(orderId);
        detail.setNormalprice(recharge.getAmount());
        detail.setPayamount(recharge.getAmount());
        detail.setOpendate(workDate);
        detail.setUsername(recharge.getUserId());
        settlementDetailMapper.insert(detail);

        MemberAPIData result = null;
        try {
            if (lock.tryLock(waitTime, TimeUnit.SECONDS)) {
                result = yzAPI.storeCardDeposit(recharge.getMemberNo(), recharge.getAmount(), recharge.getSerial(), recharge.getTransType(), recharge.getPayType());
            } else {
                return "{\"Data\":\"0\",\"Info\":\"系统繁忙，请稍后再试\"}";
            }
        } catch (Exception e) {
            logger.error("###Member storeCardDeposit occur error! error={}", e);
            return "{\"Data\":\"0\",\"Info\":\"系统错误\"}";
        } finally {
            lock.unlock();
        }
        if (!StringUtils.isEmpty(result.getErrMsg())) {
            return "{\"Data\":\"0\",\"Info\":\"" + result.getErrMsg() + "\"}";
        }
        Map<String, String> data = (Map<String, String>) result.getData();
        data.put("Data", "1");
        settlementDetailMapper.update(data.get("pszTrace"), orderId);
        return JSON.toJSONString(data);
    }

    @Override
    public String memberSale(ConsumerData consumer) {
        if (StringUtils.isEmpty(consumer.getTicketList()) || consumer.getTicketList().length() < 12) {
            consumer.setTicketList("");
        }

        int pwd = 0;
        if (!StringUtils.isEmpty(consumer.getMemberPwd())) {
            try {
                pwd = Integer.parseInt(consumer.getMemberPwd());
            } catch (Exception e) {
                pwd = 0;
            }
        }

        MemberAPIData result = null;
        try {
            if (lock.tryLock(waitTime, TimeUnit.SECONDS)) {
                result = yzAPI.memberSale(consumer.getMemberNo(), consumer.getSerail(), consumer.getCash(), consumer.getPoint(), consumer.getTransType(), consumer.getStore(), consumer.getTicketList(), pwd, consumer.getMemberyhqamount());
            } else {
                return "{\"Data\":\"0\",\"Info\":\"系统繁忙，请稍后再试\"}";
            }
        } catch (Exception e) {
            logger.error("###Member memberSale occur error! error={}", e);
            return "{\"Data\":\"0\",\"Info\":\"系统错误\"}";
        } finally {
            lock.unlock();
        }
        if (!StringUtils.isEmpty(result.getErrMsg())) {
            return "{\"Data\":\"0\",\"Info\":\"" + result.getErrMsg() + "\"}";
        }
        orderMemberMapper.deleteByOrderId(consumer.getOrderId());

        Map<String, String> data = (Map<String, String>) result.getData();
        OrderMember orderMember = new OrderMember();
        orderMember.setOrderid(consumer.getOrderId()); //收银帐单号
        orderMember.setUserid(consumer.getUserId());//收银员
        orderMember.setOrdertime(new Date());//交易时间
        orderMember.setOperatetype(1);//交易类型
        orderMember.setBusiness(business);//商户号
        orderMember.setTerminal(terminal);//终端号
        orderMember.setBatchno(data.get("pszTrace"));//批次号
        orderMember.setSerial(data.get("pszTrace"));//主机流水号
        orderMember.setBusinessname(businessName); //商户名称
        orderMember.setScore(Float.parseFloat(data.get("pszCreditsPoint")) - Float.parseFloat(consumer.getPoint())); //本次积分（可增可减，根据交易类型）
        orderMember.setCoupons(Double.parseDouble(consumer.getMemberyhqamount())); //本次券   coupons;
        float storeFloat = Float.parseFloat(consumer.getStore());
        orderMember.setStored(storeFloat); //本次储值
        orderMember.setScorebalance(Float.parseFloat(data.get("psIntegralOverall")));//积分余额
        orderMember.setCouponsbalance(0);//券余额
        orderMember.setStoredbalance(Float.parseFloat(data.get("psStoredCardsBalance")));//储值余额
        orderMember.setCardno(data.get("pszPan")); //卡号
        orderMember.setPsexpansivity(data.get("psexpansivity"));//增加膨胀系数
        orderMember.setNetvalue(storeFloat * (Double.parseDouble(data.get("psexpansivity")) / 100000000)); //会员净值
        orderMember.setInflated(storeFloat - orderMember.getNetvalue());     //会员虚增
        orderMemberMapper.insert(orderMember);

        return "{\"Data\":\"1\",\"Info\":\"" + data.get("pszRestInfo") + "\"}";
    }

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

    @Override
    public String cardActive(String mobile, String track2, String pwd) {
        if (StringUtils.isEmpty(pwd)) {
            pwd = "000000";//默认密码
        }
        MemberAPIData result = null;
        try {
            if (lock.tryLock(waitTime, TimeUnit.SECONDS)) {
                result = yzAPI.cardActive(mobile, track2, pwd);
            } else {
                return "{\"Data\":\"0\",\"Info\":\"系统繁忙，请稍后再试\"}";
            }
        } catch (Exception e) {
            logger.error("###Member memberSale occur error! error={}", e);
            return "{\"Data\":\"0\",\"Info\":\"系统错误\"}";
        } finally {
            lock.unlock();
        }

        if (!StringUtils.isEmpty(result.getErrMsg())) {
            return "{\"Data\":\"0\",\"Info\":\"" + result.getErrMsg() + "\"}";
        }
        Map<String, String> data = (Map<String, String>) result.getData();
        String info = data.get("pszRestInfo");
        return "{\"Data\":\"1\",\"Info\":\"" + (null == info ? "" : info) + "\"}";
    }

    @Override
    @Transactional
    public String queryBalanceForPad(String input, String orderId) {
        if (null == orderId || orderId.trim().isEmpty()) {
            return "{\"Data\":\"0\",\"Info\":\"帐单不能为空!\"}";
        }
        /*如果帐单已经结帐，不能登录*/
        Integer orderStatus = orderMapper.selectOrderStatus(orderId);
        if (null == orderStatus) {
            return "{\"Data\":\"0\",\"Info\":\"帐单不能为空!\"}";
        }
        if (orderStatus == Constant.ORDERSTATUS.INTERNAL_SETTLED_STATUS) {
            return "{\"Data\":\"0\",\"Info\":\"帐单已经结算,不能再登录会员!\"}";
        }
        MemberAPIData result = null;
        try {
            if (lock.tryLock(waitTime, TimeUnit.SECONDS)) {
                result = yzAPI.queryBanlance(input);
            } else {
                return "{\"Data\":\"0\",\"Info\":\"系统繁忙，请稍后再试\"}";
            }
        } catch (Exception e) {
            logger.error("###Member queryBalance occur error! error={}", e);
            return "{\"Data\":\"0\",\"Info\":\"系统错误\"}";
        } finally {
            lock.unlock();
        }
        if (!StringUtils.isEmpty(result.getErrMsg())) {
            return "{\"Data\":\"0\",\"Info\":\"" + result.getErrMsg() + "\"}";
        }

        int update = orderMapper.updateMemberNo(input, orderId);// TODO: 2016/3/31  事务
        if (update > 0) {
            setMemberPrice("PAD", orderId, input);
        }
        Map<String, String> data = (Map<String, String>) result.getData();
        data.put("Data", "1");
        return JSON.toJSONString(data);
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

    public static void main(String[] args) {
        String libpath = System.getProperty("java.library.path");
        System.out.println(libpath);
        if (libpath == null || libpath.length() == 0) {
            throw new RuntimeException("java.library.path is null");
        }
        //javaBinPath   jdk的bin目录D:\Program Files\Java\jdk1.6.0_11\bin
        String javaBinPath = null;
        StringTokenizer st = new StringTokenizer(libpath,
                System.getProperty("path.separator"));
        if (st.hasMoreElements()) {
            javaBinPath = st.nextToken();
        } else {
            throw new RuntimeException("can not split library path:"
                    + libpath);
        }
        URL resource = MemberService.class.getResource("/");
        System.out.println(resource.getFile().toString());
    }
}
