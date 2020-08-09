package com.hotnews.controller;

import com.hotnews.async.EventModel;
import com.hotnews.async.EventProducer;
import com.hotnews.async.EventType;
import com.hotnews.model.EntityType;
import com.hotnews.model.HostHolder;
import com.hotnews.model.News;
import com.hotnews.service.LikeService;
import com.hotnews.service.NewsService;
import com.hotnews.util.HotNewsUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.attribute.AclEntryType;

/**
 * Author: XianDaLi
 * Date: 2020/8/9 13:01
 * Remark:
 */
@Controller
public class LikeController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	HostHolder hostHolder;

	@Autowired
	LikeService likeService;

	@Autowired
	NewsService newsService;

	@Autowired
	EventProducer eventProducer;

	@RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String like(@Param("newId") int newsId) {
		long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
		// 更新喜欢数
		News news = newsService.getById(newsId);
		newsService.updateLikeCount(newsId, (int) likeCount);

		/*eventProducer.fireEvent(new EventModel(EventType.LIKE)
				.setEntityOwnerId(news.getUserId())
				.setActorId(hostHolder.getUser().getId()).setEntityId(newsId));*/

		eventProducer.fireEvent(new EventModel(EventType.LIKE)
				.setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
				.setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(news.getUserId()));

		return HotNewsUtil.getJSONString(0, String.valueOf(likeCount));
	}

	@RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String dislike(@Param("newId") int newsId) {
		long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
		// 更新喜欢数
		newsService.updateLikeCount(newsId, (int) likeCount);
		return HotNewsUtil.getJSONString(0, String.valueOf(likeCount));
	}
}
