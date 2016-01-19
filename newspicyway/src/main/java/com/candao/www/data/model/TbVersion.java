package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 
 * tb_version表
 * @author mew
 *
 */
public class TbVersion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4616096905366575907L;
	private java.lang.String no; // 版本号
	private java.lang.String info; // 版本更新介绍
	private java.lang.String url; // 版本更新裢接
	private java.lang.Integer type; //  终端类型  1： android平板  2：android手机   3:ios平板  4：ios手机
	private java.lang.Integer isforce; // 0：不强制更新 1：强制更新

	/**
     * 获取版本号属性
     *
     * @return no
     */
	public java.lang.String getNo() {
		return no;
	}
	
	/**
	 * 设置版本号属性
	 *
	 * @param no
	 */
	public void setNo(java.lang.String no) {
		this.no = no;
	}
	
	/**
     * 获取版本更新介绍属性
     *
     * @return info
     */
	public java.lang.String getInfo() {
		return info;
	}
	
	/**
	 * 设置版本更新介绍属性
	 *
	 * @param info
	 */
	public void setInfo(java.lang.String info) {
		this.info = info;
	}
	
	/**
     * 获取版本更新裢接属性
     *
     * @return url
     */
	public java.lang.String getUrl() {
		return url;
	}
	
	/**
	 * 设置版本更新裢接属性
	 *
	 * @param url
	 */
	public void setUrl(java.lang.String url) {
		this.url = url;
	}
	
	/**
     * 获取1： android平板  2：android手机   3:ios平板  4：ios手机属性
     *
     * @return type
     */
	public java.lang.Integer getType() {
		return type;
	}
	
	/**
	 * 设置1： android平板  2：android手机   3:ios平板  4：ios手机属性
	 *
	 * @param type
	 */
	public void setType(java.lang.Integer type) {
		this.type = type;
	}
	
	/**
     * 获取0：不强制更新 1：强制更新属性
     *
     * @return isforce
     */
	public java.lang.Integer getIsforce() {
		return isforce;
	}
	
	/**
	 * 设置0：不强制更新 1：强制更新属性
	 *
	 * @param isforce
	 */
	public void setIsforce(java.lang.Integer isforce) {
		this.isforce = isforce;
	}
	

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TbVersion");
        sb.append("{no=").append(no);
        sb.append(", info=").append(info);
        sb.append(", url=").append(url);
        sb.append(", type=").append(type);
        sb.append(", isforce=").append(isforce);
		sb.append('}');
        return sb.toString();
    }
}