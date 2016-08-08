package com.candao.www.data.model;

import java.util.Date;

public class TbInstrument {
	/**
	 * 主键
	 */
    private String id;
    /**
     * 手环名称
     */
    private String instrument_name;
    /**
     * 手环编号
     */
    private String instrument_code;
    /**
     * 手环消息id
     */
    private String msg_id;
    /**
     * 插入时间
     */
    private Date inserttime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstrument_name() {
		return instrument_name;
	}
	public void setInstrument_name(String instrument_name) {
		this.instrument_name = instrument_name;
	}
	public String getInstrument_code() {
		return instrument_code;
	}
	public void setInstrument_code(String instrument_code) {
		this.instrument_code = instrument_code;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public Date getInserttime() {
		return inserttime;
	}
	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}
    
}
