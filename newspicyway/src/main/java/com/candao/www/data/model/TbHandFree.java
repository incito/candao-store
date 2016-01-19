/**
 * 手工优免
 */
package com.candao.www.data.model;

import java.sql.Timestamp;


/**
 * @author zhao
 *  
 */
public class TbHandFree {
  private String id;
  private String preferential;
  private String freeReason;
  private Timestamp createtime;
  /**
   * @return the id
   */
  public String getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }
  /**
   * @return the preferential
   */
  public String getPreferential() {
    return preferential;
  }
  /**
   * @param preferential the preferential to set
   */
  public void setPreferential(String preferential) {
    this.preferential = preferential;
  }
  /**
   * @return the freeReason
   */
  public String getFreeReason() {
    return freeReason;
  }
  /**
   * @param freeReason the freeReason to set
   */
  public void setFreeReason(String freeReason) {
    this.freeReason = freeReason;
  }
  /**
   * @return the createtime
   */
  public Timestamp getCreatetime() {
    return createtime;
  }
  /**
   * @param createtime the createtime to set
   */
  public void setCreatetime(Timestamp createtime) {
    this.createtime = createtime;
  }
  
}
