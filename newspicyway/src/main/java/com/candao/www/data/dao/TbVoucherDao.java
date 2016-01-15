/**
 * 
 */
package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TbVoucher;

/**
 * @author zhao
 *
 */
public interface TbVoucherDao {
  public final static String PREFIX = TbVoucherDao.class.getName();
  
  public TbVoucher get(String id);
  
  public <T, K, V> List<T> find(Map<K, V> params);
  
  public int insert(TbVoucher tbVoucher);
  
  public int update(TbVoucher tbVoucher);
  
  public int delete(String id);
  
}
