package com.candao.www.support;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.www.data.dao.TbResourceDao;
import com.candao.www.data.model.TbResource;
import com.candao.www.data.model.TbUser;
import com.candao.www.data.model.User;
import com.candao.www.spring.SpringContext;
import com.candao.www.utils.SessionUtils;

public class FunctionTag {
	private static final Logger logger = LoggerFactory.getLogger(FunctionTag.class);

	@SuppressWarnings("unchecked")
	public static List<TbResource> getMenu() {
		User currentUser = SessionUtils.getCurrentUser();
		if (logger.isDebugEnabled()) {
			logger.debug(">>>MenuTag getMenu");
		}
		if (null == currentUser) {
			return Collections.EMPTY_LIST;
		}
		TbResourceDao tbResourceDao = (TbResourceDao) SpringContext.getBean(TbResourceDao.class);
		List<TbResource> list = tbResourceDao.getLeftMenu(currentUser.getId());
		if (logger.isDebugEnabled()) {
			logger.debug("<<<getMenu by " + currentUser.getId() + " with row " + list.size());
		}
		return list;
	}
}
