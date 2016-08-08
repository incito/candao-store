/**
 * 
 */
package com.candao.www.permit.vo;

import java.util.List;

import com.candao.www.data.model.Function;
import com.candao.www.data.model.Role;

/**
 * 
 * 主要用于编辑角色的时候使用。
 * @author YHL
 *
 */
public class RoleVO {
	
	/**
	 * 权限角色对象
	 */
	private Role role;
	
	/**
	 * 功能模块对象
	 */
	private List<Function> functions;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}
	
	
	
}
