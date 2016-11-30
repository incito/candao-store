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
                        table.put("chargetDesc", "按菜品价格比例"+chargeRate+"%收取服务费");
                    }
                    break;
                case Constant.SERVICE_CHARGE_TYPE.CONST:
                    Object chargeAmount = table.get("chargeAmount");
                    if(null!=chargeAmount) {
                        table.put("chargetDesc", "收取固定服务费￥"+chargeAmount);
                    }
                    break;
                case Constant.SERVICE_CHARGE_TYPE.TIME:
                    Object chargeTime = table.get("chargeTime");
                    if(null!=chargeTime) {
                        table.put("chargetDesc", "按用餐时长收取服务费￥"+ table.get("chargeAmount")+"/"+chargeTime+"分钟");
                    }
                    break;
            }
        }
    }
}
