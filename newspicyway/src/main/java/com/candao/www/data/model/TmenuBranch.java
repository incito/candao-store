package com.candao.www.data.model;

public class TmenuBranch {
	/**
	 * 主键
	 */
    private String id;
    /**
	 * 菜谱id
	 */
    private String menuid;
    /**
	 * 分店id
	 */
    private Integer branchid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public Integer getBranchid() {
        return branchid;
    }

    public void setBranchid(Integer branchid) {
        this.branchid = branchid;
    }
}