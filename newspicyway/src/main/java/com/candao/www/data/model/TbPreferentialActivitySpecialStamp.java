/**
 * 
 */
package com.candao.www.data.model;

import java.math.BigDecimal;

/**
 * 优惠券-->特价券明细
 * 对应表： t_p_special_ticket
 * @author yanghongli
 * @version 1.0
 * @date 2015-03-12
 */
public class TbPreferentialActivitySpecialStamp {
	
	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 外键 。 优惠券，对应类 TbPreferentialActivity 
	 */
	private String preferential ;
	
	/**
	 * 外键。菜品，对应类 Tdish
	 */
	private String dish;
	
	/**
	 * 菜品名称
	 */
	private String dish_title;
	
	/**
	 * 价格
	 */
	private BigDecimal price ;
	
	/**
	 * 排序
	 */
	private int sequence ;
	
	private String unit;

	private boolean unitflag;
	
	
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
	 * @return the dish
	 */
	public String getDish() {
		return dish;
	}

	/**
	 * @param dish the dish to set
	 */
	public void setDish(String dish) {
		this.dish = dish;
	}

	/**
	 * @return the dish_title
	 */
	public String getDish_title() {
		return dish_title;
	}

	/**
	 * @param dish_title the dish_title to set
	 */
	public void setDish_title(String dish_title) {
		this.dish_title = dish_title;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

  /**
   * @return the unit
   */
  public String getUnit() {
    return unit;
  }

  /**
   * @param unit the unit to set
   */
  public void setUnit(String unit) {
    this.unit = unit;
  }

  /**
   * @return the unitflag
   */
  public boolean isUnitflag() {
    return unitflag;
  }

  /**
   * @param unitflag the unitflag to set
   */
  public void setUnitflag(boolean unitflag) {
    this.unitflag = unitflag;
  }

}
