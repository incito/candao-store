package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.model.ConsumerData;
import com.candao.www.dataserver.model.RechargeData;
import com.candao.www.dataserver.service.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 会员业务
 */
@Controller
@RequestMapping("/datasnap/rest/TServerMethods1")
public class DataServerMemberController {
    private static final Logger logger = LoggerFactory.getLogger(DataServerMemberController.class);
    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/QueryBalance/{memberNo}", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String queryBalance(@PathVariable String memberNo) {
        logger.info("###REQUEST### member QueryBalance memberNo={}", memberNo);
        String s = memberService.queryBalance(memberNo);
        logger.info("###RESPONSE### member QueryBalance response={}", s);
        return s;
    }

    /**
     * 消费撤销
     *
     * @param orderId
     * @param pszPwd
     * @param pszGPwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/VoidSale/{orderId}/{pszPwd}/{pszGPwd}", produces = {"application/text;charset=UTF-8"})
    public String voidSale(@PathVariable String orderId, @PathVariable String pszPwd, @PathVariable String pszGPwd) {
        logger.info("###REQUEST### member VoidSale orderId={}  pszPwd={} pszGPwd={}", orderId, pszPwd, pszGPwd);
        String s = memberService.VoidSale(orderId, pszPwd, pszGPwd);
        logger.info("###RESPONSE### member VoidSale response={}", s);
        return s;
    }

    /**
     * 会员储值
     *
     * @param userId
     * @param memberNo
     * @param pszAmount
     * @param pszSerial
     * @param psTransType
     * @param paytype
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/StoreCardDeposit/{userId}/{memberNo}/{pszAmount}/{pszSerial}/{psTransType}/{paytype}", produces = {"application/text;charset=UTF-8"})
    public String storeCardDeposit(@PathVariable String userId, @PathVariable String memberNo, @PathVariable String pszAmount,
                                   @PathVariable String pszSerial, @PathVariable String psTransType, @PathVariable String paytype) {
        logger.info("###REQUEST### member StoreCardDeposit userId={}  memberNo={} pszAmount={} pszSerial={} psTransType={} paytype={}", userId, memberNo, pszAmount, pszSerial, psTransType, paytype);
        RechargeData recharge = new RechargeData();
        recharge.setUserId(userId);
        recharge.setMemberNo(memberNo);
        recharge.setAmount(pszAmount);
        recharge.setSerial(pszSerial);
        recharge.setTransType(psTransType);
        recharge.setPayType(paytype);
        String s = memberService.storeCardDeposit(recharge);
        logger.info("###RESPONSE### member StoreCardDeposit response={}", s);
        return s;
    }

    /**
     * 会员消费
     *
     * @param aUserId
     * @param orderId
     * @param pszInput
     * @param pszSerial
     * @param pszCash
     * @param pszPoint
     * @param psTransType
     * @param pszStore
     * @param pszTicketList
     * @param pszPwd
     * @param memberyhqamount
     * @param server
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/Sale/{aUserId}/{orderId}/{pszInput}/{pszSerial}/{pszCash}/{pszPoint}/{psTransType}/{pszStore}/{pszTicketList}/{pszPwd}/{memberyhqamount}/{server}", produces = {"application/text;charset=UTF-8"})
    public String memberSale(@PathVariable String aUserId, @PathVariable String orderId, @PathVariable String pszInput,
                             @PathVariable String pszSerial, @PathVariable String pszCash, @PathVariable String pszPoint,
                             @PathVariable String psTransType, @PathVariable String pszStore, @PathVariable String pszTicketList,
                             @PathVariable String pszPwd, @PathVariable String memberyhqamount, @PathVariable String server) {
        logger.info("###REQUEST### member MemberSale aUserId={}  orderId={} pszInput={} " +
                        "pszSerial={} pszCash={} pszPoint={} psTransType={} pszStore={} " +
                        "pszTicketList={} pszPwd={} memberyhqamount={} server={}",
                aUserId, orderId, pszInput, pszSerial, pszCash, pszPoint, psTransType, pszStore, pszTicketList, pszPwd, memberyhqamount, server);
        ConsumerData consumer = new ConsumerData();
        consumer.setUserId(aUserId);
        consumer.setOrderId(orderId);
        consumer.setMemberNo(pszInput);
        consumer.setSerail(pszSerial);
        consumer.setCash(pszCash);
        consumer.setPoint(pszPoint);
        consumer.setTransType(psTransType);
        consumer.setStore(pszStore);
        consumer.setTicketList(pszTicketList);
        consumer.setMemberPwd(pszPwd);
        consumer.setMemberyhqamount(memberyhqamount);
        consumer.setServer(server);
        String s = memberService.memberSale(consumer);
        logger.info("###RESPONSE### member MemberSale response={}", s);
        return s;
    }

    /**
     * 会员激活
     *
     * @param track2
     * @param pwd
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/CardActive/{track2}/{pwd}/{mobile}", produces = {"application/text;charset=UTF-8"})
    public String cardActive(@PathVariable String track2, @PathVariable String pwd, @PathVariable String mobile) {
        logger.info("###REQUEST### member CardActive track2={}  pwd={} mobile={} ", track2, pwd, mobile);
        String s = memberService.cardActive(mobile, track2, pwd);
        logger.info("###RESPONSE### member CardActive response={}", s);
        return s;
    }

    /**
     * PAD端会员查询/登录
     *
     * @param input
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/QueryBalance3/{input}/{orderId}", produces = {"application/text;charset=UTF-8"})
    public String queryBalanceForPad(@PathVariable String input, @PathVariable String orderId) {
        logger.info("###REQUEST### member QueryBalance3 input={}  orderId={}", input, orderId);
        String s = memberService.queryBalanceForPad(input, orderId);
        logger.info("###RESPONSE### member QueryBalance3 response={}", s);
        return s;
    }

//    @ResponseBody
//    @RequestMapping(value = "/setMemberPrice/{userId}/{orderId}/{ip}/{memberNo}", produces = {"application/text;charset=UTF-8"})
//    public String setMemberPrice(@PathVariable String userId, @PathVariable String orderId, @PathVariable String ip, @PathVariable String memberNo) {
//        logger.info("###REQUEST### member setMemberPrice userId={}  orderId={} ip={} memberNo={}", userId, orderId,ip,memberNo);
//        String s = memberService.queryBalanceForPad(input, orderId);
//        logger.info("###RESPONSE### member setMemberPrice response={}", s);
//        return s;
//    }

}
