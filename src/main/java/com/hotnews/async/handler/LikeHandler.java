package com.hotnews.async.handler;

import com.hotnews.async.EventHandler;
import com.hotnews.async.EventModel;
import com.hotnews.async.EventType;
import com.hotnews.model.Message;
import com.hotnews.model.User;
import com.hotnews.service.MessageService;
import com.hotnews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/9 20:43
 * Remark:关注点赞相关信息
 */
@Component
public class LikeHandler implements EventHandler {
	@Autowired
	MessageService messageService;

	@Autowired
	UserService userService;

	@Override
	public void doHandle(EventModel model) {
		Message message = new Message();
		User user = userService.getUser(model.getActorId());
		message.setToId(model.getEntityOwnerId());
		message.setContent("用户" + user.getName() +
				" 赞了你的资讯,http://127.0.0.1:8080/news/"
				+ String.valueOf(model.getEntityId()));
		// SYSTEM ACCOUNT
		message.setFromId(3);
		message.setCreatedDate(new Date());
		messageService.addMessage(message);


		System.out.println("Liked");
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LIKE);
	}
}
