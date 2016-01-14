package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TOrderMemberDao;
import com.candao.www.data.model.TOrderMember;
import com.candao.www.webroom.service.OrderMemberService;

@Service
public class OrderMemberServiceImpl implements OrderMemberService {
	
	@Autowired
	private TOrderMemberDao tOrderMemberDao ;

	@Override
	public int saveOrderMember(TOrderMember tOrderMember) {
		// TODO Auto-generated method stub
		return tOrderMemberDao.insert(tOrderMember);
	}

	@Override
	public int updateValid(String orderid) {
		// TODO Auto-generated method stub
		return tOrderMemberDao.updateValid(orderid);
	}

	@Override
	public TOrderMember get(String orderid) {
		// TODO Auto-generated method stub
		
		
		return tOrderMemberDao.get(orderid);
	}

}
