package com.candao.www.dataserver.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ytq on 2016/3/18.
 */
public class WatchLoginData extends BaseData {
    @JSONField(name = "userid")
    private String userId;
    @JSONField(name = "meid")
    private String meId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMeId() {
        return meId;
    }

    public void setMeId(String meId) {
        this.meId = meId;
    }
}
