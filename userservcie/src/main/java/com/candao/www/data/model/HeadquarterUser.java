package com.candao.www.data.model;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * 用户总店关联-用户总店关联-实体类
 * @author lishoukun
 * @date 2015/04/21
 */
public class HeadquarterUser extends User implements Serializable{
	public HeadquarterUser(){
		
	}
	public HeadquarterUser(User user){
		this.setId(user.getId());
		this.setAccount(user.getAccount());
		this.setPassword(user.getPassword());
		this.setName(user.getName());
		this.setMobile(user.getMobile());
		this.setAreaCode(user.getAreaCode());
		this.setTelephone(user.getTelephone());
		this.setEmail(user.getEmail());
		this.setCreatetime(user.getCreatetime());
		this.setCreator(user.getCreator());
		this.setStatus(user.getStatus());
		this.setUserType(user.getUserType());
	}
	//序列化
	private static final long serialVersionUID = 6905308258132311722L;
    //门店列表
    private List<UserBranch> branchs;
	public List<UserBranch> getBranchs() {
		return branchs;
	}
	public void setBranchs(List<UserBranch> branchs) {
		this.branchs = branchs;
	}
	
}
