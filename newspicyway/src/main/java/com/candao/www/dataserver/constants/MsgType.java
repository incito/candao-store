package com.candao.www.dataserver.constants;

public enum MsgType {
    CLEANING("01", "沽清消息"), NORMAL("02", "普通消息");
    private String value;
    private String text;

    MsgType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
