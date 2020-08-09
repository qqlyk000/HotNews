package com.hotnews.service;

import com.hotnews.dao.LoginTicketDao;
import com.hotnews.dao.UserDao;
import com.hotnews.model.LoginTicket;
import com.hotnews.model.User;
import com.hotnews.util.HotNewsUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author: XianDaLi
 * Date: 2020/8/5 23:33
 * Remark:
 */
@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserDao userDao;

	@Autowired
	private LoginTicketDao loginTicketDao;

	// 注册
	public Map<String,Object> register(String username, String password){
		Map<String,Object> map = new HashMap<>();
		if(StringUtils.isBlank(username)){
			map.put("msgname","用户名不能为空");
			return map;
		}

		if(StringUtils.isBlank(password)){
			map.put("msgpwd","密码不能为空");
			return map;
		}

		User user = userDao.selectByName(username);

		if(user != null){
			map.put("msgname","用户名已经被注册！！");
			return map;
		}

		// 密码强度
		user = new User();
		user.setName(username);

		// 生成随机字符，用于保护密码
		user.setSalt(UUID.randomUUID().toString().substring(0,5));
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));

		// user.setPassword(password);  禁止直接保存，密码安全相关问题
		// 保存的密码 = 用户输入的密码 + 随机字符 + MD5加密.  之后才存入数据库
		user.setPassword(HotNewsUtil.MD5(password+user.getSalt()));
		userDao.addUser(user);

		// 如果注册成功 下发ticket
		String ticket = addLoginTicket(user.getId());
		map.put("ticket",ticket);
		return map;

	}

	// 登录验证
	public Map<String,Object> login(String username, String password){
		Map<String,Object> map = new HashMap<>();
		if(StringUtils.isBlank(username)){
			map.put("msgname","用户名不能为空");
			return map;
		}

		if(StringUtils.isBlank(password)){
			map.put("msgpwd","密码不能为空");
			return map;
		}

		// 查找该用户是否存在
		User user = userDao.selectByName(username);
		// 如果查找为空
		if(user == null){
			map.put("msgname","用户名不存在");
			return map;
		}

		// 如果找到  判断是否匹配
		if(!HotNewsUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
			map.put("msgpwd","密码不正确");
			return map;
		}

		// 如果登录成功 下发ticket
		String ticket = addLoginTicket(user.getId());
		map.put("ticket",ticket);
		return map;
	}

	// 为登录成功的用户发放ticket,保持登录状态
	private String addLoginTicket(int userId){
		LoginTicket ticket = new LoginTicket();
		ticket.setUserId(userId);
		Date date = new Date();
		date.setTime(date.getTime() + 1000*3600*24);
		ticket.setExpired(date);
		ticket.setStatus(0);
		ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
		loginTicketDao.addTicket(ticket);
		return ticket.getTicket();
	}

	public User getUser(int id){
		return userDao.selectById(id);
	}

	public void logout(String ticket){
		loginTicketDao.updateStatus(ticket,1);
	}
}
