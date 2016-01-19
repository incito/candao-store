package com.candao.www.data.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * 优惠活动->其他优惠->内部减免（合作单位）
 * @author YHL
 *
 */
public class TbInnerFree {

	/**
	 * 主键
	 */
	public String id ;
	
	/**
	 * 对应的优惠活动
	 */
	public String preferential ;
	
	/**
	 * 公司名称
	 */
	public String company_name ;
	
	/**
	 * 公司名称首字母
	 */
	public String company_first_letter ;
	
	/**
	 * 排序
	 */
	public int sequence ;
	
	/**
	 * 折扣
	 */
	public BigDecimal discount ;
	
	/**
	 * 是否可记账
	 */
	public boolean can_credit ;
	
	/**
	 * 有效期开始时间
	 */
	public Date starttime;
	
	/**
	 * 有效期结束时间
	 */
	public Date endtime;

	public Timestamp createtime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPreferential() {
		return preferential;
	}

	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCompany_first_letter() {
		return company_first_letter;
	}

	public void setCompany_first_letter(String company_first_letter) {
		this.company_first_letter = company_first_letter;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public boolean isCan_credit() {
		return can_credit;
	}

	public void setCan_credit(boolean can_credit) {
		this.can_credit = can_credit;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
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
