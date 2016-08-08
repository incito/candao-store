package com.candao.www.security.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbRoleDao;
import com.candao.www.data.model.TbRole;
import com.candao.www.security.service.RoleService;
//@Service
public class RoleServiceImpl implements RoleService {
@Autowired
  private TbRoleDao tbRoleDao;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return tbRoleDao.page(params, current, pagesize);
	}
	@Override
	public boolean save(TbRole tbRole) {
		return tbRoleDao.insert(tbRole)>0;
	}
	@Override
	public TbRole findById(String roleid) {
		return tbRoleDao.get(roleid);
	}
	@Override
	public boolean update(TbRole tbRole) {
		return tbRoleDao.update(tbRole)>0;
	}
	@Override
	public boolean deleteById(String roleid,int status) {
		if(status==1){
			return tbRoleDao.delete(roleid,0)>0;
		}else{
			return tbRoleDao.delete(roleid,1)>0;
		}
	
	}
	@Override
	public List<Map<String, Object>> getRoleList() {
		// TODO Auto-generated method stub
		return tbRoleDao.getRoleList();
	}

}
