package com.candao.www.dataserver.model;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ytq on 2016/4/14.
 */
public class ResultData {
    private List<String> result = new ArrayList<>();

    public ResultData(String resultData) {
        result.add(resultData);
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public static void main(String[] args) {
        //{"result":["{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}"]}
        //{"result":["{\"Data\":\"1\",\"Info\":\"\",\"workdate\":\"\"}"]}
        //{"result":["{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}"]}
        //{"result":["{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}"]}
        ResponseJsonData responseJsonData = new ResponseJsonData();
        System.out.println(JSON.toJSONString(new ResultData(JSON.toJSONString(responseJsonData))));
    }
}
