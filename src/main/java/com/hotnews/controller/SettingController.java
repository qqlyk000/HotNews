package com.hotnews.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author: XianDaLi
 * Date: 2020/8/5 12:36
 * Remark:
 */
@Controller
public class SettingController {
	@RequestMapping("/setting")
	@ResponseBody
	public String setting(){
		return "Setting:OK";
	}
}
