package com.hotnews.controller;

import com.hotnews.aspect.LogAspect;
import com.hotnews.util.HotNewsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 21:13
 * Remark:上传文件
 */
@Controller
public class NewController {

	//日志
	private static final Logger logger = LoggerFactory.getLogger(NewController.class);

	@RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
	@ResponseBody
	public String uploadImage(@RequestParam("file")MultipartFile file){
		try{

		}catch (Exception e){
			logger.error("上传图片失败" + e.getMessage());
			return HotNewsUtil.getJSONString(1,"上传失败");
		}
	}
}
