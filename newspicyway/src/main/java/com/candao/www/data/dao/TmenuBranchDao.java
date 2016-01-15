package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TmenuBranch;

public interface TmenuBranchDao {
	public final static String PREFIX = TmenuBranchDao.class.getName();
	/**
	 * 添加菜谱和门店的关系数据
	 * @param list
	 * @return
	 */
	public int addTmenuBranch(List<TmenuBranch> list);
	/**
	 * 删除菜谱和门店的关系
	 * @param menuid
	 * @return
	 */
	public int delTmenuBranch(String menuid);
	/**
	 * 获取该菜谱适应的门店
	 * @param menuid
	 * @return
	 */
	public List<TmenuBranch> getTmenuBranchBymenuId(String menuid);
	/**
	 * 获取菜谱适用门店的详细信息，包括门店名称
	 * @author shen
	 * @date:2015年5月18日下午5:14:49
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getBranchDetailBymenuId(String menuid);
	
	public List<Integer> getBranchIdBymenuId(String menuid);
	
	
}
