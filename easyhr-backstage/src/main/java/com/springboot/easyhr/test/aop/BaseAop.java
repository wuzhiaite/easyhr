package com.springboot.easyhr.test.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class BaseAop {

    @Pointcut(value = "execution(* com.springboot.easyhr.test..*(..))")
     public void pointcut(){};

    @Before("pointcut()")
    public void before(){
        System.out.println("before behaviours ++++++++++");
    }

    @After("pointcut()")
    public void after(){
        System.out.println("after behaviours +++++++++++++++");
    }

}
