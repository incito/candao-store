package com.candao.www.interceptor;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.common.utils.JacksonJsonMapper;



public class WorkLogAdvice{
	
	
	 /** 
     * 在核心业务执行前执行，不能阻止核心业务的调用。 
     * @param joinPoint 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
     */  
    public void doBefore(JoinPoint jp) throws Exception {
    	String classname=jp.getTarget().getClass().getName();
    	Logger logger = LoggerFactory.getLogger(Class.forName(classname)); 
    	logger.info("切入点:"+jp.getTarget().getClass().getName()+"."+jp.getSignature().getName());
    	 StringBuilder  builder=new StringBuilder();
    	    for(Object obj:jp.getArgs()){
    	    	if(obj!=null){
    	    		builder.append(JacksonJsonMapper.objectToJson(obj)).append(";");
    	    	}
    	    }
    	    logger.info("参数有:"+builder.toString());
    }  
      
    /** 
     * 手动控制调用核心业务逻辑，以及调用前和调用后的处理, 
     *  
     * 注意：当核心业务抛异常后，立即退出，转向After Advice 
     * 执行完毕After Advice，再转到Throwing Advice 
     * @param pjp 
     * @return 
     * @throws Throwable 
     */  
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {  
   
    	return pjp.proceed();
    }  
 
    /** 
     * 核心业务逻辑退出后（包括正常执行结束和异常退出），执行此Advice 
     * @param joinPoint 
     */  
    public void doAfter(JoinPoint jp) throws Exception{  
    }  
      
    /** 
     * 核心业务逻辑调用正常退出后，不管是否有返回值，正常退出后，均执行此Advice 
     * @param joinPoint 
     */  
    public void doReturn(JoinPoint jp) throws Exception {

    }
  
      
    /** 
     * 核心业务逻辑调用异常退出后，执行此Advice，处理错误信息 
     * @param joinPoint 
     * @param ex 
     * @throws ClassNotFoundException 
     */  
    public void doThrowing(JoinPoint joinPoint,Throwable ex) throws ClassNotFoundException {  
    	String classname=joinPoint.getTarget().getClass().getName();
    	Logger logger = LoggerFactory.getLogger(Class.forName(classname)); 
    	logger.error("异常地点:"+joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName());
    	logger.error("-----",ex);
    	 StringBuilder  builder=new StringBuilder();
    	    for(Object obj:joinPoint.getArgs()){
    	    	if(obj!=null){
    	    		builder.append(JacksonJsonMapper.objectToJson(obj)).append(";");
    	    	}
    	    }
    	    logger.error("参数有:"+builder.toString());
    }  
}
