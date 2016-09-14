package com.candao.inorder.dao;

import java.util.List;
import java.util.Map;

import com.candao.inorder.pojo.TblEmpdept;
import com.candao.inorder.pojo.TblEmployee;

public interface InorderEmployeeDao {
	public final static String PREFIX = InorderEmployeeDao.class.getName();
	public List<TblEmpdept> empdepts(Map<String, String> params);
	public TblEmployee queryForEmpNo(String empno);
}
