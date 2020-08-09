package com.hotnews.controller;

import com.hotnews.async.EventModel;
import com.hotnews.async.EventProducer;
import com.hotnews.async.EventType;
import com.hotnews.model.News;
import com.hotnews.model.ViewObject;
import com.hotnews.service.NewsService;
import com.hotnews.service.UserService;
import com.hotnews.util.HotNewsUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 0:04
 * Remark:
 */
@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	UserService userService;

	@Autowired
	EventProducer eventProducer;
	// 注册操作
	@RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String reg(Model model, @RequestParam("username") String username,
	                  @RequestParam("username") String password,
	                  @RequestParam(value = "rember", defaultValue = "0") int rememberMe,
	                  HttpServletResponse response) {

		logger.info("Visit Login");

		try{
			Map<String, Object> map = userService.register(username, password);
			if(map.containsKey("ticket")){
				Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
				cookie.setPath("/");
				//记住登录状态
				if(rememberMe > 0){
					//设置cookie最大时限为5天
					cookie.setMaxAge(3600*24*5);
				}
				response.addCookie(cookie);
				return HotNewsUtil.getJSONString(0,"注册成功");
			}else {
				return HotNewsUtil.getJSONString(1, map);
			}
		}catch (Exception e){
			logger.error("注册异常" + e.getMessage());
			return HotNewsUtil.getJSONString(1,"注册异常");
		}
	}

	// 登录操作
	@RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String login(Model model, @RequestParam("username") String username,
	                    @RequestParam("username") String password,
	                    @RequestParam(value = "rember", defaultValue = "0") int rememberMe,
	                    HttpServletResponse response) {

		logger.info("Visit Login");

		try{
			Map<String, Object> map = userService.login(username, password);
			if(map.containsKey("ticket")){
				Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
				response.addCookie(cookie);
				cookie.setPath("/");
				//记住登录状态
				if(rememberMe > 0){
					//设置cookie最大时限为5天
					cookie.setMaxAge(3600*24*5);
				}
				response.addCookie(cookie);
				eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
						.setExt("username",username).setExt("email","lxd@123.com"));
				return HotNewsUtil.getJSONString(0,"成功");
			}else {
				return HotNewsUtil.getJSONString(1, map);
			}
		}catch (Exception e){
			logger.error("注册异常" + e.getMessage());
			return HotNewsUtil.getJSONString(1,"异常");
		}
	}

	//登出操作
	//	@ResponseBody 如果加上此注解，则下面的return会被认为成String
	//将ticket的status更改即可
	@RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String logout(@CookieValue("ticket") String ticket){
		userService.logout(ticket);
		return "redirect:/";
	}
}
