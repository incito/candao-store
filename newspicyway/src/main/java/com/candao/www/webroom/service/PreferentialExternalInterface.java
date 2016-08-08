/**
 * 优惠和其他业务模块交互接口
 */
package com.candao.www.webroom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.data.model.Tdish;
import com.candao.www.security.service.ShopMgService;

/**
 * @author zhao
 *
 */
@Service
public class PreferentialExternalInterface {
  @Autowired
  DishTypeService dishTypeService;
  @Autowired
  DishService dishService;
  @Autowired
  ShopMgService shopMgService;
  
  /**
   * 按分类取所有菜品
   * @return
   */
  public Map<Map<String,Object>,List<Map<String,Object>>> getGroupedDishList(){
    Map<Map<String,Object>,List<Map<String,Object>>> maptotal=new  HashMap<Map<String,Object>,List<Map<String,Object>>>();
    List<Map<String,Object>> listDishType=dishTypeService.findAll("0");
    for(Map<String,Object> map:listDishType){
      List<Map<String,Object>> dishlist= dishService.getDishListByType(map.get("id").toString(),"0");
      maptotal.put(map, dishlist);
    }
    return maptotal;
  }
  
  /**
   * 按分类取所有菜品
   * 修正第一个方法中，传到前台，菜品分类是个字符串，不是标准JSON字符串或者对象，无法直接使用的BUG
   * @return
   * edit by sgy
   *  2015.4.14
   */
  public Map<String,List<Map<String,Object>>> getGroupedDishList2(){
    Map<String,List<Map<String,Object>>> maptotal=new  HashMap<String,List<Map<String,Object>>>();
    List<Map<String,Object>> listDishType=dishTypeService.findAll("0");
    for(Map<String,Object> map:listDishType){
      List<Map<String,Object>> dishlist= dishService.getDishListByType(map.get("id").toString(),"0");
      maptotal.put(JacksonJsonMapper.objectToJson(map), dishlist);
    }
    return maptotal;
  }
  
  /**
   * 获取所有门店
   * @return
   */
  public List<HashMap<String, Object>> getAllShopes(){
    return shopMgService.getAll();
  }
  
//  public Map<String, Object> getCurrentShop(){
//    
//  }
  
}
