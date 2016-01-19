package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Ttemplate;


public interface TemplateService {
/**
 * 分页查询模板
 * @param params
 * @param current
 * @param pagesize
 * @return
 */
 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
 /**
  * 保存模板
  * @param tbuser
  * @return
  */
 public boolean save(Ttemplate Ttemplate,String articleids);
 /**
  * 更改模板
  * @param tbuser
  * @return
  */
 public boolean update(Ttemplate ttemplate,String articleids,String oldarticleids);
 /**
  * 查询单个模板
  * @param userid
  * @return
  */
 public Ttemplate findById(String id);
 /**
  * 删除单个模板
  * @param userid
  * @return
  */
 public boolean deleteById(String id);
 /**
  * 更改模板状态
  * @param id
  * @param status
  * @return
  */
 public boolean updateStatus(String id,int status);
 /**
  * 查询条件
  * @param params
  * @return
  */
 public List<Map<String,Object>> getTemplates(Map<String, Object> params);
 /**
  * 检测文章是否在别的模板使用
  * @param articleids
  * @return
  */
 public List<Ttemplate> validateTemplate(Map<String, Object> params);
 /**
  * 批量更新模板数据
  * @param list
  * @return
  */
 public boolean updateTemplates(List<Map<String,Object>> list);
}
