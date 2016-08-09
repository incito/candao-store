package com.candao.www.webroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.candao.www.webroom.service.BusinessAnalysisChartsService;

/**
 * 营业分析图表
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/businessAnalysisCharts")
public class BusinessAnalysisChartsController {

	@Autowired
	private BusinessAnalysisChartsService businessAnalysisChartsService;
}
