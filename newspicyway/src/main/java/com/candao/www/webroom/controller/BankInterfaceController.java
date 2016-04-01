package com.candao.www.webroom.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.constant.Constant;
import com.candao.www.webroom.service.DataDictionaryService;

@Controller
@RequestMapping("/bankinterface")
public class BankInterfaceController {
	private Logger logger = LoggerFactory.getLogger(BankInterfaceController.class);
	@Autowired
	DataDictionaryService dataDictionaryService;

	/**
	 * 返回所有的刷卡银行，POS调用
	 * @return
	 */
	@RequestMapping("/getallbank")
	@ResponseBody
	public String getAllBank(){
		try{
			List<Map<String, Object>> datasByType = dataDictionaryService.getDatasByType("BANK");
			return JacksonJsonMapper.objectToJson(datasByType);
		}catch(Exception e){
			logger.error("-->",e);
			return Constant.FAILUREMSG;
		}
	}

}
