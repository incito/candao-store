package com.candao.www.webroom.model;

import java.math.BigDecimal;
import java.util.List;

import com.candao.www.data.model.TbNoDiscountDish;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TbPreferentialActivityBranch;

public class DiscountTicketsVo {
  private TbPreferentialActivity preferentialActivity;
  private BigDecimal discount;
  
  private List<TbNoDiscountDish> noDiscountDish;
  private List<TbPreferentialActivityBranch> branchs;

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

  /**
   * @return the noDiscountDish
   */
  public List<TbNoDiscountDish> getNoDiscountDish() {
    return noDiscountDish;
  }

  /**
   * @param noDiscountDish the noDiscountDish to set
   */
  public void setNoDiscountDish(List<TbNoDiscountDish> noDiscountDish) {
    this.noDiscountDish = noDiscountDish;
  }

  /**
   * @return the preferentialActivity
   */
  public TbPreferentialActivity getPreferentialActivity() {
    return preferentialActivity;
  }

  /**
   * @param preferentialActivity the preferentialActivity to set
   */
  public void setPreferentialActivity(TbPreferentialActivity preferentialActivity) {
    this.preferentialActivity = preferentialActivity;
  }

  /**
   * @return the branchs
   */
  public List<TbPreferentialActivityBranch> getBranchs() {
    return branchs;
  }

  /**
   * @param branchs the branchs to set
   */
  public void setBranchs(List<TbPreferentialActivityBranch> branchs) {
    this.branchs = branchs;
  }
}
