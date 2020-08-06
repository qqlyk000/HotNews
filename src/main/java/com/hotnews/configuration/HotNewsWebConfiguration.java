package com.hotnews.configuration;

import com.hotnews.interceptor.LoginRequiredInterceptor;
import com.hotnews.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 15:45
 * Remark:注册拦截器
 */
@Component
public class HotNewsWebConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	PassportInterceptor passportInterceptor;

	@Autowired
	LoginRequiredInterceptor loginRequiredInterceptor;

	//先看用户是谁，再判断用户是否符合访问要求
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(passportInterceptor);
		//注册登录拦截器
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
		super.addInterceptors(registry);
	}
}
