package com.candao.www.data.model;

import java.util.Date;

public class TbMessageInstrument {
	/**
	 * 主键
	 */
    private String id;
    /**
     * 消息类型
     */
    private String msg_type;
    /**
     * 插入时间
     */
    private Date inserttime;
    /**
     * 消息状态
     */
    private Integer status;
    /**
     * 插入时间
     */
    private Date reply_time;
    /**
     * 插入时间
     */
    private Date sent_time;
    /**
     * 餐台编号
     */
    private String tableno;
    /**
     * 应答的服务员
     */
    private String userid;
    
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	public Date getInserttime() {
		return inserttime;
	}
	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getReply_time() {
		return reply_time;
	}
	public void setReply_time(Date reply_time) {
		this.reply_time = reply_time;
	}
	public Date getSent_time() {
		return sent_time;
	}
	public void setSent_time(Date sent_time) {
		this.sent_time = sent_time;
	}
	public String getTableno() {
		return tableno;
	}
	public void setTableno(String tableno) {
		this.tableno = tableno;
	}

}
