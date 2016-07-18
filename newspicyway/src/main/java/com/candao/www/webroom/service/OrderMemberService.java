package com.candao.www.webroom.service;

import java.util.Map;

import com.candao.www.data.model.TOrderMember;

public interface OrderMemberService {

	public int saveOrderMember(TOrderMember tOrderMember );
	
	public int update(TOrderMember tOrderMember );
	
	public int updateValid(java.lang.String orderid);
	
	public TOrderMember get(java.lang.String orderid, Integer valid);
	
}
