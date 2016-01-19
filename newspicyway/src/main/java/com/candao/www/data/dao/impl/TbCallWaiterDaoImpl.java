package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TbCallWaiterDao;

/**
 * 呼叫服务员Dao接口实现类
 * @author Administrator
 *
 */
@Repository
public class TbCallWaiterDaoImpl implements TbCallWaiterDao{

	@Autowired
	private DaoSupport daoSupport;
}
