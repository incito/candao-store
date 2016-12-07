package com.candao.www.utils;

import java.util.List;
import java.util.Map;

import com.candao.www.constant.Constant;

public class ServiceChargeDescUnit {
   
	  /**
     * 处理服务费说明
     * @param list
     */
    public static void handleServiceCharge(List<Map<String, Object>> list) {
    	StringBuffer sb = new StringBuffer();
        for(Map<String, Object> table :list){
            Object chargeOn=table.get("chargeOn");
            short chargeOnShort = Short.valueOf(chargeOn.toString());
            if(chargeOnShort == Constant.SERVICE_CHARGE_ON.OFF){
                continue;
            }
            Object chargeType=table.get("chargeType");
            if(null==chargeType){
                continue;
            }
            switch (Short.valueOf(chargeType.toString()).shortValue()){
                case Constant.SERVICE_CHARGE_TYPE.RATE:
                    Object chargeRate = table.get("chargeRate");
                    if(null!=chargeRate) {
                    	sb.delete(0, sb.length());
                    	sb.append("按菜品价格比例");
                    	sb.append(chargeRate);
                    	sb.append("%收取服务费");
                    	sb.append("#");
                    	sb.append("the service fee is ");
                    	sb.append(chargeRate);
                    	sb.append("% of the food price");
                        table.put("chargetDesc", sb.toString());
                    }
                    break;
                case Constant.SERVICE_CHARGE_TYPE.CONST:
                    Object chargeAmount = table.get("chargeAmount");
                    if(null!=chargeAmount) {
                    	sb.delete(0, sb.length());
                    	sb.append("收取固定服务费￥");
                    	sb.append(chargeAmount);
                    	sb.append("#");
                    	sb.append(" a fixed service fee of ￥");
                    	sb.append(chargeAmount);
                    	sb.append(" will be charged");
                        table.put("chargetDesc", sb.toString());
                    }
                    break;
                case Constant.SERVICE_CHARGE_TYPE.TIME:
                    Object chargeTime = table.get("chargeTime");
                    if(null!=chargeTime) {
                      	sb.delete(0, sb.length());
                    	sb.append("按用餐时长收取服务费￥");
                    	sb.append(table.get("chargeAmount"));
                    	sb.append("/");
                    	sb.append(chargeTime);
                    	sb.append("分钟");
                    	sb.append("#");
                    	sb.append("the service fee will be charged according to the meal time￥");
                    	sb.append(table.get("chargeAmount"));
                    	sb.append(" for ");
                    	sb.append(chargeTime);
                    	sb.append("minutes");
                        table.put("chargetDesc", sb.toString());
                    }
                    break;
            }
        }
    }
}
