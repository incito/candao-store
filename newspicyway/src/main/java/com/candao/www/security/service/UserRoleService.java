package com.candao.www.security.service;

import java.util.List;



public interface UserRoleService {

	public boolean inserts(String userid, String[] roleids);
	
	public List<String> getUserRole(String userid);
}
