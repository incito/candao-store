package com.candao.www.webroom.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.www.data.dao.TCouponRuleDao;
import com.candao.www.data.dao.TCouponsDao;
import com.candao.www.data.dao.TparternerCouponDao;
import com.candao.www.data.model.TCouponRule;
import com.candao.www.data.model.TCoupons;
import com.candao.www.data.model.TparternerCoupon;
import com.candao.www.webroom.model.TCouponGroup;
import com.candao.www.webroom.service.CouponsService;

@Service
public class CouponsServiceImpl implements CouponsService {
	@Autowired
	private TCouponRuleDao tCouponRuleDao;
	@Autowired
	private TCouponsDao tCouponsDao;
	@Autowired
	private TparternerCouponDao tparternerCouponDao;

	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params,
			int current, int pagesize) {
		// TODO Auto-generated method stub
		return tCouponsDao.page(params, current, pagesize);
	}

	@Override
	public boolean save(TCouponGroup tCoupons) {
		// TODO Auto-generated method stub
		boolean flag=true;
		if (tCouponsDao.insert(tCoupons.gettCoupons()) > 0) {
			if (tCoupons.getList() != null && tCoupons.getList().size() > 0) {
				for (TCouponRule tcr : tCoupons.getList()) {
					tcr.setCouponid(tCoupons.gettCoupons().getCouponid());
					tcr.setRuleid(IdentifierUtils.getId().generate().toString());
				}
				flag= tCouponRuleDao.insert(tCoupons.getList()) > 0&&flag;
			} 
			if (tCoupons.getListpac() != null && tCoupons.getListpac().size() > 0) {
				for (TparternerCoupon tcr : tCoupons.getListpac()) {
					tcr.setCouponid(tCoupons.gettCoupons().getCouponid());
					tcr.setId(IdentifierUtils.getId().generate().toString());
				}
				flag= tparternerCouponDao.insert(tCoupons.getListpac()) > 0&&flag;
			}
			return flag;

		} else {
			return false;
		}
	}

	@Override
	public boolean update(TCouponGroup tCoupons) {
		// TODO Auto-generated method stub
		boolean flag=true;
		if (tCouponsDao.update(tCoupons.gettCoupons()) > 0) {
			tCouponRuleDao.delete(tCoupons.gettCoupons().getCouponid());
			tparternerCouponDao.delete(tCoupons.gettCoupons().getCouponid());
			if (tCoupons.getList() != null && tCoupons.getList().size() > 0) {
				for (TCouponRule tcr : tCoupons.getList()) {
					tcr.setCouponid(tCoupons.gettCoupons().getCouponid());
					tcr.setRuleid(IdentifierUtils.getId().generate().toString());
				}
				flag= tCouponRuleDao.insert(tCoupons.getList()) > 0&&flag;
			} 
			if (tCoupons.getListpac() != null && tCoupons.getListpac().size() > 0) {
				for (TparternerCoupon tcr : tCoupons.getListpac()) {
					tcr.setCouponid(tCoupons.gettCoupons().getCouponid());
					tcr.setId(IdentifierUtils.getId().generate().toString());
				}
				flag= tparternerCouponDao.insert(tCoupons.getListpac()) > 0&&flag;
			}
			return flag;

		} else {
			return false;
		}
	}

	@Override
	public TCouponGroup findById(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		// TODO Auto-generated method stub
		TCouponGroup tCouponGroup = new TCouponGroup();
		tCouponGroup.settCoupons(tCouponsDao.get(id));
		List<TCouponRule> list = tCouponRuleDao.find(map);
		List<TparternerCoupon> listpac = tparternerCouponDao.find(map);
		tCouponGroup.setList(list);
		tCouponGroup.setListpac(listpac);
		return tCouponGroup;
	}

	@Override
	public boolean deleteById(String id) {
		// TODO Auto-generated method stub
		tCouponRuleDao.delete(id);
		tparternerCouponDao.delete(id);
		return tCouponsDao.delete(id) > 0;
	}
	
	@Override
	 public List<TCouponRule>  findRuleByDishId(String dishId){
		return tCouponRuleDao.findRuleByDishId(dishId);
	}
	
	@Override
    public TCoupons findCouponByDishId(String dishid){
		return tCouponsDao.findCouponByDishId(dishid);
	  }

}
