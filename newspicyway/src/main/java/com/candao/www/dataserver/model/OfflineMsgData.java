package com.candao.www.dataserver.model;

import com.candao.www.dataserver.util.MsgAnalyzeTool;

/**
 * Created by ytq on 2016/3/24.
 */
public class OfflineMsgData {
    private String offLineMsgId;
    private String content;

    public OfflineMsgData() {

    }

    public OfflineMsgData(String offLineMsgId, String content) {
        this.offLineMsgId = offLineMsgId;
        this.content = content;
    }

    public String getOffLineMsgId() {
        return offLineMsgId;
    }

    public void setOffLineMsgId(String offLineMsgId) {
        this.offLineMsgId = offLineMsgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
