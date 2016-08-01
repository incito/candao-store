package com.candao.www.dataserver.service.msghandler.obj;

/**
 * Created by liaoy on 2016/7/26.
 */
public class Result<E> {
    private boolean success = true;
    private String msg;
    private E data;

    public Result() {
    }

    public Result(boolean success, String msg, E data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
