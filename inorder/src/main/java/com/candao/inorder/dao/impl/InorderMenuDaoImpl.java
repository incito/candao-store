package com.candao.inorder.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.inorder.dao.DaoSupport;
import com.candao.inorder.dao.InorderMenuDao;
import com.candao.inorder.pojo.TblMenu;

@Repository
public class InorderMenuDaoImpl implements InorderMenuDao {

	@Autowired
	 private DaoSupport daoSupport;
	@Override
	public List<TblMenu> queryMeun(Object[] menuItems) {
		Map<String, Object>  params=new HashMap<String,Object>();
		params.put("items", menuItems);
		return daoSupport.find(PREFIX+".queryMenuList", params) ;
	}

}
