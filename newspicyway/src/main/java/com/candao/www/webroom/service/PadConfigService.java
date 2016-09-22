package com.candao.www.webroom.service;

import com.candao.www.webroom.model.PadConfig;

public interface PadConfigService {

	int saveorupdate(PadConfig padConfig);

	PadConfig getconfiginfos();
	PadConfig saveorupdateToDic(String iupItemid,String imgName);
}
