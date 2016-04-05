package com.candao.www.dataserver.entity;

/**
 * 会员实体
 * Created by lenovo on 2016/3/28.
 */
public class Member {
    private int id; //ID
    private String memberNo;//会员号（手机）
    private String track2;//第二磁道信息（卡号，多个卡号以逗号隔开）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }
}
