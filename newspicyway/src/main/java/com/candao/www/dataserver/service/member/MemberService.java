package com.candao.www.dataserver.service.member;

import com.candao.www.webroom.model.MemberTransModel;

/**
 * 会员业务接口
 * Created by lenovo on 2016/3/21.
 */
public interface MemberService {
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
     * 取消会员价
     *
     * @param userId 员工号
     * @param orderId 账单号
     * @return
     */
    String revertMemberPrice(String userId, String orderId);

    /**
     * 设置会员价
     * @param userId
     * @param orderId
     * @param ip
     * @return
     */
    String revertMemberPrice2(String userId, String orderId, String ip);
    /**
     * 会员储值
     * @param memberTrans
     * @return
     */
    String StoreCardDeposit(String memberTrans);
}
