package com.candao.www.dataserver.service.member;

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
}
