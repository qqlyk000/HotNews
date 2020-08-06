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
 * Date: 2020/8/6 15:16
 * Remark:拦截器  认证用户是否登录
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

	@Autowired
	private LoginTicketDao loginTicketDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private HostHolder hostHolder;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		//遍历Cookie里是否存在ticket
		String ticket = null;
		if(httpServletRequest.getCookies() != null){
			for(Cookie cookie : httpServletRequest.getCookies()){
				if(cookie.getName().equals("ticket")){
					ticket = cookie.getValue();
					break;
				}
			}
		}

		//如果有ticket
		if(ticket != null){
			//从数据库中查找是否存在相同ticket (防伪造)
			LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
			//若查不到 || ticket过期 || 或状态码不对
			if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
				return true;
			}

			User user = userDao.selectById(loginTicket.getUserId());

			//方式一：放入request中
//			httpServletRequest.setAttribute("user",user);

			//方式二：spring 依赖注入
			hostHolder.setUsers(user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
		if(modelAndView != null && hostHolder.getUser() != null){
			modelAndView.addObject("user",hostHolder.getUser());
		}
	}

	//清除用户登录信息
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
		hostHolder.clear();
	}
}
