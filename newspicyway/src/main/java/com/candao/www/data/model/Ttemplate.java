package com.candao.www.data.model;

import java.util.List;


public class Ttemplate {
	/**
	 * 主键
	 */
    private String id;
    private String templateid;
    /**
     * 创建者id
     */
    private String createuserid;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 
     */
    private String data;
    /**
     * 状态
     */
    private Integer status;
    /**
     * releasetime和createtime值相同，如果修改了，releasetime就是更新时间，排序按创建时间createtime排序
     */
    private String releasetime;
    /**
     * 创建时间
     */
    private String createtime;
    /**
     * 文章id，用逗号分隔
     */
    private String articleids;
    /**
     * 
     */
    private String dishtype;
    /**
     * 分类id
     */
    private String columnid;
    /**
     * 菜谱id
     */
    private String menuid;
    /**
     * 排序
     */
    private int sort;
    private String  itemDesc;
    /**
     * 分类排序
     */
    private int columnsort;
    
    private List<TtemplateDetail> detaillist;
    
    public int getColumnsort() {
		return columnsort;
	}

	public void setColumnsort(int columnsort) {
		this.columnsort = columnsort;
	}

	public String getTemplateid() {
		return templateid;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public List<TtemplateDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<TtemplateDetail> detaillist) {
		this.detaillist = detaillist;
	}

	public String getColumnid() {
		return columnid;
	}

	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getDishtype() {
		return dishtype;
	}

	public void setDishtype(String dishtype) {
		this.dishtype = dishtype;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getArticleids() {
        return articleids;
    }

    public void setArticleids(String articleids) {
        this.articleids = articleids;
    }
}