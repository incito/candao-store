package com.candao.www.printer.v2;

/**
 * 打印结果
 * Created by liaoy on 2016/6/12.
 */
public class PrintResult {
    /**
     * 打印结果代码，打印机返回。
     */
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
