package com.candao.www.data.model;

import java.util.List;
import java.util.Map;

public class TtemplateDetail {
	/**
	 * 主键
	 */
    private String id;
    /**
	 * 模板id
	 */
    private String templateid;
    /**
	 * 菜谱id
	 */
    private String menuid;
    /**
	 * 模板位置
	 */
    private String location;
    /**
	 * 排序
	 */
    private Integer sortnum;
    /**
	 * 关联的菜品id
	 */
    private String redishid;
    /**
     * 菜谱中当前菜的图片
     */
    private String image;
    /**
     * 原始图片
     */
    private String originalImage;
    
    /**
     * 店长推荐（0不推荐1推荐）
     */
    private int recommend;
    
    private List<TtemplateDishUnit>  dishunitlist;
    
    private List<TtemplateDishUnit>  fishpotlist;
    private String level;
    

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(String originalImage) {
		this.originalImage = originalImage;
	}


	public List<TtemplateDishUnit> getFishpotlist() {
		return fishpotlist;
	}

	public void setFishpotlist(List<TtemplateDishUnit> fishpotlist) {
		this.fishpotlist = fishpotlist;
	}


	private String dishtype;

    public String getDishtype() {
		return dishtype;
	}

	public void setDishtype(String dishtype) {
		this.dishtype = dishtype;
	}

	public List<TtemplateDishUnit> getDishunitlist() {
		return dishunitlist;
	}

	public void setDishunitlist(List<TtemplateDishUnit> dishunitlist) {
		this.dishunitlist = dishunitlist;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateid() {
        return templateid;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public String getRedishid() {
        return redishid;
    }

    public void setRedishid(String redishid) {
        this.redishid = redishid;
    }
}