package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbParterner;

public interface ParternerService {
	/**
	 * 分页查询数据
	 * @param params
	 * @param current
	 * @param pagesize
	 * @return
	 */
	 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
	 
	 public List<TbParterner> getTbParternerList(Map<String,Object> map);
	 /**
	  * 增
	  * @param tdish
	  * @return
	  */
	 public boolean save(TbParterner tbParterner);
	 /**
	  * 改
	  * @param tdish
	  * @return
	  */
	 public boolean update(TbParterner tbParterner);
	 /**
	  * 查
	  * @param id
	  * @return
	  */
	 public TbParterner findById(String id);
	 /**
	  * 删
	  * @param id
	  * @return
	  */
	 public boolean deleteById(String id);

}
