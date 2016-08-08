package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 
 * tb_resource表
 * @author mew
 *
 */
public class TbResource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8454301975583999245L;
	private java.lang.String resourcesid; // 资源标识
	private java.lang.String resourcespid; // 资源父标识
	private java.lang.String resourcesname; // 资源名称
	private java.lang.String resourcespath; // 资源路径
	private java.lang.String resourcesdesc; // 资源描述
	private java.lang.String resourcestype; // 资源类型
	private java.lang.String indexvalue; // 资源权值
	private java.lang.Integer status; // 状态
	private java.util.Date createtime; // 创建时间
	/**
	 * 有效
	 */
	public static final int STATUS_ENABLE = 1;
	/**
	 * 失效
	 */
	public static final int STATUS_DISABLE = 0;
	/**
     * 获取资源标识属性
     *
     * @return resourcesid
     */
	public java.lang.String getResourcesid() {
		return resourcesid;
	}
	
	/**
	 * 设置资源标识属性
	 *
	 * @param resourcesid
	 */
	public void setResourcesid(java.lang.String resourcesid) {
		this.resourcesid = resourcesid;
	}
	
	/**
     * 获取资源父标识属性
     *
     * @return resourcespid
     */
	public java.lang.String getResourcespid() {
		return resourcespid;
	}
	
	/**
	 * 设置资源父标识属性
	 *
	 * @param resourcespid
	 */
	public void setResourcespid(java.lang.String resourcespid) {
		this.resourcespid = resourcespid;
	}
	
	/**
     * 获取资源名称属性
     *
     * @return resourcesname
     */
	public java.lang.String getResourcesname() {
		return resourcesname;
	}
	
	/**
	 * 设置资源名称属性
	 *
	 * @param resourcesname
	 */
	public void setResourcesname(java.lang.String resourcesname) {
		this.resourcesname = resourcesname;
	}
	
	/**
     * 获取资源路径属性
     *
     * @return resourcespath
     */
	public java.lang.String getResourcespath() {
		return resourcespath;
	}
	
	/**
	 * 设置资源路径属性
	 *
	 * @param resourcespath
	 */
	public void setResourcespath(java.lang.String resourcespath) {
		this.resourcespath = resourcespath;
	}
	
	/**
     * 获取资源描述属性
     *
     * @return resourcesdesc
     */
	public java.lang.String getResourcesdesc() {
		return resourcesdesc;
	}
	
	/**
	 * 设置资源描述属性
	 *
	 * @param resourcesdesc
	 */
	public void setResourcesdesc(java.lang.String resourcesdesc) {
		this.resourcesdesc = resourcesdesc;
	}
	
	/**
     * 获取资源类型属性
     *
     * @return resourcestype
     */
	public java.lang.String getResourcestype() {
		return resourcestype;
	}
	
	/**
	 * 设置资源类型属性
	 *
	 * @param resourcestype
	 */
	public void setResourcestype(java.lang.String resourcestype) {
		this.resourcestype = resourcestype;
	}
	
	/**
     * 获取资源权值属性
     *
     * @return indexvalue
     */
	public java.lang.String getIndexvalue() {
		return indexvalue;
	}
	
	/**
	 * 设置资源权值属性
	 *
	 * @param indexvalue
	 */
	public void setIndexvalue(java.lang.String indexvalue) {
		this.indexvalue = indexvalue;
	}
	
	/**
     * 获取状态属性
     *
     * @return status
     */
	public java.lang.Integer getStatus() {
		return status;
	}
	
	/**
	 * 设置状态属性
	 *
	 * @param status
	 */
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	
	/**
     * 获取创建时间属性
     *
     * @return createtime
     */
	public java.util.Date getCreatetime() {
		return createtime;
	}
	
	/**
	 * 设置创建时间属性
	 *
	 * @param createtime
	 */
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}
	

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TbResource");
        sb.append("{resourcesid=").append(resourcesid);
        sb.append(", resourcespid=").append(resourcespid);
        sb.append(", resourcesname=").append(resourcesname);
        sb.append(", resourcespath=").append(resourcespath);
        sb.append(", resourcesdesc=").append(resourcesdesc);
        sb.append(", resourcestype=").append(resourcestype);
        sb.append(", indexvalue=").append(indexvalue);
        sb.append(", status=").append(status);
        sb.append(", createtime=").append(createtime);
		sb.append('}');
        return sb.toString();
    }
}