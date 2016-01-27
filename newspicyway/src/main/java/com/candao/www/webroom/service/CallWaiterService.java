package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 呼叫服务员Service接口
 * @author Administrator
 *
 */
public interface CallWaiterService {
	
	/**
	 * 
	 * 保存呼叫信息
	 * 
	 *主键ID 生成  IdentifierUtils.getId().generate()
	 * @return
	 */
	public int saveCallInfo(JSONObject data);
	
	
	
	public void updateCallStatus(String orderid);
	
	public String findrelateUserid(List<Map<String, Object>> retableList,String tableno);

}
