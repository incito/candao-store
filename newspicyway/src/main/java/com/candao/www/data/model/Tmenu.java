package com.candao.www.data.model;

import java.util.List;
import java.util.Map;

public class Tmenu {
	/**
	 * 菜谱id
	 */
    private String menuid;
    /**
     * 菜品名称			
     */
    private String menuname;
    /**
     * 启用时间
     */
    private String effecttime;
    /**
     * 菜谱状态（0未启用,1已启用,2定时启用,3删除）
     */
    private Integer status;
    /**
     * 创建者id
     */
    private String createuserid;
    /**
     * 创建时间
     */
    private String createtime;
    /**
     * 排序
     */
    private Integer sortnum;
    /**
     * 查询菜谱适用的门店
     */
    private List<Map<String,Object>> branchMap;
    /**
     * 当前菜谱适用门店的id的list
     */
    private List<Integer> branchidlist;
    /**
     * 表示菜谱是否适用所有门店
     * flag=0 非全选
     * flag=1 全选
     */
    private int flag;

    public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public List<Integer> getBranchidlist() {
		return branchidlist;
	}

	public void setBranchidlist(List<Integer> branchidlist) {
		this.branchidlist = branchidlist;
	}

	public List<Map<String, Object>> getBranchMap() {
		return branchMap;
	}

	public void setBranchMap(List<Map<String, Object>> branchMap) {
		this.branchMap = branchMap;
	}

	public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }


    public String getEffecttime() {
		return effecttime;
	}

	public void setEffecttime(String effecttime) {
		this.effecttime = effecttime;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }


    public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }
}