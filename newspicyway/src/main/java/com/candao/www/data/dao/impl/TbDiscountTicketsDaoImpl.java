/**
 * 
 */
package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.model.TbDiscountTickets;
import com.candao.www.data.model.TbNoDiscountDish;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.Tdish;

/**
 * @author zhao
 *
 */
@Repository
public class TbDiscountTicketsDaoImpl implements TbDiscountTicketsDao {
  @Autowired
  private DaoSupport dao;

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbDiscountTicketsDao#get(java.lang.String)
   */
  @Override
  public TbDiscountTickets get(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.get(PREFIX + ".get", params);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbDiscountTicketsDao#find(java.util.Map)
   */
  @Override
  public <T, K, V> List<T> find(Map<K, V> params) {
    return dao.find(PREFIX + ".find", params);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbDiscountTicketsDao#insert(com.candao.www.data.model.TbPreferentialActivity)
   */
  @Override
  public int insert(TbDiscountTickets tbDiscountTickets) {
    return dao.insert(PREFIX + ".insert", tbDiscountTickets);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbDiscountTicketsDao#update(com.candao.www.data.model.TbPreferentialActivity)
   */
  @Override
  public int update(TbDiscountTickets tbDiscountTickets) {
    return dao.update(PREFIX + ".updateByPrimaryKey", tbDiscountTickets);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbDiscountTicketsDao#delete(java.lang.String)
   */
  @Override
  public int delete(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.delete(PREFIX + ".delete", params);
  }

  @Override
  public int batchInsertNoDiscountDishs(List<TbNoDiscountDish> dishes) {
    return dao.insert(PREFIX + ".batchInsertNoDiscountDishs", dishes);
  }

  @Override
  public int deleteNoDiscountDishsByDiscount(String discountId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("discountTicket", discountId);
    return dao.delete(PREFIX + ".deleteNoDiscountDishsByDiscount", params);
  }

  @Override
  public int deleteNoDiscountDishsByPreterential(String preterentialId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("preterential", preterentialId);
    List<TbDiscountTickets> discounts = this.find(params);
    if(discounts == null || discounts.size() != 1){
      return 0;
    }
    
    return deleteNoDiscountDishsByDiscount(discounts.get(0).getId());
  }

  @Override
  public List<TbNoDiscountDish> getNoDiscountDishsByDiscount(String discountId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("discountId", discountId);
    return dao.find(PREFIX + ".getNoDiscountDishsByDiscount", params);
  }
  

  @Override
  public List<Tdish> getDishidList(String dishid){
	  Map<String, Object> params = new HashMap<String, Object>();
	  params.put("dishid", dishid);
	  return dao.find(PREFIX + ".getDishidList", params);
  }

}
