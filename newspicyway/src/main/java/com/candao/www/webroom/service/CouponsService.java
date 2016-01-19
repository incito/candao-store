package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TCouponRule;
import com.candao.www.data.model.TCoupons;
import com.candao.www.webroom.model.TCouponGroup;
public interface CouponsService {
/**
 * 分页查询数据
 * @param params
 * @param current
 * @param pagesize
 * @return
 */
 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
 /**
  * 保存数据
  * @param
  * @return
  */
 public boolean save(TCouponGroup tCouponGroup);
 /**
  * 更改数据
  * @param 
  * @return
  */
 public boolean update(TCouponGroup tCouponGroup);
 /**
  * 查询单个数据
  * @param id
  * @return
  */
 public TCouponGroup findById(String id);
 /**
  * 删除单个数据
  * @param id
  * @return
  */
 public boolean deleteById(String id);
 
 /**
  * 根据菜品id 得到菜品的所有优惠规则
  * @author zhao
  * @return
  */
 public List<TCouponRule>  findRuleByDishId(String dishId);
 
  public TCoupons findCouponByDishId(String dishid);
 
}
