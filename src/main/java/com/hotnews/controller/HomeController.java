package com.hotnews.controller;

import com.hotnews.model.EntityType;
import com.hotnews.model.HostHolder;
import com.hotnews.model.News;
import com.hotnews.model.ViewObject;
import com.hotnews.service.LikeService;
import com.hotnews.service.NewsService;
import com.hotnews.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 0:04
 * Remark:
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	NewsService newsService;

	@Autowired
	UserService userService;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	LikeService likeService;

	private List<ViewObject> getNews(int userId, int offset, int limit) {
		List<News> newsList = newsService.getLatestNews(userId, offset, limit);
		int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
		List<ViewObject> vos = new ArrayList<>();
		for (News news : newsList) {
			ViewObject vo = new ViewObject();
			vo.set("news", news);
			vo.set("user", userService.getUser(news.getUserId()));

			if(localUserId != 0){
				vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
			}else {
				vo.set("like",0);
			}
			vos.add(vo);
		}
		return vos;
	}

	@RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String index(Model model) {
		logger.info("Visit Home");
		model.addAttribute("vos", getNews(0, 0, 10));
		return "home";
	}

	@RequestMapping(path = {"/user/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String userIndex(Model model, @PathVariable("userId") int userId,
	                        @RequestParam(value = "pop",defaultValue = "0") int pop) {
		model.addAttribute("vos", getNews(userId, 0, 10));
		model.addAttribute("pop",pop);
		return "home";
	}
}
