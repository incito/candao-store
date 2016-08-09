/**
 * 代金券
 */
package com.candao.www.data.model;

import java.math.BigDecimal;

/**
 * @author zhao
 *  
 */
public class TbVoucher {
  private String id;
  private String preferential;
  private BigDecimal amount;
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
   * @return the amount
   */
  public BigDecimal getAmount() {
    return amount;
  }
  /**
   * @param amount the amount to set
   */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
  
}
