package com.candao.www.dataserver.service.msghandler.aop;

import com.candao.www.dataserver.mapper.OfflineMsgMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ytq on 2016/4/20.
 */
@Service
@Aspect
public class OfflineMsgAop {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OfflineMsgAop.class);
    @Autowired
    private OfflineMsgMapper offlineMsgMapper;

//    @Pointcut("execution(* com.candao.www.dataserver.service.msghandler.impl.offline.*.*(..))")
//    private void pointCutMethod() {
//    }
//
//    //声明前置通知
//    @Before("pointCutMethod()")
//    public void doBefore() {
//        LOGGER.info("### 删除过期消息 ###");
//        offlineMsgMapper.deleteMsgByExpireTime();
//    }
//
//    //声明后置通知
//    @AfterReturning(pointcut = "pointCutMethod()", returning = "result")
//    public void doAfterReturning(String result) {
//    }
//
//    //声明例外通知
//    @AfterThrowing(pointcut = "pointCutMethod()", throwing = "e")
//    public void doAfterThrowing(Exception e) {
//    }
//
//    //声明最终通知
//    @After("pointCutMethod()")
//    public void doAfter() {
//    }
//
//    //声明环绕通知
//    @Around("pointCutMethod()")
//    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
//        Object o = pjp.proceed();
//        return o;
//    }
}
