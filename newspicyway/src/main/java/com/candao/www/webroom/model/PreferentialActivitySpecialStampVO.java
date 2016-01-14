/**
 * 
 */
package com.candao.www.webroom.model;

import java.util.List;

import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TbPreferentialActivityBranch;
import com.candao.www.data.model.TbPreferentialActivitySpecialStamp;

/**
 * 优惠-->特价券，VO
 * <pre>
 * 特价券构成如下方式：
 *     1个优惠券明细
 *     N个特价券明细（主要是指菜品明细）
 * </pre>
 * @author yanghongli
 * @version 1.0
 * @date 2015-03-12
 */
public class PreferentialActivitySpecialStampVO {
	
	/**
	 * 优惠券类型。（参见表 t_p_preferential_type_dict）
	 * 当前为 01，代表特价券
	 */
	public static final String PREFERENTIAL_TYPE="01"; 

	/**
	 * 优惠券明细（主要是优惠券介绍等信息）
	 */
	private TbPreferentialActivity activity;
	
	/**
	 * 特价券列表
	 */
	private List<TbPreferentialActivitySpecialStamp> stamps;

	/**
	 * 优惠门店。
	 * <br/>
	 * 	如果 “适用门店” 选择了所有门店，则这里为空。
	 * 	如果 “适用门店” 选择了指定门店，这这里为指定的门店的列表。
	 */
	private List<TbPreferentialActivityBranch> branchs;
	
	//----------getter\setter-----------\\
	/**
	 * @return the activity
	 */
	public TbPreferentialActivity getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(TbPreferentialActivity activity) {
		this.activity = activity;
	}

	/**
	 * @return the stamps
	 */
	public List<TbPreferentialActivitySpecialStamp> getStamps() {
		return stamps;
	}

	/**
	 * @param stamps the stamps to set
	 */
	public void setStamps(List<TbPreferentialActivitySpecialStamp> stamps) {
		this.stamps = stamps;
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
