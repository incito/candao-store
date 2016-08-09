package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 
 * tb_resource_role表
 * @author mew
 *
 */
public class TbResourceRole implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1911851552964699579L;
	private java.lang.String resourceid; // 
	private java.lang.String roleid; // 

	/**
     * 获取属性
     *
     * @return resourceid
     */
	public java.lang.String getResourceid() {
		return resourceid;
	}
	
	/**
	 * 设置属性
	 *
	 * @param resourceid
	 */
	public void setResourceid(java.lang.String zhiyuanid) {
		this.resourceid = resourceid;
	}
	
	/**
     * 获取属性
     *
     * @return roleid
     */
	public java.lang.String getRoleid() {
		return roleid;
	}
	
	/**
	 * 设置属性
	 *
	 * @param roleid
	 */
	public void setRoleid(java.lang.String roleid) {
		this.roleid = roleid;
	}
	

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TbResourceRole");
        sb.append("{resourceid=").append(resourceid);
        sb.append(", roleid=").append(roleid);
		sb.append('}');
        return sb.toString();
    }
}