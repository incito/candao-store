package com.candao.www.webroom.zookeeper;

public class ZKSynObject implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 /**
	  * 0 总店下发的数据 1 分店上传的数据
	  */
	 private String flag ;
	 
	 private String branchId;
	 
	 /**
	  * 同步的sql 语句
	  */

	 private String synSql;
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getSynSql() {
		return synSql;
	}

	public void setSynSql(String synSql) {
		this.synSql = synSql;
	}
	 
	 
}
