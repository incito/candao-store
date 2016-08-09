package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.candao.www.webroom.model.MemberInfo;
import com.candao.www.webroom.service.MemeberService;


@Service
public class MemeberServiceImpl implements MemeberService {

	@Override
	public int queryMemberByNo(String memberCardNo, String memberPwd) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addMoneyToMemberCard(String memberCardNo, BigDecimal bigDecimal) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int registerMemberCard(MemberInfo memberInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int discardMemberCard(String memberCardNo) {
		// TODO Auto-generated method stub
		return 0;
	}

}
