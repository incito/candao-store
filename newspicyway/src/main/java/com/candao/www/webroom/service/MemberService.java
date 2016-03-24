package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

public interface MemberService {

	public Map<String, String> queryMemberDealInfos(String branchId, List<Integer> typeList, String beginTime,String endTime, String shiftid,String cardno);
	
	public List<Map<String, String>> queryMemberDealInfosByDay(String branchId, List<Integer> typeList, String beginTime,String endTime, String shiftid,String cardno);

	public List<Map<String, String>> queryMemberDealInfosToTime(String branchId,List<Integer> typeList, String beginTime, String endTime,String shiftid,String cardno);
}
