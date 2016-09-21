package com.candao.inorder.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.inorder.dao.InorderCheckTableDao;
import com.candao.inorder.pojo.TblCheck;
import com.candao.inorder.service.InorderCheckTableService;

@Service
public class InorderCheckTableServiceImpl implements InorderCheckTableService {

	@Autowired
	private InorderCheckTableDao checktabledao;

	@Override
	public String addCheck(TblCheck tblCheck) {
		return checktabledao.addCheck(tblCheck);
	}

	@Override
	public TblCheck openTable(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

}
