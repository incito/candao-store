package com.candao.www.webroom.model;

import java.io.Serializable;
public class Code implements Serializable{

	private static final long serialVersionUID = 4162463104833001343L;
	
	private String codeId;
	
	private String codeDesc;
	
	private String aredId;

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAredId() {
		return aredId;
	}

	public void setAredId(String aredId) {
		this.aredId = aredId;
	}
}
