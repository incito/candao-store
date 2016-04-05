package com.candao.www.dataserver.service.member;

import com.candao.www.dataserver.model.MemberAPIData;

/**
 * Created by lenovo on 2016/3/23.
 */
public interface MemberCard {
    /**
     * 业务执行结果 成功
     */
    public static final int RET_SUCCESS = 0;
    /**
     * 业务执行结果 超时
     */
    public static final int RET_TIMEOUT = 1;
    /**
     * 业务执行结果 失败
     */
    public static final int RET_FAILED = 2;
    /**
     * 业务执行结果 成功 字符串形式
     */
    public static final String RET_SUCCESS_STR = "0";

    MemberAPIData queryBanlance(String memberNo);

    MemberAPIData voidSale(String memberNo, String pszTrace, String serial, String userPwd, String adminPwd);

    MemberAPIData storeCardDeposit(String memberNo, String amount, String serial, String transType, String payType);

    MemberAPIData memberSale(String memberNo, String serial, String cash, String point, String transType, String store, String ticketList, int memberPwd, String memberyhqamount);

    MemberAPIData cardActive(String mobile, String track2, String pszPwd);

    MemberAPIData resetPwd(String track2, String mobile);

    MemberAPIData changePwd(String track2, String oldPwd, String newPwd);
}
