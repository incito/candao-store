package com.candao.www.webroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.candao.www.webroom.service.BusinessDataDetailService;

/**
 * 营业数据明细表
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/businessDataDetail")
public class BusinessDataDetailController {

	@Autowired
	private BusinessDataDetailService businessDataDetailService;
}
