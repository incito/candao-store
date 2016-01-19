package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TOrderMember;

public interface TOrderMemberDao {
    public final static String PREFIX = TOrderMemberDao.class.getName();
    
  	public TOrderMember get(java.lang.String orderId);
  	
  	public int insert(TOrderMember tOrderMember);
  	
  	public int update(TOrderMember tOrderMember);
  	
  	public int updateValid(java.lang.String orderId );

}