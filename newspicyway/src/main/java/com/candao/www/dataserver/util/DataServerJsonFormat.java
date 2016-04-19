package com.candao.www.dataserver.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.candao.www.dataserver.model.ResponseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/4/14.
 */
public class DataServerJsonFormat {
    public static String jsonFormat(Object object, String repStr) {
        String jsonStr = "{\"Data\":" + JSON.toJSONString(object, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero) + "}";
        return jsonStr.replace("\"", repStr);
    }

    public static String jsonFormat(Object object) {
        return "{\"Data\":" + JSON.toJSONString(object, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero) + "}";
    }

    public static void main(String[] args) {
        List<Map> mapList = new ArrayList<>();
        Map map = new HashMap();
        Map map1 = new HashMap();
        map.put("131", 2132);
        map1.put("132", null);
        mapList.add(map);
        mapList.add(map1);
        System.out.println(DataServerJsonFormat.jsonFormat(mapList, "|"));
        System.out.println(DataServerJsonFormat.jsonFormat(new ResponseData(), "|"));
        System.out.println(DataServerJsonFormat.jsonFormat(mapList));
    }
}
