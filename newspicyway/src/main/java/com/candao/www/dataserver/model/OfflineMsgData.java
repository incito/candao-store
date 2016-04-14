package com.candao.www.dataserver.model;

import com.candao.www.dataserver.util.MsgAnalyzeTool;

/**
 * Created by ytq on 2016/3/24.
 */
public class OfflineMsgData {
    private Integer offLineMsgId;
    private String content;

    public OfflineMsgData() {

    }

    public OfflineMsgData(Integer offLineMsgId, String content) {
        this.offLineMsgId = offLineMsgId;
        this.content = content;
    }

    public Integer getOffLineMsgId() {
        return offLineMsgId;
    }

    public void setOffLineMsgId(Integer offLineMsgId) {
        this.offLineMsgId = offLineMsgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
