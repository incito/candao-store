package com.candao.www.data.model;

import java.util.Date;

public class TbUserInstrument {
	/**
	 * 主键
	 */
    private String id;
    /**
     * 服务员id
     */
    private String userid;
    /**
     * 手环id
     */
    private String instrumentid;
    /**
     * 插入时间
     */
    private Date inserttime;
    /**
     * 登录时间
     */
    private Date logintime;
    /**
     * 退出时间
     */
    private Date logouttime;
    /**
     * 当前 手环状态 0 正常 1 失效
     */
    private Integer status;
    /**
     * 餐台编号
     */
    private String tableno;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getInstrumentid() {
		return instrumentid;
	}
	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}
	public Date getInserttime() {
		return inserttime;
	}
	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}
	public Date getLogintime() {
		return logintime;
	}
	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}
	public Date getLogouttime() {
		return logouttime;
	}
	public void setLogouttime(Date logouttime) {
		this.logouttime = logouttime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTableno() {
		return tableno;
	}
	public void setTableno(String tableno) {
		this.tableno = tableno;
	}
    
}
