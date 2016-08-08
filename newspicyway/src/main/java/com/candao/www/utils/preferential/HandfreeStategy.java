package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/**
 * 
 * @author Candao 手工优惠---手工优免 三种优惠方式：赠菜，折扣，优免 此类优惠需要收银员手动输入的
 */
public class HandfreeStategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String orderid = (String) paraMap.get("orderid"); // 账单号
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		String disrate = String.valueOf(paraMap.get("disrate"));
		String giveDish = (String) paraMap.get("dishid");
		int preferentialNum = Integer.valueOf((String) paraMap.get("preferentialNum"));
		BigDecimal discount = new BigDecimal(disrate.trim().isEmpty() ? "0" : disrate);
		Map<String, Object> cashGratis = cashGratis(paraMap, torderDetailDao, tbPreferentialActivityDao);
		if (cashGratis != null) {
			return cashGratis;
		}

		// 返回优惠集合
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		/** 优惠卷信息 **/
		Map tempMap = this.discountInfo(preferentialid, PropertiesUtils.getValue("current_branch_id"),
				tbPreferentialActivityDao);

		/** 当前订单信息 **/
		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
		// 将List转换成MAP
		Map<String, TorderDetail> orderDetailmap = new HashMap<>();
		for (TorderDetail detail : orderDetailList) {
			orderDetailmap.put(detail.getDishid(), detail);
		}
		// 当前订单的原始价格
		BigDecimal amount = new BigDecimal(0);
		// 菜品原价
		BigDecimal amountCount = new BigDecimal(0.0);
		for (TorderDetail d : orderDetailList) {
			// 判断价格，如果菜品价格存在null的问题，则返回错误信息
			if (null != d.getOrderprice()) {
				// 如果此菜品是多份，则计算多份总的优惠价格
				BigDecimal numOfDish = new BigDecimal("1");
				if (new BigDecimal(d.getDishnum()).compareTo(new BigDecimal("0")) > 0) {
					numOfDish = new BigDecimal(d.getDishnum());
				}
				amountCount = amountCount.add(d.getOrderprice().multiply(numOfDish));
			}

		}
		/**
		 * disrate不为空为折扣优免 preferentialAmout 不为空为现金优惠 giveDish 不为空为优惠卷信息
		 */
		String preferentialAmout = (String) paraMap.get("preferentialAmout");
		if (!StringUtils.isEmpty(disrate.trim())&&new BigDecimal(preferentialAmout).doubleValue()<=0 &&StringUtils.isEmpty(giveDish)) {
			BigDecimal decimalDisrate = new BigDecimal(disrate);
			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
			
			// 如果需要折扣的菜品的总价不大于0或者小于已经折扣掉的金额，则不计算本次折扣金额
			// 手工输入折扣优免
			if (amountCount.compareTo(BigDecimal.ZERO) > 0 && (amountCount.subtract(bd).compareTo(BigDecimal.ZERO)) != -1){
				amount = amountCount.subtract(bd)
						.multiply(new BigDecimal("1").subtract(decimalDisrate.divide(new BigDecimal(10))));
				TorderDetailPreferential addPreferential = new TorderDetailPreferential(updateId, orderid, "",
						preferentialid, amount, String.valueOf(orderDetailList.size()), 1, 1, discount, 1);
				//设置优惠名称
				TbPreferentialActivity activity = new TbPreferentialActivity();
				activity.setName((String) tempMap.get("name"));
				addPreferential.setActivity(activity);
				//设置优免
				addPreferential.setToalFreeAmount(amount);
				detailPreferentials.add(addPreferential);
			}

		} else if (!StringUtils.isEmpty(preferentialAmout.trim())&&StringUtils.isEmpty(giveDish)&&StringUtils.isEmpty(disrate.trim())) {
			// 手工输入现金优免
			BigDecimal cashprelAmout = new BigDecimal(preferentialAmout);
			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
			amount = cashprelAmout;
			TorderDetailPreferential addPreferential = new TorderDetailPreferential(updateId, orderid, "",
					preferentialid, amount, String.valueOf(orderDetailList.size()), 1, 1, discount, 1);
			//设置优惠名称
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) tempMap.get("name"));
			addPreferential.setActivity(activity);
			//设置优免
			addPreferential.setToalFreeAmount(amount);
			detailPreferentials.add(addPreferential);
		} else if (!StringUtils.isEmpty(giveDish)&&StringUtils.isEmpty(paraMap.get("discount"))&&StringUtils.isEmpty(paraMap.get("amount"))) {
			// 赠菜卷，赠卷有dishID，无折扣 ，无现金优免
			TorderDetail ordetail = orderDetailmap.get(giveDish);
			if (ordetail != null) {
				for (int i = 0; i < preferentialNum; i++) {
					String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId")
							: IDUtil.getID();
					BigDecimal orderprice=ordetail.getOrderprice();
					amount=amount.add(orderprice);
					TorderDetailPreferential addPreferential = new TorderDetailPreferential(updateId, orderid,
							ordetail.getDishid(), preferentialid, orderprice,
							String.valueOf(orderDetailList.size()), 0, 1, discount, 0);
					//设置优惠名称
					TbPreferentialActivity activity = new TbPreferentialActivity();
					activity.setName((String) tempMap.get("name"));
					addPreferential.setActivity(activity);
					//设置优免
					addPreferential.setToalFreeAmount(amount);
					detailPreferentials.add(addPreferential);
				}
			}
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
