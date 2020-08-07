package com.hotnews.controller;

import com.hotnews.model.Message;
import com.hotnews.model.User;
import com.hotnews.model.ViewObject;
import com.hotnews.service.MessageService;
import com.hotnews.service.UserService;
import com.hotnews.util.HotNewsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/8 0:36
 * Remark:
 */
@Controller
public class MessageController {

	//日志
	private static final Logger logger = LoggerFactory.getLogger(NewController.class);

	@Autowired
	MessageService messageService;

	@Autowired
	UserService userService;

	@RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
	public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId){
		try {
			List<Message> conversationList= messageService.getConversationDetail(conversationId, 0, 10);
			List<ViewObject> messages = new ArrayList<>();
			for(Message msg : conversationList){
				ViewObject vo = new ViewObject();
				vo.set("message",msg);
				User user = userService.getUser(msg.getFromId());
				if(user == null){
					continue;
				}
				vo.set("headUrl",user.getHeadUrl());
				vo.set("userName",user.getId());
				messages.add(vo);
			}
			model.addAttribute("messages",messages);

		}catch (Exception e){
			logger.error("获取详情消息失败" + e.getMessage());
		}
		return "letterDetail";
	}

	@RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.POST})
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId,
	                         @RequestParam("toId") int toId,
	                         @RequestParam("content") String content){
		try{
			Message msg = new Message();
			msg.setContent(content);
			msg.setFromId(fromId);
			msg.setToId(toId);
			msg.setCreatedDate(new Date());
			msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
			messageService.addMessage(msg);
//			messageDao.addMessage(msg);
			return HotNewsUtil.getJSONString(msg.getId());
		}catch (Exception e){
			logger.error("增加评论失败" + e.getMessage());
			return HotNewsUtil.getJSONString(1,"插入评论失败");
		}
	}
}
