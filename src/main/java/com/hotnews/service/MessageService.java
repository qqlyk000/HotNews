package com.hotnews.service;

import com.hotnews.dao.MessageDao;
import com.hotnews.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/8 1:02
 * Remark:
 */
@Service
public class MessageService {
	@Autowired
	MessageDao messageDao;

	public int addMessage(Message message){
		return messageDao.addMessage(message);
	}

	public List<Message> getConversationDetail(String conversationId, int offset,  int limit){
		return messageDao.getConversationDetail(conversationId,offset,limit);
	}

	public List<Message> getConversationList(int userId, int offset,  int limit){
		return messageDao.getConversationList(userId,offset,limit);
	}

	public int getConversationUnReadCount(int userId,String conversationId){
		return messageDao.getConversationUnReadCount(userId,conversationId);
	}
}
