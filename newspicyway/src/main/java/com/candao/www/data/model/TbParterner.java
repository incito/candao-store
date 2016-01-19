package com.candao.www.data.model;

import java.io.Serializable;

public class TbParterner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3357901923418063077L;
	/**
	 * 主键
	 */
	private String parternerId;
	/**
	 * 客户编码
	 */
	private String code;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 客户名称
	 */
	private String name;
	/**
	 * 电话
	 */
	private String telephone;
	/**
	 * 联系人
	 */
	private String relaperson;
	/**
	 * 联系地址
	 */
	private String address;
	/**
	 * 状态 默认是0 ，如果是可以挂账的为1 2是删除
	 */
	private int status;

	public String getParternerId() {
		return parternerId;
	}

	public void setParternerId(String parternerId) {
		this.parternerId = parternerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRelaperson() {
		return relaperson;
	}

	public void setRelaperson(String relaperson) {
		this.relaperson = relaperson;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
