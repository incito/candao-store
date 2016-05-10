package com.candao.www.data.dao;

import java.util.Map;



public interface BranchDataSynDao {
	public final static String PREFIX = BranchDataSynDao.class.getName();
	
	public  int checkBizData();
	
	public int checkSynDataFinish();
	/**
	 * 
	 * @Description:检查最近一次同步是否有未同步的记录
	 * @create: 余城序
	 * @Modification:
	 * @return int 1-有,0-没有
	 */
	public int checkLastSynDataFinish();
	
	public int insertSynRecord(Map<String, String> mapValue) ;
	
	public int updateSynRecord(Map<String, Object> mapValue) ;
	/**
	 * 
	 * @Description:更新同步数据的记录
	 * @create: 余城序
	 * @Modification:
	 * @param mapValue
	 * @return int
	 */
	public int updateSynData(Map<String, Object> mapValue);

	public void transferToHistory();
	
	public String getSynSql(String tableName,String sqlCondition);

	public void deleteSynRecord();

	public void updateBizLog();

	public Map<String, String> getBizDate();
}
