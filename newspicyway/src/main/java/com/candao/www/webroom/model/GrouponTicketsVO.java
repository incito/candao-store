/**
 * 
 */
package com.candao.www.webroom.model;

import java.util.List;

import com.candao.www.data.model.TbGroupon;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TbPreferentialActivityBranch;

/**
 * 优惠券-->团购券的VO类。封装
 * @author YHL
 *
 */
public class GrouponTicketsVO {

	/**
	 * 团购
	 */
	 public TbGroupon groupon; 
	 
	 /**
	  * 优惠对象信息
	  */
	 public TbPreferentialActivity activity;
	 
	 /**
	  * 优惠的门店
	  */
	 public List<TbPreferentialActivityBranch> branchs;

	 
	 
	 
	public TbGroupon getGroupon() {
		return groupon;
	}

	public void setGroupon(TbGroupon groupon) {
		this.groupon = groupon;
	}

	public TbPreferentialActivity getActivity() {
		return activity;
	}

	public void setActivity(TbPreferentialActivity activity) {
		this.activity = activity;
	}

	public List<TbPreferentialActivityBranch> getBranchs() {
		return branchs;
	}

	public void setBranchs(List<TbPreferentialActivityBranch> branchs) {
		this.branchs = branchs;
	}
	 
	 
	 
}
