package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

public interface AnimalService {

	/**
	 * 查询所有的玩偶
	 * 根据sort升序排列
	 * @return
	 */
	List<Map<String, Object>> allanimals();

}
