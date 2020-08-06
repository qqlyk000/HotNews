package com.hotnews.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * Author: XianDaLi
 * Date: 2020/8/5 12:43
 * Remark:
 */
@Aspect
@Component
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Before("execution(* com.hotnews.controller.*Controller.*(..))")
	public void beforeMethod(JoinPoint joinPoint){
		StringBuilder sb = new StringBuilder();
		for(Object arg : joinPoint.getArgs()){
			sb.append("arg:" + arg.toString() + "|");
		}
		logger.info("before time: " + new Date());
		logger.info("before method: " + sb.toString());
	}

	@After("execution(* com.hotnews.controller.*Controller.*(..))")
	public void afterMethod(JoinPoint joinPoint){
		logger.info("after method: ");
		logger.info("after time: " + new Date());
	}
}
