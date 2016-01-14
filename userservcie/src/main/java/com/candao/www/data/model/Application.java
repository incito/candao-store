/**
 * 
 */
package com.candao.www.data.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhao
 *
 */
public class Application implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 9068108657195173128L;
  private String id;
  private String name;
  private String type;
  private String webroot;
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
  public String getWebroot() {
    return webroot;
  }
  public void setWebroot(String webroot) {
    this.webroot = webroot;
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
  
}
