package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbTableArea;


public interface TableAreaService {
/**
 * 分页查询数据
 * @param params
 * @param current
 * @param pagesize
 * @return
 */
 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
 /**
  * 保存数据
  * @param tbTableArea
  * @return
  */
 public List<Map<String,Object>> find(Map<String, Object> params);
 
 public List<Map<String,Object>> count(String id);
 //public List<Map<String,Object>> count(Map<String, Object> params);
 public boolean save(TbTableArea tbTableArea);
 /**
  * 更改数据
  * @param tbTableArea
  * @return
  */
 public boolean update(TbTableArea tbTableArea);
 /**
  * 查询单个数据
  * @param id
  * @return
  */
 public TbTableArea findById(String id);
 public TbTableArea tableAvaliableStatus(String id);
 /**
  * 删除单个数据
  * @param id
  * @return
  */
 public boolean deleteById(String id);

 /**
  * 取得数据字典
  * @return
  */
 
  public List<Map<String,Object>> getTableAreaTag();
public int updateListOrder(List<TbTableArea> tbTableArea);
public List<Map<String,Object>> findTableCountAndAreaname();

 public void delTablesAndArea(String areaid);
}
