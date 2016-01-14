package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.BranchDataSynDao;
import com.candao.www.webroom.model.SynSqlObject;

@Repository
public class BranchDataSynDaoImpl implements BranchDataSynDao{

	@Autowired
	private DaoSupport daoSupport;
	
	@Override
	public int checkBizData() {
	 
		SynSqlObject  retMap = daoSupport.findOne(PREFIX+".checkeBizData");
		if(retMap == null || "0".equals(retMap.getResult())){
			return 0;
		}
		return 1;
	}

	@Override
	public int checkSynDataFinish() {
		SynSqlObject  retMap = daoSupport.findOne(PREFIX+".checkSynDataFinish");
		if(retMap == null || "0".equals(retMap.getResult())){
			return 0; 
		}
		return 1;
	}

	@Override
	public int insertSynRecord(Map<String, String> mapValue) {
		return daoSupport.insert(PREFIX+".insertSynRecord", mapValue);
	}

	@Override
	public int updateSynRecord(Map<String, Object> mapValue) {
		return daoSupport.insert(PREFIX+".updateSynRecord", mapValue);
	}

	@Override
	public void transferToHistory() {
		 
		  daoSupport.insert(PREFIX+".transferToHistory", null);
		  daoSupport.update(PREFIX+".updateHistoryStatus", null);
		  daoSupport.delete(PREFIX+".deleteSynRecord", null);
	}

	@Override
	public String getSynSql(String tableName,String sqlCondition) {
		 return daoSupport.getSynSql(tableName,sqlCondition);
		
	}

	@Override
	public void deleteSynRecord() {
		   daoSupport.delete(PREFIX+".deleteSynRecord",null);
		
	}

	@Override
	public void updateBizLog() {
		  daoSupport.update(PREFIX+".updateSynRecord", null);
		
	}
	
	@Override
	public Map<String, String> getBizDate(){
		return daoSupport.findOne(PREFIX+".getBizDate", null);
	}
 
}
