package com.candao.www.security.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;


public interface ShopMgService {
  /**
   * 新增门店
   * @param branchMap
   * @return
   */
   public int addShop(HashMap<String,Object> branchMap);
   public int getCountByNameAddress(HashMap<String, Object> queryParam);
   public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) ;
   
   public int update(HashMap<String,Object> tbBranchMap);
   /**
    * 删除门店信息（为删除）
    * @param tbBranchMap
    * @return
    */
   public int update_del(HashMap<String,Object> tbBranchMap);
   
   public HashMap<String, Object> getOne(String id) ;

    /**
     * 获取所有的门店信息
     * @author tom_zhao
     * @return
     */
   public List<HashMap<String, Object>> getAll();
   
   /**
    * 获取分店信息
    * @author tom_zhao
    * @return
    */
   	public Map<String ,Object> getBranchInfo();
   	public List<HashMap<String, Object>> findByParams(Map<String, Object> params);
}
