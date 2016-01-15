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
public class Role implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6575464123271519359L;
  
  private String id;
  private String code;
  private String name;
  private String type;
  private String scopeCode;
  private String scopeName;
  private String status;
  private String description;
  private Timestamp createtime;
  private Timestamp modifytime;
	private String branch_id;
  
  private List<Function> functions;

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
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
  public Timestamp getModifytime() {
    return modifytime;
  }
  public void setModifytime(Timestamp modifytime) {
    this.modifytime = modifytime;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getScopeCode() {
    return scopeCode;
  }
  public void setScopeCode(String scopeCode) {
    this.scopeCode = scopeCode;
  }
  public String getScopeName() {
    return scopeName;
  }
  public void setScopeName(String scopeName) {
    this.scopeName = scopeName;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public List<Function> getFunctions() {
    return functions;
  }
  public void setFunctions(List<Function> functions) {
    this.functions = functions;
  }
}
