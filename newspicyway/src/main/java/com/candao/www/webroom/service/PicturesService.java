package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbPictures;




public interface PicturesService {
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
  * @param tbPictures
  * @return
  */
 public boolean save(TbPictures tbPictures);
 /**
  * 更改数据
  * @param tbPictures
  * @return
  */
 public boolean update(TbPictures tbPictures);
 /**
  * 查询单个数据
  * @param id
  * @return
  */
 public TbPictures findById(String id);
 /**
  * 删除单个数据
  * @param id
  * @return
  */
 public boolean deleteById(String id);

public List<Map<String, Object>>  find(Map<String, Object> params);
}
