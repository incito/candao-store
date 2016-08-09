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
import com.candao.www.data.dao.TbHandFreeDao;
import com.candao.www.data.model.TbHandFree;

/**
 * @author zhao
 *
 */
@Repository
public class TbHandFreeDaoImpl implements TbHandFreeDao {
  @Autowired
  private DaoSupport dao;

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbHandFreeDao#get(java.lang.String)
   */
  @Override
  public TbHandFree get(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.get(PREFIX + ".get", params);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbHandFreeDao#find(java.util.Map)
   */
  @Override
  public <T, K, V> List<T> find(Map<K, V> params) {
    return dao.find(PREFIX + ".find", params);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbHandFreeDao#insert(com.candao.www.data.model.TbPreferentialActivity)
   */
  @Override
  public int insert(TbHandFree tbHandFree) {
    return dao.insert(PREFIX + ".insert", tbHandFree);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbHandFreeDao#update(com.candao.www.data.model.TbPreferentialActivity)
   */
  @Override
  public int update(TbHandFree tbHandFree) {
    return dao.update(PREFIX + ".updateByPrimaryKey", tbHandFree);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbHandFreeDao#delete(java.lang.String)
   */
  @Override
  public int delete(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.delete(PREFIX + ".delete", params);
  }

}
