package com.candao.file.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class PandoranewsLog {
	/**
	 * 用户操作日志注解
	 * @author chenm
	 *
	 */
	@Target(ElementType.METHOD)   
	@Retention(RetentionPolicy.RUNTIME)   
	@Documented  
	@Inherited 
	public @interface PandoranLog {
			
		/**
		 * 操作描述
		 * @return
		 */
		public String description();

	}

}
