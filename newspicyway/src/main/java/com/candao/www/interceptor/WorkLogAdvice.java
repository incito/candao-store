package com.candao.www.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import com.candao.file.common.PandoranewsLog.PandoranLog;
import com.candao.www.data.model.Tworklog;
import com.candao.www.webroom.service.WorkLogService;

public class WorkLogAdvice{
	
	@Autowired
	private WorkLogService workLogService;


	 /** 
     * 在核心业务执行前执行，不能阻止核心业务的调用。 
     * @param joinPoint 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
     */  
    private void doBefore(JoinPoint jp) throws Exception {
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
    private Object doAround(ProceedingJoinPoint pjp) throws Throwable {  
   
    	Tworklog tworklog = new Tworklog();
    	String	descriptions = null;
    	MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();   
        //获取所有参数
   	    Object[] params = pjp.getArgs();
        //获取对象的参数类型
     	Class<?>[] parameterTypes = joinPointObject.getMethod().getParameterTypes();   
        //获取连接点目标对象的方法
   	    Method method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), parameterTypes); 
   	   if(method.isAnnotationPresent(PandoranLog.class)){
   		 PandoranLog pandoranLog = method.getAnnotation(PandoranLog.class);
   	 	descriptions = pandoranLog.description();  		   		
   	   }
   	     Object  retVal = pjp.proceed();
     	 String sad = retVal.toString().replace("\\", "");     	
    	  if (sad.contains("result")) {
    		    JSONObject json = JSONObject.fromObject(sad);
    	         System.out.println(json.getString("result"));
    		  if(String.valueOf(json.getString("result")).equals("0")){   			     	      			
    	      	    tworklog.setWorktype(descriptions);
    	      	    tworklog.setStatus(1);
    	      		tworklog.setWorkid(UUID.randomUUID().toString());	
    	      		tworklog.setBegintime(new Date());
    	      		tworklog.setEndtime(new Date());
    	      		tworklog.setIpaddress("127.0.0.1");
    	      		workLogService.saveLog(tworklog);
    	      		System.out.println("日志操作成功！！");
    		  }     		
      	}
          return retVal;  
    }  
 
    /** 
     * 核心业务逻辑退出后（包括正常执行结束和异常退出），执行此Advice 
     * @param joinPoint 
     */  
    private void doAfter(JoinPoint jp) throws Exception{  
    	
    }  
      
    /** 
     * 核心业务逻辑调用正常退出后，不管是否有返回值，正常退出后，均执行此Advice 
     * @param joinPoint 
     */  
    private void doReturn(JoinPoint jp) throws Exception {
   	
}
  
      
    /** 
     * 核心业务逻辑调用异常退出后，执行此Advice，处理错误信息 
     * @param joinPoint 
     * @param ex 
     */  
    private void doThrowing(JoinPoint joinPoint,Throwable ex) {  
        System.out.println("-----doThrowing().invoke-----");  
        System.out.println(" 错误信息："+ex.getMessage());  
        System.out.println(" 此处意在执行核心业务逻辑出错时，捕获异常，并可做一些日志记录操作等等");  
        System.out.println(" 可通过joinPoint来获取所需要的内容");  
        System.out.println("-----End of doThrowing()------");  
    }  
}
