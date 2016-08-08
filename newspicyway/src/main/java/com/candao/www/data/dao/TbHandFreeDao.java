/**
 * 
 */
package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TbHandFree;

/**
 * @author zhao
 *
 */
public interface TbHandFreeDao {
  public final static String PREFIX = TbHandFreeDao.class.getName();
  
  public TbHandFree get(String id);
  
  public <T, K, V> List<T> find(Map<K, V> params);
  
  public int insert(TbHandFree tbHandFree);
  
  public int update(TbHandFree tbHandFree);
  
  public int delete(String id);
  
}
