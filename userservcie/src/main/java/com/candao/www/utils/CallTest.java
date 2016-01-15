package com.candao.www.utils;

import java.util.HashMap;
import java.util.Map;

public class CallTest{

    public static void main(String[] args) throws Exception {
        
        /* Post Request */
        Map dataMap = new HashMap();
        dataMap.put("id", "1");
        System.out.println(new HttpRequestor().doPost("http://localhost:8080/admin/category/selectisExistChild", dataMap));
        
        /* Get Request */
        System.out.println(new HttpRequestor().doGet("http://localhost:8080/admin/category/selectisExistChild?id=1"));
    }

}