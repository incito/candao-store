package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 
 * tb_operation表
 * @author mew
 *
 */
public class TbOperation implements Serializable {
	private java.lang.String id; // 
	private java.lang.String userid; // 访问者用户ID
	private java.lang.String targetid; // 操作对像ID(文章id)
	private java.lang.Integer operationttype; // 操作类型(1: 浏览 )
	private java.lang.String address; // 访问者IP地址
	private java.lang.String useragent; // 
	private java.util.Date createtime; // 操作时间
	private java.util.Date endtime;//结束时间

	public java.util.Date getEndtime() {
		return endtime;
	}

	public void setEndtime(java.util.Date endtime) {
		this.endtime = endtime;
	}


	/**
     * 获取属性
     *
     * @return id
     */
	public java.lang.String getId() {
		return id;
	}
	
	/**
	 * 设置属性
	 *
	 * @param id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}
	
	/**
     * 获取访问者用户ID属性
     *
     * @return userid
     */
	public java.lang.String getUserid() {
		return userid;
	}
	
	/**
	 * 设置访问者用户ID属性
	 *
	 * @param userid
	 */
	public void setUserid(java.lang.String userid) {
		this.userid = userid;
	}
	
	/**
     * 获取操作对像ID(文章id)属性
     *
     * @return targetid
     */
	public java.lang.String getTargetid() {
		return targetid;
	}
	
	/**
	 * 设置操作对像ID(文章id)属性
	 *
	 * @param targetid
	 */
	public void setTargetid(java.lang.String targetid) {
		this.targetid = targetid;
	}
	
	/**
     * 获取操作类型(1: 浏览 )属性
     *
     * @return operationttype
     */
	public java.lang.Integer getOperationttype() {
		return operationttype;
	}
	
	/**
	 * 设置操作类型(1: 浏览 )属性
	 *
	 * @param operationttype
	 */
	public void setOperationttype(java.lang.Integer operationttype) {
		this.operationttype = operationttype;
	}
	
	/**
     * 获取访问者IP地址属性
     *
     * @return address
     */
	public java.lang.String getAddress() {
		return address;
	}
	
	/**
	 * 设置访问者IP地址属性
	 *
	 * @param address
	 */
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	
	/**
     * 获取属性
     *
     * @return useragent
     */
	public java.lang.String getUseragent() {
		return useragent;
	}
	
	/**
	 * 设置属性
	 *
	 * @param useragent
	 */
	public void setUseragent(java.lang.String useragent) {
		this.useragent = useragent;
	}
	
	/**
     * 获取操作时间属性
     *
     * @return createtime
     */
	public java.util.Date getCreatetime() {
		return createtime;
	}
	
	/**
	 * 设置操作时间属性
	 *
	 * @param createtime
	 */
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}
	

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TbOperation");
        sb.append("{id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", targetid=").append(targetid);
        sb.append(", operationttype=").append(operationttype);
        sb.append(", address=").append(address);
        sb.append(", useragent=").append(useragent);
        sb.append(", createtime=").append(createtime);
		sb.append('}');
        return sb.toString();
    }
}