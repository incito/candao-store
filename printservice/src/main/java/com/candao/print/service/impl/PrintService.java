package com.candao.print.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public  class PrintService {

	protected Map<String, String> printMap;
	
	public PrintService(){}
	 
	@PostConstruct
	public void setPrinterMap(){
		System.out.println("begin to pring setting");
		//加菜  String print_ip = "192.168.1.251";		  int print_port = 9100;
		//正常点菜 
		printMap = new HashMap<String, String>();
		printMap.put("0", "192.168.1.251:9100");
		printMap.put("1", "192.168.1.251:9100");
		printMap.put("2", "192.168.1.251:9100");
		printMap.put("3", "192.168.1.251:9100");
		
	}
	public  String print(String printType,String msg){
		return null;
	}
	
	public String getPrinter(String printerType){
		return printMap.get(printerType);
	}
	 
}
