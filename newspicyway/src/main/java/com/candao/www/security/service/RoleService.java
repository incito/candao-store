package com.candao.www.security.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbRole;
import com.candao.www.data.model.TbUser;


public interface RoleService {
/**
 * 分页查询角色
 * @param params
 * @param current
 * @param pagesize
 * @return
 */
 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
 /**
  * 获取角色list
  * @return
  */
 public List<Map<String,Object>> getRoleList();
 /**
  * 保存角色
  * @param tbuser
  * @return
  */
 public boolean save(TbRole tbRole);
 /**
  * 更改角色
  * @param tbuser
  * @return
  */
 public boolean update(TbRole tbRole);
 /**
  * 查询单个角色
  * @param userid
  * @return
  */
 public TbRole findById(String roleid);
 /**
  * 删除单个角色
  * @param userid
  * @return
  */
 public boolean deleteById(String roleid,int status);
}
