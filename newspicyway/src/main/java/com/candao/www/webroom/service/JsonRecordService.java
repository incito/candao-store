package com.candao.www.webroom.service;

import com.candao.www.data.model.TJsonRecord;

public interface JsonRecordService {

	/**
	 * 插入pad 的json 数据
	 * @author zhao
	 * @param jsonRecord
	 * @return
	 */
	public int insertJsonRecord(TJsonRecord  jsonRecord);
}
