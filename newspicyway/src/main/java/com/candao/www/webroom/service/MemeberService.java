package com.candao.www.webroom.service;

import java.math.BigDecimal;

import com.candao.www.webroom.model.MemberInfo;

public interface MemeberService {

	public int queryMemberByNo(String memberCardNo,String memberPwd);
	
	public int addMoneyToMemberCard(String memberCardNo,BigDecimal bigDecimal);
	
	public int registerMemberCard(MemberInfo memberInfo);
	
	public int discardMemberCard(String memberCardNo);
}
