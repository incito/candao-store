/**
 * 
 */
package com.candao.www.data.model;

/**
 * @author zhao
 *
 */
public class TbNoDiscountDish {
	private String id;
	private String discountTicket;
	private String dish;
	private String dish_title;
	private int dishtype;
	private String unit;
	private boolean unitflag;
	private String itemtype;
	private String itemdesc;
	private boolean item_select;

	/**
	 * @return the discountTicket
	 */
	public String getDiscountTicket() {
		return discountTicket;
	}

	/**
	 * @param discountTicket
	 *            the discountTicket to set
	 */
	public void setDiscountTicket(String discountTicket) {
		this.discountTicket = discountTicket;
	}

	/**
	 * @return the dish
	 */
	public String getDish() {
		return dish;
	}

	/**
	 * @param dish
	 *            the dish to set
	 */
	public void setDish(String dish) {
		this.dish = dish;
	}

	/**
	 * @return the dish_titles
	 */
	public String getDish_title() {
		return dish_title;
	}

	/**
	 * @param dish_titles
	 *            the dish_titles to set
	 */
	public void setDish_title(String dish_title) {
		this.dish_title = dish_title;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
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
	 * @param unitflag
	 *            the unitflag to set
	 */
	public void setUnitflag(boolean unitflag) {
		this.unitflag = unitflag;
	}

	/**
	 * @return the itemtype
	 */
	public String getItemtype() {
		return itemtype;
	}

	/**
	 * @param itemtype
	 *            the itemtype to set
	 */
	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	/**
	 * @return the itemdesc
	 */
	public String getItemdesc() {
		return itemdesc;
	}

	/**
	 * @param itemdesc
	 *            the itemdesc to set
	 */
	public void setItemdesc(String itemdesc) {
		this.itemdesc = itemdesc;
	}

	/**
	 * @return the item_select
	 */
	public boolean isItem_select() {
		return item_select;
	}

	/**
	 * @param item_select
	 *            the item_select to set
	 */
	public void setItem_select(boolean item_select) {
		this.item_select = item_select;
	}

	public int getDishtype() {
		return dishtype;
	}

	public void setDishtype(int dishtype) {
		this.dishtype = dishtype;
	}

}
