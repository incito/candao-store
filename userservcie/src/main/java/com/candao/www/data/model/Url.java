package com.candao.www.data.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Url implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -2993095519235633444L;
  
  private String id;
  private String name;
  private String url;
  private String type;
  private String description;
  private Timestamp createtime;
  private String creator;
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public Timestamp getCreatetime() {
    return createtime;
  }
  public void setCreatetime(Timestamp createtime) {
    this.createtime = createtime;
  }
  /**
   * @return the creator
   */
  public String getCreator() {
    return creator;
  }
  /**
   * @param creator the creator to set
   */
  public void setCreator(String creator) {
    this.creator = creator;
  }
}
