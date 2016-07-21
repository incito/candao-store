package com.candao.print.listener.template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.candao.print.listener.template.impl.SimpleCustDishTemplateImpl;
import com.candao.print.listener.template.impl.SimpleMultiDishTemplateImpl;
import com.candao.print.listener.template.impl.SimpleNormalDishTemplateImpl;

@Configuration
public class TemplateConfigure {

	@Bean(name={"normalDishListenerTemplateImpl"})
	@Scope(value="prototype")
	public SimpleNormalDishTemplateImpl getNormalDishTemplateImpl(){
		return new SimpleNormalDishTemplateImpl();
	}
	
	@Bean(name={"multiDishListenerTemplateImpl"})
	@Scope(value="prototype")
	public SimpleMultiDishTemplateImpl getMultiDishTemplateImpl(){
		return new SimpleMultiDishTemplateImpl();
	}
	
	@Bean(name={"custDishListenerTemplateImpl"})
	@Scope(value="prototype")
	public SimpleCustDishTemplateImpl getCustDishTemplateImpl(){
		return new SimpleCustDishTemplateImpl();
	}
}
