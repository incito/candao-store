package com.candao.www.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.utils.UUIDGenerator;
import com.candao.www.data.dao.RoleDao;
import com.candao.www.data.model.Function;
import com.candao.www.data.model.Role;
import com.candao.www.permit.common.ScopeDict;

@Repository
public class RoleDaoImpl implements RoleDao {
	@Autowired
	private DaoSupport dao;

	@Override
	public int insertRole(Role role) {
		// TODO Auto-generated method stub
		return dao.insert(PREFIX + ".insertRole", role);
	}

	@Override
	public int updateRole(Role role) {
		return dao.update(PREFIX + ".updateRole", role);
	}

	@Override
	public void deleteRole(String id) {
		Map<String, Object> param = new HashMap();
		param.put("id", id);
		this.dao.delete(PREFIX + ".deleteRole", param);
	}

	@Override
	public List<Role> getRoleList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> getRoleList(Object role) {
		Map param = new HashMap();
		param.put("role", role);
		return dao.find(PREFIX + ".getRoleList", param);
	}


	@Override
	public List<Role> getRoletbrolebranchList(Object role,String branch_id) {
		Map param = new HashMap();
		param.put("role", role);
		param.put("branch_id", branch_id);
		return dao.find(PREFIX + ".getRoletbrolebranchList", param);
	}

	@Override
	public List<Role> getRoleListPage(int start, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> getRoleListPage(Object role, int start, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Function> getRoleFunction(Object role, String application) {
		Map param = new HashMap();
		param.put("role", role);
		param.put("application", application);
		return dao.find(PREFIX + ".getRoleFunction", param);
	}

	@Override
	public int countRoleList(Object role) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteRoleFunction(String roleId) {
		Map params = new HashMap();
		params.put("roleId", roleId);
		dao.delete(PREFIX + ".deleteRoleFunction", params);
	}

	@Override
	public int batchInsertRoleFunction(String roleId, List functions) {
		Map params = new HashMap();
		params.put("roleId", roleId);
		params.put("functions", functions);

		//生成 UUID，插入到数据库
		List uuids = new ArrayList();
		for (int i = 0; i < functions.size(); i++) {
			uuids.add(UUID.randomUUID().toString());
		}
		params.put("uuids", uuids);
		return dao.update(PREFIX + ".batchInsertRoleFunction", params);
	}

	@Override
	public List<String> getPermitUrl(String roleId) {
		Map param = new HashMap();
		param.put("roleId", roleId);
		return this.dao.find(PREFIX + ".getPermitUrl", param);
	}

	@Override
	public List<Role> getRoleListByScope(ScopeDict scope) {
		Map param = new HashMap();
		Role role = new Role();
		role.setScopeCode(scope.getValue());
		param.put("role", role);
		return dao.find(PREFIX + ".getRoleList", param);
	}

	@Override
	public int getUserCountOfRole(String roleid) {
		Map param = new HashMap();
		param.put("role_id", roleid);
		List list = dao.find(PREFIX + ".getUserCountOfRole", param);
		int count = 0;
		//count=(int) list.get(0);
		count = Integer.parseInt(list.get(0).toString());
		return count;
	}

	@Override
	public Role get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Role r = this.dao.get(PREFIX + ".getRoleById", params);
		return r;
	}
	@Override
	public  <T, K, V> List<T> getRoleByName(Map<K, V> params){
		return this.dao.find(PREFIX + ".getRoleByName", params);
	}

	@Override
	public List<Role> getRoleListForCurrentStore(String branch_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("branch_id", branch_id);
		return this.dao.find(PREFIX + ".getRoleListForCurrentStore", params);
	}

	@Override
	public int insertBranchRole(Map<String, Object> param) {
		return this.dao.insert(PREFIX + ".insertRoleForCurrentStore", param);
	}

	@Override
	public int deleteRoleForCurrentStore(String roleId, String branch_id) {
		Map<String, Object> param = new HashMap();
		param.put("role_id", roleId);
		param.put("branch_id", branch_id);
		return this.dao.delete(PREFIX + ".deleteRoleForCurrentStore", param);
	}

	@Override
	public Map getCurrentStoreInfo() {
		return this.dao.get(PREFIX + ".getCurrentStoreInfo", null);
	}

	@Override
	public List<Role> getRoleListByFunctionsCodes(List<String> codes) {
		Map<String, Object> params = new HashMap();
		params.put("functionCodes", codes);
		return this.dao.find(PREFIX + ".getRoleListByFunctionsCodes", params);
	}

}
