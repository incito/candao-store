package com.candao.print.listener.template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.candao.print.listener.template.impl.SimpleNormalDishTemplateImpl;

@Configuration
public class TemplateConfigure {

	@Bean(name={"normalDishListenerTemplateImpl"})
	public SimpleNormalDishTemplateImpl getNormalDishTemplateImpl(){
		return new SimpleNormalDishTemplateImpl();
	}
}
