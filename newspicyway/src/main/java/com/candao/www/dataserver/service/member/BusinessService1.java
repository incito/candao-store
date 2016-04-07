package com.candao.www.dataserver.service.member;

/**
 * Created by lenovo on 2016/4/5.
 */
public interface BusinessService1 {
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
     * @param userId 员工号
     * @param userName 员工姓名
     * @param ip IP地址
     * @param posId POS编号
     * @param authorizer 清机授权人
     * @return
     */
    String clearMachine(String userId,String userName,String ip,String posId,String authorizer);
}
