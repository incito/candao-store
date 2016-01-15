/**
 * 
 */
package com.candao.www.data.model;

import java.io.Serializable;

/**
 * @author zhao
 *
 */
public class RoleFunction implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6844569776980766006L;
  private String id;
  private String role;
  private String function;
  private String roleName; //角色表
  private String functionName; //功能表
//  private String 
  private String urls; //可访问url
  
  public String getRole() {
    return role;
  }
  public void setRole(String role) {
    this.role = role;
  }
  public String getFunction() {
    return function;
  }
  public void setFunction(String function) {
    this.function = function;
  }
  public String getRoleName() {
    return roleName;
  }
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  public String getFunctionName() {
    return functionName;
  }
  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }
  public String getUrls() {
    return urls;
  }
  public void setUrls(String urls) {
    this.urls = urls;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  
}
