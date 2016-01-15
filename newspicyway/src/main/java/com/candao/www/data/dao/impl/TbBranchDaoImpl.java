package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbBranchDao;

/**
 * 数据访问接口 后台门店管理
 * @author zt
 *
 */
@Repository
public class TbBranchDaoImpl  implements TbBranchDao {
	@Autowired
	private DaoSupport dao;

  @Override
  public int insert(HashMap<String, Object> tbBranchMap) {
    return dao.insert(PREFIX + ".insert", tbBranchMap);
  }
  
  @Override
  public int getCountByNameAddress(HashMap<String, Object> queryParam) {
    // TODO Auto-generated method stub
    return   dao.get(PREFIX+".getCountByNameAddress", queryParam);
  }
  
  
  @Override
  public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
    return dao.page(PREFIX + ".page", params, current, pagesize);
  }

  @Override
  public int update(HashMap<String, Object> tbBranchMap) {
     
    return dao.update(PREFIX+".update", tbBranchMap);
  }

  @Override
  public int update_del(HashMap<String, Object> tbBranchMap) {
    return   dao.update(PREFIX+".update_del", tbBranchMap);
  }

  @Override
  public HashMap<String, Object> getOne(HashMap<String, Object> tbBranchMap) {
     
    return dao.get(PREFIX+".getone", tbBranchMap);
  }

	@Override
	public List<HashMap<String, Object>> getAll() {
			return dao.find(PREFIX + ".getAll", null);
	 
	}

	@Override
	public Map<String, Object> getBranchInfo() {
		
		return dao.get(PREFIX+".getBranchInfo",null);
	}

	@Override
	public List<Integer> getBranchidList() {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getBranchidList", null);
	}

	@Override
	public List<HashMap<String, Object>> findByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findByParams", params);
	}
	 
}


