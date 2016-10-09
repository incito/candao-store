package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TbNoDiscountDish;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/**
 * 
 * @author Candao 折扣优惠卷
 */
public class DiscountTicketStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {
		String orderid = (String) paraMap.get("orderid"); // 账单号
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String branchid = PropertiesUtils.getValue("current_branch_id");
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid, branchid, tbPreferentialActivityDao);
		Map tempMap = tempMapList.get(0);
		BigDecimal discount = (BigDecimal) tempMap.get("discount");

		// 获取当前账单的 菜品列表
		Map<String, Object> result = new HashMap<>();
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);

		// 将菜单表转换为菜品编号(key):菜品交易详情（Value）
		Set<String> orderDetailSet=new HashSet<>();
		for (TorderDetail detail : orderDetailList) {
			String key=detail.getDishid()+detail.getDishunit();
			orderDetailSet.add(key);
		}
		// 获取特价券 不参与折扣的菜品。并放入map
		Map<String, Object> noDisMap = new HashMap<>();
		noDisMap.put("discountId", preferentialid);
		noDisMap.put("orderDetail", orderDetailList);
		List<TbNoDiscountDish> noDiscountDishlist = tbDiscountTicketsDao.getNoDiscountDishsByDish(noDisMap);
		// 用map方式存放不参与折扣的菜品
		Set<String> noDiscountDishSet=new HashSet<>();
		// 根据不优惠的菜品获取菜品库的菜品信息
		for (TbNoDiscountDish t : noDiscountDishlist) {
			String dishID=t.getDish();
			String dishUnit=t.getUnit();
			// 处理鱼锅
			Tdish tdish = tdishDao.get(dishID);
			if (tdish.getDishtype() == 1 &&  orderDetailSet.contains(dishID+dishUnit)) {
				List<Tdish> tdishList = tbDiscountTicketsDao.getDishidList(dishID);
				if (tdishList.size() > 0) {
					for (Tdish dish : tdishList) {
						noDiscountDishSet.add(dish.getDishid());
					}
				}
			}
			noDiscountDishSet.add(dishID+dishUnit);
		}
		// 4.遍历账单的菜品，如果输入不进行折扣的菜品，则不处理。否则，认为是需要计算优惠。
		BigDecimal orignalprice = null;
		BigDecimal amount = new BigDecimal(0); // 最终优惠金额
		// 菜品原价
		BigDecimal amountCount = new BigDecimal(0.0);
		// 优惠折扣菜单个数
		double tempDishNum = 0;
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		for (TorderDetail d : orderDetailList) {
			String key=d.getDishid()+d.getDishunit();
			if (!noDiscountDishSet.contains(key)) {
				// 如果在不优惠的列表中没有找到这个菜品，则认为这个菜品是可以优惠打折的。
				// 判断价格，如果菜品价格存在null的问题，
				if (null != d.getOrderprice()) {
					orignalprice = d.getOrderprice().multiply(discount.divide(new BigDecimal(10))); // 设置优惠后的金额
					// 如果此菜品是多份，则计算多份总的优惠价格
					BigDecimal numOfDish = new BigDecimal("0");
					if (new BigDecimal(d.getDishnum()).compareTo(new BigDecimal("0")) > 0) {
						numOfDish = new BigDecimal(d.getDishnum());
					}
					orignalprice = orignalprice.multiply(numOfDish);
					amountCount = amountCount.add(d.getOrderprice().multiply(numOfDish));
					tempDishNum += Double.valueOf(d.getDishnum());
				}
			}
		}
		// 如果需要折扣的菜品的总价不大于0或者小于已经折扣掉的金额，则不计算本次折扣金额
		if (amountCount.compareTo(BigDecimal.ZERO) > 0 && (amountCount.subtract(bd).compareTo(BigDecimal.ZERO)) != -1) {
			amount = amountCount.subtract(bd)
					.multiply(new BigDecimal("1").subtract(discount.divide(new BigDecimal(10))));
			// 是重新计算优惠还是，新加优惠

			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
			Date insertime = (paraMap.containsKey("insertime") ?  (Date) paraMap.get("insertime")
					: new Date());
			TorderDetailPreferential addPreferential = new TorderDetailPreferential(updateId, orderid, "",
					preferentialid, amount, String.valueOf(tempDishNum), 1, 1, discount, 0,insertime);
			// 设置优惠名称
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) tempMap.get("name"));
			addPreferential.setActivity(activity);
			addPreferential.setCoupondetailid(
					(String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id")));

			// 设置优免金额
			addPreferential.setToalFreeAmount(amount);

			detailPreferentials.add(addPreferential);

		} else {
			// 如果为空说明当前已经删除了此菜品，那么就应该删除此优惠卷
			if(paraMap.containsKey("updateId")){
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("DetalPreferentiald",  paraMap.get("updateId"));
				delMap.put("orderid", orderid);
				orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
			}
		
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
