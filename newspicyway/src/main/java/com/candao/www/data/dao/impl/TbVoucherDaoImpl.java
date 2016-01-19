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
import com.candao.www.data.dao.TbVoucherDao;
import com.candao.www.data.model.TbVoucher;

/**
 * @author zhao
 *
 */
@Repository
public class TbVoucherDaoImpl implements TbVoucherDao {
  @Autowired
  private DaoSupport dao;

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbVoucherDao#get(java.lang.String)
   */
  @Override
  public TbVoucher get(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.get(PREFIX + ".get", params);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbVoucherDao#find(java.util.Map)
   */
  @Override
  public <T, K, V> List<T> find(Map<K, V> params) {
    return dao.find(PREFIX + ".find", params);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbVoucherDao#insert(com.candao.www.data.model.TbPreferentialActivity)
   */
  @Override
  public int insert(TbVoucher tbVoucher) {
    return dao.insert(PREFIX + ".insert", tbVoucher);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbVoucherDao#update(com.candao.www.data.model.TbPreferentialActivity)
   */
  @Override
  public int update(TbVoucher tbVoucher) {
    return dao.update(PREFIX + ".updateByPrimaryKey", tbVoucher);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TbVoucherDao#delete(java.lang.String)
   */
  @Override
  public int delete(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.delete(PREFIX + ".delete", params);
  }

}
