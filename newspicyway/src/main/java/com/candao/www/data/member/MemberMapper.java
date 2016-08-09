package com.candao.www.data.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * 会员数据操作
 * 
 * 
 * @author YANGZHONGLI
 *
 */
@Repository
public interface MemberMapper {
	
	/**
	 * 
	 * 查询指定门店，指定类型，指定时间段的所有操作
	 * @param branchid
	 * @param date
	 * @return
	 */
	public List<Map<String,Object>> queryMemberDealInfos(@Param("branchId") String branchId,@Param("typeList") List<Integer> typeList,@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("cardno") String cardno);
		
	
	/**
	 * 
	 * 查询指定门店，指定类型，指定时的所有操作
	 * @param branchid
	 * @param date
	 * @return
	 */
	public List<Map<String,Object>> queryMemberDealInfosToTime(@Param("branchId") String branchId,@Param("typeList") List<Integer> typeList,@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("cardno") String cardno);
		
}
