/**
 * 
 */
package com.candao.www.webroom.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.Pinyin;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.UUIDGenerator;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbHandFreeDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TbVoucherDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TbDiscountTickets;
import com.candao.www.data.model.TbGroupon;
import com.candao.www.data.model.TbHandFree;
import com.candao.www.data.model.TbInnerFree;
import com.candao.www.data.model.TbNoDiscountDish;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TbPreferentialActivityBranch;
import com.candao.www.data.model.TbPreferentialActivitySpecialStamp;
import com.candao.www.data.model.TbPreferentialTypeDict;
import com.candao.www.data.model.TbVoucher;
import com.candao.www.data.model.Tbbranch;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.data.model.User;
import com.candao.www.dataserver.mapper.CaleTableAmountMapper;
import com.candao.www.dataserver.mapper.OrderMapper;
import com.candao.www.utils.SessionUtils;
import com.candao.www.utils.preferential.CalMenuOrderAmount;
import com.candao.www.utils.preferential.CalMenuOrderAmountInterface;
import com.candao.www.utils.preferential.CalPreferentialStrategyInterface;
import com.candao.www.utils.preferential.StrategyFactory;
import com.candao.www.webroom.model.DiscountTicketsVo;
import com.candao.www.webroom.model.GrouponTicketsVO;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.model.PreferentialActivitySpecialStampVO;
import com.candao.www.webroom.model.VoucherVo;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.PreferentialActivityService;
import com.candao.www.webroom.service.TableService;

/**
 * @author zhao
 *
 */
@Service
public class PreferentialActivityServiceImpl implements PreferentialActivityService {

	private static final Logger logger = LoggerFactory.getLogger(PreferentialActivityServiceImpl.class);

	@Autowired
	private TbPreferentialActivityDao tbPreferentialActivityDao;
	@Autowired
	private TbDiscountTicketsDao tbDiscountTicketsDao;
	@Autowired
	private TbBranchDao tbBranchDao;
	@Autowired
	private TbVoucherDao tbVoucherDao;
	@Autowired
	private TbHandFreeDao tbHandFreeDao;
	@Autowired
	private TorderDetailMapper torderDetailDao;
	@Autowired
	private TdishDao tdishDao;
	@Autowired
	private TorderDetailPreferentialDao orderDetailPreferentialDao;
	@Autowired
	private CaleTableAmountMapper caleTableAmountMapper;
	@Autowired
	private DataDictionaryService dataDictionaryService;

	@Autowired
	private OrderMapper orderMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.candao.www.webroom.service.PreferentialActivityService#grid(java.util
	 * .Map, int, int)
	 */
	@Override
	public Page<Map<String, Object>> page(Map<String, Object> params, int current, int pagesize) {
		return tbPreferentialActivityDao.page(params, current, pagesize);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(TbPreferentialActivity preferentialActivity, String type) {
		UUIDGenerator iDGenerator = new UUIDGenerator();
		preferentialActivity.setId(iDGenerator.generate().toString().replaceAll("-", ""));
		preferentialActivity.setCode(getNextPreferentialActivityCode());
		setPreferentialType(preferentialActivity, type);
		if (preferentialActivity.getName() != null) {
			if (isNameExisted(preferentialActivity)) {
				StringBuffer buf = new StringBuffer("");
				buf.append(preferentialActivity.getName()).append("已经存在，请更换。");
				throw new RuntimeException(buf.toString());
			}
			// 设置拼音首字母
			preferentialActivity.setNameFirstLetter(Pinyin.getPinYinHeadChar(preferentialActivity.getName()));
		} else {
			throw new RuntimeException("优惠活动名称不能为空。");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		preferentialActivity.setCreator(SessionUtils.getCurrentUser().getId());
		return tbPreferentialActivityDao.insert(preferentialActivity) == 1;
	}

	/**
	 * 判断同类优惠活动是否存在重名的
	 * 
	 * @param preferentialActivity
	 * @return
	 */
	private boolean isNameExisted(TbPreferentialActivity preferentialActivity) {
		boolean ret = false;
		Map params = new HashMap();

		params.put("name", preferentialActivity.getName());
		params.put("type", preferentialActivity.getType());
		if (preferentialActivity.getSubType() != null) {
			params.put("subType", preferentialActivity.getSubType());
		}
		List<Map> activitys = tbPreferentialActivityDao.find(params);
		if (activitys != null && activitys.size() > 0) {
			// 更新操作并且不修改名称可以保存
			if (activitys.size() == 1 && activitys.get(0).get("id").equals(preferentialActivity.getId())) {
				ret = false;
			} else {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * 设置优惠的类型信息
	 * 
	 * @param preferentialActivity
	 * @param type
	 */
	private void setPreferentialType(TbPreferentialActivity preferentialActivity, String type) {
		TbPreferentialTypeDict preferentialType = tbPreferentialActivityDao.getPreferentialType(type);
		if (preferentialType.isSubtype()) {
			TbPreferentialTypeDict parentType = tbPreferentialActivityDao
					.getPreferentialType(preferentialType.getParentType());
			preferentialActivity.setType(parentType.getCode());
			preferentialActivity.setTypeName(parentType.getName());
			preferentialActivity.setSubType(preferentialType.getCode());
			preferentialActivity.setSubTypeName(preferentialType.getName());
		} else {
			preferentialActivity.setType(preferentialType.getCode());
			preferentialActivity.setTypeName(preferentialType.getName());
		}
		preferentialActivity.setSubtableName(preferentialType.getSubtableName());
	}

	/**
	 * 获取新的优惠活动编码，编码格式为8xxxx。暂时没处理并发的问题
	 * 
	 * @return
	 */
	private String getNextPreferentialActivityCode() {
		String lastCode = tbPreferentialActivityDao.getLastPreferentialActivityCode();
		if (lastCode != null && StringUtils.isNumeric(lastCode)) {
			return Integer.toString((Integer.parseInt(lastCode) + 1));
		} else {
			return Constant.PREFERENTIAL_INIT_CODE;
		}
	}

	@Override
	public boolean updateById(TbPreferentialActivity preferentialActivity) {
		if (isNameExisted(preferentialActivity)) {
			StringBuffer buf = new StringBuffer("");
			buf.append(preferentialActivity.getName()).append("已经存在，请更换。");
			throw new RuntimeException(buf.toString());
		}
		if (preferentialActivity.getName() != null) {
			preferentialActivity.setNameFirstLetter(Pinyin.getPinYinHeadChar(preferentialActivity.getName()));
		}
		return tbPreferentialActivityDao.update(preferentialActivity) == 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteById(String id) {
		boolean success = true;
		TbPreferentialActivity preferentialActivity = tbPreferentialActivityDao.get(id);
		String subDelSql = generalDelSql(preferentialActivity);
		if (subDelSql != null && subDelSql.trim().length() > 0) {
			// 删除优惠子表数据
			// tbPreferentialActivityDao.deleteSubCoupon(subDelSql);
			tbPreferentialActivityDao.deletePreferentialDetail(id);
		}
		// 需要删除指定门店数据
		tbPreferentialActivityDao.deleteBranchs(id);
		success = tbPreferentialActivityDao.delete(id) == 1;
		// 如果是折扣券，还需要删除不参与折扣菜品记录
		if (preferentialActivity.getType().equals(Constant.CouponType.DISCOUNT_TICKET)) {
			tbDiscountTicketsDao.deleteNoDiscountDishsByPreterential(id);
		}

		return success;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteByStatus(String id) {
		boolean success = true;
		TbPreferentialActivity preferentialActivity = tbPreferentialActivityDao.get(id);
		// 需要删除指定门店数据
		tbPreferentialActivityDao.deleteByPreferenStatus(id);
		// 如果是折扣券，还需要删除不参与折扣菜品记录
		if (preferentialActivity.getType().equals(Constant.CouponType.DISCOUNT_TICKET)) {
			tbDiscountTicketsDao.deleteNoDiscountDishsByPreterential(id);
		}
		return success;
	}

	/**
	 * 生成删除优惠子表的sql
	 * 
	 * @param preferentialActivity
	 * @return
	 */
	private String generalDelSql(TbPreferentialActivity preferentialActivity) {
		StringBuffer str = new StringBuffer("");
		if (preferentialActivity == null) {
			return str.toString();
		}
		String table = preferentialActivity.getSubtableName();
		if (table != null && table.trim().length() > 0) {
			str.append("DELETE FROM ").append(table).append(" WHERE preferential='")
					.append(preferentialActivity.getId()).append("'");
		}
		return str.toString();
	}

	@Override
	public boolean addSpecialStamp(PreferentialActivitySpecialStampVO specialStampVO) {
		boolean result = true;
		// 1.获取优惠分类, 所有门店信息
		TbPreferentialTypeDict typeDict = this.tbPreferentialActivityDao
				.getPreferentialType(specialStampVO.PREFERENTIAL_TYPE);
		List<Tbbranch> branchList = this.converTbbranchListFromMap(this.tbBranchDao.getAll());
		Map<Integer, Tbbranch> shopMap = new HashMap();
		for (int i = 0; i < branchList.size(); i++) {
			Tbbranch s = (Tbbranch) branchList.get(i);
			shopMap.put(s.getBranchid(), s);
		}

		// 2.先保存优惠券。因为更多优惠菜品与优惠门店需要使用它的主键
		TbPreferentialActivity activity = specialStampVO.getActivity();
		add(activity, Constant.CouponType.SPECIAL_TICKET);

		// 3.取出所有门店。 需要用当前优惠的门店，取到门店名称。
		List<TbPreferentialActivityBranch> selectedBranchs = new ArrayList();
		Tbbranch shop = null;
		for (TbPreferentialActivityBranch b : specialStampVO.getBranchs()) {
			shop = shopMap.get(b.getBranch());
			if (null != shop) {
				b.setBranch_name(shop.getBranchname());
			}
			b.setPreferential(activity.getId());
			selectedBranchs.add(b);
		}
		// 批量保存门店
		tbPreferentialActivityDao.savePreferentialActivityBranchs(selectedBranchs);

		// 4.保存优惠菜品
		List<TbPreferentialActivitySpecialStamp> selectedStamps = new ArrayList();
		for (TbPreferentialActivitySpecialStamp s : specialStampVO.getStamps()) {
			s.setId(IdentifierUtils.getId().generate().toString().replaceAll("-", ""));
			s.setPreferential(activity.getId());
			selectedStamps.add(s);
		}
		tbPreferentialActivityDao.savePreferentialActivitySpecialStamp(selectedStamps);

		return result;
	}

	@Override
	public boolean updateSpecialStamp(PreferentialActivitySpecialStampVO specialStampVO) {
		boolean result = true; // 返回状态
		List<Tbbranch> branchList = converTbbranchListFromMap(this.tbBranchDao.getAll());
		Map<Integer, Tbbranch> shopMap = new HashMap(); // 存放所有门店，用来获取门店名称
		for (int i = 0; i < branchList.size(); i++) {
			Tbbranch s = (Tbbranch) branchList.get(i);
			shopMap.put(s.getBranchid(), s);
		}

		// --begin 保存一些原来 优惠存在的内容
		// TbPreferentialTypeDict
		// typeDict=this.tbPreferentialActivityDao.getPreferentialType(
		// specialStampVO.PREFERENTIAL_TYPE);
		// TbPreferentialActivity activityObj=
		// this.tbPreferentialActivityDao.get(
		// specialStampVO.getActivity().getId());
		//
		// specialStampVO.getActivity().setCode( activityObj.getCode());
		// if(specialStampVO.getActivity().getName() != null){
		// //设置拼音首字母
		// specialStampVO.getActivity().setNameFirstLetter(Pinyin.getPinYinHeadChar(specialStampVO.getActivity().getName()));
		// }
		// //分类
		// specialStampVO.getActivity().setSubType( activityObj.getSubType());
		// specialStampVO.getActivity().setSubTypeName(activityObj.getSubTypeName());
		// specialStampVO.getActivity().setSubtableName(
		// activityObj.getSubtableName() );
		//
		// specialStampVO.getActivity().setType( activityObj.getType() );
		// specialStampVO.getActivity().setTypeName( activityObj.getTypeName()
		// );
		//
		// specialStampVO.getActivity().setCreatetime( new Timestamp( (new
		// Date()).getDate()));
		// specialStampVO.getActivity().setCreator(
		// SessionUtils.getCurrentUser().getUserid());
		// --end

		// 特价券的更新保存，需要先清除原来保存的门店与菜品，然后重新保存这些内容。
		TbPreferentialActivity preferentialActivity = tbPreferentialActivityDao
				.get(specialStampVO.getActivity().getId());
		// 1.删除该特价券原来对应的 菜品 ，保存新提交过来的菜品
		// this.tbPreferentialActivityDao.deleteSubCoupon(
		// this.generalDelSql(preferentialActivity));
		this.tbPreferentialActivityDao.deletePreferentialDetail(preferentialActivity.getId());
		// 2.删除该特价券原来对应的门店，保存新提交过来的门店信息
		this.tbPreferentialActivityDao.deleteBranchs(preferentialActivity.getId());

		List<TbPreferentialActivityBranch> selectedBranchs = new ArrayList();
		Tbbranch shop = null;
		for (TbPreferentialActivityBranch b : specialStampVO.getBranchs()) {
			shop = shopMap.get(b.getBranch());
			if (null != shop) {
				b.setBranch_name(shop.getBranchname());
			}
			b.setPreferential(preferentialActivity.getId());
			selectedBranchs.add(b);
		}
		this.tbPreferentialActivityDao.savePreferentialActivityBranchs(selectedBranchs);

		List<TbPreferentialActivitySpecialStamp> selectedStamps = new ArrayList();
		for (TbPreferentialActivitySpecialStamp s : specialStampVO.getStamps()) {
			s.setId(IdentifierUtils.getId().generate().toString().replaceAll("-", ""));
			s.setPreferential(preferentialActivity.getId());
			selectedStamps.add(s);
		}
		tbPreferentialActivityDao.savePreferentialActivitySpecialStamp(selectedStamps);

		// 3.更新优惠券信息
		if (specialStampVO.getActivity().getType() == null || specialStampVO.getActivity().getType().isEmpty()) {
			specialStampVO.getActivity().setType(Constant.CouponType.SPECIAL_TICKET);
		}
		result = updateById(specialStampVO.getActivity());
		return result;
	}

	/**
	 * 用于将 调用门店返回的map 转换为list
	 * 
	 * @param list
	 * @return
	 */
	private List<Tbbranch> converTbbranchListFromMap(List<HashMap<String, Object>> list) {
		List l = new ArrayList(0);
		Tbbranch obj = null;

		for (Map<String, Object> map : list) {
			obj = new Tbbranch();
			obj.setBranchid((int) map.get("branchid"));
			obj.setBranchname((String) map.get("branchname"));
			l.add(obj);
		}

		return l;
	}

	@Override
	public PreferentialActivitySpecialStampVO getPreferentialActivitySpecialStampVO(String activity_id) {
		PreferentialActivitySpecialStampVO vo = new PreferentialActivitySpecialStampVO();
		TbPreferentialActivity activity = this.tbPreferentialActivityDao.get(activity_id);
		List<TbPreferentialActivityBranch> branchs = this.tbPreferentialActivityDao
				.findPreferentialBranchs(activity_id);
		List<TbPreferentialActivitySpecialStamp> stamps = this.tbPreferentialActivityDao
				.findPreferentialSpecialStamps(activity_id);
		vo.setActivity(activity);
		vo.setBranchs(branchs);
		vo.setStamps(stamps);

		return vo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addDiscountTicket(DiscountTicketsVo discountTicket) {
		boolean isSuccess = true;
		TbPreferentialActivity preferentialActivity = discountTicket.getPreferentialActivity();
		if (!add(preferentialActivity, Constant.CouponType.DISCOUNT_TICKET)) {
			isSuccess = false;
			return isSuccess;
		}
		TbDiscountTickets discount = new TbDiscountTickets();
		discount.setDiscount(discountTicket.getDiscount());
		discount.setId(preferentialActivity.getId());
		discount.setPreferential(preferentialActivity.getId());
		if (tbDiscountTicketsDao.insert(discount) != 1) {
			isSuccess = false;
			return isSuccess;
		}
		List<TbNoDiscountDish> noDiscounts = discountTicket.getNoDiscountDish();
		if (noDiscounts != null && noDiscounts.size() > 0) {
			UUIDGenerator iDGenerator = new UUIDGenerator();
			for (int i = 0; i < noDiscounts.size(); i++) {
				noDiscounts.get(i).setId(iDGenerator.generate().toString().replaceAll("-", ""));
				noDiscounts.get(i).setDiscountTicket(discount.getId());
			}
			tbDiscountTicketsDao.batchInsertNoDiscountDishs(noDiscounts);
		}

		if (!preferentialActivity.isApplyAll()) {
			List<TbPreferentialActivityBranch> branchs = discountTicket.getBranchs();
			if (branchs != null) {
				for (int i = 0; i < branchs.size(); i++) {
					branchs.get(i).setPreferential(preferentialActivity.getId());
				}
				tbPreferentialActivityDao.savePreferentialActivityBranchs(branchs);
			}
		}

		return true;
	}

	@Override
	public boolean updateDiscountTicket(DiscountTicketsVo discountTicket) {
		boolean isSuccess = true;
		TbPreferentialActivity preferentialActivity = discountTicket.getPreferentialActivity();
		if (preferentialActivity.getType() == null || preferentialActivity.getType().isEmpty()) {
			preferentialActivity.setType(Constant.CouponType.DISCOUNT_TICKET);
		}
		if (!updateById(preferentialActivity)) {
			isSuccess = false;
			return isSuccess;
		}
		TbDiscountTickets discount = new TbDiscountTickets();
		discount.setDiscount(discountTicket.getDiscount());
		discount.setId(preferentialActivity.getId());
		if (tbDiscountTicketsDao.update(discount) != 1) {
			isSuccess = false;
			return isSuccess;
		}
		// 先批量删除不参加折扣菜品，再重新添加
		tbDiscountTicketsDao.deleteNoDiscountDishsByDiscount(discount.getId());
		List<TbNoDiscountDish> noDiscounts = discountTicket.getNoDiscountDish();
		if (noDiscounts != null && noDiscounts.size() > 0) {
			UUIDGenerator iDGenerator = new UUIDGenerator();
			for (int i = 0; i < noDiscounts.size(); i++) {
				noDiscounts.get(i).setId(iDGenerator.generate().toString().replaceAll("-", ""));
				noDiscounts.get(i).setDiscountTicket(discount.getId());
			}
			tbDiscountTicketsDao.batchInsertNoDiscountDishs(noDiscounts);
		}

		// 指定门店，先删除再添加
		tbPreferentialActivityDao.deleteBranchs(preferentialActivity.getId());
		if (!preferentialActivity.isApplyAll()) {
			List<TbPreferentialActivityBranch> branchs = discountTicket.getBranchs();
			if (branchs != null) {
				for (int i = 0; i < branchs.size(); i++) {
					branchs.get(i).setPreferential(preferentialActivity.getId());
				}
				tbPreferentialActivityDao.savePreferentialActivityBranchs(branchs);
			}
		}
		return true;
	}

	public DiscountTicketsVo getDiscountTicketsVo(String activity_id) {
		TbPreferentialActivity preferentialActivity = tbPreferentialActivityDao.get(activity_id);
		if (preferentialActivity == null) {
			throw null;
		}
		DiscountTicketsVo discountTicketsVo = new DiscountTicketsVo();
		discountTicketsVo.setPreferentialActivity(preferentialActivity);
		List<TbPreferentialActivityBranch> branchs = tbPreferentialActivityDao.findPreferentialBranchs(activity_id);
		discountTicketsVo.setBranchs(branchs);
		Map parm = new HashMap();
		parm.put("preferential", preferentialActivity.getId());

		List<TbDiscountTickets> discounts = tbDiscountTicketsDao.find(parm);
		if (discounts != null && discounts.size() > 0) {
			discountTicketsVo.setDiscount(discounts.get(0).getDiscount());
			List<TbNoDiscountDish> noDiscountDish = tbDiscountTicketsDao
					.getNoDiscountDishsByDiscount(discounts.get(0).getId());
			discountTicketsVo.setNoDiscountDish(noDiscountDish);
		}

		return discountTicketsVo;
	}

	@Override
	public boolean addGrouponTicket(GrouponTicketsVO groupon) {
		boolean result = true;
		// 1.获取优惠分类, 所有门店信息
		TbPreferentialTypeDict typeDict = this.tbPreferentialActivityDao
				.getPreferentialType(Constant.CouponType.GROUPON);
		List<Tbbranch> branchList = this.converTbbranchListFromMap(this.tbBranchDao.getAll());
		Map<Integer, Tbbranch> shopMap = new HashMap();
		for (int i = 0; i < branchList.size(); i++) {
			Tbbranch s = (Tbbranch) branchList.get(i);
			shopMap.put(s.getBranchid(), s);
		}

		// 2.先保存优惠券。因为更多优惠菜品与优惠门店需要使用它的主键
		TbPreferentialActivity activity = groupon.getActivity();
		if (!add(activity, Constant.CouponType.GROUPON)) {
			return false;
		}

		// 3.取出所有门店。 需要用当前优惠的门店，取到门店名称。
		List<TbPreferentialActivityBranch> selectedBranchs = new ArrayList();
		Tbbranch shop = null;
		for (TbPreferentialActivityBranch b : groupon.getBranchs()) {
			shop = shopMap.get(b.getBranch());
			if (null != shop) {
				b.setBranch_name(shop.getBranchname());
			}
			b.setPreferential(activity.getId());
			selectedBranchs.add(b);
		}
		// 批量保存门店
		tbPreferentialActivityDao.savePreferentialActivityBranchs(selectedBranchs);
		// 保存优惠->团购信息
		groupon.getGroupon().setId(IdentifierUtils.getId().generate().toString().replaceAll("-", ""));
		groupon.getGroupon().setPreferential(activity.getId());
		tbPreferentialActivityDao.savePreferentialGroupon(groupon.getGroupon());

		return result;
	}

	@Override
	public GrouponTicketsVO getGrouponTicket(String activity_id) {
		GrouponTicketsVO grouponVO = new GrouponTicketsVO();

		TbPreferentialActivity activity = this.tbPreferentialActivityDao.get(activity_id);
		List<TbPreferentialActivityBranch> branchs = tbPreferentialActivityDao.findPreferentialBranchs(activity_id);
		TbGroupon groupon = tbPreferentialActivityDao.findPreferentialGroupon(activity_id);

		grouponVO.setActivity(activity);
		grouponVO.setGroupon(groupon);
		grouponVO.setBranchs(branchs);
		return grouponVO;
	}

	@Override
	public boolean updateGrouponTicket(GrouponTicketsVO groupon) {
		boolean result = true;
		TbPreferentialActivity oldActivity = this.tbPreferentialActivityDao.get(groupon.getActivity().getId());
		// 1.获取优惠分类, 所有门店信息
		TbPreferentialTypeDict typeDict = this.tbPreferentialActivityDao
				.getPreferentialType(Constant.CouponType.GROUPON);
		List<Tbbranch> branchList = this.converTbbranchListFromMap(this.tbBranchDao.getAll());
		Map<Integer, Tbbranch> shopMap = new HashMap();
		for (int i = 0; i < branchList.size(); i++) {
			Tbbranch s = (Tbbranch) branchList.get(i);
			shopMap.put(s.getBranchid(), s);
		}

		// 2.先保存优惠券。因为更多优惠菜品与优惠门店需要使用它的主键
		TbPreferentialActivity activity = groupon.getActivity();
		// activity.setCode( oldActivity.getCode() );
		// if(activity.getName() != null){
		// //设置拼音首字母
		// activity.setNameFirstLetter(Pinyin.getPinYinHeadChar(activity.getName()));
		// }
		// //分类
		//
		// activity.setSubType( oldActivity.getSubType() );
		// activity.setSubTypeName( oldActivity.getSubTypeName() );
		// activity.setSubtableName( oldActivity.getSubtableName() );
		//
		// activity.setType( oldActivity.getType() );
		// activity.setTypeName( oldActivity.getTypeName() );
		//
		//
		// activity.setCreatetime( oldActivity.getCreatetime() );
		// activity.setCreator( oldActivity.getCreator() );
		if (activity.getType() == null || activity.getType().isEmpty()) {
			activity.setType(Constant.CouponType.GROUPON);
		}
		if (!updateById(activity)) {
			return false;
		}
		// 3.取出所有门店。 需要用当前优惠的门店，取到门店名称。
		tbPreferentialActivityDao.deleteBranchs(activity.getId());
		List<TbPreferentialActivityBranch> selectedBranchs = new ArrayList();
		Tbbranch shop = null;
		for (TbPreferentialActivityBranch b : groupon.getBranchs()) {
			shop = shopMap.get(b.getBranch());
			if (null != shop) {
				b.setBranch_name(shop.getBranchname());
			}
			b.setPreferential(activity.getId());
			selectedBranchs.add(b);
		}
		// 批量保存门店
		tbPreferentialActivityDao.savePreferentialActivityBranchs(selectedBranchs);
		// 保存优惠->团购信息
		// tbPreferentialActivityDao.deleteSubCoupon(
		// this.generalDelSql(oldActivity));
		tbPreferentialActivityDao.deletePreferentialDetail(oldActivity.getId());
		groupon.getGroupon().setId(IdentifierUtils.getId().generate().toString().replaceAll("-", ""));
		groupon.getGroupon().setPreferential(activity.getId());
		tbPreferentialActivityDao.savePreferentialGroupon(groupon.getGroupon());

		return result;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addVoucher(VoucherVo voucher) {
		boolean isSuccess = true;
		TbPreferentialActivity preferentialActivity = voucher.getPreferentialActivity();
		if (!add(preferentialActivity, Constant.CouponType.VOUCHER)) {
			isSuccess = false;
			return isSuccess;
		}
		TbVoucher tbVoucher = new TbVoucher();
		tbVoucher.setAmount(voucher.getAmount());
		tbVoucher.setId(preferentialActivity.getId());
		tbVoucher.setPreferential(preferentialActivity.getId());
		if (tbVoucherDao.insert(tbVoucher) != 1) {
			isSuccess = false;
			return isSuccess;
		}

		if (!preferentialActivity.isApplyAll()) {
			List<TbPreferentialActivityBranch> branchs = voucher.getBranchs();
			if (branchs != null) {
				for (int i = 0; i < branchs.size(); i++) {
					branchs.get(i).setPreferential(preferentialActivity.getId());
				}
				tbPreferentialActivityDao.savePreferentialActivityBranchs(branchs);
			}
		}

		return true;
	}

	@Override
	public boolean updateVoucher(VoucherVo voucher) {
		boolean isSuccess = true;
		TbPreferentialActivity preferentialActivity = voucher.getPreferentialActivity();
		if (preferentialActivity.getType() == null || preferentialActivity.getType().isEmpty()) {
			preferentialActivity.setType(Constant.CouponType.VOUCHER);
		}
		if (!updateById(preferentialActivity)) {
			isSuccess = false;
			return isSuccess;
		}
		TbVoucher tbVoucher = new TbVoucher();
		tbVoucher.setAmount(voucher.getAmount());
		tbVoucher.setId(preferentialActivity.getId());
		if (tbVoucherDao.update(tbVoucher) != 1) {
			isSuccess = false;
			return isSuccess;
		}
		// 指定门店，先删除再添加
		tbPreferentialActivityDao.deleteBranchs(preferentialActivity.getId());
		if (!preferentialActivity.isApplyAll()) {
			List<TbPreferentialActivityBranch> branchs = voucher.getBranchs();
			if (branchs != null) {
				for (int i = 0; i < branchs.size(); i++) {
					branchs.get(i).setPreferential(preferentialActivity.getId());
				}
				tbPreferentialActivityDao.savePreferentialActivityBranchs(branchs);
			}
		}
		return true;
	}

	public VoucherVo getVoucherVo(String activity_id) {
		TbPreferentialActivity preferentialActivity = tbPreferentialActivityDao.get(activity_id);
		if (preferentialActivity == null) {
			throw null;
		}
		VoucherVo voucherVo = new VoucherVo();
		voucherVo.setPreferentialActivity(preferentialActivity);
		List<TbPreferentialActivityBranch> branchs = tbPreferentialActivityDao.findPreferentialBranchs(activity_id);
		voucherVo.setBranchs(branchs);
		Map parm = new HashMap();
		parm.put("preferential", preferentialActivity.getId());

		List<TbVoucher> vouchers = tbVoucherDao.find(parm);
		if (vouchers != null && vouchers.size() > 0) {
			voucherVo.setAmount(vouchers.get(0).getAmount());
		}

		return voucherVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addHandFree(TbHandFree handFree) {
		UUIDGenerator iDGenerator = new UUIDGenerator();
		handFree.setId(iDGenerator.generate().toString().replaceAll("-", ""));
		Map params = new HashMap();
		params.put("subType", Constant.CouponType.HANDFREE);
		// 第一次添加手工优免记录时在优惠活动中创建一条手工优免的记录
		if (handFree.getPreferential() == null || handFree.getPreferential().trim().length() <= 0) {
			List<Map> activitys = tbPreferentialActivityDao.find(params);
			if (activitys == null || activitys.size() <= 0) {
				TbPreferentialActivity activity = new TbPreferentialActivity();
				activity.setName("手工优免");
				activity.setColor("#677988");
				activity.setApplyAll(true);
				if (!add(activity, Constant.CouponType.HANDFREE)) {
					return false;
				}
				handFree.setPreferential(activity.getId());
			} else {
				handFree.setPreferential((String) activitys.get(0).get("id"));
				if (isHandFreeExisted(handFree, params)) {
					StringBuffer buf = new StringBuffer("");
					buf.append(handFree.getFreeReason()).append("已经存在，请更换。");
					throw new RuntimeException(buf.toString());
				}
			}
		} else {
			if (isHandFreeExisted(handFree, params)) {
				StringBuffer buf = new StringBuffer("");
				buf.append(handFree.getFreeReason()).append("已经存在，请更换。");
				throw new RuntimeException(buf.toString());
			}
		}
		return tbHandFreeDao.insert(handFree) == 1;
	}

	/**
	 * 判断手工优免原因是否已经存在
	 * 
	 * @param handFree
	 * @param params
	 * @return
	 */
	private boolean isHandFreeExisted(TbHandFree handFree, Map params) {
		boolean ret = false;
		params.put("preferential", handFree.getPreferential());
		params.put("freeReason", handFree.getFreeReason());
		List<TbHandFree> hfs = tbHandFreeDao.find(params);
		if (hfs != null && hfs.size() > 0) {
			// 更新操作并且不修改名称可以保存
			if (hfs.size() == 1 && hfs.get(0).getId().equals(handFree.getId())) {
				ret = false;
			} else {
				ret = true;
			}
		}
		return ret;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteHandFree(String id) {
		boolean ret = tbHandFreeDao.delete(id) == 1;
		if (ret) {
			associateDeleteOtherCoupon(Constant.CouponType.HANDFREE);
		}
		return ret;
	}

	/**
	 * 删除手工优免和内部优免记录时，如果是最后一条子表记录同时删除优惠主表记录
	 * 
	 * @param subType
	 */
	private void associateDeleteOtherCoupon(String subType) {
		Map params = new HashMap();
		params.put("subType", subType);
		List<Map> activitys = tbPreferentialActivityDao.find(params);
		if (activitys != null && activitys.size() > 0) {
			params.clear();
			String preferential = (String) activitys.get(0).get("id");
			params.put("preferential", preferential);

			if (Constant.CouponType.HANDFREE.equals(subType)) {
				// 查询是否还有手工优免，没有的话删除优惠主表对应记录
				if (tbHandFreeDao.find(params).size() <= 0) {
					tbPreferentialActivityDao.delete(preferential);
				}
			} else if (Constant.CouponType.INNERFREE.equals(subType)) {
				// 查询是否还有内部优免，没有的话删除优惠主表对应记录
				List<TbInnerFree> free = tbPreferentialActivityDao.findInnerFree(params);
				if (tbPreferentialActivityDao.findInnerFree(params).size() <= 0) {
					tbPreferentialActivityDao.delete(preferential);
				}
			}

		}
	}

	@Override
	public List<TbHandFree> findHandFree(Map param) {
		Map p_maps = new HashMap();
		p_maps.put("subType", Constant.CouponType.HANDFREE);
		List<Map> activitys = tbPreferentialActivityDao.find(p_maps);
		String preferential = "";
		if (activitys == null || activitys.size() <= 0) {
			return new ArrayList<TbHandFree>();
		}

		param.put("preferential", (String) activitys.get(0).get("id"));
		List<TbHandFree> handfrees = tbHandFreeDao.find(param);
		return handfrees;
	}

	@Override
	public boolean saveInnerFree(TbInnerFree innerfree) {
		innerfree.setId((new UUIDGenerator()).generate().toString().replaceAll("-", ""));
		Map params = new HashMap();
		params.put("subType", Constant.CouponType.INNERFREE);
		if (StringUtils.isBlank(innerfree.getPreferential())) {
			List<Map> activitys = tbPreferentialActivityDao.find(params);
			if (activitys == null || activitys.size() <= 0) {
				TbPreferentialTypeDict typeDict = this.tbPreferentialActivityDao
						.getPreferentialType(Constant.CouponType.INNERFREE);
				TbPreferentialActivity activity = new TbPreferentialActivity();
				activity.setName(typeDict.getName());
				activity.setColor("#677988");
				activity.setApplyAll(true);
				if (!add(activity, Constant.CouponType.INNERFREE)) {
					return false;
				}
				innerfree.setPreferential(activity.getId());
			} else {
				innerfree.setPreferential((String) activitys.get(0).get("id"));
				if (isInnerFreeExisted(innerfree, params)) {
					StringBuffer buf = new StringBuffer("");
					buf.append(innerfree.getCompany_name()).append("已经存在，请更换。");
					throw new RuntimeException(buf.toString());
				}
			}
		} else {
			if (isInnerFreeExisted(innerfree, params)) {
				StringBuffer buf = new StringBuffer("");
				buf.append(innerfree.getCompany_name()).append("已经存在，请更换。");
				throw new RuntimeException(buf.toString());
			}
		}

		if (!StringUtils.isBlank(innerfree.getCompany_name())) {
			// 设置拼音首字母
			innerfree.setCompany_first_letter(Pinyin.getPinYinHeadChar(innerfree.getCompany_name()));
		}

		return tbPreferentialActivityDao.saveInnerFree(innerfree) == 1;
	}

	/**
	 * 判断内部优免是否已经存在
	 * 
	 * @param innerFree
	 * @param params
	 * @return
	 */
	private boolean isInnerFreeExisted(TbInnerFree innerFree, Map params) {
		boolean ret = false;
		params.put("preferential", innerFree.getPreferential());
		params.put("name", innerFree.getCompany_name());
		List<TbInnerFree> ifs = tbPreferentialActivityDao.findInnerFree(params);
		if (ifs != null && ifs.size() > 0) {
			// 更新操作并且不修改名称可以保存
			if (ifs.size() == 1 && ifs.get(0).getId().equals(innerFree.getId())) {
				ret = false;
			} else {
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public boolean deleteInnerFree(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateInnerFree(TbInnerFree innerfree) {
		if (!StringUtils.isBlank(innerfree.getCompany_name())) {
			// 设置拼音首字母
			innerfree.setCompany_first_letter(Pinyin.getPinYinHeadChar(innerfree.getCompany_name()));
		}
		Map params = new HashMap();
		params.put("subType", Constant.CouponType.INNERFREE);
		if (isInnerFreeExisted(innerfree, params)) {
			StringBuffer buf = new StringBuffer("");
			buf.append(innerfree.getCompany_name()).append("已经存在，请更换。");
			throw new RuntimeException(buf.toString());
		}
		return tbPreferentialActivityDao.updateInnerFree(innerfree) == 1;
	}

	@Override
	public Page<Map<String, Object>> pageInnerFree(Map param, int current, int pagesize) {
		// 先获取内部优免对应的 优惠的id，然后根据ID查询
		Map p_map = new HashMap();
		p_map.put("subType", Constant.CouponType.INNERFREE.toString());
		List<Map> activitys = tbPreferentialActivityDao.find(p_map);
		if (activitys == null || activitys.size() <= 0) {
			return null;
		}

		param.put("preferential", (String) activitys.get(0).get("id"));
		return this.tbPreferentialActivityDao.pageInnerFree(param, current, pagesize);
	}

	@Override
	public TbInnerFree findInnerFreeById(String id) {
		return this.tbPreferentialActivityDao.findInnerFreeById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteInnerFreeById(String id) {
		boolean ret = (tbPreferentialActivityDao.deleteInnerFree(id) == 1);
		if (ret) {
			this.associateDeleteOtherCoupon(Constant.CouponType.INNERFREE);
		}
		return ret;
	}

	/**
	 * 将对象转换为 map
	 * 
	 * @param o
	 * @return
	 */
	private Map<String, Object> objectToMapViaFields(Object o) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Field[] declaredFields = o.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			try {
				// 过滤内容为空的
				// if (field.get(o) == null) {
				// continue;
				// }
				resMap.put(field.getName(), field.get(o));
			} catch (Exception e) {
				logger.error("-->", e);
			}
		}
		return resMap;
	}

	@Override
	public List<Map<String, Object>> findCouponsByType4Pad(String typeid) {
		List<Map<String, Object>> list = new ArrayList();
		Map branchInfoMap = tbBranchDao.getBranchInfo();
		// 如果没有默认门店，则返回所有的门店信息。 关于branchid，会在SQL中判断，这里只需要 值为 NULL即可
		String branchid = null;
		if (null != branchInfoMap) {
			branchid = (String) branchInfoMap.get("branchid");
		}
		/*
		 * //在SQL 中处理 不同券所使用的不同SQL
		 * if(Constant.CouponType.HANDFREE.toString().equals( typeid.trim() ) ||
		 * Constant.CouponType.INNERFREE.toString().equals( typeid.trim() ) ){
		 * //手工优免 或者 //内部优惠 Map params=new HashMap(); params.put("subType",
		 * typeid); List<Map<String, Object>> activitys=
		 * this.tbPreferentialActivityDao.find(params);
		 * //取第一条记录，然后用优惠的ID，去获取detail中得记录 if( activitys.size() > 0 ){ Map
		 * m=activitys.get(0); String id=(String) m.get("id"); Map
		 * detail_params=new HashMap(); detail_params.put("preferential", id);
		 * detail_params.put("type", typeid); List<Map<String, Object>> details=
		 * this.tbPreferentialActivityDao.findPreferentialDetail(detail_params);
		 * 
		 * list=filterValidCoupon(details); } }else{
		 */// 更多优惠券 获取的信息由t_p_preferential_activity 和t_p_preferential_detail
			// 表的信息共同组成。其中key:id使用 detail表的内容
		Map params = new HashMap();
		params.put("type", typeid);
		params.put("branchid", branchid);
		// added by caicai 2 隐藏，表示不提供隐藏的优惠券
		params.put("status", 2);
		List<Map<String, Object>> activitys = this.tbPreferentialActivityDao.findPreferentialDetail(params);
		// 特价券单独处理
		if ("-1".equals(typeid)) {
			params.put("type", "01");
			List<Map<String, Object>> specialActivitys = tbPreferentialActivityDao.selectSpecialActivity(params);
			if (specialActivitys != null)
				activitys.addAll(specialActivitys);
		}

		list = filterValidCoupon(activitys);

		/* } */

		return list;
	}

	public List<Map<String, Object>> findCouponsByType4Pad(Map params) {
		List<Map<String, Object>> list = new ArrayList();
		Map branchInfoMap = tbBranchDao.getBranchInfo();
		// 如果没有默认门店，则返回所有的门店信息。 关于branchid，会在SQL中判断，这里只需要 值为 NULL即可
		String branchid = null;
		if (null != branchInfoMap) {
			branchid = (String) branchInfoMap.get("branchid");
		}
		params.put("branchid", branchid);
		// added by caicai 2 隐藏，表示不提供隐藏的优惠券
		params.put("status", 2);
		List<Map<String, Object>> activitys = this.tbPreferentialActivityDao.findPreferentialDetail(params);

		return list;
	}

	/**
	 * 查询所有的可挂账的合作单位
	 *
	 */
	@Override
	public List<Map<String, Object>> findCooperationUnit(Map params) {
		params.put("branchid", PropertiesUtils.getValue("current_branch_id"));
		List<Map<String, Object>> CooperationUnitList = this.tbPreferentialActivityDao.findCooperationUnit(params);
		return CooperationUnitList;
	}

	@Override
	public OperPreferentialResult updateOrderDetailWithPreferential(Map<String, Object> params) {
		/** 参数解析 **/
		String orderid = String.valueOf(params.get("orderid")); // 账单号
		String preferentialid = String.valueOf(params.get("preferentialid")); // 优惠活动id
		String type = String.valueOf(params.get("type"));
		type=type.contains("0")?type:"0"+type;
		params.put("type", type);
		String isCustom=String.valueOf(params.get("isCustom"));
		OperPreferentialResult result = new OperPreferentialResult();
		try {
			if (StringUtils.isBlank(orderid)) {
			} else {
				if(isCustom!=null&& isCustom.equals("2")&&type.equals("03")){
					type="Auto";
				}
				CalPreferentialStrategyInterface straFactory = StrategyFactory.INSTANCE.buildAnimal(type);
				if (straFactory == null) {
					result.setAmount(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
				} else {
					Map<String, Object> resultMap = straFactory.calPreferential(params, tbPreferentialActivityDao,
							torderDetailDao, orderDetailPreferentialDao, tbDiscountTicketsDao, tdishDao);
					List<TorderDetailPreferential> detailPreferentials = (List<TorderDetailPreferential>) resultMap
							.get("detailPreferentials");
					if (!detailPreferentials.isEmpty()) {
						int row = orderDetailPreferentialDao.addBatchInfo(detailPreferentials);
					}
					//总的优惠金额=本次优惠+以往优惠
					
					result.setDetailPreferentials(detailPreferentials);
					// 是否计算收入金额
					if (!params.containsKey("resultAmount")) {
						BigDecimal bd = new BigDecimal((String) params.get("preferentialAmt"));
						result.setAmount(bd.add((BigDecimal) resultMap.get("amount")));
						StrategyFactory.INSTANCE.calcAmount(caleTableAmountMapper, orderid, dataDictionaryService, result, orderMapper);
					}

				}
			}

		} catch (Exception e) {
			logger.error("-->", e);
			e.printStackTrace();
		}
		logger.info("使用优惠券，计算金额，result: " + result);
		return result;
	}

	/**
	 * 判断订单中是否点了不参与折扣的鱼锅
	 * 
	 * @param orderDetailList
	 * @param t
	 * @return
	 */
	private boolean hasFishInOrder(List<TorderDetail> orderDetailList, TbNoDiscountDish t) {
		for (TorderDetail d : orderDetailList) {
			if (d.getDishid().equals(t.getDish())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断优惠的日期。保留在有效范围内的优惠券。 <br>
	 * 优惠券的有效时间分为4种情况。 1.空 - 空 表示开始时间和结束时间都没有限制。 2.开始时间 - 空 表示有开始使用时间，不限制结束时间
	 * 3.开始时间 - 结束时间 表示有效期是一个范围段 4.空 - 结束时间 表示开始时间没有限制，但是有结束时间。
	 * 
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> filterValidCoupon(List<Map<String, Object>> list) {
		List<Map<String, Object>> returnList = new ArrayList(0);
		Calendar calendar = Calendar.getInstance();
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		Timestamp starttime = null;
		Timestamp endtime = null;

		for (Map<String, Object> m : list) {
			starttime = (Timestamp) m.get("starttime");
			endtime = (Timestamp) m.get("endtime");
			// System.out.println(" ############## starttime:"+starttime +"
			// ;endtime:"+endtime);
			if (null == starttime && null == endtime) { // 符合
				returnList.add(m);
			} else if (null != starttime && null == endtime) { // 判断
																// 当前时间是否大于开始时间
				if (starttime.before(time)) {
					returnList.add(m);
				}
			} else if (null != starttime && null != endtime) { // 判断当前时间是否大于开始时间，并且小于结束时间
				if (starttime.before(time) && endtime.after(time)) {
					returnList.add(m);
				}
			} else if (null == starttime && null != endtime) { // 判断当前时间是否小于结束时间
				if (endtime.after(time)) {
					returnList.add(m);
				}
			}

		}

		return returnList;
	}

	@Override
	public OperPreferentialResult cancelPreferentialItemInOrder(String orderid, String preferentialid) {
		OperPreferentialResult result = new OperPreferentialResult();
		// 默认设置为1，代表成功，每次认为操作失败后，设置为0
		// result.setResult(1);

		if (StringUtils.isBlank(orderid)) {
			// result.setResult(0);
			// result.setMsg("账单号为空!");
		} else {
			Map params = new HashMap();
			params.put("orderid", orderid);
			params.put("preferentialid", preferentialid);
			int row = this.torderDetailDao.cancelPreferentialItemInOrder(params);
			// result.setResult(1);
			// result.setMsg("撤销优惠成功!");
			int a = 0 / 0;
		}
		return result;
	}

	@Override
	public boolean updateHandFree(TbHandFree handFree) {
		Map params = new HashMap();
		params.put("subType", Constant.CouponType.HANDFREE);
		if (isHandFreeExisted(handFree, params)) {
			StringBuffer buf = new StringBuffer("");
			buf.append(handFree.getFreeReason()).append("已经存在，请更换。");
			throw new RuntimeException(buf.toString());
		}
		return tbHandFreeDao.update(handFree) == 1;
	}

	@Override
	public Page<Map<String, Object>> pageForBranchs(User currentUser, Map<String, Object> params, int current,
			int pagesize) {
		// if( currentUser.getUserType().equalsIgnoreCase( Constants.BUSINESS)
		// || currentUser.getUserType().equalsIgnoreCase(
		// Constants.HEADQUARTER_USER)){ //租户 //总店用户
		if ("N".equals(PropertiesUtils.getValue("isbranch"))) {
			return tbPreferentialActivityDao.page(params, current, pagesize);

		} else if ("Y".equals(PropertiesUtils.getValue("isbranch"))) { // 员工用户
			String currentBranch = PropertiesUtils.getValue("current_branch_id");
			List<String> l = new ArrayList();
			l.add(currentBranch);
			String[] branchIds = l.toArray(new String[0]);
			return tbPreferentialActivityDao.pageForBranchs(params, branchIds, current, pagesize);
		}

		return tbPreferentialActivityDao.page(params, current, pagesize);
	}

	@Override
	public int updateBySelective(Map params) {
		if ("0".equals(params.get("operationtype"))) {
			params.put("status", 0);
		} else {
			params.put("status", 2);
		}
		params.put("id", params.get("preferential"));
		return tbPreferentialActivityDao.updateBySelective(params);
	}
}
