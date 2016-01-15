/**
 * 
 */
package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbDiscountTickets;
import com.candao.www.data.model.TbNoDiscountDish;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.Tdish;

/**
 * @author zhao
 *
 */
public interface TbDiscountTicketsDao {
  public final static String PREFIX = TbDiscountTicketsDao.class.getName();
  
  public TbDiscountTickets get(String id);
  
  public <T, K, V> List<T> find(Map<K, V> params);
  
  public int insert(TbDiscountTickets tbDiscountTickets);
  
  public int update(TbDiscountTickets tbDiscountTickets);
  
  public int delete(String id);
  
  /**
   * 批量增加不参加折扣菜品
   * @param dishes
   * @return
   */
  public int batchInsertNoDiscountDishs(List<TbNoDiscountDish> dishes);
  
  /**
   * 根据折扣券删除不参加折扣菜品
   * @param dishes
   * @return
   */
  public int deleteNoDiscountDishsByDiscount(String discountId);
  
  /**
   * 根据优惠删除不参加折扣菜品
   * @param dishes
   * @return
   */
  public int deleteNoDiscountDishsByPreterential(String preterentialId);
  
  /**
   * 查询不参加折扣菜品
   * @param discountId
   * @return
   */
  public List<TbNoDiscountDish> getNoDiscountDishsByDiscount(String discountId);
  /**
   * 查询所有鱼锅、锅底、鱼的dishid
   * @param discountId
   * @return
   */
  public List<Tdish> getDishidList(String dishid);
}
