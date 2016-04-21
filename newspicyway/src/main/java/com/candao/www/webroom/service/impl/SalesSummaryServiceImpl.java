package com.candao.www.webroom.service.impl;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbSalesSummaryDao;
import com.candao.www.webroom.service.SalesSummaryService;
@Service
public class SalesSummaryServiceImpl implements SalesSummaryService {
	@Autowired
	private TbSalesSummaryDao tbSalesSummary;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params,
			int current, int pagesize) {
		// TODO Auto-generated method stub
		return  tbSalesSummary.page(params, current, pagesize);
	}
	@SuppressWarnings("static-access")
	@Override
	public void exportxlsD(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Map<String,Object>> list = tbSalesSummary.findFD(params);
		String vasd ="营业额统计表";
		PoiExcleTest poi = new PoiExcleTest();
		poi.exportExcleD(list,params, vasd, req,resp);
	}
	public List<Map<String,Object>> dates(Map<String, Object> params){
		return tbSalesSummary.find(params);
	}
}
 