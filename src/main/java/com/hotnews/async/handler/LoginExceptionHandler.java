package com.hotnews.async.handler;

import com.hotnews.async.EventHandler;
import com.hotnews.async.EventModel;
import com.hotnews.async.EventType;
import com.hotnews.model.Message;
import com.hotnews.service.MessageService;
import com.hotnews.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Author: XianDaLi
 * Date: 2020/8/9 21:54
 * Remark:
 */
@Component
public class LoginExceptionHandler implements EventHandler {
	@Autowired
	MessageService messageService;

	@Autowired
	MailSender mailSender;

	@Override
	public void doHandle(EventModel model) {
		// 判断登陆是否异常
		Message message = new Message();
		message.setToId(model.getActorId());
		message.setContent("你上次的登陆IP异常");
		// SYSTEM ACCOUNT
		message.setFromId(3);
		message.setCreatedDate(new Date());
		messageService.addMessage(message);

		Map<String, Object> map = new HashMap();
		map.put("username", model.getExt("username"));
		mailSender.sendWithHTMLTemplate(model.getExt("email"),
				"登陆异常", "mails/welcome.html", map);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LOGIN);
	}
}
