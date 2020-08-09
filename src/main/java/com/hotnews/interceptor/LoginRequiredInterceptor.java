package com.hotnews.interceptor;

import com.hotnews.dao.LoginTicketDao;
import com.hotnews.dao.UserDao;
import com.hotnews.model.HostHolder;
import com.hotnews.model.LoginTicket;
import com.hotnews.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 16:54
 * Remark: 用户登录拦截器
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

	@Autowired
	private HostHolder hostHolder;

	//访问跳转
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		//先判断，只有true 才会进行后面的流程
		if(hostHolder.getUser() == null){
			httpServletResponse.sendRedirect("/?pop=1");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
	}
}
