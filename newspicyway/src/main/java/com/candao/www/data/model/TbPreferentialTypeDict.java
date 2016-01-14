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
public class TbPreferentialTypeDict {
  private String code;
  private String name;
  private String subtableName;
  private boolean isSubtype;
  private String parentType;
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
  public String getSubtableName() {
    return subtableName;
  }
  public void setSubtableName(String subtableName) {
    this.subtableName = subtableName;
  }
  /**
   * @return the isSubtype
   */
  public boolean isSubtype() {
    return isSubtype;
  }
  /**
   * @param isSubtype the isSubtype to set
   */
  public void setSubtype(boolean isSubtype) {
    this.isSubtype = isSubtype;
  }
  /**
   * @return the parentType
   */
  public String getParentType() {
    return parentType;
  }
  /**
   * @param parentType the parentType to set
   */
  public void setParentType(String parentType) {
    this.parentType = parentType;
  }
}
