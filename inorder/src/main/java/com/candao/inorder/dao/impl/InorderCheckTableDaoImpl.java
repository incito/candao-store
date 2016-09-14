package com.candao.inorder.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.inorder.dao.DaoSupport;
import com.candao.inorder.dao.InorderCheckTableDao;
import com.candao.inorder.pojo.TblCheck;
/***
 * 
 * @author liangdong
 *订单操作接口
 */
@Repository
public class InorderCheckTableDaoImpl implements InorderCheckTableDao  {

	@Autowired
	 private DaoSupport daoSupport;
	@Override
	public TblCheck tableInfoMes(Map<String, Object> params) {
		return daoSupport.get(PREFIX+".activeTableInfo",params);
		
	}

	@Override
	public String addCheck(TblCheck check) {
		int status= daoSupport.insert(PREFIX+".insert", check);
		return  String.valueOf(status);
	}

	@Override
	public String updateCheckForTot(TblCheck check) {
		int status= daoSupport.update(PREFIX+".updateCheckTot", check);
		return  String.valueOf(status);
	}

}
