package com.candao.www.webroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.candao.www.webroom.service.PreferenceDetailService;

/**
 * 优惠活动明细表
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/preferenceDetail")
public class PreferenceDetailController {

	@Autowired
	private PreferenceDetailService preferenceDetailService;
	
}
