package com.candao.www.data.model;

/**
 * 区域和经理的关系表
 * @author weizhifang
 * @since 2016-3-21
 *
 */
public class AreaManager {

	private String id;
	private String tableareaId;   //区域编号
	private String jobNumber;     //工号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTableareaId() {
		return tableareaId;
	}
	public void setTableareaId(String tableareaId) {
		this.tableareaId = tableareaId;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	
}
