package com.jd.trackingmicroservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The intention of this class is to trace the calls in the application and
 * logger it.
 * 
 * @author Aswini Priyanka
 *
 */
@Aspect
@Component
public class MyAspect {

	private static final Logger LOG = LoggerFactory.getLogger(MyAspect.class);

	@Pointcut("within(com.jd.trackingmicroservice..*)")
	private void everythingInMyApplication() {
	}

	@Before("com.jd.trackingmicroservice.aop.MyAspect.everythingInMyApplication()")
	public void logMethodName(JoinPoint joinPoint) {
		LOG.info("Called {}", joinPoint.getSignature().getName());
	}

}
