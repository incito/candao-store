package com.candao.www.webroom.service;

import com.candao.www.webroom.model.Order;

public interface PayForOrderService {

	public int payByCash(Order order,int payWay);
	
	public int payByMemeberCard(Order order,int payWay);
	
	
}
