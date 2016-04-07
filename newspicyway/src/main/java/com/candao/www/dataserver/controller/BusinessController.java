package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.member.BusinessService;
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
public class BusinessController {
    private static final Logger logger = LoggerFactory.getLogger(BusinessController.class);
    @Autowired
    private BusinessService businessService;


    /**
     * 3、获取帐单列表
     *
     * @param userId  员工号
     * @param orderId 帐单号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/GetServerTableList/{orderId}/{userId}", produces = {"application/text;charset=UTF-8"})
    public String setMemberPrice(@PathVariable String userId, @PathVariable String orderId) {
        logger.info("###REQUEST### member GetServerTableList userId={}  orderId={}", userId, orderId);
        String s = businessService.getServerTableList(userId, orderId);
        logger.info("###RESPONSE### member GetServerTableList response={}", s);
        return s;
    }

    /**
     * 开业和是否开业接口
     *
     * @param userId   员工号
     * @param userPwd  员工密码
     * @param ip       IP地址
     * @param callType 0 检查有没有开业(检查用户名和密码可填空) 1 开业
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/OpenUp/{userId}/{userPwd}/{ip}/{callType}", produces = {"application/text;charset=UTF-8"})
    public String openUp(@PathVariable String userId, @PathVariable String userPwd, @PathVariable String ip, @PathVariable Integer callType) {
        logger.info("###REQUEST### member OpenUp userId={}  userPwd={} ip={} callType={}", userId, userPwd, ip, callType);
        String result;
        if (null == callType || 0 == callType) {
            result = businessService.checkOpen();
        } else {
            result = businessService.open(userId, userPwd, ip);
        }
        logger.info("###RESPONSE### member OpenUp response={}", result);
        return result;
    }

    /**
     * 保存优惠内容
     * @param userId 员工号
     * @param ip IP地址
     * @param orderId 帐单号
     * @param preferential 已选优惠json数组
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrderPreferential/{userId}/{ip}/{orderId}/{preferential}", produces = {"application/text;charset=UTF-8"})
    public String saveOrderPreferential(@PathVariable String userId, @PathVariable String ip, @PathVariable String orderId, @PathVariable String preferential) {
        logger.info("###REQUEST### member saveOrderPreferential userId={} ip={} orderId={} preferential={}", userId, ip, orderId, preferential);
        String result = businessService.saveOrderPreferential(userId, orderId, preferential);
        logger.info("###RESPONSE### member saveOrderPreferential response={}", result);
        return result;
    }

}
