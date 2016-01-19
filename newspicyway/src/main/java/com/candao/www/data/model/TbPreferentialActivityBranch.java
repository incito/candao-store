/**
 * 
 */
package com.candao.www.data.model;

/**
 * 优惠券-->优惠门店
 * 
 * @author yanghongli
 * @version 1.0
 * @date 2015-03-12
 */
public class TbPreferentialActivityBranch {
	
	/**
	 * 优惠券（ID）。外键，对应TbPreferentialActivity（表：t_p_preferential_activity）对象
	 */
	private String preferential;
	
	/**
	 * 门店（ID）。外键，对应Tbranchshop(表：t_branchshop)
	 */
	private int branch ;
	
	/**
	 * 门店名称
	 */
	private String branch_name;

	
	//-----------getter\setter-----------\\
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
	 * @return the branch
	 */
	public int getBranch() {
		return branch;
	}

	/**
	 * @param branch the branch to set
	 */
	public void setBranch(int branch) {
		this.branch = branch;
	}

	/**
	 * @return the branch_name
	 */
	public String getBranch_name() {
		return branch_name;
	}

	/**
	 * @param branch_name the branch_name to set
	 */
	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	
}
