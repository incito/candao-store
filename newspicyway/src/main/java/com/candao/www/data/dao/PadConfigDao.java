package com.candao.www.data.dao;

import com.candao.www.webroom.model.PadConfig;

public interface PadConfigDao {
	 public final static String PREFIX = PadConfigDao.class.getName();

	int selectisExsit();

	int update(PadConfig padConfig);

	int insert(PadConfig padConfig);

	PadConfig getconfiginfos();

}
