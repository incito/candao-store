package com.candao.www.data.model;

import java.io.Serializable;


/**
 * 
 * 用户门店关联-用户门店关联-实体类
 * @author lishoukun
 * @date 2015/04/21
 */
public class UserBranch implements Serializable{
	//序列化
	private static final long serialVersionUID = 6905308258132311722L;
    //id,id
    private String id ;
    //用户id,user_id
    private String userId ;
    //门店id,branch_id
    private String branchId ;
    //门店名称,branch_name
    private String branchName ;
    public String getId(){
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    public String getUserId(){
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    public String getBranchId(){
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
    public String getBranchName(){
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
}
