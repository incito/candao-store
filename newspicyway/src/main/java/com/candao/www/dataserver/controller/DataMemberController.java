package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.member.MemberService;
import org.apache.commons.lang.StringEscapeUtils;
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
public class DataMemberController {
    private static final Logger logger = LoggerFactory.getLogger(DataMemberController.class);
    @Autowired
    private MemberService memberService;


    /**
     * 设置会员价
     *
     * @param userId
     * @param orderId
     * @param ip
     * @param memberNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setMemberPrice/{userId}/{orderId}/{ip}/{memberNo}", produces = {"application/text;charset=UTF-8"})
    public String setMemberPrice(@PathVariable String userId, @PathVariable String orderId, @PathVariable String ip, @PathVariable String memberNo) {
        logger.info("###REQUEST### MemberController setMemberPrice userId={}  orderId={} ip={} memberNo={}", userId, orderId, ip, memberNo);
        String result = memberService.setMemberPrice(userId, orderId, memberNo);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        logger.info("###RESPONSE### MemberController setMemberPrice response={}", result);
        return result;
    }

    /**
     * 取消会员价
     *
     * @param userId
     * @param orderId
     * @param ip
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setMemberPrice3/{userId}/{orderId}/{ip}", produces = {"application/text;charset=UTF-8"})
    public String setMemberPrice3(@PathVariable String userId, @PathVariable String orderId, @PathVariable String ip) {
        logger.info("###REQUEST### MemberController setMemberPrice3 userId={}  orderId={} ip={}", userId, orderId, ip);
        String result = memberService.revertMemberPrice(userId, orderId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        logger.info("###RESPONSE### MemberController setMemberPrice3 response={}", result);
        return result;
    }

    /**
     * 取消会员价2
     *
     * @param userId
     * @param orderId
     * @param ip
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setMemberPrice2/{userId}/{orderId}/{ip}", produces = {"application/text;charset=UTF-8"})
    public String setMemberPrice2(@PathVariable String userId, @PathVariable String orderId, @PathVariable String ip) {
        logger.info("###REQUEST### MemberController setMemberPrice2 userId={}  orderId={} ip={}", userId, orderId, ip);
        String result = memberService.revertMemberPrice2(userId, orderId, ip);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        logger.info("###RESPONSE### MemberController setMemberPrice2 response={}", result);
        return result;
    }

}
