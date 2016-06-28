package com.candao.print.listener.template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.candao.print.listener.template.impl.SimpleCustDishTemplateImpl;
import com.candao.print.listener.template.impl.SimpleMultiDishTemplateImpl;
import com.candao.print.listener.template.impl.SimpleNormalDishTemplateImpl;

@Configuration
public class TemplateConfigure {

	@Bean(name={"normalDishListenerTemplateImpl"})
	public SimpleNormalDishTemplateImpl getNormalDishTemplateImpl(){
		return new SimpleNormalDishTemplateImpl();
	}
	
	@Bean(name={"multiDishListenerTemplateImpl"})
	public SimpleMultiDishTemplateImpl getMultiDishTemplateImpl(){
		return new SimpleMultiDishTemplateImpl();
	}
	
	@Bean(name={"custDishListenerTemplateImpl"})
	public SimpleCustDishTemplateImpl getCustDishTemplateImpl(){
		return new SimpleCustDishTemplateImpl();
	}
}
