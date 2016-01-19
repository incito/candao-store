package com.candao.www.webroom.model;

import java.math.BigDecimal;
import java.util.List;

import com.candao.www.data.model.TbNoDiscountDish;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TbPreferentialActivityBranch;

public class VoucherVo {
  private TbPreferentialActivity preferentialActivity;
  private BigDecimal amount;
  
  private List<TbPreferentialActivityBranch> branchs;

  /**
   * @return the discount
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * @param discount the discount to set
   */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
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
