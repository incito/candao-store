package com.candao.www.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TbUserRoleDao;
import com.candao.www.security.service.UserRoleService;

//@Service
public class UesrRoleServiceImpl implements UserRoleService {
	@Autowired
	private TbUserRoleDao tbUserRoleDao;

	@Override
	public boolean inserts(String userid, String[] roleids) {
		tbUserRoleDao.delete(userid);
		return tbUserRoleDao.inserts(userid, roleids) > 0;
	}

	@Override
	public List<String> getUserRole(String userid) {
		return tbUserRoleDao.getUserRole(userid);
	}

}
