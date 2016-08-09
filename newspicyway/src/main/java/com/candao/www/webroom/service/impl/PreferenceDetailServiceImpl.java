package com.candao.www.webroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TPreferenceDetailDao;
import com.candao.www.webroom.service.PreferenceDetailService;

/**
 * 优惠活动明细表
 * @author Administrator
 *
 */
@Service
public class PreferenceDetailServiceImpl implements PreferenceDetailService {

	@Autowired
	private TPreferenceDetailDao tpreferenceDetailDao;
}
