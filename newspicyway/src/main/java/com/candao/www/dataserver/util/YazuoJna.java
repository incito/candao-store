package com.candao.www.dataserver.util;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 雅座JNA映射dll接口
 * Created by lenovo on 2016/3/22.
 */
public interface YazuoJna extends Library {
    YazuoJna instanceDll = (YazuoJna) Native.loadLibrary("YazuoCashier", YazuoJna.class);

    public int Login(String userId,
                     String userPwd,
                     int nPosType);

    public int GetTrack2(byte[] pszTrack2,
                         String memberNo,
                         byte[] pszRestInfo,
                         int nPosType);

    public int ResetPwd(String pszTrack2,
                        String pszSendPwdPhone,
                        byte[] pszRestInfo, int nPosType);

    public int Logout(int nPosType);

    public int Sale(byte[] pszRetcode,
                    byte[] pszRestInfo,
                    byte[] pszRefnum,
                    byte[] pszTrace,
                    byte[] pszPan,
                    byte[] pszCreditsPoint,
                    byte[] pszGiftsTicket,
                    String pszSerial,
                    String pszTrack2,
                    String pszCash,
                    String pszPoint,
                    String pszCoupons,
                    String psTransType,
                    String psPrintOrder,
                    byte[] psexpansivity,
                    String pszCom,
                    String pszStore,
                    String pszTicketList,
                    String psDishesList,
                    byte[] psStoredCardsBalance,
                    byte[] psIntegralOverall,
                    int Pwd,
                    int nPosType);

    public int QueryBalance(byte[] pszRetcode,
                            byte[] pszRestInfo,
                            byte[] pszRefnum,
                            byte[] pszTrace,
                            byte[] pszPan,
                            byte[] psIntegralOverall,
                            byte[] psIntegralAvail,
                            byte[] psCouponsOverall,
                            byte[] psCouponsAvail,
                            byte[] psStoredCardsBalance,
                            String pszTrack2,
                            String pszCom,
                            byte[] psTicketInfo,
                            byte[] psCardType,
                            byte[] pszMobile,
                            byte[] pszName,
                            byte[] pszGender,
                            byte[] pszBirthday,
                            byte[] pszJoindate,
                            byte[] pszEmail,
                            byte[] pszAddress,
                            byte[] pszdescription,
                            int nPosType);

    public int VoidSale(byte[] pszRetcode,
                        byte[] pszRestInfo,
                        byte[] pszRefnum,
                        byte[] pszTrace,
                        byte[] pszPan,
                        byte[] pszCreditsPoint,
                        byte[] pszCreditsGifts,
                        String pszSerial,
                        String pszPwd,
                        String pszGPwd,
                        String pszCoupons,
                        String pszTrack2,
                        String psPrintOrder,
                        byte[] psexpansivity,
                        String pszCom,
                        byte[] psStoredCardsBalance,
                        byte[] pszTicketList,
                        byte[] psIntegralOverall,
                        byte[] pszStore,
                        int nPosType);

    public int StoreCardDeposit(byte[] pszRetcode,
                                byte[] pszRestInfo,
                                byte[] pszRefnum,
                                byte[] pszTrace,
                                byte[] pszPan,
                                byte[] psStoreCardBalance,
                                String pszTrack2,
                                String pszAmount,
                                String pszSerial,
                                String psTransType,
                                String psPrintOrder,
                                String pszCom,
                                int nPosType);

    public int ChangePwd(String pszTrack2,
                         String pszOldpwd,
                         String pszNewpwd,
                         byte[] pszRestInfo,
                         int nPosType);

    public int CardActive(String pszTrack2,
                          String pszPwd,
                          String pszMobile,
                          byte[] pszRestInfo,
                          int nPosType);
}
