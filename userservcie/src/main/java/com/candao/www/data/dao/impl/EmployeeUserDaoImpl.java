package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.EmployeeUserDao;
import com.candao.www.data.model.EmployeeUser;
import com.candao.www.data.model.Role;

@Repository
public class EmployeeUserDaoImpl implements EmployeeUserDao{

	@Autowired
	private DaoSupport dao;
	
	@Override
	public List<EmployeeUser> getAll() {
		return this.dao.find(PREFIX+".getAll");
	}

	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return this.dao.page(PREFIX+".pageUser", params, current, pagesize);
	}

	@Override
	public int save(EmployeeUser employeeUser) {
		return this.dao.insert(PREFIX+".insert", employeeUser);
	}

	@Override
	public int update(EmployeeUser employeeUser) {
		return this.dao.update(PREFIX+".update", employeeUser);
	}
	
	@Override
	public int updateUserStatus(String user_id, String status) {
		Map<String ,Object> params=new HashMap();
		
		params.put("user_id", user_id);
		params.put("status", status);		
		return  this.dao.update(PREFIX+".updateUserStatus", params);
	}

	@Override
	public EmployeeUser get(String id) {
		Map<String,Object> param=new HashMap();
		param.put("id", id);
		return this.dao.get(PREFIX+".get", param);
	}
	
	/**
	 * 获取员工用户对象列表，根据条件
	 * @param param
	 * @return
	 */
	public List<EmployeeUser> get(Map<String,Object> param) {
		return this.dao.find(PREFIX+".get", param);
	}

	@Override
	public int delete(String id) {
		Map<String,Object> param=new HashMap();
		param.put("id", id);
		return this.dao.delete(PREFIX+".deleteById", param);
	}

	@Override
	public Map<String, Object> getBranchById(String branch_id) {
		Map<String ,Object > param= new HashMap();
		param.put("branch_id", branch_id);
		return this.dao.get(PREFIX+".getBranchById", param);
	}

	@Override
	public List<EmployeeUser> getEmployeeUserByRoles4Store(List<Role> roles,
			String branchId) {
		Map<String ,Object > param= new HashMap();
		param.put("branchId", branchId);
		param.put("roles", roles);
		return this.dao.find(PREFIX+".getEmployeeUserByRoles4Store", param);
	}

	@Override
	public List<EmployeeUser> getEmployeeUser4Store(String branchId) {
		Map<String ,String > params=new HashMap();
		params.put("branch_id", branchId);
		return this.dao.find(PREFIX+".getEmployeeUser4Store",params);
	}

	@Override
	public EmployeeUser getByparams(Map<String, Object> map) {
		return this.dao.get(PREFIX+".get", map);
	}

}
