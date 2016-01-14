package com.candao.www.data.model;

import java.util.Date;
import java.util.List;

public class TbasicData {
	/**
	 * 主键
	 */
    private String id;
    /**
     * 项目编号
     */
    private String itemid;
    /**
     * 项目名称
     */
    private String itemdesc;
    /**
     * 排序
     */
    private Integer itemsort;
    /**
     * 是否在pad端展示 0不展示  1展示
     */
    private Integer isShow;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 父id
     */
    private String fid;
    /**
     * 上级分类
     */
    private String fitemDesc;
    /**
     * 深度
     */
    private Integer depthnum;
    /**
     * 字典类型
     */
    private String itemtype;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;
    

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getFitemDesc() {
		return fitemDesc;
	}

	public void setFitemDesc(String fitemDesc) {
		this.fitemDesc = fitemDesc;
	}

	private List<TbasicData> children;
    

    public List<TbasicData> getChildren() {
		return children;
	}

	public void setChildren(List<TbasicData> children) {
		this.children = children;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public Integer getItemsort() {
        return itemsort;
    }

    public void setItemsort(Integer itemsort) {
        this.itemsort = itemsort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Integer getDepthnum() {
        return depthnum;
    }

    public void setDepthnum(Integer depthnum) {
        this.depthnum = depthnum;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}