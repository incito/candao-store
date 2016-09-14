package com.candao.inorder.dao;

import java.util.List;

import com.candao.inorder.pojo.TblMenu;

/**
 * 
 * @author Candao
 *获取菜品信息
 */
public interface InorderMenuDao {
	public final static String PREFIX = InorderMenuDao.class.getName();

	public List<TblMenu> queryMeun(Object[] objects);
}
