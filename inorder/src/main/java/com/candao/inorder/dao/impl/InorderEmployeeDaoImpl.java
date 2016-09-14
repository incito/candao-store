package com.candao.inorder.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.inorder.dao.DaoSupport;
import com.candao.inorder.dao.InorderEmployeeDao;
import com.candao.inorder.pojo.TblEmpdept;
import com.candao.inorder.pojo.TblEmployee;

@Repository
public class InorderEmployeeDaoImpl implements InorderEmployeeDao {

	@Autowired
	private DaoSupport daoSupport;
	@Override
	public List<TblEmpdept> empdepts(Map<String, String> params) {
		return daoSupport.find(PREFIX+".queryEmployee", params);
	}
	@Override
	public TblEmployee queryForEmpNo(String empNo) {
		return daoSupport.getOne(PREFIX+".queryEmployeeForEmpNo",empNo);
	}

}
