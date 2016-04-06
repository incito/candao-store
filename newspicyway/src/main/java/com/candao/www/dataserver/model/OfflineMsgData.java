package com.candao.www.dataserver.model;

/**
 * Created by ytq on 2016/3/24.
 */
public class OfflineMsgData {
    private Integer id;
    private String content;

    public OfflineMsgData(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
