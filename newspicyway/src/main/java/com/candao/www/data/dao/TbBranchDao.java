package com.candao.www.data.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;



/**
 * 数据访问接口
 *
 */
public interface TbBranchDao {    
    public final static String PREFIX = TbBranchDao.class.getName();
    public int insert(HashMap<String,Object> tbBranchMap);

    public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
    
    public int update(HashMap<String,Object> tbBranchMap);
    /**
     * 删除门店信息（为删除）
     * @param tbBranchMap
     * @return
     */
    public int update_del(HashMap<String,Object> tbBranchMap);
    
    public HashMap<String,Object> getOne(HashMap<String, Object> tbBranchMap);

    /**
     * 获取所有的门店
     * @author tom_zhao
     * @return
     */
	public List<HashMap<String, Object>> getAll();
	/**
	 * 获取门店的id的list
	 * @author shen
	 * @date:2015年6月17日下午7:48:53
	 * @Description: TODO
	 */
	public List<Integer> getBranchidList();
	
	/**
	 * 返回 t_branch_info 表中得 一条记录。（此表只存放一条记录。）
	 * @return
	 */
	public Map<String ,Object> getBranchInfo();
    
  public int  getCountByNameAddress(HashMap<String,Object> queryParam);

  public List<HashMap<String, Object>> findByParams(Map<String, Object> params);
}


