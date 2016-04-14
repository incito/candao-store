package com.candao.www.dataserver.service.member;

import java.util.Date;

/**
 * Created by lenovo on 2016/4/5.
 */
public interface BusinessService {
    /**
     * 获取帐单列表
     *
     * @param userId  员工号
     * @param orderId 帐单号
     * @return
     */
    String getServerTableList(String userId, String orderId);

    /**
     * 检查是否开业
     *
     * @return
     */
    String checkOpen();

    /**
     * 开业
     *
     * @param userId  员工号
     * @param userPwd 员工密码
     * @param ip      IP地址
     * @return
     */
    String open(String userId, String userPwd, String ip);

    /**
     * 保存优惠内容
     *
     * @param userId       员工号
     * @param orderId      帐单号
     * @param preferential 已选优惠json数组
     * @return
     */
    String saveOrderPreferential(String userId, String orderId, String preferential);

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
    String clearMachine(String userId, String userName, String ip, String posId, String authorizer);

    /**
     * 结业
     *
     * @param userId 员工号
     * @param ip     IP地址
     * @return
     */
    String endWork(String userId, String ip);

    /**
     * 检查是否输入找零金
     *
     * @param ip
     * @return
     */
    String checkTellerCash(String ip, String userId);

    /**
     * 输入找零金
     *
     * @param userId     员工号
     * @param ip         IP地址
     * @param cashAmount 备用金金额
     * @return
     */
    String inputTellerCash(String userId, String ip, float cashAmount);

    /**
     * 挂单
     *
     * @param tableNo    桌号
     * @param orderId    帐单号
     * @param gzCode     挂帐单位编号
     * @param gzName     挂帐单位
     * @param telephone  单位电话
     * @param relaperson 联系人
     * @return
     */
    String putOrder(String tableNo, String orderId, String gzCode, String gzName, String telephone, String relaperson);

    /**
     * 获取下单序号
     *
     * @param tableNo 桌号
     * @return
     */
    String getOrderSequence(String tableNo);

    /**
     * 获取帐单信息结口
     *
     * @param tableNo
     * @param userId
     * @return
     */
    String getServerTableInfo(String tableNo, String userId);

    /**
     * //合并 GetServerTableInfo GetServerTableList setMemberPrice3 为一个接口，加快速度
     *
     * @param tableNo
     * @param userId
     * @return
     */
    String getOrder(String tableNo, String userId);

    String getServerTableInfo2(String tableNo, String userId);

    String getServerTableList2(String orderId, String userId);

    Date getOpenDate();
}
