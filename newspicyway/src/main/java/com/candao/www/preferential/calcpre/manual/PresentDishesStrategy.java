package com.candao.www.preferential.calcpre.manual;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TbOrderDetailPreInfo;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.preferential.calcpre.CalPreferentialStrategy;

/**
 * 
 * @author Liangdong 手工优惠---赠送菜品优惠
 */
public class PresentDishesStrategy extends CalPreferentialStrategy {

	private List<Map<String, Object>> tempMapList;

	public PresentDishesStrategy(List<Map<String, Object>> tempMapList) {
		this.tempMapList = tempMapList;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		Map<String, ComplexTorderDetail> orederMenuMap = new HashMap<>();
		// 返回优惠集合
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();

		// 赠菜卷，赠卷有dishID，无折扣 ，无现金优免
		// 获取所有ID 以及对应的菜品个数
		String giveDish = (String) paraMap.get("dishid");
		String[] preDishNum = ((String) paraMap.get("preferentialNum")).split(",");
		String[] preALLDishId = giveDish.split(",");
		String[] units = ((String) paraMap.get("unit")).split(",");
		String orderid = (String) paraMap.get("orderid"); // 账单号
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		Map tempMap = tempMapList.get(0);
		// 优惠金额
		BigDecimal amount = new BigDecimal(0);

		// 赠送菜品个数
		Map<String, Double> beforGiftDishMap = new HashMap<>();
		// 已经赠送过得菜品菜品唯一标号对应菜品数据key:oredridlid Value:dishID+unit
		Map<String, Double> beforGiftDishUnitMap = new HashMap<>();
		// 卷对应的菜品数据（一个菜多少个卷）
		List<TorderDetailPreferential> torderDetailPreferentials = this.getPresentAndspecialPriclist(orderid,
				orderDetailPreferentialDao);
		// 主键ID对应数据内容
		Map<String, TorderDetailPreferential> updateMap = new HashMap<>();
		for (TorderDetailPreferential detailPreferential : torderDetailPreferentials) {
			updateMap.put(detailPreferential.getId(), detailPreferential);
			List<TbOrderDetailPreInfo> detailPreinfoList = detailPreferential.getDetailPreInfos();
			for (TbOrderDetailPreInfo preInfo : detailPreinfoList) {
				String key = preInfo.getDishID() + preInfo.getUnit();

				double getDishNum = preInfo.getDishNum();
				if (beforGiftDishUnitMap.containsKey(preInfo.getOrderidetailid())) {
					Double giftNum = beforGiftDishUnitMap.get(preInfo.getOrderidetailid());
					beforGiftDishUnitMap.put(preInfo.getOrderidetailid(), getDishNum + giftNum);
				} else {
					beforGiftDishUnitMap.put(preInfo.getOrderidetailid(), getDishNum);
				}
				if (beforGiftDishMap.containsKey(key)) {
					Double dishNum = beforGiftDishMap.get(key);
					beforGiftDishMap.put(key, dishNum + preInfo.getDishNum());
				} else {
					beforGiftDishMap.put(key, preInfo.getDishNum());
				}
			}
		}

		/** 获取对应的菜品ID编号 **/
		Map<String, List<ComplexTorderDetail>> detailidListMap = new HashMap<>();
		/** 订单菜品对应个数 **/
		Map<String, Double> orderDishNumMap = new HashMap<>();
		/** 当前订单对应关系 **/
		Map<String, ComplexTorderDetail> orderDetailIdSet = new HashMap<>();
		for (ComplexTorderDetail detail : orderDetailList) {
			if(detail.getPricetype().equals("1")){
				continue;
			}
			orderDetailIdSet.put(detail.getOrderdetailid(), detail);
			String key = detail.getDishid() + detail.getDishunit();
			double dishNum = Double.valueOf(detail.getDishnum());
			if (orderDishNumMap.containsKey(key)) {
				Double resDishNum = orderDishNumMap.get(key);
				orderDishNumMap.put(key, dishNum + resDishNum);
			} else {
				orderDishNumMap.put(key, Double.valueOf(detail.getDishnum()));
			}
			/** 查看那些菜品没有使用优惠 **/
			boolean isHaving = beforGiftDishUnitMap.containsKey(detail.getOrderdetailid());

			if ((isHaving && beforGiftDishUnitMap.get(detail.getOrderdetailid()) < Double.valueOf(detail.getDishnum()))
					|| !isHaving) {
				if (detailidListMap.containsKey(key)) {
					detailidListMap.get(key).add(detail);
				} else {
					List<ComplexTorderDetail> detailidlist = new ArrayList<>();
					detailidlist.add(detail);
					detailidListMap.put(key, detailidlist);
				}
			}
		}

		// 是更新还是新使用赠菜优惠卷
		if (paraMap.containsKey("updateId")) {
			String key = preALLDishId[0] + units[0];
			// 获取当前菜品个数
			Double orderDishNum = orderDishNumMap.get(key);
			// 获取当前使用菜品优惠个数如果优惠大于菜品者删除优惠
			Double userPreferId = beforGiftDishMap.get(key);
			//
			TorderDetailPreferential tempOr = updateMap.get(paraMap.get("updateId"));

			if (orderDishNum==null||orderDishNum < userPreferId) {
				// 如果为空说明当前已经删除了此菜品，那么就应该删除此优惠卷
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("DetalPreferentiald", paraMap.get("updateId"));
				delMap.put("orderid", orderid);
				orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
			} else {

				ComplexTorderDetail orderDetail = null;
				TbOrderDetailPreInfo tempdeorin = tempOr.getDetailPreInfos().get(0);
				if (orderDetailIdSet.containsKey(tempdeorin.getOrderidetailid())) {
					orderDetail = new ComplexTorderDetail();
					orderDetail.setOrderdetailid(tempdeorin.getOrderidetailid());
					orderDetail.setDishid(tempdeorin.getDishID());
					orderDetail.setDishunit(tempdeorin.getUnit());
					orderDetail.setOrderprice(orderDetailIdSet.get(tempdeorin.getOrderidetailid()).getOrderprice());
					orderDetail.setTitle(orderDetailIdSet.get(tempdeorin.getOrderidetailid()).getTitle());
				} else {
					orderDetail = detailidListMap.get(key).get(0);
				}

				String preName=(String) tempMap.get("name") + "(" + orderDetail.getTitle() + "/"
						+ orderDetail.getDishunit() + ")";
				// 创建根元素
				TorderDetailPreferential detailPreferential = this.createTorderDetail(orderDetail, key, 1, paraMap,
						orderid, preferentialid, tempMap, tempMapList,preName);
				amount = amount.add(detailPreferential.getDeAmount());
				// 创建子元素
				String subId = tempdeorin.getId();
				List<TbOrderDetailPreInfo> detailPreInfos = new ArrayList<>();
				detailPreInfos.add(createOrderDetailInfo(subId, detailPreferential, orderDetail,
						detailPreferential.getDeAmount()));
				detailPreferential.setDetailPreInfos(detailPreInfos);

				detailPreferentials.add(detailPreferential);

			}

		} else {
			/** 增材菜品对应个数 key:dishId+unit Value:赠送数量 **/
			Map<String, Integer> dishForDishNum = new HashMap<>();
			for (int i = 0; i < preALLDishId.length; i++) {
				String key = preALLDishId[i] + units[i];
				String dishNum = preDishNum[i];
				if (dishForDishNum.containsKey(key)) {
					dishForDishNum.put(key, dishForDishNum.get(key) + Integer.valueOf(dishNum));
				} else {
					dishForDishNum.put(key, Integer.valueOf(dishNum));
				}
			}
			// 遍历赠菜信息Key:dishID+dishUnit
			for (String key : dishForDishNum.keySet()) {
				if (detailidListMap.containsKey(key)) {
					int inputNum = dishForDishNum.get(key);
					List<ComplexTorderDetail> torderDetails = detailidListMap.get(key);
					ComplexTorderDetail orderDetail = torderDetails.get(0);
					String preName=(String) tempMap.get("name") + "(" + orderDetail.getTitle() + "/"
							+ orderDetail.getDishunit() + ")";
					for (int i = 0; i < inputNum; i++) {
						// 创建使用优惠跟节点
						TorderDetailPreferential detailPreferential = createTorderDetail(orderDetail, key, 1, paraMap,
							 orderid, preferentialid, tempMap, tempMapList,preName);
						// 创建子节点
						List<TbOrderDetailPreInfo> detailPreInfos = new ArrayList<>();
						detailPreInfos.add(createOrderDetailInfo(IDUtil.getID(), detailPreferential, orderDetail,
								detailPreferential.getDeAmount()));
						detailPreferential.setDetailPreInfos(detailPreInfos);

						amount = amount.add(detailPreferential.getDeAmount());
						detailPreferentials.add(detailPreferential);
						//判断删除条件
						ComplexTorderDetail deletaFalg = detailidListMap.get(key).get(0);
						   double deletNum = Double.valueOf(deletaFalg.getDishnum());
						if(deletNum<=1){
							detailidListMap.get(key).remove(0);
						}else{
							detailidListMap.get(key).get(0).setDishnum(String.valueOf(deletNum-1));
						}
					}
				}

			}

		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		result.put("resultDebitmap", orederMenuMap);
		return result;
	}



	@SuppressWarnings("rawtypes")
	private TorderDetailPreferential createTorderDetail(ComplexTorderDetail orderDetail, String dishId, int inputNum,
			Map<String, Object> paraMap,  String orderid, String preferentialid, Map tempMap,
			List<Map<String, Object>> tempMapList,String preName) {
		// 创建优惠
		TorderDetailPreferential addPreferential = null;
		TorderDetail ordetail = orderDetail;
		if (ordetail != null) {
			for (int i = 0; i < inputNum; i++) {
				BigDecimal orderprice = ordetail.getOrderprice() == null ? new BigDecimal("0")
						: ordetail.getOrderprice();

				String conupId = (String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id"));
				addPreferential = this.createPreferentialBean(paraMap, orderprice, orderprice, new BigDecimal("0"), 1,
						new BigDecimal(0), Constant.CALCPRETYPE.NOGROUP, preName, conupId,
						Constant.CALCPRETYPE.GIVEUSEPRE);
			}
		}
		return addPreferential;
	}

}
