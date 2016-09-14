package com.candao.inorder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.inorder.dao.InorderMenuDao;
import com.candao.inorder.pojo.TblMenu;
import com.candao.inorder.service.InorderMenuService;

@Service
public class InorderMenuServiceIml implements InorderMenuService {

	@Autowired
	private InorderMenuDao inorderMenuDao;
	
	@Override
	public List<TblMenu> queryMeun(Object[] params) {
		return inorderMenuDao.queryMeun(params);
	}

	

}
