package com.candao.www.printer.v2;

/**
 * 封装打印数据
 * Created by liaoy on 2016/10/31.
 */
public class PrintData<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "";
    }
}
