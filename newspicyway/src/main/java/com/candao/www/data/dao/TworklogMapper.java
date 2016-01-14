package com.candao.www.data.dao;

import com.candao.www.data.model.Tworklog;

public interface TworklogMapper {
	
	public final static String PREFIX = TworklogMapper.class.getName();

	public int insert(Tworklog record);

}