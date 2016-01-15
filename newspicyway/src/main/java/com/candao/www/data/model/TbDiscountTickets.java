/**
 * 
 */
package com.candao.www.data.model;

import java.math.BigDecimal;

/**
 * @author zhao
 *
 */
public class TbDiscountTickets{
  private String id;
  private String preferential;
  private BigDecimal discount;
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
   * @return the discount
   */
  public BigDecimal getDiscount() {
    return discount;
  }
  /**
   * @param discount the discount to set
   */
  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }
}
