package com.candao.www.dataserver.service.member;

import com.candao.www.dataserver.model.ConsumerData;
import com.candao.www.dataserver.model.RechargeData;

/**
 * 会员业务接口
 * Created by lenovo on 2016/3/21.
 */
public interface MemberService {
    /**
     * 查询会员余额(登录)
     *
     * @param memberNo 会员手机号或卡号
     * @return
     */
    String queryBalance(String memberNo);

    /**
     * 会员取消上一笔交易
     *
     * @param orderId  帐单号
     * @param userPwd  会员密码
     * @param adminPwd 管理员密码 111111
     * @return
     */
    String VoidSale(String orderId, String userPwd, String adminPwd);

    /**
     * 会员储值
     *
     * @return
     */
    String storeCardDeposit(RechargeData recharge);

    /**
     * 会员消费
     *
     * @param consumer
     * @return
     */
    String memberSale(ConsumerData consumer);

    /**
     * 设置会员价
     *
     * @param userId   员工号
     * @param orderId  账单号
     * @param memberNo 会员号(手机或者卡号)
     * @return
     */
    String setMemberPrice(String userId, String orderId, String memberNo);

    /**
     * 卡激活
     *
     * @param mobile 会员号（手机号）
     * @param track2 卡号(第二磁道)
     * @param pwd    密码，默认000000
     * @return
     */
    String cardActive(String mobile, String track2, String pwd);

    /**
     * PAD查询会员信息(PAD登录)
     *
     * @param input   手机或者卡号
     * @param orderId 订单号
     * @return
     */
    String queryBalanceForPad(String input, String orderId);

}
