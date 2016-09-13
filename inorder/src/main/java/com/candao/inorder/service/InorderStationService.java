package com.candao.inorder.service;

import java.util.List;
import java.util.Map;

import com.candao.inorder.pojo.TblStation;

/**
 * 
 * @author Candao
 *获取站点信息
 */
public interface InorderStationService {
	/**查询所有的工作站**/
  public List<TblStation> tblStations(Map<String, String> params);
  /**根据工作站的编号，查询当前工作站信息**/
  public TblStation tblStation(Map<String, String> params);
  /**更新工具站内容**/
  public int updateStation(Map<String,String> params);
  
}
