package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.AreaManager;

public interface ManagerWatchService {

	/**
	 * 根据工号删除经理区域信息
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param jobNumber
	 */
	public int deleteAreaManager(String jobNumber);
	
	/**
	 * 批量保存经理区域
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param ams
	 * @return
	 */
	public int batchInsertAreaManager(List<AreaManager> ams);
	
	/**
	 * 推送经理手环消息
	 * @author weizhifang
	 * @since 2016-3-22
	 * @param tableno
	 * @param userid
	 * @param status
	 * @return
	 */
	public int sendManagerWatchMsg(Map<String,String> params);

}
