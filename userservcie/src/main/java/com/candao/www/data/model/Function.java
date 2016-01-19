/**
 * 
 */
package com.candao.www.data.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author zhao
 *
 */
public class Function implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2398965840999104813L;
  
  private String id;
  private String code;
  private String name;
  private String application;
  private String applicationName;
  private String applicationType;
  private String webroot;
  private int level;
  private String parentFun;
  private String isLeaf;
  private String operationType;
  private String status;
  private String scopeCode;
  private String scopeName;
  private String description;
  private Timestamp createtime;
  private String creator;
  
  private List<String> urls; 
  
  private List<Function> childFuns;
  
  /**
   * 存放该节点的父节点。如果不存在，则为NULL
   */
  private Function parentFunction;
  
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
  public String getApplication() {
    return application;
  }
  public void setApplication(String application) {
    this.application = application;
  }
  public int getLevel() {
    return level;
  }
  public void setLevel(int level) {
    this.level = level;
  }
  public String getParentFun() {
    return parentFun;
  }
  public void setParentFun(String parentFun) {
    this.parentFun = parentFun;
  }
  public String getIsLeaf() {
    return isLeaf;
  }
  public void setIsLeaf(String isLeaf) {
    this.isLeaf = isLeaf;
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
   * @return the urls
   */
  public List<String> getUrls() {
    return urls;
  }
  /**
   * @param urls the urls to set
   */
  public void setUrls(List<String> urls) {
    this.urls = urls;
  }
  /**
   * @return the childFuns
   */
  public List<Function> getChildFuns() {
    return childFuns;
  }
  /**
   * @param childFuns the childFuns to set
   */
  public void setChildFuns(List<Function> childFuns) {
    this.childFuns = childFuns;
  }
  /**
   * @return the operationType
   */
  public String getOperationType() {
    return operationType;
  }
  /**
   * @param operationType the operationType to set
   */
  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }
  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }
  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }
  /**
   * @return the scopeCode
   */
  public String getScopeCode() {
    return scopeCode;
  }
  /**
   * @param scopeCode the scopeCode to set
   */
  public void setScopeCode(String scopeCode) {
    this.scopeCode = scopeCode;
  }
  /**
   * @return the scopeName
   */
  public String getScopeName() {
    return scopeName;
  }
  /**
   * @param scopeName the scopeName to set
   */
  public void setScopeName(String scopeName) {
    this.scopeName = scopeName;
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
  /**
   * @return the applicationName
   */
  public String getApplicationName() {
    return applicationName;
  }
  /**
   * @param applicationName the applicationName to set
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }
  /**
   * @return the applicationType
   */
  public String getApplicationType() {
    return applicationType;
  }
  /**
   * @param applicationType the applicationType to set
   */
  public void setApplicationType(String applicationType) {
    this.applicationType = applicationType;
  }
  /**
   * @return the webroot
   */
  public String getWebroot() {
    return webroot;
  }
  /**
   * @param webroot the webroot to set
   */
  public void setWebroot(String webroot) {
    this.webroot = webroot;
  }
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public Function getParentFunction() {
	return parentFunction;
}
public void setParentFunction(Function parentFunction) {
	this.parentFunction = parentFunction;
}
  
}
