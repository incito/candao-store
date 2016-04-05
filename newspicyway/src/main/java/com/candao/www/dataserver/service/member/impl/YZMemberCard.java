package com.candao.www.dataserver.service.member.impl;

import com.candao.www.dataserver.entity.Member;
import com.candao.www.dataserver.mapper.MemberMapper;
import com.candao.www.dataserver.model.MemberAPIData;
import com.candao.www.dataserver.service.member.MemberCard;
import com.candao.www.dataserver.util.YazuoJna;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 雅座会员接口
 * Created by lenovo on 2016/3/23.
 */
@Service
public class YZMemberCard implements MemberCard {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(YZMemberCard.class);
    /**
     * 最后登录时间
     */
    private long lastLogin = 0;
    /**
     * session有效期
     */
    private static final long expiry_date = 1000 * 60 * 60 * 23;
    /**
     * POS类型  1软POS，2 硬POS
     */
    private static final int POS_TYPE = 1;
    private static final String COM1 = "com1";
    private static final String PRINT_ORDER = "0";
    private static final Charset CHARSET = Charset.forName("GBK");

    private YazuoJna yazuo = YazuoJna.instanceDll;
    @Value("${yazuo.username}")
    private String userId;
    @Value("${yazuo.passsword}")
    private String userPwd;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public MemberAPIData queryBanlance(String memberNo) {
        MemberAPIData result = new MemberAPIData();
        String[] track2 = getTrack2(memberNo);
        if (!RET_SUCCESS_STR.equals(track2[0])) {
            result.setErrMsg(track2[1]);
            return result;
        }

        //查询余额
        byte[] pszRetcode = createBytes(2);
        byte[] pszRestInfo = createBytes(50);
        byte[] pszRefnum = createBytes(12);
        byte[] pszTrace = createBytes(6);
        byte[] pszPan = createBytes(19);
        byte[] psStoredCardsBalance = createBytes(12);
        byte[] psIntegralOverall = createBytes(12);
        byte[] psIntegralAvail = createBytes(12);
        byte[] psCouponsOverall = createBytes(12);
        byte[] psCouponsAvail = createBytes(12);
        byte[] psTicketInfo = createBytes(200);
        byte[] psCardType = createBytes(20);
        byte[] pszMobile = createBytes(11);
        byte[] pszName = createBytes(10);
        byte[] pszGender = createBytes(2);
        byte[] pszBirthday = createBytes(11);
        byte[] pszJoindate = createBytes(11);
        byte[] pszEmail = createBytes(50);
        byte[] pszAddress = createBytes(100);
        byte[] pszdescription = createBytes(100);
        LOGGER.info("#####YaZuoAPI QueryBalance start memberNo={} pszTrack2={}", memberNo, track2[1]);
        int retCode = yazuo.QueryBalance(pszRetcode, pszRestInfo, pszRefnum, pszTrace, pszPan, psIntegralOverall, psIntegralAvail, psCouponsOverall, psCouponsAvail, psStoredCardsBalance,
                track2[1], COM1, psTicketInfo, psCardType, pszMobile, pszName, pszGender, pszBirthday, pszJoindate, pszEmail, pszAddress, pszdescription, POS_TYPE);
        LOGGER.info("#####YaZuoAPI QueryBalance end  memberNo={} pszTrack2={} result={}", memberNo, track2[1], retCode);
        if (RET_SUCCESS != retCode) {
            result.setErrMsg("雅座:查询失败!(请检查服务器外网是否稳定)");
            return result;
        }

        //构建查询结果
        Map<String, String> data = new HashMap<>();
        data.put("pszRestInfo", byteToStr(pszRestInfo));
        data.put("psStoredCardsBalance", leftZeroToPrice(byteToStr(psStoredCardsBalance)));
        data.put("psIntegralOverall", leftZeroToPrice(byteToStr(psIntegralOverall)));
        data.put("psIntegralAvail", leftZeroToPrice(byteToStr(psIntegralAvail)));
        data.put("psCouponsOverall", leftZeroToPrice(byteToStr(psCouponsOverall)));
        data.put("psCouponsAvail", leftZeroToPrice(byteToStr(psCouponsAvail)));
        data.put("psCardType", byteToStr(psCardType));
        data.put("pszMobile", byteToStr(pszMobile));
        data.put("pszName", byteToStr(pszName));
        data.put("pszGender", byteToStr(pszGender));
        data.put("pszBirthday", byteToStr(pszBirthday));
        data.put("pszJoindate", byteToStr(pszJoindate));
        data.put("pszEmail", byteToStr(pszEmail));
        data.put("pszAddress", byteToStr(pszAddress));
        data.put("pszdescription", byteToStr(pszdescription));
        data.put("pszPan", byteToStr(pszPan));
        data.put("psTicketInfo", byteToStr(psTicketInfo));
        data.put("pszTrack2", track2[1]);
        result.setData(data);
        return result;
    }

    @Override
    public MemberAPIData voidSale(String memberNo, String pszTrace, String serial, String userPwd, String adminPwd) {
        MemberAPIData result = new MemberAPIData();
        String[] trackResult = getTrack2(memberNo);
        if (!RET_SUCCESS_STR.equals(trackResult[0])) {
            result.setErrMsg(trackResult[1]);
            return result;
        }

        serial = getYazuoSerial(serial);
        byte[] pszRetcode = createBytes(2);
        byte[] pszRestInfo = createBytes(50);
        byte[] pszRefnum = createBytes(12);
        byte[] pszTraceByte = pszTrace.getBytes();
        byte[] pszTraceTarget = createBytes(6);
        System.arraycopy(pszTraceByte, 0, pszTraceTarget, 0, pszTraceByte.length);
        byte[] pszPan = createBytes(19);
        byte[] pszCreditsPoint = createBytes(12);
        byte[] pszCreditsGifts = createBytes(12);
        byte[] psStoredCardsBalance = createBytes(12);
        byte[] pszTicketList = createBytes(12);
        byte[] psIntegralOverall = createBytes(12);
        byte[] pszStore = createBytes(12);
        byte[] psexpansivity = createBytes(12);
        LOGGER.info("#####YaZuoAPI VoidSale start memberNo={} pszTrack2={}", memberNo, trackResult[1]);
        int retCode = yazuo.VoidSale(pszRetcode, pszRestInfo, pszRefnum, pszTraceTarget, pszPan, pszCreditsPoint, pszCreditsGifts, serial, userPwd, adminPwd, "", trackResult[1], PRINT_ORDER, psexpansivity, COM1, psStoredCardsBalance, pszTicketList, psIntegralOverall, pszStore, POS_TYPE);
        LOGGER.info("#####YaZuoAPI VoidSale end memberNo={} pszTrack2={} result={}", memberNo, trackResult[1], retCode);
        String pszRestInfoString = byteToStr(pszRestInfo);
        if (RET_SUCCESS != retCode && !pszRestInfoString.contains("此交易已撤销")) {
            if (StringUtils.isEmpty(pszRestInfoString)) {
                pszRestInfoString = "雅座：交易撤销失败";
            }
            result.setErrMsg(pszRestInfoString);
        } else {
            Map<String, String> data = new HashMap<>();
            data.put("pszRetcode", byteToStr(pszRetcode));
            data.put("pszRestInfo", pszRestInfoString);
            data.put("pszRefnum", byteToStr(pszRefnum));
            data.put("pszTrace", byteToStr(pszTraceTarget));
            data.put("pszPan", byteToStr(pszPan));
            data.put("pszCreditsPoint", leftZeroToPrice(byteToStr(pszCreditsPoint)));
            data.put("pszCreditsGifts", leftZeroToPrice(byteToStr(pszCreditsGifts)));
            data.put("psStoredCardsBalance", leftZeroToPrice(byteToStr(psStoredCardsBalance)));
            data.put("pszTicketList", byteToStr(pszTicketList));
            data.put("psIntegralOverall", leftZeroToPrice(byteToStr(psIntegralOverall)));
            data.put("pszStore", leftZeroToPrice(byteToStr(pszStore)));
            double psexpansivityD = 100000000;
            try {
                psexpansivityD = Double.parseDouble(byteToStr(psexpansivity));
            } catch (Exception e) {
            }
            data.put("psexpansivity", String.valueOf(psexpansivityD));
            result.setData(data);
        }
        return result;
    }

    @Override
    public MemberAPIData storeCardDeposit(String memberNo, String amount, String serial, String transType, String payType) {
        MemberAPIData result = new MemberAPIData();
        String[] track2 = getTrack2(memberNo);
        if (!RET_SUCCESS_STR.equals(track2[0])) {
            result.setErrMsg(track2[1]);
            return result;
        }

        byte[] pszRetcode = createBytes(2);
        byte[] pszRestInfo = createBytes(50);
        byte[] pszRefnum = createBytes(12);
        byte[] pszTrace = createBytes(6);
        byte[] pszPan = createBytes(19);
        byte[] psStoreCardBalance = createBytes(12);
        amount = priceToLeftZero(amount, 12);
        String pszTrack2 = track2[1];
        LOGGER.info("#####YaZuoAPI StoreCardDeposit start memberNo={} pszTrack2={} amount={} serial={} transType={}", memberNo, pszTrack2, amount, serial, transType, payType);
        int retCode = yazuo.StoreCardDeposit(pszRetcode, pszRestInfo, pszRefnum, pszTrace, pszPan, psStoreCardBalance, pszTrack2, amount, serial, transType, PRINT_ORDER, COM1, POS_TYPE);
        LOGGER.info("#####YaZuoAPI StoreCardDeposit end result={}", retCode);
        String errMsg = byteToStr(pszRestInfo);
        if (RET_SUCCESS != retCode) {
            if (StringUtils.isEmpty(errMsg)) {
                errMsg = "雅座：储值失败";
            }
            result.setErrMsg(errMsg);
        } else {
            //构建结果
            Map<String, String> data = new HashMap<>();
            data.put("pszRestInfo", errMsg);
            data.put("pszRetcode", byteToStr(pszRetcode));
            data.put("pszRefnum", byteToStr(pszRefnum));
            data.put("pszTrace", byteToStr(pszTrace));
            data.put("pszPan", byteToStr(pszPan));
            String balance = leftZeroToPrice(byteToStr(psStoreCardBalance));
            data.put("psStoreCardBalance", balance);
            result.setData(data);
        }
        return result;
    }

    @Override
    public MemberAPIData memberSale(String memberNo, String serial, String cash, String point, String transType, String store, String ticketList, int memberPwd, String memberyhqamount) {
        MemberAPIData result = new MemberAPIData();
        String[] trackResult = getTrack2(memberNo);
        String track2 = trackResult[1];
        if (!RET_SUCCESS_STR.equals(trackResult[0])) {
            result.setErrMsg(track2);
            return result;
        }

        cash = priceToLeftZero(cash, 12);
        point = priceToLeftZero(point, 12);
        store = priceToLeftZero(store, 12);
        serial = getYazuoSerial(serial);
        byte[] pszRetcode = createBytes(2);
        byte[] pszRestInfo = createBytes(50);
        byte[] pszRefnum = createBytes(12);
        byte[] pszTrace = createBytes(6);
        byte[] pszPan = createBytes(16);
        byte[] pszCreditsPoint = createBytes(12);
        byte[] pszGiftsTicket = createBytes(12);
        byte[] psStoredCardsBalance = createBytes(12);
        byte[] psIntegralOverall = createBytes(12);
        byte[] psexpansivity = createBytes(12);
        LOGGER.info("#####YaZuoAPI Sale start memberNo={} pszTrack2={} serial={}", memberNo, track2, serial);
        int retCode = yazuo.Sale(pszRetcode, pszRestInfo, pszRefnum, pszTrace, pszPan, pszCreditsPoint, pszGiftsTicket, serial, track2, cash, point, "", transType, PRINT_ORDER, psexpansivity, COM1, store, ticketList, "", psStoredCardsBalance, psIntegralOverall, memberPwd, POS_TYPE);
        LOGGER.info("#####YaZuoAPI Sale end result={}", retCode);
        String restInfo = byteToStr(pszRestInfo);
        if (RET_SUCCESS != retCode) {
            if (StringUtils.isEmpty(restInfo)) {
                restInfo = "雅座：消费失败";
            }
            result.setErrMsg(restInfo);
        } else {
            Map<String, String> data = new HashMap<>();
            data.put("pszRetcode", byteToStr(pszRetcode));
            data.put("pszRestInfo", restInfo);
            data.put("pszRefnum", byteToStr(pszRefnum));
            data.put("pszTrace", byteToStr(pszTrace));
            data.put("pszPan", byteToStr(pszPan));
            data.put("pszCreditsPoint", leftZeroToPrice(byteToStr(pszCreditsPoint)));
            data.put("pszGiftsTicket", leftZeroToPrice(byteToStr(pszGiftsTicket)));
            data.put("psStoredCardsBalance", leftZeroToPrice(byteToStr(psStoredCardsBalance)));
            data.put("psIntegralOverall", leftZeroToPrice(byteToStr(psIntegralOverall)));
            double psexpansivityD = 100000000;
            try {
                psexpansivityD = Double.parseDouble(byteToStr(psexpansivity));
            } catch (Exception e) {
            }
            data.put("psexpansivity", String.valueOf(psexpansivityD));
            result.setData(data);
        }
        return result;
    }

    /**
     * 雅座API初始化，登录、获取track2
     *
     * @return
     */
    private String[] getTrack2(String memberNo) {
        String[] result = new String[2];
        String login = login();
        if (null != login) {
            result[0] = String.valueOf(RET_FAILED);
            result[1] = login;
            return result;
        }
        /*本地查找会员二磁道信息 本地有则直接返回*/
        Member member = memberMapper.selectOneByNo(memberNo);
        if (null != member && !StringUtils.isEmpty(member.getTrack2())) {
            result[0] = RET_SUCCESS_STR;
            result[1] = member.getTrack2();
            return result;
        }
        byte[] track2 = createBytes(170);
        byte[] resultInfo = createBytes(50);
        LOGGER.info("#####YaZuoAPI GetTrack2 start memberNo={}", memberNo);
        int retCode = yazuo.GetTrack2(track2, memberNo, resultInfo, POS_TYPE);
        result[0] = String.valueOf(retCode);
        if (RET_SUCCESS != retCode) {
            result[1] = "未找到会员资料！";
        } else {
            result[1] = byteToStr(track2);
            /*本地缓存会员二磁道信息*/
            member = new Member();
            member.setMemberNo(memberNo);
            member.setTrack2(result[1]);
            memberMapper.insert(member);
        }
        LOGGER.info("#####YaZuoAPI GetTrack2 end memberNo={}  result={} data={}", memberNo, retCode, result[1]);
        return result;
    }

    /**
     * 重置密码
     *
     * @param track2 卡号
     * @param mobile 注册时的手机号，用于接收密码
     * @return
     */
    @Override
    public MemberAPIData resetPwd(String track2, String mobile) {
        return null;
    }

    /**
     * 修改密码
     *
     * @param track2 卡号
     * @param oldPwd 原密码
     * @param newPwd 新密码
     * @return
     */
    @Override
    public MemberAPIData changePwd(String track2, String oldPwd, String newPwd) {
        return null;
    }

    /**
     * 登录
     *
     * @return 成功返回null，失败返回错误信息
     */
    private String login() {
        //登录过期后重新登录
        if (0 == lastLogin || System.currentTimeMillis() - lastLogin > expiry_date) {
            LOGGER.info("#####YaZuoAPI Login start");
            int login = yazuo.Login(userId, userPwd, POS_TYPE);
            LOGGER.info("#####YaZuoAPI Login end result={}", login);
            if (RET_SUCCESS == login) {
                lastLogin = System.currentTimeMillis();
            } else {
                return "雅座:登录失败!(请检查服务器外网是否稳定)";
            }
        }
        return null;
    }

    /**
     * 卡激活
     *
     * @param mobile 会员号（手机号）
     * @param track2 卡号
     * @param pszPwd 密码
     * @return
     */
    @Override
    public MemberAPIData cardActive(String mobile, String track2, String pszPwd) {
        MemberAPIData result = new MemberAPIData();
        String loginResult = this.login();
        if (null != loginResult) {
            result.setErrMsg(loginResult);
            return result;
        }
        byte[] pszRestInfo = createBytes(50);
        LOGGER.info("#####YaZuoAPI CardActive start mobile={} track2={} pszPwd={}", mobile, track2, pszPwd);
        int ret = yazuo.CardActive(track2, pszPwd, mobile, pszRestInfo, POS_TYPE);
        String resultInfo = byteToStr(pszRestInfo);
        LOGGER.info("#####YaZuoAPI CardActive end resultInfo={} result", resultInfo, ret);
        if (RET_SUCCESS != ret) {
            if (StringUtils.isEmpty(resultInfo)) {
                resultInfo = "雅座:卡激活失败";
            }
            result.setErrMsg(resultInfo);
        } else {
            //激活成功后清除卡号缓存
            memberMapper.deleteByNo(mobile);
            Map<String, String> data = new HashMap<>();
            data.put("pszRestInfo", resultInfo);
            result.setData(data);
        }
        return result;
    }

    private byte[] createBytes(int size) {
        return new byte[size + 10];
    }

    /**
     * byte数组转字符串
     *
     * @param bytes
     * @return
     */
    private String byteToStr(byte[] bytes) {
        return new String(bytes, 0, getBytesLen(bytes), CHARSET);
    }

    /**
     * 获取byte数组真实有效长度
     *
     * @param bytes
     * @return
     */
    private int getBytesLen(byte[] bytes) {
        int i = 0;
        for (; i < bytes.length; i++) {
            if (bytes[i] == 0) {
                return i;
            }
        }
        return i;
    }

    private String priceToLeftZero(String amount, int length) {
        float amountFloat = 0;
        try {
            amountFloat = Float.parseFloat(amount);
        } catch (Exception e) {
            amountFloat = 0;
        }
        int amountInt = (int) (amountFloat * 100);
        return String.format("%0" + length + "d", amountInt);
    }

    private String leftZeroToPrice(String price) {
        try {
            return Double.parseDouble(price) / 100 + "";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 处理serial
     *
     * @param serial
     * @return
     */
    private String getYazuoSerial(String serial) {
        if (serial.length() > 16) {
            String preSerail = serial.substring(0, 9);
            return preSerail + serial.substring(serial.length() - 5);
        }
        return serial;
    }
}
