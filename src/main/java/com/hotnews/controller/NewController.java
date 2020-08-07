package com.hotnews.controller;

import com.hotnews.aspect.LogAspect;
import com.hotnews.model.HostHolder;
import com.hotnews.model.News;
import com.hotnews.service.NewsService;
import com.hotnews.service.QiniuService;
import com.hotnews.util.HotNewsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 21:13
 * Remark:上传文件
 */
@Controller
public class NewController {

	//日志
	private static final Logger logger = LoggerFactory.getLogger(NewController.class);

	@Autowired
	NewsService newsService;

	@Autowired
	QiniuService qiniuService;

	@Autowired
	HostHolder hostHolder;
	//展示图片
	//读取图片,传参数
	@RequestMapping(path = {"/image"},method = {RequestMethod.GET})
	@ResponseBody
	public void getImage(@RequestParam("name") String imageName,
	                     HttpServletResponse response){
		try {
			response.setContentType("image/jpeg");
			//把从目录中读取图片的二进制流写入响应包
			StreamUtils.copy(new FileInputStream(new
					File(HotNewsUtil.IMAGE_DIR + imageName)),
					response.getOutputStream());

		}catch (IOException e){
			logger.error("读取图片错误" + e.getMessage());
		}

	}

	@RequestMapping(path = {"/user/addNews/"},method = {RequestMethod.POST})
	@ResponseBody
	public String addNews(@RequestParam("image") String image,
	                      @RequestParam("title") String title,
	                      @RequestParam("link") String link){
		try{
			News news = new News();
			if(hostHolder.getUser() != null){
				news.setUserId(hostHolder.getUser().getId());
			}else {
				//匿名用户id
				news.setUserId(250);
			}
			news.setImage(image);
			news.setCreatedDate(new Date());
			news.setTitle(title);
			news.setLink(link);
			newsService.addNews(news);
			return HotNewsUtil.getJSONString(0);
		}catch (Exception e){
			logger.error("添加资讯错误" + e.getMessage());
			return HotNewsUtil.getJSONString(1,"发布失败");
		}
	}

	//获取文件流
	@RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file){
		try{
//			file.transferTo();
			//通过本地上传
//			String fileUrl = newsService.saveImage(file);
			//通过七牛上传
			String fileUrl = qiniuService.saveImage(file);
			if(fileUrl == null){
				return HotNewsUtil.getJSONString(1,"上传图片失败");
			}
			//不为空，将图片链接返回至前端
			return HotNewsUtil.getJSONString(0,fileUrl);
		}catch (Exception e){
			logger.error("上传图片失败" + e.getMessage());
			return HotNewsUtil.getJSONString(1,"上传失败");
		}
//		return "";
	}
}
