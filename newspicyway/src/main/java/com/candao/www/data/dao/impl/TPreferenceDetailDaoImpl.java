package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TPreferenceDetailDao;

/**
 * 优惠活动明细表
 * @author Administrator
 *
 */
@Repository
public class TPreferenceDetailDaoImpl implements TPreferenceDetailDao {

	@Autowired
	private DaoSupport daoSupport;
}
