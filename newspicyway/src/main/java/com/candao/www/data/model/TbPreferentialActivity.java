/**
 * 
 */
package com.candao.www.data.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author zhao
 *
 */
public class TbPreferentialActivity {
  private String id;
  private String code;
  private String name;
  private String nameFirstLetter;
  private String type;
  private String typeName;
  private String subtableName;
  private String subType;
  private String subTypeName;
  private String color;
  private Date starttime; 
  private Date endtime;
  private boolean applyAll;
  private String activityIntroduction;
  private String useNotice;
  private Timestamp createtime;
  private String creator;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getNameFirstLetter() {
    return nameFirstLetter;
  }
  public void setNameFirstLetter(String nameFirstLetter) {
    this.nameFirstLetter = nameFirstLetter;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getTypeName() {
    return typeName;
  }
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
  public String getSubtableName() {
    return subtableName;
  }
  public void setSubtableName(String subtableName) {
    this.subtableName = subtableName;
  }
  public String getColor() {
    return color;
  }
  public void setColor(String color) {
    this.color = color;
  }
  public Date getStarttime() {
    return starttime;
  }
  public void setStarttime(Date starttime) {
    this.starttime = starttime;
  }
  public Date getEndtime() {
    return endtime;
  }
  public void setEndtime(Date endtime) {
    this.endtime = endtime;
  }
  public boolean isApplyAll() {
    return applyAll;
  }
  public void setApplyAll(boolean applyAll) {
    this.applyAll = applyAll;
  }
  public String getActivityIntroduction() {
    return activityIntroduction;
  }
  public void setActivityIntroduction(String activityIntroduction) {
    this.activityIntroduction = activityIntroduction;
  }
  public String getUseNotice() {
    return useNotice;
  }
  public void setUseNotice(String useNotice) {
    this.useNotice = useNotice;
  }
  public Timestamp getCreatetime() {
    return createtime;
  }
  public void setCreatetime(Timestamp createtime) {
    this.createtime = createtime;
  }
  public String getCreator() {
    return creator;
  }
  public void setCreator(String creator) {
    this.creator = creator;
  }
  public String getSubType() {
    return subType;
  }
  public void setSubType(String subType) {
    this.subType = subType;
  }
  public String getSubTypeName() {
    return subTypeName;
  }
  public void setSubTypeName(String subTypeName) {
    this.subTypeName = subTypeName;
  }
}
