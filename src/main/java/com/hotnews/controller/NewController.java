package com.hotnews.controller;

import com.hotnews.aspect.LogAspect;
import com.hotnews.model.*;
import com.hotnews.service.CommentService;
import com.hotnews.service.NewsService;
import com.hotnews.service.QiniuService;
import com.hotnews.service.UserService;
import com.hotnews.util.HotNewsUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	@Autowired
	UserService userService;

	@Autowired
	CommentService commentService;

	@RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
	public String newsDetail(@PathVariable("newsId") int newsId, Model model){
		News news = newsService.getById(newsId);
		if (news != null){
			//评论
			List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
			List<ViewObject> commentVOs = new ArrayList<ViewObject>();
			for(Comment comment : comments){
				ViewObject vo = new ViewObject();
				vo.set("comment",comment);
				vo.set("user",userService.getUser(comment.getUserId()));
				commentVOs.add(vo);
			}
			model.addAttribute("comments",commentVOs);
		}
		model.addAttribute("news",news);
		model.addAttribute("owner",userService.getUser(news.getUserId()));
		return "detail";
	}

	@RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
	public String addComment(@RequestParam("newsId") int newsId,
	                         @RequestParam("content") String content){
		try{
			content = HtmlUtils.htmlEscape(content);
			//过滤content
			Comment comment = new Comment();
			comment.setUserId(hostHolder.getUser().getId());
			comment.setContent(content);
			comment.setEntityId(newsId);
			comment.setEntityType(EntityType.ENTITY_NEWS);
			comment.setCreatedDate(new Date());
			comment.setStatus(0);

			commentService.addComment(comment);
			// 更新news里面的评论数量
			int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
			newsService.updateCommentCount(comment.getEntityId(),count);
			// 怎么异步进行

		}catch (Exception e){
			logger.error("增加评论失败" + e.getMessage());
		}
		return "redirect:/news/" + String.valueOf(newsId);
	}


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
