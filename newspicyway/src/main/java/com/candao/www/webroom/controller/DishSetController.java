package com.candao.www.webroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dishset")
public class DishSetController {
	@RequestMapping("/index")
	public String index(){
		return "/dishset/show";
	}

}
