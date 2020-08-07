package com.hotnews.model;

import org.springframework.stereotype.Component;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 15:30
 * Remark: 用来保存用户登录状态  面向切面
 */
@Component
public class HostHolder {
	//本地线程
	private static ThreadLocal<User> users = new ThreadLocal<User>();

	//提取用户
	public User getUser(){
		return users.get();
	}

	//保存用户
	public void setUsers(User user){
		users.set(user);
	}

	//清除用户
	public void clear(){
		users.remove();
	}
}
