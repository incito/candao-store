package com.candao.www.dataserver.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by ytq on 2016/4/14.
 */
public class ResultData {
    private String result;

    public ResultData(String result) {
        this.result = "[" + result + "]";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static void main(String[] args) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        System.out.println(JSON.toJSON(new ResultData(JSON.toJSONString(responseJsonData))));
    }
}
