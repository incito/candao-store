package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Tbranchshop;

public interface BranchShopService {
	/**
	 * 分页查询数据
	 * @param params
	 * @param current
	 * @param pagesize
	 * @return
	 */
	 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
	 
	 public List<Tbranchshop> getTbParternerList(Map<String,Object> map);
	 
	 public List<Tbranchshop> getAllListList();
	 
	 /**
	  * 增
	  * @param tdish
	  * @return
	  */
	 public boolean save(Tbranchshop tbranchshop);
	 /**
	  * 改
	  * @param tdish
	  * @return
	  */
	 public boolean update(Tbranchshop tbranchshop);
	 /**
	  * 查
	  * @param id
	  * @return
	  */
	 public Tbranchshop findById(String id);
	 /**
	  * 删
	  * @param id
	  * @return
	  */
	 public boolean deleteById(String id);

     
}
