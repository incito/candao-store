package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.member.BusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
        logger.info("###REQUEST### BusinessController GetServerTableList userId={}  orderId={}", userId, orderId);
        String s = businessService.getServerTableList(userId, orderId);
        logger.info("###RESPONSE### BusinessController GetServerTableList response={}", s);
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
    @RequestMapping(value = "/OpenUp/{userId}/{userPwd:\\d+|\\s+]}/{ip}/{callType}", produces = {"application/text;charset=UTF-8"})
    public String openUp(@PathVariable String userId, @PathVariable String userPwd, @PathVariable String ip, @PathVariable Integer callType) {
        logger.info("###REQUEST### BusinessController OpenUp userId={}  userPwd={} ip={} callType={}", userId, userPwd, ip, callType);
        String result;
        if (null == callType || 0 == callType) {
            result = businessService.checkOpen();
        } else {
            result = businessService.open(userId, userPwd, ip);
        }
        logger.info("###RESPONSE### BusinessController OpenUp response={}", result);
        return result;
    }

    /**
     * 保存优惠内容
     *
     * @param userId       员工号
     * @param ip           IP地址
     * @param orderId      帐单号
     * @param preferential 已选优惠json数组
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrderPreferential/{userId}/{ip}/{orderId}/{preferential}", produces = {"application/text;charset=UTF-8"})
    public String saveOrderPreferential(@PathVariable String userId, @PathVariable String ip, @PathVariable String orderId, @PathVariable String preferential) {
        logger.info("###REQUEST### BusinessController saveOrderPreferential userId={} ip={} orderId={} preferential={}", userId, ip, orderId, preferential);
        String result = businessService.saveOrderPreferential(userId, orderId, preferential);
        logger.info("###RESPONSE### BusinessController saveOrderPreferential response={}", result);
        return result;
    }

    /**
     * 清机
     *
     * @param userId     员工号
     * @param userName   员工姓名
     * @param ip         IP地址
     * @param posId      POS编号
     * @param authorizer 清机授权人
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clearMachine/{userId}/{userName}/{ip}/{posId}/{authorizer}", produces = {"application/text;charset=UTF-8"})
    public String clearMachine(@PathVariable String userId, @PathVariable String userName, @PathVariable String ip, @PathVariable String posId, @PathVariable String authorizer) {
        logger.info("###REQUEST### BusinessController clearMachine userId={} userName={} ip={} posId={} authorizer={}", userId, userName, ip, posId, authorizer);
        String result = businessService.clearMachine(userId, userName, ip, posId, authorizer);
        logger.info("###RESPONSE### BusinessController clearMachine response={}", result);
        return result;
    }

    /**
     * 结业
     *
     * @param userId 员工号
     * @param ip     IP地址
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/endWork/{userId}/{ip}", produces = {"application/text;charset=UTF-8"})
    public String endWork(@PathVariable String userId, @PathVariable String ip) {
        logger.info("###REQUEST### BusinessController endWork userId={} userName={} ip={} posId={} authorizer={}", userId, ip);
        String result = businessService.endWork(userId, ip);
        logger.info("###RESPONSE### BusinessController endWork response={}", result);
        return result;
    }

    /**
     * 输入零找金
     *
     * @param userId     员工号
     * @param ip         IP地址
     * @param cachAmount 备用金金额
     * @param callType   0 检查有没有输入过备用金 1输入备用金
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/InputTellerCash/{userId}/{ip}/{cachAmount}/{callType}", produces = {"application/text;charset=UTF-8"})
    public String inputTellerCash(@PathVariable String userId, @PathVariable String ip, @PathVariable Float cachAmount, @PathVariable Integer callType) {
        logger.info("###REQUEST### BusinessController InputTellerCash userId={} ip={} cachAmount={} callType={}", userId, ip, cachAmount, callType);
        String result;
        if (callType == null || callType == 0) {
            result = businessService.checkTellerCash(ip);
        } else {
            result = businessService.inputTellerCash(userId, ip, cachAmount);
        }
        logger.info("###RESPONSE### BusinessController InputTellerCash response={}", result);
        return result;
    }

    /**
     * 挂单
     *
     * @param tableNo 桌号
     * @param orderId 帐单号
     * @param gzCode  挂帐单位编号
     * @param gzName  挂帐单位
     * @param gzTele  单位电话
     * @param gzUser  联系人
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/putOrder/{tableNo}/{orderId}/{gzCode}/{gzName}/{gzTele}/{gzUser}", produces = {"application/text;charset=UTF-8"})
    public String putOrder(@PathVariable String tableNo, @PathVariable String orderId, @PathVariable String gzCode, @PathVariable String gzName, @PathVariable String gzTele, @PathVariable String gzUser) {
        logger.info("###REQUEST### BusinessController putOrder tableNo={} orderId={} gzCode={} gzName={} gzTele={} gzUser={}", tableNo, orderId, gzCode, gzName, gzTele, gzUser);
        String result = businessService.putOrder(tableNo, orderId, gzCode, gzName, gzTele, gzUser);
        logger.info("###RESPONSE### BusinessController putOrder response={}", result);
        return result;
    }

    /**
     * 获取下单序号
     * <br>
     * POS下单前需要获取下一个下单序号才能下单成功
     *
     * @param tableNo 桌号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderSequence/{tableNo}", produces = {"application/text;charset=UTF-8"})
    public String getOrderSequence(@PathVariable String tableNo) {
        logger.info("###REQUEST### BusinessController getOrderSequence tableNo={}", tableNo);
        String result = businessService.getOrderSequence(tableNo);
        logger.info("###RESPONSE### BusinessController getOrderSequence response={}", result);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/GetOrder/{tableNo}/{userId}", produces = {"application/text;charset=UTF-8"})
    public String getOrder(@PathVariable String tableNo, @PathVariable String userId) {
        logger.info("###REQUEST### BusinessController GetOrder tableNo={} userId={}", tableNo, userId);
        String result = businessService.getOrder(tableNo, userId);
        logger.info("###RESPONSE### BusinessController GetOrder response={}", result);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/GetServerTableInfo/{tableNo}/{userId}/", produces = {"application/text;charset=UTF-8"})
    public String getServerTableInfo(@PathVariable String tableNo, @PathVariable String userId) {
        logger.info("###REQUEST### BusinessController getServerTableInfo tableNo={} userId={}", tableNo, userId);
        String result = businessService.getServerTableInfo(tableNo, userId);
        logger.info("###RESPONSE### BusinessController getServerTableInfo response={}", result);
        return result;
    }

    public static void main(String[] args) {
        String str= "/werwer/werwer/ /werwer";
        String[] a=StringUtils.tokenizeToStringArray(str,"/",false,true);
        System.out.println();
    }
}
